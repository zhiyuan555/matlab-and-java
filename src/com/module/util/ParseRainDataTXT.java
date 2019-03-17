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


public class ParseRainDataTXT {
	/**
	 * 解析原始文件txt为一个map，map中包含红河州的降雨数据与怒江州的降雨数据两不封，筛选是通过示范区外接矩形框经纬度进行的筛选
	 * @param file
	 * @return
	 */
	public static Map<String , List<RainDataEntity>> parseRainDataTXT(File file) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = file.getName();
		String dateStr = fileName.substring(8, 12) + "-" + fileName.substring(12, 14) + "-" + fileName.substring(14, 16);
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
			while(scan.hasNextLine()) {
				String oneRecord = scan.nextLine();
				String recordArr[] = oneRecord.split("[ ]+");
				if(recordArr.length < 5) {
					continue;
				}else {
					double longitude = Double.parseDouble(recordArr[1]);
					double latitude = Double.parseDouble(recordArr[2]);
					if(longitude >= 98.130806 && longitude <= 99.135168 && latitude >= 25.552698 && latitude <= 28.397261) {
						RainDataEntity oneRainData = new RainDataEntity();
						oneRainData.setPointId(recordArr[0]);
						oneRainData.setLongitude(longitude);
						oneRainData.setLatitude(latitude);
						oneRainData.setAltitude(Double.parseDouble(recordArr[3]));
						oneRainData.setTime(date);
						oneRainData.setRainData(Double.parseDouble(recordArr[4]));
						rainDatas.get("nujiang").add(oneRainData);
					}
					
					if(longitude >= 102.525131 && longitude <= 104.278433 && latitude >= 22.442318 && latitude <= 23.390396) {
						RainDataEntity oneRainData = new RainDataEntity();
						oneRainData.setPointId(recordArr[0]);
						oneRainData.setLongitude(longitude);
						oneRainData.setLatitude(latitude);
						oneRainData.setAltitude(Double.parseDouble(recordArr[3]));
						oneRainData.setTime(date);
						oneRainData.setRainData(Double.parseDouble(recordArr[4]));
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
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("E:\\各种数据\\数据仓库相关数据\\云南区域站24小时雨量数据\\JSSK_QY_2014091508.024.txt");
		if(file.getName().length() == 26 && "JSSK_QY_".equals(file.getName().substring(0, 8)) && ".txt".equals(file.getName().substring(22, 26))) {
			System.out.println(file.getName());
		}else {
			System.out.println("不符合！");
		}
/*		Map<String , List<RainDataEntity>> rainDatas = null;
		rainDatas = parseRainDataTXT(file);
		if(rainDatas.isEmpty() || rainDatas == null) {
			
		}else {
			List<RainDataEntity> nujiang = rainDatas.get("nujiang");
			List<RainDataEntity> honghe = rainDatas.get("honghe");
			for(RainDataEntity one : nujiang) {
				System.out.println(one);
				System.out.println("---------------------------------------------");
			}
			System.out.println("**********************示范区分界线*************************");
			for(RainDataEntity one : honghe) {
				System.out.println(one);
				System.out.println("---------------------------------------------");
			}
		}*/
	}
}
