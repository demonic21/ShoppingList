package shoppinglist;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import shoppinglist.model.Information;
import shoppinglist.model.ShoppingList;
import shoppinglist.model.UserInfo;

import com.example.shoppinglist.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements HttpGetListener {
	private Button loginButton;
	private TextView idView;
	private TextView passwordView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		loginButton = (Button)findViewById(R.id.login_button);
		idView = (TextView)findViewById(R.id.id);
		passwordView = (TextView)findViewById(R.id.password);
		
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = null;
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("SHA-1");
					md.update(new String(passwordView.getText().toString()).getBytes("UTF-8"));
					byte[] digested = md.digest();
					password = new String(digested);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
//				new DBAsyncTask((HttpGetListener) LoginActivity.this).execute("3", idView.getText().toString(), password);
				new DBAsyncTask((HttpGetListener) LoginActivity.this).execute("3", idView.getText().toString(), passwordView.getText().toString());
			}
		});
	}
	
	@Override
	public void doStuff(String result) {
		if(result.equals("-1")) {
			Toast.makeText(this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
			Log.d("test", "Not login");
			return;
		} else {
			InputSource is = new InputSource(new StringReader(result));
			try {
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
				XPath xpath = XPathFactory.newInstance().newXPath();

				NodeList nodeList = (NodeList) xpath.evaluate("//UserInfo", document, XPathConstants.NODESET);
				
				String id = nodeList.item(0).getChildNodes().item(0).getTextContent();
				String name = nodeList.item(0).getChildNodes().item(1).getTextContent();
				String userNum = nodeList.item(0).getChildNodes().item(2).getTextContent();
				
				UserInfo.setUserInfo(id, name, userNum);

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

			startActivity(new Intent(this, ShoppingActivity.class));
			finish();
		}
	}
}
