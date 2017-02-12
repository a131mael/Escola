package org.escola.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      	
      com.cedarsoftware.util.io.JsonReader.jsonToJava(jsonText);
      
      return json;
    } finally {
      is.close();
    }
  }
  
  public static String readJsonFromUrlString(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      
	      return jsonText;
	    } finally {
	      is.close();
	    }
	  }
  
  public static JSONArray readListJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONArray json = new JSONArray(jsonText);
	      return json;
	    } finally {
	      is.close();
	      
	    }
	  }
	  

  public static void main(String[] args) throws IOException, JSONException {
   // JSONObject json = readJsonFromUrl("http://localhost/UI/rest/teams/avaliable/1");
    //System.out.println(json.toString());
   // System.out.println(json.get("league"));
  }
  
  public static JSONObject getObject(String url){
	  JSONObject json = null;
	try {
		json = readJsonFromUrl(url);
	} catch (JSONException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	  return json;
  }
  
  public static String getText(String url){
	  String retorno = null;
	try {
		retorno = readJsonFromUrlString(url);
	} catch (JSONException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	  return retorno;
  }

  public static JSONArray getList(String url){
	  JSONArray json = null;
	try {
		json = readListJsonFromUrl(url);
	} catch (JSONException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	  return json;
  }

  
}