package com.ppdai.open.core;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by xuzhishen on 2016/3/16.
 */
public class AuthInfo {

	private static Logger log = Logger.getLogger(AuthInfo.class);
	
    //错误信息
    @JsonProperty("ErrMsg")
    private int errMsg ;

    //用户在第三方唯一标识
    @JsonProperty("OpenID")
    private String openID ;

    //访问令牌
    @JsonProperty("AccessToken")
    private String accessToken ;

    //刷新令牌
    @JsonProperty("RefreshToken")
    private String refreshToken ;

    //超时时间，单位秒
    @JsonProperty("ExpiresIn")
    private int expiresIn ;

    public int getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(int errMsg) {
        this.errMsg = errMsg;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    
    public void logPrint(){
    	log.info("访问令牌 AccessToken : "+this.getAccessToken());
    	log.info("第三方唯一标识 OpenID : "+this.getOpenID());
    	log.info("刷新令牌 RefreshToken : "+this.getRefreshToken());
    	log.info("超时时间 ExpiresIn : "+this.getExpiresIn());
    }
}
