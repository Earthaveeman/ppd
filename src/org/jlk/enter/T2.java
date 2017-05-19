package org.jlk.enter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class T2 {

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://invest.ppdai.com/Bid/Bid");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		urlConnection.setRequestMethod("POST");
        urlConnection.setConnectTimeout(3000);
        urlConnection.setReadTimeout(8000);

        urlConnection.setRequestProperty("Reason", "");
        urlConnection.setRequestProperty("Amount", "50");
        urlConnection.setRequestProperty("ListingId", "47881996");
        urlConnection.setRequestProperty("UrlReferrer", "1");
        urlConnection.setRequestProperty("ActivityId", "719");
        urlConnection.setRequestProperty("SubListType", "0");

        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String strResponse = bufferedReader.readLine();
        
        System.out.println(strResponse);
	}

}
