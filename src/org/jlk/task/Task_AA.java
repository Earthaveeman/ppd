package org.jlk.task;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jlk.tools.ReadProperties;
import org.jlk.tools.Util;

import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.PropertyObject;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.ValueTypeEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Task_AA extends TimerTask{
	private static Logger log = Logger.getLogger(Task_AA.class);

	private String accessToken;
	
	private static String loanListUrl = ReadProperties.getProperties("loanListUrl");
	private static int AAAmount = Integer.parseInt((ReadProperties.getProperties("AAAmount")));
	
	public Task_AA(){
		
	}
	public Task_AA(String accessToken){
		this.accessToken = accessToken;
	}

	@Override
	public void run() {
		try {
			long begin = System.currentTimeMillis();
			Result result = OpenApiClient.send(loanListUrl,
					new PropertyObject("PageIndex", 1, ValueTypeEnum.Int32));
			long end = System.currentTimeMillis();
			if (result.isSucess()) {

				JSONObject jsonObject = JSONObject.fromObject(result.getContext());
				JSONArray jsonArray = jsonObject.getJSONArray("LoanInfos");
				
				log.info("Monitoring loan list ... Total " + jsonArray.size() + ", cost " + (end-begin) + "ms.");
				
				List<Integer> highQualityAAList = Util.getHighQualityAAList(jsonArray);
				
				if (highQualityAAList.size() > 0) {
					Util.bidding(accessToken, highQualityAAList, AAAmount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
}
