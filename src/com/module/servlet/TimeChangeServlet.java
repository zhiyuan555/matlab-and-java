package com.module.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.module.dbconn.Dbconn;
import com.module.modle.ServiceRainEffecForWarning;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ParamServlet
 */
@WebServlet("/TimeChangeServlet")
public class TimeChangeServlet extends HttpServlet {
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeChangeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = Dbconn.getConnection();
		String selectTime = request.getParameter("selectTime");
		String area = request.getParameter("area");
		Date date = null;
		try {
			date = SDF.parse(selectTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(date != null && area!= null) {
			
			if("honghe".equals(area)) {
				if(ServiceRainEffecForWarning.getDateDataNumber(conn, "HONGHE_RAINEFFEC", date) > 0) {
					System.out.println("有该天的降雨数据!");
					response.getWriter().print("true");
				}else {
					response.getWriter().print("false");
				}
			}else if("nujiang".equals(area)) {
				if(ServiceRainEffecForWarning.getDateDataNumber(conn , "NUJIANG_RAINEFFEC", date) > 0) {
					System.out.println("有该天的降雨数据!");
					response.getWriter().print("true");
				}else {
					response.getWriter().print("false");
				}
			}
		}else {
			response.getWriter().print("false");
		}
		Dbconn.closeConn();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
