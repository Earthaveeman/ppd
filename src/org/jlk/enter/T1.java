package org.jlk.enter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jlk.tools.ReadProperties;
import org.jlk.tools.Util;

import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.PropertyObject;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.RsaCryptoHelper;
import com.ppdai.open.core.ValueTypeEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class T1 {

	public static void main(String[] args) throws Exception {
		String appid = ReadProperties.getProperties("appid");
		String clientPrivateKey = ReadProperties.getProperties("clientPrivateKey");
		String serverPublicKey =ReadProperties.getProperties("serverPublicKey");
		String loanListUrl = ReadProperties.getProperties("loanListUrl");
		String batchListingInfosUrl = ReadProperties.getProperties("batchListingInfosUrl");
		String accessToken = ReadProperties.getProperties("accessToken");
		OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);

		Date now = new Date();
		Date startDateTime = new Date(now.getTime() - (1*60*1000));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(startDateTime);
		
		for(int i=0;i<10;i++){
			Result result = OpenApiClient.send(loanListUrl,
					new PropertyObject("PageIndex", i, ValueTypeEnum.Int32));
			JSONObject jsonObject = JSONObject.fromObject(result.getContext());
			JSONArray jsonArray = jsonObject.getJSONArray("LoanInfos");
			System.out.println(jsonArray.size());
			
			Util.findHighQuality22RateListAndBid(jsonArray, 50);
		}
	}

}
