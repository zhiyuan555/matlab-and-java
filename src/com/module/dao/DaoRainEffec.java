/**
 * 
 */
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

/**
 * 关于表RainEffec的操作，该表有两个RainEffec_NJ与RainEffec_HH，分别保存的是怒江，红河的有效降雨量
 * @author admin
 *
 */
public class DaoRainEffec {
	
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
				+ "(ID , LONGITUDE , LATITUDE , TIME , EFFECRAIN) "
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
	 * 查询该日期包含记录数量
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
}
