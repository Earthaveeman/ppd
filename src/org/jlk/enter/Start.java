package org.jlk.enter;

import java.util.Timer;

import org.apache.log4j.Logger;
import org.jlk.task.AccessTokenRefreshTask;
import org.jlk.task.Task_AA;
import org.jlk.task.Task__C;
import org.jlk.task.Task__D;
import org.jlk.tools.ReadProperties;
import org.jlk.tools.Util;

import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.RsaCryptoHelper;

public class Start {
	private static Logger log = Logger.getLogger(Start.class);
	private static AuthInfo authInfo = null;

	public static void main(String[] args) throws Exception {
		int refreshTime = Integer.parseInt(ReadProperties.getProperties("refreshTime"));
		boolean isAAEnable = Boolean.parseBoolean((ReadProperties.getProperties("isAAEnable")));
		boolean isCEnable = Boolean.parseBoolean((ReadProperties.getProperties("isCEnable")));
		boolean isDEnable = Boolean.parseBoolean((ReadProperties.getProperties("isDEnable")));

		String appid = ReadProperties.getProperties("appid");
		String clientPrivateKey = ReadProperties.getProperties("clientPrivateKey");
		String serverPublicKey = ReadProperties.getProperties("serverPublicKey");

		log.info("客户端初始化中 ... ...");
		OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
		log.info("初始化完毕.");
		
		if (args.length > 0) {
			log.info("获取授权信息... ...");
			authInfo = Util.getAuthInfo(args[0]);
			authInfo.logPrint();
			return;
		}
		
		Timer refreshAccessTokenTimer = new Timer();
		refreshAccessTokenTimer.schedule(new AccessTokenRefreshTask(), 1, 1*24*60*60*1000);
		
		if(isAAEnable){
			Timer timerAA = new Timer();
			timerAA.schedule(new Task_AA(), 1000, refreshTime);
		}
		if(isCEnable){
			Timer timerC = new Timer();
			timerC.schedule(new Task__C(), 4000, refreshTime);
		}
		if(isDEnable){
			Timer timerD = new Timer();
			timerD.schedule(new Task__D(), 7000, refreshTime);
		}
		
	}

}
