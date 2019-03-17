package com.module.modle;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.module.dao.DaoRainData;
import com.module.dao.DaoRainEffecForWarning;
import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;

/**
 * RainData数据表相关业务
 * @author admin
 *
 */
public class ServiceRainData {
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 判断文本数据文件是否已经录入过
	 * @param filename
	 * @return true代表已经录入，false代表未录入
	 */
	public static boolean isUploaded(Connection conn , Date date) {
		int njCount = 0;
		int hhCount = 0;
		njCount = DaoRainData.queryCountByTime(conn , "RAINDATA_NJ" ,date);
		hhCount = DaoRainData.queryCountByTime(conn , "RAINDATA_HH" ,date);

		if(njCount > 0 || hhCount > 0) {
			return true;	
		}else {
			return false;
		}
	}
	
	/**
	 * 服务层对数据层录入数据的封装
	 * @param tableName
	 * @param datas
	 */
	public static void inputRainData(Connection conn , String tableName , List<RainDataEntity> datas) {
		DaoRainData.multInsert(conn , tableName, datas);
	}
	/**
	 * 截断表服务层封装
	 * @param tableName
	 */
	public static void truncateTable(Connection conn , String tableName) {
		DaoRainData.truncateTable(conn , tableName);
	}
	
	/**
	 * 服务层对数据层获取历史7天的衰减雨量和函数的封装
	 * @param conn
	 * @param tableName
	 * @param date
	 * @return
	 */
	public static List<RainDataEntity> getHistoryRainSum(Connection conn ,String tableName ,  Date date){
		return DaoRainData.getHistoryRainSum(conn, tableName, date);
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = Dbconn.getConnection();
		try {
			List<RainDataEntity> result = getHistoryRainSum(conn ,"RAINDATA_NJ" , SDF.parse("2016-06-06"));
			for(RainDataEntity one : result){
				if("848420".equals(one.getPointId())){
					System.out.println("id:" + one.getPointId());
					System.out.println("经度：" + one.getLongitude());
					System.out.println("纬度：" + one.getLatitude());
					System.out.println("高程：" + one.getAltitude());
					System.out.println("时间：" + SDF.format(one.getTime()));
					System.out.println("雨量：" + one.getRainData());
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dbconn.closeConn();
	}
}
