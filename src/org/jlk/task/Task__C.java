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

public class Task__C extends TimerTask{
	private static Logger log = Logger.getLogger(Task__C.class);
	
	private static String accessToken = ReadProperties.getProperties("accessToken");
	private static String loanListUrl = ReadProperties.getProperties("loanListUrl");
	private static int CDAmount = Integer.parseInt((ReadProperties.getProperties("CDAmount")));

	@Override
	public void run() {
		try {
			Result result = OpenApiClient.send(loanListUrl,
					new PropertyObject("PageIndex", 1, ValueTypeEnum.Int32));
			if (result.isSucess()) {

				JSONObject jsonObject = JSONObject.fromObject(result.getContext());
				JSONArray jsonArray = jsonObject.getJSONArray("LoanInfos");
				
				log.info("获取可投标列表 ... ...  数量 " + jsonArray.size());

				List<Integer> highQualityC22List = Util.getHighQualityC22List(jsonArray);
				
				if (highQualityC22List.size() > 0) {
					Util.bidding(accessToken, highQualityC22List, CDAmount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
}
