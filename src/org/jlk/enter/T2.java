package org.jlk.enter;

import org.apache.log4j.Logger;
import org.jlk.tools.ReadProperties;
import org.jlk.tools.Util;

import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.PropertyObject;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.RsaCryptoHelper;
import com.ppdai.open.core.ValueTypeEnum;

import net.sf.json.JSONObject;

public class T2 {

	private static Logger log = Logger.getLogger(T2.class);
	public static void main(String[] args) throws Exception {
		String appid = ReadProperties.getProperties("appid");
		String clientPrivateKey = ReadProperties.getProperties("clientPrivateKey");
		String serverPublicKey = ReadProperties.getProperties("serverPublicKey");
		String accessToken = ReadProperties.getProperties("accessToken");

		OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
		
//		String openid="4dd8bcc10cf64d32ae47c2431ce7ed0c";
//		String refToken="420c78a8-d913-410c-9061-4104c4d0b083";
//		AuthInfo authInfo = Util.refreshtoken(openid, refToken);
//		authInfo.logPrint();

		log.info("尝试投标[标号"  +  "]");
		Result biddingResult = OpenApiClient.send("http://gw.open.ppdai.com/invest/BidService/Bidding","8c62ac6d-ee15-4d86-9367-e34286fb7300",
				new PropertyObject("ListingId",33333,ValueTypeEnum.Int32),
				new PropertyObject("Amount",50,ValueTypeEnum.Double)
				);
		if(biddingResult.isSucess()){
			String message = biddingResult.getContext();
			int resultCode = JSONObject.fromObject(message).getInt("Result");
			if(resultCode == 0){
				log.error("success.");
			}
			log.info("投标详情[" + biddingResult.getContext() + "]");
		}
	}

}
