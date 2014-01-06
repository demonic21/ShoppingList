package shoppinglist.model;

import java.io.Serializable;
import java.util.LinkedList;

public class ShoppingList implements Serializable {
	private String id;
	private String date;
	private LinkedList<ShoppingItem> items;
	
	public ShoppingList(String id) {
		this.id = id;
		items = new LinkedList<ShoppingItem>();
	}
	
	public ShoppingList(String id, String date, LinkedList<ShoppingItem> items) {
		this(id);
		this.date = date;
		this.items = items;
	}
	
	public void addItems(ShoppingItem item) {
		items.add(item);
	}
	
	public ShoppingItem removeItems(int position) {
		ShoppingItem item = items.get(position);
		items.remove(position);
		
		return item;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public String getID() {
		return id;
	}

	public boolean isEmpty() {
		if(items.size() == 0)
			return true;
		
		return false;
	}
	
	public LinkedList<ShoppingItem> getItems(){
		return (LinkedList<ShoppingItem>)items.clone();
	}
}
