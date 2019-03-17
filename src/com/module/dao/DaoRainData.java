package com.module.dao;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;
/**
 * 
 * @author rxj
 *
 */
public class DaoRainData {
	
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
				+ "(ID , LONGITUDE , LATITUDE , ALTITUDE ,TIME , RAINDATA) "
				+ "values(?,?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i = 0 ; i < datas.size() ; i++){
				ps.setString(1, datas.get(i).getPointId());
				ps.setDouble(2, datas.get(i).getLongitude());
				ps.setDouble(3, datas.get(i).getLatitude());
				ps.setDouble(4, datas.get(i).getAltitude());
				ps.setDate(5, new java.sql.Date(datas.get(i).getTime().getTime()));
				ps.setDouble(6, datas.get(i).getRainData());
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
	 * 获取历史7天的衰减雨量和
	 * @return
	 */
	public static List<RainDataEntity> getHistoryRainSum(Connection conn ,String tableName ,  Date date){
		
		List<RainDataEntity> results = new ArrayList<RainDataEntity>();
		
		String formatDate = SDF.format(date);
		String sql = "select ID , bb.LONGITUDE , bb.LATITUDE , bb.ALTITUDE , (bb.TIME + 1) TIME ,\r\n" + 
				"(0.84*bb.RAINDATA + POWER(0.84, 2)*cc.RAINDATA + POWER(0.84, 3)*dd.RAINDATA + POWER(0.84, 4)*ee.RAINDATA + POWER(0.84, 5)*ff.RAINDATA + POWER(0.84, 6)*gg.RAINDATA + POWER(0.84, 7)*hh.RAINDATA) EFFECRAIN\r\n" + 
				"from (select ID , LONGITUDE , LATITUDE , ALTITUDE , TIME , RAINDATA from " + tableName + " where TIME=to_date('" + formatDate + "' , 'yyyy-mm-dd') - 1) bb \r\n" + 
				"JOIN (select ID , RAINDATA from " + tableName + " where TIME=to_date('" + formatDate + "' , 'yyyy-mm-dd') - 2) cc USING (ID)\r\n" + 
				"JOIN (select ID , RAINDATA from " + tableName + " where TIME=to_date('" + formatDate + "' , 'yyyy-mm-dd') - 3) dd USING (ID)\r\n" + 
				"JOIN (select ID , RAINDATA from " + tableName + " where TIME=to_date('" + formatDate + "' , 'yyyy-mm-dd') - 4) ee USING (ID)\r\n" + 
				"JOIN (select ID , RAINDATA from " + tableName + " where TIME=to_date('" + formatDate + "' , 'yyyy-mm-dd') - 5) ff USING (ID)\r\n" + 
				"JOIN (select ID , RAINDATA from " + tableName + " where TIME=to_date('" + formatDate + "' , 'yyyy-mm-dd') - 6) gg USING (ID)\r\n" + 
				"JOIN (select ID , RAINDATA from " + tableName + " where TIME=to_date('" + formatDate + "' , 'yyyy-mm-dd') - 7) hh USING (ID)";
		try {
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			while(rs.next()){
				RainDataEntity one = new RainDataEntity();
				one.setPointId(rs.getString("ID"));
				one.setLongitude(rs.getDouble("LONGITUDE"));
				one.setLatitude(rs.getDouble("LATITUDE"));
				one.setAltitude(rs.getDouble("ALTITUDE"));
				one.setTime(rs.getDate("TIME"));
				one.setRainData(rs.getDouble("EFFECRAIN"));
				results.add(one);
			}
			ps1.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = Dbconn.getConnection();
		try {
			List<RainDataEntity> result = getHistoryRainSum(conn ,"RAINDATA_NJ" , SDF.parse("2016-05-27"));
			for(RainDataEntity one : result){
				System.out.println("id:" + one.getPointId());
				System.out.println("经度：" + one.getLongitude());
				System.out.println("纬度：" + one.getLatitude());
				System.out.println("高程：" + one.getAltitude());
				System.out.println("时间：" + SDF.format(one.getTime()));
				System.out.println("雨量：" + one.getRainData());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dbconn.closeConn();
	}
}
