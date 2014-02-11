package com.molinari.androidstructure.webservices;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ClipData.Item;
import android.os.Build;

public class RestClient {
	
	public static JSONObject requestWebService(String serviceUrl) {
	    disableConnectionReuseIfNecessary();
	 
	    HttpURLConnection urlConnection = null;
	    try {
	        // create connection
	        URL urlToRequest = new URL(serviceUrl);
	        urlConnection = (HttpURLConnection) 
	            urlToRequest.openConnection();
//	        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//	        urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);
	         
	        // handle issues
	        int statusCode = urlConnection.getResponseCode();
	        if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
	            // handle unauthorized (if service requires user login)
	        } else if (statusCode != HttpURLConnection.HTTP_OK) {
	            // handle any other errors, like 404, 500,..
	        }
	         
	        // create JSON object from content
	        InputStream in = new BufferedInputStream(
	            urlConnection.getInputStream());
	        return new JSONObject(getResponseText(in));
	         
	    } catch (MalformedURLException e) {
	        // URL is invalid
	    } catch (SocketTimeoutException e) {
	        // data retrieval or connection timed out
	    } catch (IOException e) {
	        // could not read response body 
	        // (could not create input stream)
	    } catch (JSONException e) {
	        // response body is no valid JSON string
	    } finally {
	        if (urlConnection != null) {
	            urlConnection.disconnect();
	        }
	    }       
	     
	    return null;
	}
	
	/**
	 * required in order to prevent issues in earlier Android version.
	 */
	private static void disableConnectionReuseIfNecessary() {
	    // see HttpURLConnection API doc
	    if (Integer.parseInt(Build.VERSION.SDK) 
	            < Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}
	 
	private static String getResponseText(InputStream inStream) {
	    // very nice trick from 
	    // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
	    return new Scanner(inStream).useDelimiter("\\A").next();
	}
//	public List<MyItem> findAllItems() {
//	    JSONObject serviceResult = RestClient.requestWebService(
//	        "http://url/to/findAllService");
//	     
//	    List<MyItem> foundItems = new ArrayList<MyItem>(20);
//	     
//	    try {
//	        JSONArray items = serviceResult.getJSONArray("items");
//	         
//	        for (int i = 0; i < items.length(); i++) {
//	            JSONObject obj = items.getJSONObject(i);
//	            foundItems.add(
//	                    new Item(obj.getInt("id"), 
//	                        obj.getString("name"), 
//	                        obj.getBoolean("active")));
//	        }
//	         
//	    } catch (JSONException e) {
//	        // handle exception
//	    }
//	     
//	    return foundItems;
//	}
}
