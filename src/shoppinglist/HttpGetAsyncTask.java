package shoppinglist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpGetAsyncTask extends AsyncTask<String, Void, String>{

	private HttpGetListener httpGetListener;
	
	public HttpGetAsyncTask(HttpGetListener	httpGetListener) {
		this.httpGetListener = httpGetListener;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		
		try {
			response = httpClient.execute(new HttpGet("http://apis.daum.net/shopping/search?q=" + params[0] + "&result=1&pageno=1&apikey=DAUM_SHOP_DEMO_APIKEY"));
			StatusLine statusLine = response.getStatusLine();
			
			if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				
				return out.toString();
			} else {
				Log.e("test", "Http Status is NOT ok");
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if(result != null) {
			this.httpGetListener.doStuff(result);			
		}
	}
}
