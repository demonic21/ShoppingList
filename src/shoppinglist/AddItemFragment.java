package shoppinglist;

import java.io.IOException;
import java.io.StringReader;

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

import com.example.shoppinglist.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AddItemFragment extends Fragment implements HttpGetListener {
	private Context mContext;
	private Button addButton, inquiryButton;
	private TextView item, webCost;
	private CustomOnClickListener buttonListener;
		
	public AddItemFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_item, null);
	
		addButton = (Button)view.findViewById(R.id.item_add_button);
		inquiryButton = (Button)view.findViewById(R.id.item_inquiry_button);
		item = (TextView)view.findViewById(R.id.item_text);
		webCost = (TextView)view.findViewById(R.id.web_cost); 
		
		addButton.setOnClickListener(onClickListener);
		inquiryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new HttpGetAsyncTask((HttpGetListener) AddItemFragment.this).execute(getItemName());
			}
		});
		
		return view;
	}
	
	public interface CustomOnClickListener {
		public void onClicked();
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(getItemName().isEmpty() || getItemName().trim().length() == 0) {
				Toast.makeText(mContext, "물품명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
				clear();
				return;
			}
			
			buttonListener.onClicked();
			Toast.makeText(mContext, "물품이 추가되었습니다.", Toast.LENGTH_SHORT).show();
			InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			keyboard.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
			clear();
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		buttonListener = (CustomOnClickListener)activity;
	}

	public String getItemName() {
		return item.getText().toString();
	}

	@Override
	public void doStuff(String result) {
		String str = "<a href=\"";
		InputSource is = new InputSource(new StringReader(result));
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			str += ((NodeList)xpath.evaluate("//channel/item/link", document, XPathConstants.NODESET)).item(0).getTextContent();
			str += "\">";
			str += ((NodeList)xpath.evaluate("//channel/item/price_min", document, XPathConstants.NODESET)).item(0).getTextContent();
			
			webCost.setText(Html.fromHtml(str));
			webCost.setMovementMethod(LinkMovementMethod.getInstance());			
			
			Log.d("test", str);
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
	
	public void clear() {
		item.setText("");
		webCost.setText("");
	}
}