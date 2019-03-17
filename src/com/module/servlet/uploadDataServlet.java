package com.module.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;
import com.module.modle.ServiceRadarRainData;
import com.module.modle.ServiceRainData;
import com.module.modle.ServiceRainEffec;
import com.module.modle.ServiceRainEffecForWarning;
import com.module.util.ParseRadarRainData;
import com.module.util.ParseRainDataTXT;


@WebServlet("/UploadDataServlet")
public class uploadDataServlet extends HttpServlet {
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	public uploadDataServlet() {
		super();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 9204049211127837015L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/**
	 * 将文本数据录入至数据库表"RAINDATA_NJ" 与 "RAINDATA_HH"中，返回"ok"代表录入成功
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Connection conn = Dbconn.getConnection();
		String filePath = req.getParameter("filePath");
		File file = new File(filePath);
		System.out.println("输入路径为:" + filePath);
		
		//用来保存本次录入的文件的日期，后面计算信息量时遍历该list
		List<Date> inputDates = new ArrayList<Date>();
		System.out.println(file.exists());
		if(file.exists()) {
			if(file.isFile()) {
				String fileName = file.getName();
				//判别文件名是否符合，不符合跳过
				if(fileName.length() == 26 && "JSSK".equals(fileName.substring(0, 4)) && ".txt".equals(fileName.substring(22, 26))) {
					
					//解析文件名得到日期
					Date date = null;
					try {
						date = SDF.parse(fileName.substring(8, 12) + "-" + fileName.substring(12, 14) + "-" + fileName.substring(14, 16));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//判断文件是否已经录入过
					if(ServiceRainData.isUploaded(conn , date)) {
						System.out.println("文件"+ fileName +"已经录入了！");
						resp.getWriter().print("文件"+ fileName +"已经录入了！");
						Dbconn.closeConn();
						return;
					}else {
						Map<String , List<RainDataEntity>> rainDatas = ParseRainDataTXT.parseRainDataTXT(file);
						ServiceRainData.inputRainData(conn , "RAINDATA_NJ", rainDatas.get("nujiang"));
						ServiceRainData.inputRainData(conn , "RAINDATA_HH", rainDatas.get("honghe"));
						System.out.println("雨量数据录入完成！");
					}
				}else if(fileName.length() == 26 && "GBEKM".equals(fileName.substring(0, 5)) && ".txt".equals(fileName.substring(22, 26))){

					//解析文件名得到日期
					Date date = null;
					try {
						date = SDF.parse(fileName.substring(5, 9) + "-" + fileName.substring(9, 11) + "-" + fileName.substring(11, 13));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//添加日期以后面计算信息量
					if(date != null) {
						inputDates.add(date);
					}
					
					
					//输出调试
					System.out.println(fileName);
					
					
					//判断文件是否已经录入过
					if(ServiceRadarRainData.isUploaded(conn , date)) {
						System.out.println("文件"+ fileName +"已经录入了！");
						resp.getWriter().print("文件"+ fileName +"已经录入了！");
						Dbconn.closeConn();
						return;
					}else {
						Map<String , List<RainDataEntity>> rainDatas = ParseRadarRainData.parseRainDataTXT(file);
						ServiceRadarRainData.inputRainData(conn , "RADARRAINDATA_NJ", rainDatas.get("nujiang"));
						ServiceRadarRainData.inputRainData(conn , "RADARRAINDATA_HH", rainDatas.get("honghe"));
						System.out.println("雨量数据录入完成！");
					}
				}else {
					System.out.println("文件名不符合！");
					resp.getWriter().print("文件名不符合！");
					Dbconn.closeConn();
					return;
				}
			}else if(file.isDirectory()) {
				
				File childrenFile[] = file.listFiles();
				for(File child : childrenFile) {
					String fileName = child.getName();
					if(fileName.length() == 26 && "JSSK".equals(fileName.substring(0, 4)) && ".txt".equals(fileName.substring(22, 26))) {
						

						//解析文件名得到日期
						Date date = null;
						try {
							date = SDF.parse(fileName.substring(8, 12) + "-" + fileName.substring(12, 14) + "-" + fileName.substring(14, 16));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//判断文件是否已经录入过
						if(ServiceRainData.isUploaded(conn , date)) {
							System.out.println("文件"+ fileName +"已经录入了！");
							continue;
						}else {
							Map<String , List<RainDataEntity>> rainDatas = ParseRainDataTXT.parseRainDataTXT(child);
							
							ServiceRainData.inputRainData(conn , "RAINDATA_NJ", rainDatas.get("nujiang"));
							ServiceRainData.inputRainData(conn , "RAINDATA_HH", rainDatas.get("honghe"));
							System.out.println("文件"+ fileName +"录入完成");
						}
					}else if(fileName.length() == 26 && "GBEKM".equals(fileName.substring(0, 5)) && ".txt".equals(fileName.substring(22, 26))){
						
						
						//解析文件名得到日期
						Date date = null;
						try {
							date = SDF.parse(fileName.substring(5, 9) + "-" + fileName.substring(9, 11) + "-" + fileName.substring(11, 13));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//添加日期以后面计算信息量
						if(date != null) {
							inputDates.add(date);
						}
						
						//判断文件是否已经录入过
						if(ServiceRadarRainData.isUploaded(conn , date)) {
							System.out.println("文件"+ fileName +"已经录入了！");
							continue;
						}else {
							Map<String , List<RainDataEntity>> rainDatas = ParseRadarRainData.parseRainDataTXT(child);
							ServiceRadarRainData.inputRainData(conn , "RADARRAINDATA_NJ", rainDatas.get("nujiang"));
							ServiceRadarRainData.inputRainData(conn , "RADARRAINDATA_HH", rainDatas.get("honghe"));
							System.out.println("文件"+ fileName +"录入完成");
						}
					}else {
						System.out.println("文件名不符合！");
						continue;
					}
				}
				System.out.println("雨量数据录入完成！");
			}
		}else {
			System.out.println("文件或文件夹不存在！请正确的路径！");
			resp.getWriter().print("文件或文件夹不存在！请正确的路径！");
			Dbconn.closeConn();
			return;
		}
		
		/**
		 * 重点修改内容
		 * 
		 */
		
		
		//计算有效降雨量，并保存至数据库中
		if(!inputDates.isEmpty()) {
			for(Date date : inputDates) {
				if(ServiceRainEffec.isUploaded(conn , date)) {
					System.out.println("日期"+SDF.format(date)+"已经录入了！");
				}else {
					ServiceRainEffec.inputRainEffecFrom8Day(conn , "RAINEFFEC_NJ", "RAINDATA_NJ", "RADARRAINDATA_NJ", date);
					ServiceRainEffec.inputRainEffecFrom8Day(conn , "RAINEFFEC_HH", "RAINDATA_HH", "RADARRAINDATA_HH", date);
					System.out.println("日期"+SDF.format(date)+"计算有效降雨量完成！");
					
					//计算降雨诱发概率
					if(ServiceRainEffecForWarning.isUpload(conn , date)) {
						System.out.println("日期"+SDF.format(date)+"已经计算降雨诱发概率了！");
						continue;
					}else {
						int hongheCount = ServiceRainEffecForWarning.queryAllCount(conn, "HONGHE_RAINEFFEC");
						int nujiangCount = ServiceRainEffecForWarning.queryAllCount(conn, "NUJIANG_RAINEFFEC");
						ServiceRainEffecForWarning.insertData(conn , "HONGHE_RAINEFFEC", "RAINEFFEC_HH" , date , hongheCount);
						ServiceRainEffecForWarning.insertData(conn , "NUJIANG_RAINEFFEC", "RAINEFFEC_NJ" , date , nujiangCount);
						System.out.println("日期"+SDF.format(date)+"计算降雨诱发概率完成！");
					}
				}
			}
			
			System.out.println("计算有效降雨量完成！");
		}
		
		resp.getWriter().print("ok");
		Dbconn.closeConn();
	}
}
