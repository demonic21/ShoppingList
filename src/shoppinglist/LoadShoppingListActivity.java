package shoppinglist;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

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

import shoppinglist.model.ShoppingList;
import shoppinglist.model.UserInfo;

import com.example.shoppinglist.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LoadShoppingListActivity extends Activity implements HttpGetListener {
	private ListView shoppingList;
	private ArrayList<String> shoppingArrayList;
	private ArrayAdapter<String> shoppingListAdapter;
	private LinkedList<ShoppingList> list;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_shopping_list);
		
		shoppingList = (ListView)findViewById(R.id.load_shopping_list);
		shoppingArrayList = new ArrayList<String>();
		shoppingListAdapter = new ArrayAdapter<String>(this, R.layout.list_item, shoppingArrayList);
		
		new DBAsyncTask((HttpGetListener) LoadShoppingListActivity.this).execute("0", UserInfo.getUserInfo().getUserNum());
		
		shoppingList.setAdapter(shoppingListAdapter);
		shoppingList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		shoppingList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ShoppingList shoppingList = list.get(arg2);
				
				intent = new Intent(LoadShoppingListActivity.this, LoadItemListActivity.class); 
				intent.putExtra("ShoppingList", shoppingList);
				
				startActivityForResult(intent, 2);
			}
		});
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("test", "Result1");
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if(requestCode == 2) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	@Override
	public void doStuff(String result) {
		InputSource is = new InputSource(new StringReader(result));
		list = new LinkedList<ShoppingList>();
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			NodeList nodeList = (NodeList) xpath.evaluate("//List/ShoppingList", document, XPathConstants.NODESET);

			for(int i = 0; i < nodeList.getLength(); i++) {
				String id = nodeList.item(i).getChildNodes().item(0).getTextContent();
				String date = nodeList.item(i).getChildNodes().item(1).getTextContent();
				
				ShoppingList shoppingList = new ShoppingList(id);
				shoppingList.setDate(date);
				list.add(shoppingList);
				shoppingArrayList.add(date);
			}
		} catch (SAXException e) {
			e.printStackTrace();
			Log.d("test", e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		shoppingListAdapter.notifyDataSetChanged();
	}
}
