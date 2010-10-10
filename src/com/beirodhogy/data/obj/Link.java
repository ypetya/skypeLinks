package com.beirodhogy.data.obj;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Link implements JavaDataObject{

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Integer channel;

	@Persistent
	private String magick;

	@Persistent
	private String text;

	@Persistent
	private Date insertedAt;

	/** POJOs must have a default constructor. otherwise JDO will create it */
	public Link() {		
	}

	public Link(int channel, String magick, String text) {
		this.channel = channel;
		this.magick = magick;
		this.text = text;
		this.insertedAt = new Date();
	}

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @return the magick
	 */
	public String getMagick() {
		return magick;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the insertedAt
	 */
	public Date getInsertedAt() {
		return insertedAt;
	}

}
