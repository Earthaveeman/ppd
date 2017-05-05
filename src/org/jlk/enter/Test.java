package org.jlk.enter;

import org.apache.log4j.Logger;
import org.jlk.tools.ReadProperties;
import org.jlk.tools.Util;

import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.RsaCryptoHelper;

public class Test {
	private static Logger log = Logger.getLogger(Test.class);

	public static void main(String[] args) throws Exception {
		String appid = ReadProperties.getProperties("appid");
		String clientPrivateKey = ReadProperties.getProperties("clientPrivateKey");
		String serverPublicKey = ReadProperties.getProperties("serverPublicKey");
		String loanListUrl = ReadProperties.getProperties("loanListUrl");
		String batchListingInfosUrl = ReadProperties.getProperties("batchListingInfosUrl");
		String accessToken = ReadProperties.getProperties("accessToken");
		OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);

		
		System.out.println(Util.queryBalance(accessToken));
	}
}
