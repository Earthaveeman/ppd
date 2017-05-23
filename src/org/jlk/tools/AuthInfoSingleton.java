package org.jlk.tools;

public class AuthInfoSingleton {
	private String openID;
	private String accessToken;
	private String refreshToken;

	public String getOpenID() {
		return openID;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	private AuthInfoSingleton(){
		this.openID = ReadProperties.getProperties("openId");
		this.accessToken = ReadProperties.getProperties("accessToken");
		this.refreshToken = ReadProperties.getProperties("refreshToken");
	}
	
	private static AuthInfoSingleton singleton;
	
	public static synchronized AuthInfoSingleton getInstance(){
		if(singleton == null){
			singleton = new AuthInfoSingleton();
		}
		return singleton;
	}
}
