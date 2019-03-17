package com.module.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.module.dbconn.Dbconn;

public class DaoTimeMapToJobId {
	
	/**
	 * 插入时间与gp服务jobId的映射关系
	 * @param time
	 * @param jobId
	 */
	
	public static void insert(Connection conn , String time , String jobId) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sql = new StringBuffer("insert into TIMEMAPTOJOBID (SELECTTIME , JOBID , INSERTTIME) values (");
		
		sql.append("'" + time + "'");
		sql.append(" , ");
		sql.append("'" + jobId + "'");
		sql.append(" , to_date('");
		sql.append(sdf.format(new Date(System.currentTimeMillis())));
		sql.append("','yyyy-mm-dd hh24:mi:ss'))");
		String commitSql = "COMMIT";
		try {
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			PreparedStatement ps2 = conn.prepareStatement(commitSql);
			ps1.execute();
			ps2.execute();
			ps1.close();
			ps2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sql);
	}
	/**
	 * 通过时间查询记录条数
	 * @param conn
	 * @param time
	 * @return
	 */
	public static String queryJobIdByTime(Connection conn , String time) {

		String sql = "select JOBID from TIMEMAPTOJOBID where SELECTTIME = ?";
		String jobId = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, time);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				jobId = rs.getString("JOBID");
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jobId;
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
