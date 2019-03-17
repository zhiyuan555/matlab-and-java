package com.module.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.module.entity.RainDataEntity;

/**
 * 解析radaraindata的文本文件
 * @author Administrator
 *
 */
public class ParseRadarRainData {
	/**
	 * 解析原始文件txt为一个map，map中包含红河州的降雨数据与怒江州的降雨数据两部分，筛选是通过示范区外接矩形框经纬度进行的筛选
	 * @param file
	 * @return
	 */
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	public static Map<String , List<RainDataEntity>> parseRainDataTXT(File file) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = file.getName();
		String dateStr = fileName.substring(5, 9) + "-" + fileName.substring(9, 11) + "-" + fileName.substring(11, 13);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream is = null;
		Scanner scan = null;
		Map<String , List<RainDataEntity>> rainDatas = new HashMap<String , List<RainDataEntity>>();
		rainDatas.put("honghe", new ArrayList<RainDataEntity>());
		rainDatas.put("nujiang", new ArrayList<RainDataEntity>());
		try {
			is = new FileInputStream(file);
			scan = new Scanner(is);
			for(int i = 0 ; i < 4 ; i++){
				scan.nextLine();
			}
			while(scan.hasNextLine()) {
				String oneRecord = scan.nextLine().trim();
				
				String recordArr[] = oneRecord.split("[ ]+");
				if(recordArr.length < 4) {
					continue;
				}else {
					double longitude = Double.parseDouble(recordArr[1]);
					double latitude = Double.parseDouble(recordArr[2]);
					if(longitude >= 98.130806 && longitude <= 99.135168 && latitude >= 25.552698 && latitude <= 28.397261) {
						RainDataEntity oneRainData = new RainDataEntity();
						oneRainData.setPointId(recordArr[0]);
						oneRainData.setLongitude(longitude);
						oneRainData.setLatitude(latitude);
						oneRainData.setAltitude(0);
						oneRainData.setTime(date);
						oneRainData.setRainData(Double.parseDouble(recordArr[3]));
						rainDatas.get("nujiang").add(oneRainData);
					}
					
					if(longitude >= 102.525131 && longitude <= 104.278433 && latitude >= 22.442318 && latitude <= 23.390396) {
						RainDataEntity oneRainData = new RainDataEntity();
						oneRainData.setPointId(recordArr[0]);
						oneRainData.setLongitude(longitude);
						oneRainData.setLatitude(latitude);
						oneRainData.setAltitude(0);
						oneRainData.setTime(date);
						oneRainData.setRainData(Double.parseDouble(recordArr[3]));
						rainDatas.get("honghe").add(oneRainData);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(scan != null) {
				try {
					scan.close();
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return rainDatas;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		File file = new File("C:\\Users\\Administrator\\Desktop\\测试数据\\新建文件夹\\GBEKM2016060620.24.024.txt");
		
		Map<String , List<RainDataEntity>> results = parseRainDataTXT(file);
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
	}
}
