package org.jlk.task;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jlk.tools.AuthInfoSingleton;
import org.jlk.tools.Util;

public class AccessTokenRefreshTask extends TimerTask{
	
	private static Logger log = Logger.getLogger(AccessTokenRefreshTask.class);

	@Override
	public void run() {
		log.info("Refreshing AccessToken ...");
		
		try {
			AuthInfoSingleton authInfo = AuthInfoSingleton.getInstance();
			String accessToken = Util.refreshtoken(authInfo.getOpenID(), authInfo.getRefreshToken()).getAccessToken();
			authInfo.setAccessToken(accessToken);
			
			log.info("New AccessToken : [" + accessToken + "]");
		} catch (Exception e) {
			log.error(e);
		}
	}

}
