package com.module.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;

public class DaoRadarRainData {
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 批量插入数据
	 * @param tableName
	 * @param datas
	 */

	public static void multInsert(Connection conn , String tableName , List<RainDataEntity> datas) {
		if(datas.isEmpty()) {
			System.out.println("数据为空，不能插入！");
			return;
		}
		String sql = "insert into " + tableName
				+ "(ID , LONGITUDE , LATITUDE , TIME , RAINDATA) "
				+ "values(?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i = 0 ; i < datas.size() ; i++){
	
					ps.setString(1, datas.get(i).getPointId());
	
				ps.setDouble(2, datas.get(i).getLongitude());
				ps.setDouble(3, datas.get(i).getLatitude());
				ps.setDate(4, new java.sql.Date(datas.get(i).getTime().getTime()));
				ps.setDouble(5, datas.get(i).getRainData());
				ps.addBatch();
				if(i % 500 == 0 || i == datas.size()-1){
					ps.executeBatch();
				}
			}
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 通过时间查询记录条数
	 * @param conn
	 * @param tableName
	 * @param time
	 * @return
	 */
	public static int queryCountByTime(Connection conn , String tableName ,Date time) {
		
		StringBuffer sql = new StringBuffer("select count(ID) from ");
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
	 * 获取与某一点的幂为2的反距离和
	 */
	public static double getInversedistanceSum(Connection conn , String tableName , Date time , double x , double y){
		
		String sql = "select SUM(1/(power(("+ x +" - LONGITUDE),2) + power(("+ y +" - LATITUDE),2))) from "+ tableName +" where time = to_date('"+ SDF.format(time) +"' , 'yyyy-mm-dd')" ;
		PreparedStatement ps;
		double result = 0;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getDouble(1);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据点坐标与日期查询雨量值
	 * @param conn
	 * @param tableName
	 * @param time
	 * @param x
	 * @param y
	 * @return 返回-999表示没有该条记录
	 */
	public static double queryValueByXYandDate(Connection conn , String tableName , Date time , double x , double y){
		String sql = "select RAINDATA "
				+ "from " + tableName + " "
				+ "where LONGITUDE = "+ x +" and LATITUDE = "+ y +" and time = to_date('"+ SDF.format(time) +"' , 'yyyy-mm-dd')";
		PreparedStatement ps;
		double raindata = -999;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				raindata = rs.getDouble(1);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return raindata;
	}
	
	/**
	 * 获取反距离插值
	 * @return
	 */
	public static double getIdwValue(Connection conn , String tableName , Date time , double x , double y){
		
		double raindata = queryValueByXYandDate(conn , tableName , time , x , y);
		if(raindata != -999){
			return raindata;
		}else{
			//获取幂为2的反距离和
			double inversedistanceSum = getInversedistanceSum(conn , tableName , time , x , y);
			String sql = "select SUM(((1/(power(("+ x +" - LONGITUDE),2) + power(("+ y +" - LATITUDE),2))) / " + inversedistanceSum + ") * RAINDATA) from "+ tableName +" where time = to_date('"+ SDF.format(time) +"' , 'yyyy-mm-dd')"; 
			PreparedStatement ps;
			double result = 0;
			try {
				ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					result = rs.getDouble(1);
				}
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
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
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = Dbconn.getConnection();
		try {
			System.out.println(getIdwValue(conn , "RADARRAINDATA_NJ" , SDF.parse("2016-05-27") ,98.25 , 28.15));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			System.out.println(queryCountByTime(conn , "RADARRAINDATA_NJ" ,SDF.parse("2016-05-27")));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Dbconn.closeConn();
	}
}
