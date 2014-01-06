package shoppinglist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class DBAsyncTask extends AsyncTask<String, Void, String> {
private HttpGetListener httpGetListener;
	
	public DBAsyncTask(HttpGetListener	httpGetListener) {
		this.httpGetListener = httpGetListener;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		String url = "http://172.16.165.131/";
		
		if(params[0].equals("0"))
			url += "db_connection.php?functionNo=6&user_num=" + params[1];
		else if(params[0].equals("1"))
			url += "db_connection.php/?functionNo=9&id=" + params[1];
		else if(params[0].equals("2")) {
			List<NameValuePair> parameters = new LinkedList<NameValuePair>();
			
			parameters.add(new BasicNameValuePair("functionNo", "7"));
			parameters.add(new BasicNameValuePair("xml", params[1]));
			
			String paramString = URLEncodedUtils.format(parameters, "utf-8");
			url += "db_connection.php/?" + paramString;
			Log.e("test2", url);
//				url += "db_connection.php/?functionNo=7&xml=" + URLEncoder.encode(new String(params[1].getBytes("UTF-8")), "UTF-8");
		}
		else if(params[0].equals("3"))
			url += "db_connection.php/?functionNo=2&id=" + params[1] + "&password=" + params[2];
		
		try {
			response = httpClient.execute(new HttpGet(url));
			
			StatusLine statusLine = response.getStatusLine();
			
			if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				
				Log.i("test", out.toString());
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



