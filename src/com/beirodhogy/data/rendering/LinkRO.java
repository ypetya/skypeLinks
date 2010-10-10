package com.beirodhogy.data.rendering;

import java.util.Date;

import com.beirodhogy.data.obj.Link;
import com.google.appengine.api.datastore.Key;

public class LinkRO implements RenderingObject {
	private Key key;

	private Integer channel;

	private String magick;

	private String text;

	private Date insertedAt;
	
	@SuppressWarnings("unused")
	private LinkRO() {}

	public LinkRO(Link link) {
		this.key = link.getKey();
		this.channel = link.getChannel();
		this.magick = link.getMagick();
		this.text = link.getText();
		this.insertedAt = link.getInsertedAt();
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
