package shoppinglist.model;

import java.io.Serializable;

public class ShoppingItem {
	private String id;
	private String name;
	private String min;
	private String avg;
	private String web;
	
	public ShoppingItem(String id, String name) {
		this.id = id;
		this.name = name;
		this.min = null;
		this.avg = null;
		this.web = null;
	}
	
	public ShoppingItem(String id, String name, String min, String avg, String web) {
		this.id = id;
		this.name = name;
		this.min = min;
		this.avg = avg;
		this.web = web;
	}

	public String getName() {
		return name;
	}

	public String getXML() {
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<item>")
		.append("<name>").append(name).append("</name>")
		.append("</item>");
		return xmlBuilder.toString();
	}
}
