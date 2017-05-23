package org.jlk.task;

import java.text.SimpleDateFormat;
import java.util.Date;
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

	private static String loanListUrl = ReadProperties.getProperties("loanListUrl");
	private static int CDAmount = Integer.parseInt((ReadProperties.getProperties("CDAmount")));

	@Override
	public void run() {
		try {
			Date now = new Date();
			Date startDateTime = new Date(now.getTime() - (1*60*1000));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr = sdf.format(startDateTime);
			
			long begin = System.currentTimeMillis();
			Result result = OpenApiClient.send(loanListUrl,
					new PropertyObject("PageIndex", 1, ValueTypeEnum.Int32),
					new PropertyObject("StartDateTime", dateStr, ValueTypeEnum.DateTime));
			long end = System.currentTimeMillis();
			if (result.isSucess()) {

				JSONObject jsonObject = JSONObject.fromObject(result.getContext());
				JSONArray jsonArray = jsonObject.getJSONArray("LoanInfos");

				log.info("Monitoring Loan List ... Gets " + jsonArray.size() + ", takes " + (end-begin) + "ms.");

				List<Integer> highQualityC22List = Util.getHighQualityC22List(jsonArray);
				
				if (highQualityC22List.size() > 0) {
					Util.bidding(highQualityC22List, CDAmount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
}
