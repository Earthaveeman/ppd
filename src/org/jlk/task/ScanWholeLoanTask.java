package org.jlk.task;

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

public class ScanWholeLoanTask extends TimerTask{
	private static Logger log = Logger.getLogger(ScanWholeLoanTask.class);

	private static String loanListUrl = ReadProperties.getProperties("loanListUrl");
	private static int CDAmount = Integer.parseInt((ReadProperties.getProperties("CDAmount")));

	@Override
	public void run() {
		try {
			for(int page=1; page<20; page++){
				
				Result result = OpenApiClient.send(loanListUrl,
						new PropertyObject("PageIndex", page, ValueTypeEnum.Int32));
				if (result.isSucess()) {
					
					JSONObject jsonObject = JSONObject.fromObject(result.getContext());
					JSONArray jsonArray = jsonObject.getJSONArray("LoanInfos");
					
					log.info("Scanning Whole Loans ... Page " + page + ", gets " + jsonArray.size());
					
					Util.findHighQualityCDListAndBid(jsonArray, CDAmount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
}
