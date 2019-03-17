package com.module.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.module.dbconn.Dbconn;

public class DaoRainEffecForWarning {
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 从存储有效降雨量的表中查询信息量并计算降雨诱发概率
	 * @param toTable
	 * @param fromTable
	 * @param date
	 */
	public static void insertData(Connection conn , String toTable , String fromTable , Date date , int hadNum) {
		
		String dateStr = SDF.format(date);
		String dateStrs[] = dateStr.split("-");
		String sql = "INSERT INTO "+ toTable +" (OBJECTID , POINTID , LONGITUDE , LATITUDE , ALTITUDE , TIME ,  RAINEFFEC , YEAR_ , MONTH_ , DAY_ ,SHAPE) \r\n" + 
				"SELECT ROWNUM + "+ hadNum + " , ID , LONGITUDE , LATITUDE , ALTITUDE , TIME , (exp(0.076*EFFECRAIN - 3.675)/(1 + exp(0.076*EFFECRAIN - 3.675))) , "+ dateStrs[0]+" , "+ dateStrs[1] +" , " + dateStrs[2] + " ,MDSYS.SDO_GEOMETRY(2001,NULL,MDSYS.SDO_POINT_TYPE(LONGITUDE,LATITUDE,NULL),NULL,NULL) \r\n" + 
				"FROM "+ fromTable +" WHERE TIME=to_date('"+ dateStr +"' , 'yyyy-mm-dd')";
		String commitSql = "COMMIT";
		try {
			PreparedStatement ps1 = conn.prepareStatement(sql);
			PreparedStatement ps2 = conn.prepareStatement(commitSql);
			ps1.execute();
			ps2.execute();
			ps1.close();
			ps2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 截断表
	 * @param tableName
	 */
	public static void truncateTable(Connection conn , String tableName) {
		String sql = "TRUNCATE TABLE " + tableName;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 根据时间查询表
	 * @param tableName
	 * @param time
	 * @return
	 */
	public static int queryCountByTime(Connection conn , String tableName ,Date time) {
		

		StringBuffer sql = new StringBuffer("select count(POINTID) from ");
		sql.append(tableName);
		sql.append(" where TIME = to_date('");
		sql.append(SDF.format(time));
		sql.append("' , 'yyyy-mm-dd')");
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 根据时间查询表
	 * @param tableName
	 * @param time
	 * @return
	 */
	public static int queryAllCount(Connection conn , String tableName) {
		

		StringBuffer sql = new StringBuffer("select count(POINTID) from ");
		sql.append(tableName);
		int count = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		Connection conn = Dbconn.getConnection();
		insertData(conn , "HONGHE_RAINEFFEC", "RAINEFFEC_HH" , SDF.parse("2018-11-18") , 135);
		Dbconn.closeConn();
	}
}
