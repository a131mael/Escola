package org.escola.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class Herald {

		//TODO envia mensagem FCM Firebase cloude Message
		public static void sendFCMMessage(String urlPost,String keyApp, String priority, String titleMessge, String bodyMessage, String receiver){
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(urlPost);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Authorization",keyApp );

			JSONObject message = new JSONObject();
			message.put("to",receiver);
			message.put("priority", priority);

			JSONObject notification = new JSONObject();
			notification.put("title", titleMessge);
			notification.put("body", bodyMessage);

			message.put("notification", notification);

			post.setEntity(new StringEntity(message.toString(), "UTF-8"));
			HttpResponse response = null;
			try {
				response = client.execute(post);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(response);
			System.out.println(message);
		}

		
		public static void main(String[] args) {
			Herald.sendFCMMessage(Constants.FCM_URL_POST, Constants.FCM_KEY_APP, Constants.FCM_PRIORITY_HIGH, "Col�gio Adonai", "Voc� tem um novo recado !", Constants.FCM_KEY_MY_DISPOSITIVE_JUST_TO_TEST);
		}
}
