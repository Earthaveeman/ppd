package org.jlk.enter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jlk.tools.AuthInfoSingleton;

public class T2 {

	public static void main(String[] args) throws Exception {
		AuthInfoSingleton s = AuthInfoSingleton.getInstance();
		System.out.println(s.getAccessToken());
		s.setAccessToken("adf");
		System.out.println(s.getAccessToken());
		AuthInfoSingleton s2= AuthInfoSingleton.getInstance();
		System.out.println(s2.getAccessToken());
		s2.setAccessToken("ddddddddddd");
		System.out.println(s.getAccessToken());
	}

}
