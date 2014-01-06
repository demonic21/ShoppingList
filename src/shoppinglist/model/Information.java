package shoppinglist.model;

public class Information {
	private static ShoppingList shoppingList;
	private static ShoppedList shoppedList;
	
	public static ShoppingList getShoppingList() {
		return shoppingList;
	}
	
	public static void setShoppingList(ShoppingList shoppingList) {
		Information.shoppingList = shoppingList;
	}
	
	public static ShoppedList getShoppedList() {
		if(shoppedList == null)
			shoppedList = new ShoppedList();
		return shoppedList;
	}
	
	public static void setShoppedList() {
		shoppedList = new ShoppedList();
	}
}
