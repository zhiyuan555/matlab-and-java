package com.module.modle;

import java.sql.Connection;
import java.util.Date;

import com.module.dao.DaoRainEffecForWarning;
import com.module.dbconn.Dbconn;

public class ServiceRainEffecForWarning {
	
	/**
	 * 对DaoRainEffecForWarning插入数据的包装
	 * @param toTable
	 * @param fromTable
	 * @param date
	 */
	public static void insertData(Connection conn ,String toTable , String fromTable , Date date , int hadNum) {
		DaoRainEffecForWarning.insertData(conn , toTable, fromTable, date , hadNum);
	}
	/**
	 * 判断数据库中是否有该天的数据
	 * @param tableName
	 * @param time
	 * @return
	 */
	public static int getDateDataNumber(Connection conn , String tableName ,Date time) {
		
		return DaoRainEffecForWarning.queryCountByTime(conn , tableName, time);
	}
	/**
	 * 判断某天是否上传过
	 * @param conn
	 * @param time
	 * @return
	 */
	public static boolean isUpload(Connection conn ,Date time){
		if(getDateDataNumber(conn , "HONGHE_RAINEFFEC" ,time) > 0 || getDateDataNumber(conn , "NUJIANG_RAINEFFEC" ,time) > 0){
			return true;
		}else{
			return false;
		}
		
	}
	public static int queryAllCount(Connection conn , String tableName){
		return DaoRainEffecForWarning.queryAllCount(conn, tableName);
	}
	/**
	 * 截断表服务层封装
	 * @param tableName
	 */
	public static void truncateTable(Connection conn , String tableName) {
		DaoRainEffecForWarning.truncateTable(conn , tableName);
	}
}
