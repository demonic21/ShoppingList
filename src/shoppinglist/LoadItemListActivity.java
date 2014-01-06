package shoppinglist;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import shoppinglist.model.Information;
import shoppinglist.model.ShoppingItem;
import shoppinglist.model.ShoppingList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.shoppinglist.R;

public class LoadItemListActivity extends Activity implements HttpGetListener {
	private ListView itemList;
	private ArrayList<String> itemArrayList;
	private ArrayAdapter<String> itemListAdapter;
	private ShoppingList shoppingList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_shopping_list);
		
		itemList = (ListView)findViewById(R.id.load_shopping_list);
		itemArrayList = new ArrayList<String>();
		itemListAdapter = new ArrayAdapter<String>(this, R.layout.list_item, itemArrayList);
		
		shoppingList = (ShoppingList) getIntent().getSerializableExtra("ShoppingList");
		
		if(shoppingList.isEmpty());
			new DBAsyncTask((HttpGetListener) LoadItemListActivity.this).execute("1", shoppingList.getID());
			
		itemList.setAdapter(itemListAdapter);
		itemList.setChoiceMode(ListView.CHOICE_MODE_NONE);
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.load_item_menu, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	AlertDialog.Builder alert_confirm = new AlertDialog.Builder(LoadItemListActivity.this);
    	switch(item.getItemId()) {
    	case R.id.load_items :
			alert_confirm.setMessage("이 목록을 불러오시겠습니까?");
			alert_confirm.setCancelable(false);
			alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Information.setShoppingList(shoppingList);
					Information.setShoppedList();
					LoadItemListActivity.this.setResult(RESULT_OK);
					finish();
				}
			});
			alert_confirm.setNegativeButton("취소", null);
    		break;
    	}
    	
    	AlertDialog alert = alert_confirm.create();
		alert.show();
    	
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void doStuff(String result) {
		InputSource is = new InputSource(new StringReader(result));
		
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			NodeList nodeList = (NodeList) xpath.evaluate("//Items/Item", document, XPathConstants.NODESET);

			for(int i = 0; i < nodeList.getLength(); i++) {
				String id = nodeList.item(i).getChildNodes().item(0).getTextContent();
				String name = nodeList.item(i).getChildNodes().item(1).getTextContent();
				String min = nodeList.item(i).getChildNodes().item(2).getTextContent();
				String max = nodeList.item(i).getChildNodes().item(3).getTextContent();
				String web = nodeList.item(i).getChildNodes().item(4).getTextContent();
				
				ShoppingItem item = new ShoppingItem(id, name, min, max, web);
				shoppingList.addItems(item);
				itemArrayList.add(item.getName());
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
