package com.beirodhogy.data;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.beirodhogy.data.Data.Expression;
import com.beirodhogy.data.obj.Link;
import com.beirodhogy.data.rendering.LinkRO;

public class LinkDataBroker {
	private static final String CACHE_KEY_TOTAL_COUNT = "total_count";

	private static final Logger log = Logger.getLogger(LinkDataBroker.class.getName());

	private static Cache cache = null;

	public interface Cacheable {
		public String getCacheKey();

		public <T> T getDataToCache();
	}

	public static <T> T cacheFetch(Cacheable c) {
		final String key = c.getCacheKey();
		T o = cacheGet(key);
		if (o != null) {
			return o;
		}

		o = c.getDataToCache();
		cachePut(key, o);
		return o;
	}

	/** first time it is initialized */
	private static Cache getCacheInstance() {
		if( cache == null ) {
			try {
				CacheFactory cf = CacheManager.getInstance().getCacheFactory();
				cache = cf.createCache(Collections.emptyMap());
			} catch (CacheException e) {
				log.log(Level.SEVERE, "can not initialize cache", e);
				cache = null;
			}
		}
		return cache;
	}

	private static <T> T cacheGet(String key) {
		@SuppressWarnings("unchecked")
		T cachedValue = (T) getCacheInstance().get(key);
		return cachedValue;
	}

	private static <T> boolean cachePut(String key, T object) {
		if (getCacheInstance().put(key, object) != null)
			return true;
		return false;
	}

	private static void cacheInvalidate(String key) {
		getCacheInstance().remove(key);
	}

	private LinkDataBroker() {
	}

	public static void saveNewLink(final int ch, final String text, final String magick) {
		Data.access(new Expression() {
			@Override
			public void method(PersistenceManager pm) {
				// check unique
				Query q = pm.newQuery(Link.class, "text == textParam");
				q.declareParameters("String textParam");
				@SuppressWarnings("unchecked")
				List<Link> results = (List<Link>) q.execute(text);
				if (!results.isEmpty()) {
					return;
				}
				// persist new entity
				Link newLink = new Link(ch, magick, text);
				pm.makePersistent(newLink);
				cacheInvalidate(CACHE_KEY_TOTAL_COUNT);
			}
		});
	}

	public static List<LinkRO> getPage(String page, int pageSize) {
		int p;
		try {
			p = Integer.valueOf(page);
		} catch (NumberFormatException e) {
			p = 0;
		}

		return Data.queryPage(LinkRO.class, Link.class, p, pageSize);
	}

	public static int getCount() {
		return cacheFetch(new Cacheable() {

			@Override
			public String getCacheKey() {
				return CACHE_KEY_TOTAL_COUNT;
			}

			@Override
			@SuppressWarnings("unchecked")
			public Integer getDataToCache() {
				return Data.queryCount(Link.class);
			}

		});

	}

}
