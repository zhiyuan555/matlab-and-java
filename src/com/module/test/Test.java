package com.module.test;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;
import com.module.modle.ServiceRadarRainData;
import com.module.modle.ServiceRainData;
import com.module.modle.ServiceRainEffec;
import com.module.util.ParseRadarRainData;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection conn = Dbconn.getConnection();
		ServiceRainData.truncateTable(conn, "RAINDATA_HH");
		ServiceRainData.truncateTable(conn, "RAINDATA_NJ");
		ServiceRainEffec.truncateTable(conn, "RAINEFFEC_HH");
		ServiceRainEffec.truncateTable(conn, "RAINEFFEC_NJ");
		ServiceRadarRainData.truncateTable(conn, "RADARRAINDATA_NJ");
		ServiceRadarRainData.truncateTable(conn, "RADARRAINDATA_HH");
		Dbconn.closeConn();
		System.out.println("截断完成！");
		
	/*	Connection conn = Dbconn.getConnection();
		
		File file = new File("C:\\Users\\Administrator\\Desktop\\测试数据\\新建文件夹\\GBEKM2016060620.24.024.txt");
		
		Map<String , List<RainDataEntity>> results = ParseRadarRainData.parseRainDataTXT(file);
		List<RainDataEntity> nujiang = results.get("nujiang");
		List<RainDataEntity> honghe = results.get("honghe");
		
		System.out.println("*************************怒江***************************");
		System.out.println(nujiang.size());
		for(RainDataEntity one : nujiang){
			System.out.println("经度：" + one.getLongitude() + "   纬度：" + one.getLatitude() + "   雨量：" + one.getRainData());
		}
		System.out.println("*************************红河***************************");
		System.out.println(honghe.size());
		for(RainDataEntity two : honghe){
			
			System.out.println("经度：" + two.getLongitude() + "   纬度：" + two.getLatitude() + "   雨量：" + two.getRainData());
		}
		
		System.out.println("解析结束！");	
		ServiceRadarRainData.inputRainData(conn, "RADARRAINDATA_NJ", nujiang);
		System.out.println("怒江表录入结束！");
		ServiceRadarRainData.inputRainData(conn, "RADARRAINDATA_HH", honghe);
		System.out.println("红河表录入结束！");
		Dbconn.closeConn();*/
	}

}
