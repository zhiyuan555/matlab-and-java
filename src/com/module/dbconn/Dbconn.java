package com.module.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dbconn {
	private static String driver = "oracle.jdbc.OracleDriver"; 
	private static String url = "jdbc:oracle:thin:@11.10.10.23:1521:orcl";
	private static String user = "dzdx";
	private static String password = "dzdx";
	private static Connection conn = null;
	public static Connection getConnection() {
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		}catch(ClassNotFoundException e) {
			System.out.println("类未找到！");
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConn() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("数据库关闭失败");
			}			
		}
	}
	
	//连接测试
	public static void main(String[] args) {
		Connection conn = getConnection();
		String sql = "select * from JFBJ01A";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				String str = rs.getString("JFBJ01A020");
				System.out.println(str);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConn();
	}
}
