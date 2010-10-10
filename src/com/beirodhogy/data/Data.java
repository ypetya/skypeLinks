package com.beirodhogy.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.beirodhogy.data.obj.JavaDataObject;
import com.beirodhogy.data.rendering.RenderingObject;

public class Data {
	private static final Logger log = Logger.getLogger(Data.class.getName());

	private Data() {
	}

	public interface Expression {
		public void method(PersistenceManager pm);
	}

	public static void access(Expression withQuery) {
		PersistenceManager pm = PMF.getManager();
		try {
			withQuery.method(pm);
		} catch (JDOException jex) {
			log.log(Level.SEVERE, jex.getMessage(), jex);
		} finally {
			pm.close();
		}
	}
	
	/**  
	 * (cached in higher layer)
	 * */
	public static <T extends JavaDataObject> int queryCount( final Class<T> c) {
		final int[] ret = new int[1];
		access(new Expression() {

			@Override
			public void method(PersistenceManager pm) {
				int i;
				// FIXME: slow counting query(??)
				Iterator<T> iter = pm.getExtent(c).iterator();				
				for(i=0;iter.hasNext();i++) {
					iter.next();
				}
				ret[0] = i;
			}
			
		});
		
		return ret[0];
	}
	

	/** gets the class name and creates query */
	public static <T extends RenderingObject, G extends JavaDataObject> List<T> queryPage(
			final Class<T> renderingObjectClass, final Class<G> objectClass, final int page, final int pageSize) {

		final List<T> ret = new ArrayList<T>();

		access(new Expression() {
			
			@Override
			public void method(PersistenceManager pm) {
				Query q = pm.newQuery(objectClass);
				q.setRange(pageSize * page, pageSize * page + pageSize);
				// FIXME: solve this ordering
				q.setOrdering("insertedAt desc");
				@SuppressWarnings("unchecked")	
				List<G> results = (List<G>) q.execute();  
				for (G i : results) {
					try {
						T ro = renderingObjectClass.getConstructor(objectClass).newInstance(i);
						ret.add(ro);
					} catch (Exception e) {
						log.severe("can not load renderingObject");
					}
				}
			}
		});

		return ret;
	}


	/** gets the class name and creates query */
	public static <T extends RenderingObject, G extends JavaDataObject> List<T> queryAll(
			final Class<T> renderingObjectClass, final Class<G> objectClass) {

		final List<T> ret = new ArrayList<T>();

		access(new Expression() {
			@Override
			public void method(PersistenceManager pm) {
				for (G i : pm.getExtent(objectClass)) {
					try {
						T ro = renderingObjectClass.getConstructor(objectClass).newInstance(i);
						ret.add(ro);
					} catch (Exception e) {
						log.severe("can not load renderingObject");
					}
				}
			}
		});

		return ret;
	}

}
