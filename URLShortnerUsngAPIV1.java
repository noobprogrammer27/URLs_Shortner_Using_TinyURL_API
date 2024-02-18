package com.beginners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;

public class URLShortnerUsngAPIV1 {

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		String urlValue = "https://api.tinyurl.com/create";
		String requestType = "POST";
		String contentType = "application/json";
		System.out.println("Enter Url to short: ");
		String url = sc.nextLine();
		JSONObject postingObj = createRequestBody(url);
		List<String> responseList = executeHTTPCall(urlValue, requestType, contentType, postingObj);
		System.out.println("finalresponse: " + responseList.toString());
		String finalstr = "";
		if (!responseList.isEmpty()) {
			String alias = responseList.get(1);
			org.json.simple.JSONObject object = (org.json.simple.JSONObject) JSONValue.parse(alias);
			String ali = object.get("data").toString();
			int cnt = ali.indexOf("alias");
			finalstr = ali.substring(cnt, ali.length()).split(":")[1].split(",")[0].replaceAll("^\"|\"$", "");
		}
		String finalOutput = "https://tinyurl.com/" + finalstr;
		System.out.println("Your final tinyUrl is - " + finalOutput);
		sc.close();
	}

	public static List<String> executeHTTPCall(String urlValue, String requestType, String contentType,
			JSONObject obj) {
		List<String> responseList = new ArrayList<>();
		try {
			URL url = new URL(urlValue);
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestType);
			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization",
					"Bearer " + "FhQAENoceWei8kACkIkSAIl33WbTTAtVTjQ2O1eTAlj6i94HZ58IYVoDDW1R");
			conn.setDoOutput(true);
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(obj.toString().getBytes("utf-8"));
			System.out.println("outputStream written");
			outputStream.flush();
			responseList = addResponse(responseList, conn);
			return responseList;

		} catch (Exception e) {
			System.out.println("From Class name: URLShortnerUsngAPI method name: executeHTTPCall MalformedURLException:"
					+ e.getMessage());
		}
		return responseList;
	}

	public static List<String> addResponse(List<String> responseList, HttpURLConnection conn) {
		try {
			int responseCode = conn.getResponseCode();
			responseList.add(String.valueOf(responseCode));
			if (responseCode >= 200 && responseCode < 300) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				System.out.println("Response received " + response.toString());
				responseList.add(response.toString());
				return responseList;
			}
		} catch (IOException e) {
			System.out.println("From Class name: URLShortnerUsngAPI method name: executeHTTPCall MalformedURLException:"
					+ e.getMessage());
		}
		return responseList;
	}

	private static JSONObject createRequestBody(String url) {
		JSONObject postingObj = new JSONObject();
		try {
			postingObj.put("url", url);
			postingObj.put("domain", "tinyurl.com");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return postingObj;
	}

}
