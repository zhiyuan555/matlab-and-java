package com.module.modle;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.module.dao.DaoRainData;
import com.module.dao.DaoRainEffec;
import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;

public class ServiceRainEffec {
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 判断当前日期是否已经计算过有效降雨量
	 * @param filename
	 * @return true代表已经录入，false代表未录入
	 */
	public static boolean isUploaded(Connection conn , Date date) {
		int njCount = 0;
		int hhCount = 0;
		njCount = DaoRainEffec.queryCountByTime(conn , "RAINEFFEC_NJ" ,date);
		hhCount = DaoRainEffec.queryCountByTime(conn , "RAINEFFEC_HH" ,date);

		if(njCount > 0 || hhCount > 0) {
			return true;	
		}else {
			return false;
		}
	}
	/**
	 * 服务层对数据层录入数据的封装
	 * @param rainEffecTabName
	 * @param rainDataTabName
	 * @param date
	 */
	public static void inputRainEffec(Connection conn , String tableName , List<RainDataEntity> datas) {
		DaoRainEffec.multInsert(conn, tableName, datas);
	}
	
	/**
	 * 根据7天实际发生雨量+1天预报雨量进行计算有效降雨量并录入表中
	 */
	public static void inputRainEffecFrom8Day(Connection conn , String rainEffecTabName ,String rainTabName , String radarRainTabName ,  Date date){
		
		List<RainDataEntity> rainEffecs = ServiceRainData.getHistoryRainSum(conn, rainTabName, date);
		for(RainDataEntity one : rainEffecs){
			//当天预报量
			double idwValue = ServiceRadarRainData.getIdwValue(conn, radarRainTabName, date, one.getLongitude(), one.getLatitude());
			//过去7天累加量
			double sumRain7 = one.getRainData();
			one.setRainData(sumRain7 + idwValue);
		}
		inputRainEffec(conn , rainEffecTabName , rainEffecs);
	}
	
	/**
	 * 截断表服务层封装
	 * @param tableName
	 */
	public static void truncateTable(Connection conn , String tableName) {
		DaoRainEffec.truncateTable(conn , tableName);
	}
	
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = Dbconn.getConnection();
		try {
			inputRainEffecFrom8Day(conn , "RAINEFFEC_NJ" ,"RAINDATA_NJ" , "RADARRAINDATA_NJ" ,  SDF.parse("2016-06-06"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dbconn.closeConn();
	}
}
