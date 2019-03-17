package com.module.modle;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.module.dao.DaoRadarRainData;
import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;

public class ServiceRadarRainData {

/**
 * RadarRainData数据表相关业务
 * @author admin
 *
 */
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 判断文本数据文件是否已经录入过
	 * @param filename
	 * @return true代表已经录入，false代表未录入
	 */
	public static boolean isUploaded(Connection conn , Date date) {
		int njCount = 0;
		int hhCount = 0;
		njCount = DaoRadarRainData.queryCountByTime(conn , "RADARRAINDATA_NJ" ,date);
		hhCount = DaoRadarRainData.queryCountByTime(conn , "RADARRAINDATA_HH" ,date);

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
		DaoRadarRainData.multInsert(conn , tableName, datas);
	}
	/**
	 * 截断表服务层封装
	 * @param tableName
	 */
	public static void truncateTable(Connection conn , String tableName) {
		DaoRadarRainData.truncateTable(conn , tableName);
	}
	
	/**
	 * 获取反距离插值
	 * @param conn
	 * @param tableName
	 * @param time
	 * @param x
	 * @param y
	 * @return
	 */
	public static double getIdwValue(Connection conn , String tableName , Date time , double x , double y){
		return DaoRadarRainData.getIdwValue(conn, tableName, time, x, y);
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = Dbconn.getConnection();
		try {
			System.out.println(getIdwValue(conn , "RADARRAINDATA_NJ" , SDF.parse("2016-06-06") ,98.83 , 27.3942));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dbconn.closeConn();
	}
}
