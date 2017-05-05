package org.jlk.enter;

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
		String appid = "8cf65377538741c2ba8add2615a22299";
		String clientPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAObvCCKGy7XrQStuaVIZUaVoO2SQOjWc5/8oF5bKxgIF2+XaLJAXPImKN1TRnKhnf5hx1h4AAram5e66QQXm9va5x1Msj3+wRWa4H8J1Snx/xcvIvd2jtggTI+UDHE7Isi4r6DmdQIYusWIMciqWv6k1a/wOgrZ9bizZLCvyQyaNAgMBAAECgYEAkZX35uxb2KArQZolw6A6nLCNYOScDdiRWYaI+IN1OSlAbDOTDHPZskv0fgB3ZqN5CT4a4mbXTxcAtfOtsm7dGZN7uuFbmBvMfZNvbdKR1WMg8t6aN2de+pq/nxVjqDyDlq6NgMLinAp5547clpxO6fVy9C3gdu8zNujszcnkw4ECQQD3/UBxHOVUU1HQTrLsZfEwexQ9mLoz+nND1DumX5/bVfKy5tLJ5gdsTdx9lre+wQgD+ny2AgTg3XylOJthhUiZAkEA7mS9N/mSHBJxGsrU+y02fayUNBhJnqKcaHD8SQQtQM+iY8npyPKnhfOuWUWkNYWxDZMLshhSJLfowrAI4LsCFQJBAJq+tjqfHF9dSp0grrei+TkWZE/3gKG0s75srfgojXjxjeui63+/AXzMrTzVEr1+eID7Cl0Mr3yd2QzEYPkekWkCQCcSIaTr5VNuflUKyy9DcLvWLg//rwkt036WjfKVmakWnqHGNOe3RVeB4aBF9qzC9Yqo//C3ZbcY4TAPU9UpMSUCQERrLYYFMdCSkiwWvqPN+Ilh1BSheHDbhQSGiiClzw01809I1gPhT6kRM2vN94ac0vhRrz+bQ/Nf72332ZgUvBM=";
		String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8iMpEG3mnFlMfufO95DfAfor80RL3I/IzF828aoDDw/Xy86jPiihJyGyG2ZmbqsAw+8nj8eGc+U9LmKASgQhS9e0R/MmYDa9R/O2f4tQZUQr3nE3uUTES0tqCLoE3TVSd59lnVExeDL5IW+F/Yc9mz1v+xSDFcSKyfHEo0FDnnwIDAQAB";
		String loanListUrl = ReadProperties.getProperties("loanListUrl");
		String batchListingInfosUrl = ReadProperties.getProperties("batchListingInfosUrl");
		String accessToken = ReadProperties.getProperties("accessToken");
		OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);


		while(true){
			TimeUnit.MILLISECONDS.sleep(444);

			Result result = OpenApiClient.send(loanListUrl,
					new PropertyObject("PageIndex", 1, ValueTypeEnum.Int32));
			if (result.isSucess()) {

				JSONObject jsonObject = JSONObject.fromObject(result.getContext());
				JSONArray jsonArray = jsonObject.getJSONArray("LoanInfos");

				int size = jsonArray.size();
				for(int i = 0; i < size; i++){
					JSONObject loanInfo = jsonArray.getJSONObject(i);
					Integer listingId = loanInfo.getInt("ListingId");
					String creditCode = loanInfo.getString("CreditCode");
					Double rate = loanInfo.getDouble("Rate");
					
					if(rate > 22){
						System.out.println(loanInfo);
					}
				}
			}
		}
	}

}
