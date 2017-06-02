package org.jlk.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.PropertyObject;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.ValueTypeEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Util {
	
	private static Logger log = Logger.getLogger(Util.class);

	private static String batchListingInfosUrl = ReadProperties.getProperties("batchListingInfosUrl");
	private static String biddingUrl = ReadProperties.getProperties("Bidding");
//	private static String refreshtokenUrl = ReadProperties.getProperties("refreshtokenUrl");
	private static String queryBalance = ReadProperties.getProperties("queryBalance");
	private static Double AARate = Double.parseDouble(ReadProperties.getProperties("AARate"));
	private static Double overNormalRate = Double.parseDouble(ReadProperties.getProperties("overNormalRate"));
	private static int dMonths = Integer.parseInt(ReadProperties.getProperties("dMonths"));
	
	public static AuthInfo getAuthInfo(String code){
		AuthInfo authInfo = null;
		try {
			authInfo = OpenApiClient.authorize(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authInfo;
	}
	
	public static AuthInfo refreshtoken(String openID, String refreshToken){
		AuthInfo authInfo = null;
		try {
			authInfo = OpenApiClient.refreshToken(openID, refreshToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authInfo;
	}
	
	public static String queryBalance(String accessToken){
		Result result = null;
		try {
			result = OpenApiClient.send(queryBalance, accessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.isSucess() ? result.getContext() : result.getErrorMessage();
	}
	
	public static List<Integer> getHighQualityAAList(JSONArray jsonArray){
		List<Integer> returnList = new ArrayList<Integer>();
		
		int size = jsonArray.size();
		for(int i = 0; i < size; i++){
			JSONObject loanInfo = jsonArray.getJSONObject(i);
			Integer listingId = loanInfo.getInt("ListingId");
			String creditCode = loanInfo.getString("CreditCode");
			Double rate = loanInfo.getDouble("Rate");
			
			if("AA".equals(creditCode) && rate >= AARate){
				log.info("发现目标[魔镜等级AA][标号" + listingId + "]");
				log.info(loanInfo);
				returnList.add(listingId);
			}
		}
		return returnList;
	}
	
	public static List<Integer> getHighQualityC22List(JSONArray jsonArray){
		List<Integer> returnList = new ArrayList<Integer>();
		
		int size = jsonArray.size();
		for(int i = 0; i < size; i++){
			JSONObject loanInfo = jsonArray.getJSONObject(i);
			Integer listingId = loanInfo.getInt("ListingId");
			String creditCode = loanInfo.getString("CreditCode");
			Double rate = loanInfo.getDouble("Rate");
//			int months = loanInfo.getInt("Months");
			
			if("C".equals(creditCode) && rate > 20){
				log.info("发现目标[魔镜等级C][标号" + listingId + "]");
				log.info(loanInfo);
				returnList.add(listingId);
			}
		}
		
		return returnList;
	}
	
	public static void findHighQuality22RateListAndBid(JSONArray jsonArray, int amount) throws Exception{
		List<Integer> firstFilteredLoans = new ArrayList<Integer>();
		int size = jsonArray.size();
		for(int i = 0; i < size; i++){
			JSONObject loanInfo = jsonArray.getJSONObject(i);
			Integer listingId = loanInfo.getInt("ListingId");
//			String creditCode = loanInfo.getString("CreditCode");
			Double rate = loanInfo.getDouble("Rate");
			int months = loanInfo.getInt("Months");
			
			if(rate >= 20 && months <= dMonths){
				firstFilteredLoans.add(listingId);
			}
		}

		int loanSize = firstFilteredLoans.size();
		
		//每10条查一次详情
		for(int i=0; i<=loanSize; i+=10){
			int toIndex = (i+10)>loanSize ? loanSize : (i+10);
			List<Integer> tenLoans = firstFilteredLoans.subList(i, toIndex);
			
			Result detailResult = OpenApiClient.send(batchListingInfosUrl,
					new PropertyObject("ListingIds", tenLoans, ValueTypeEnum.Other));
			if(detailResult.isSucess()){
				JSONArray loanInfos = JSONObject.fromObject(detailResult.getContext()).getJSONArray("LoanInfos");
				for(int j=0; j<loanInfos.size(); j++){
					JSONObject loanInfo = loanInfos.getJSONObject(j);
					int listingId = loanInfo.getInt("ListingId");
					String creditCode = loanInfo.getString("CreditCode");
					int gender = loanInfo.getInt("Gender");	//性别	1 男 2 女 0 未知
					int age = loanInfo.getInt("Age");
					int certificateValidate = loanInfo.getInt("CertificateValidate");	//学历认证
					int successCount = loanInfo.getInt("SuccessCount");	//成功次数
					double normalCount = loanInfo.getDouble("NormalCount");	//正常还清次数
					double overdueLessCount = loanInfo.getDouble("OverdueLessCount");	//逾期(1-15)还清次数
					int overdueMoreCount = loanInfo.getInt("OverdueMoreCount");	//逾期(15天以上)还清次数
//					String studyStyle = loanInfo.getString("StudyStyle");

					if(certificateValidate==1 && overdueMoreCount==0 && (overdueLessCount/normalCount)<=overNormalRate){
						if(gender==2 && age<45 && successCount>5){
							log.info("发现目标[魔镜等级" + creditCode + "][标号" + listingId + "]");
							log.info(loanInfo);
							
							List<Integer> listingIds = new ArrayList<Integer>();
							listingIds.add(listingId);
							bidding(listingIds, amount);
						}
						if(gender==1 && age<40 && successCount>10){
							log.info("发现目标[魔镜等级" + creditCode + "][标号" + listingId + "]");
							log.info(loanInfo);

							List<Integer> listingIds = new ArrayList<Integer>();
							listingIds.add(listingId);
							bidding(listingIds, amount);
						}
					}
				}
			}
		}
	}
	
	public static void bidding(List<Integer> listingIds, int amount) throws Exception{
		AuthInfoSingleton authInfo = AuthInfoSingleton.getInstance();
		String accessToken = authInfo.getAccessToken();
		
		for(int listingId : listingIds){
			log.info("尝试投标[标号" + listingId +  "]");
			Result biddingResult = OpenApiClient.send(biddingUrl,accessToken,
					new PropertyObject("ListingId",listingId,ValueTypeEnum.Int32),
					new PropertyObject("Amount",amount,ValueTypeEnum.Double),
					new PropertyObject("UseCoupon","true",ValueTypeEnum.String)
					);
			if(biddingResult.isSucess()){
				String message = biddingResult.getContext();
				int resultCode = JSONObject.fromObject(message).getInt("Result");
				if(resultCode == 0){
					log.error("■■■■■■■■■■■■■■■ 投标成功[标号" + listingId +  "]■■■■■■■■■■■■■■■■■■■");
					log.error("投标详情[" + biddingResult.getContext() + "]");
				} else {
					log.info("投标详情[" + biddingResult.getContext() + "]");
				}
			}
		}
	}
}
