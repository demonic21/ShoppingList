package shoppinglist.model;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;

public class ShoppedList {
	private String date;
	private LinkedList<ShoppingItem> items;
	
	public ShoppedList() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		GregorianCalendar today = new GregorianCalendar();

		date = formatter.format(today.getTime());
		items = new LinkedList<ShoppingItem>();
	}
	
	public void addItems(ShoppingItem item) {
		items.add(item);
	}
	
	public ShoppingItem removeItems(int position) {
		ShoppingItem item = items.get(position);
		items.remove(position);
		
		return item;
	}
	
	public LinkedList<ShoppingItem> getItems(){
		return (LinkedList<ShoppingItem>)items.clone();
	}
	
	public String getXML() {
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<?xml version='1.0' encoding='UTF-8'?>")
		.append("<ShoppedList>");
		
		for (ShoppingItem item : Information.getShoppedList().getItems()) {
			xmlBuilder.append(item.getXML());
		}
		
		xmlBuilder.append("<user_num>").append(UserInfo.getUserInfo().getUserNum()).append("</user_num>")
		.append("<date>").append(date).append("</date>")
		.append("</ShoppedList>");
		
		return xmlBuilder.toString();
	}
}
