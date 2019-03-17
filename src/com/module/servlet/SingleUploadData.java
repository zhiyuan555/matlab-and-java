package com.module.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.module.dbconn.Dbconn;
import com.module.entity.RainDataEntity;
import com.module.modle.ServiceRadarRainData;
import com.module.modle.ServiceRainData;
import com.module.modle.ServiceRainEffec;
import com.module.modle.ServiceRainEffecForWarning;
import com.module.util.ParseRadarRainData;
import com.module.util.ParseRainDataTXT;



public class SingleUploadData extends HttpServlet {
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	public SingleUploadData() {
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
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		File file = null;
		//临时目录
		String parentDir = "C:\\EclipseWorkspace\\geologicalDisasterWarning\\upload";
		System.out.println("转至了当前servlet!");
		
		//1 工厂
        FileItemFactory fileItemFactory = new DiskFileItemFactory();
        
        //2 核心类
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        
        //防止中文乱码
		servletFileUpload.setHeaderEncoding("UTF-8"); 
        //3 解析request  ,List存放 FileItem （表单元素的封装对象，一个<input>对应一个对象）
        List<FileItem> list = null;
		try {
			list = servletFileUpload.parseRequest(req);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //4 遍历集合获得数据
        for (FileItem fileItem : list) {
            if(fileItem.isFormField()){
                // 5 是否为表单字段（普通表单元素）
                //5.1.表单字段名称
                String fieldName = fileItem.getFieldName();
                System.out.println(fieldName);
                //5.2.表单字段值
                String fieldValue = fileItem.getString("UTF-8");    //中文会出现乱码
                System.out.println(fieldValue);
            } else {
                //6 上传字段（上传表单元素）
                //6.1.表单字段名称  fileItem.getFieldName();
                //6.2.上传文件名
                String fileName = fileItem.getName();
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                
                System.out.println(fileName);    
                //6.3.上传内容
                InputStream is = fileItem.getInputStream();    //获得输入流，
                
                file = new File(parentDir,fileName);
                if(! file.getParentFile().exists()){  //父目录不存在
                    file.getParentFile().mkdirs();        //mkdirs():创建文件夹，如果上级目录没有的话，也一并创建出来。
                }
                FileOutputStream out = new FileOutputStream(file);    
                byte[] buf = new byte[1024];
                int len = -1;
                while( (len = is.read(buf)) != -1){
                    out.write(buf, 0, len);
                }
                //关闭
                out.close();
                is.close();
            }
        }
        
        
        //建立数据库连接
        Connection conn = Dbconn.getConnection();
        String fileName = file.getName();

		Date date = null;
		
		//判别文件名是否符合，不符合跳过
		if(fileName.length() == 26 && "JSSK".equals(fileName.substring(0, 4)) && ".txt".equals(fileName.substring(22, 26))) {
			try {
				//解析文件名得到日期
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
				//3代表文件已经录入过了
				resp.sendRedirect(req.getContextPath()+"/timechange.jsp?uploadResult=3");
				return;
			}else {
				Map<String , List<RainDataEntity>> rainDatas = ParseRainDataTXT.parseRainDataTXT(file);
				ServiceRainData.inputRainData(conn , "RAINDATA_NJ", rainDatas.get("nujiang"));
				ServiceRainData.inputRainData(conn , "RAINDATA_HH", rainDatas.get("honghe"));
				System.out.println("雨量数据录入完成！");
			}
		}else if(fileName.length() == 26 && "GBEKM".equals(fileName.substring(0, 5)) && ".txt".equals(fileName.substring(22, 26))){

			try {
				//解析文件名得到日期
				date = SDF.parse(fileName.substring(5, 9) + "-" + fileName.substring(9, 11) + "-" + fileName.substring(11, 13));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//判断文件是否已经录入过
			if(ServiceRadarRainData.isUploaded(conn , date)) {
				System.out.println("文件"+ fileName +"已经录入了！");
				resp.getWriter().print("文件"+ fileName +"已经录入了！");
				Dbconn.closeConn();
				
				//3代表文件已经录入过了
				resp.sendRedirect(req.getContextPath()+"/timechange.jsp?uploadResult=3");
				return;
			}else {
				Map<String , List<RainDataEntity>> rainDatas = ParseRadarRainData.parseRainDataTXT(file);
				ServiceRadarRainData.inputRainData(conn , "RADARRAINDATA_NJ", rainDatas.get("nujiang"));
				ServiceRadarRainData.inputRainData(conn , "RADARRAINDATA_HH", rainDatas.get("honghe"));
				System.out.println("雨量数据录入完成！");
			}
			
			//将临时传入的文件删除
			if(file.exists()){
				file.delete();
			}
			
			//计算有效降雨量
			if(date != null) {
				if(ServiceRainEffec.isUploaded(conn , date)) {
					System.out.println("日期"+SDF.format(date)+"已经录入了！");
				}else {

					ServiceRainEffec.inputRainEffecFrom8Day(conn , "RAINEFFEC_NJ", "RAINDATA_NJ", "RADARRAINDATA_NJ", date);
					ServiceRainEffec.inputRainEffecFrom8Day(conn , "RAINEFFEC_HH", "RAINDATA_HH", "RADARRAINDATA_HH", date);
					System.out.println("日期"+SDF.format(date)+"计算有效降雨量完成！");
					
					//计算降雨诱发概率
					if(ServiceRainEffecForWarning.isUpload(conn , date)) {
						System.out.println("日期"+SDF.format(date)+"已经计算降雨诱发概率了！");
					}else {
						int hongheCount = ServiceRainEffecForWarning.queryAllCount(conn, "HONGHE_RAINEFFEC");
						int nujiangCount = ServiceRainEffecForWarning.queryAllCount(conn, "NUJIANG_RAINEFFEC");
						ServiceRainEffecForWarning.insertData(conn , "HONGHE_RAINEFFEC", "RAINEFFEC_HH" , date , hongheCount);
						ServiceRainEffecForWarning.insertData(conn , "NUJIANG_RAINEFFEC", "RAINEFFEC_NJ" , date , nujiangCount);
						System.out.println("日期"+SDF.format(date)+"计算降雨诱发概率完成！");
					}
				}
				System.out.println("计算有效降雨量完成！");
			}
		}else {
			System.out.println("文件名不符合！");
			resp.getWriter().print("文件名不符合！");
			Dbconn.closeConn();
			//2代表文件名不符合
			resp.sendRedirect(req.getContextPath()+"/timechange.jsp?uploadResult=2");
			return;
		}
		//关闭数据库连接
		Dbconn.closeConn();
		//1代表上传成功！
		resp.sendRedirect(req.getContextPath()+"/timechange.jsp?uploadResult=1");
	}
}