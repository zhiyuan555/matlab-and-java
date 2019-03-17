<%@page import="java.io.*"%>
<%@page import="java.net.URLDecoder,com.module.util.*"%>
<%@ page language="java" import="java.util.*,com.module.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String ip;
	if (request.getHeader("x-forwarded-for") == null) {  
		ip = request.getRemoteAddr();  
    }else{
        ip = request.getHeader("x-forwarded-for");  
    }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>红河怒江示范区地质灾害预警</title>

<link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/common/bootstrap-3.3.5-dist/css/bootstrap.css">
<script type="text/javascript" src="<%=basePath%>/assets/common/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/common/bootstrap-3.3.5-dist/js/bootstrap.js"></script>


<!-- jquery ui -->
<link href="<%=basePath%>/assets/common/jquery-ui-themes-1.12.1/jquery-ui.css" rel="stylesheet">
<script type="text/javascript" src="<%=basePath%>/assets/common/jquery-ui-themes-1.12.1/jquery-ui.js"></script>
<script src="<%=basePath%>/assets/common/jquery-ui-themes-1.12.1/language/jquery.ui.datepicker-zh-CN.js"></script>
<link rel="stylesheet" href="<%=basePath%>/assets/loading/load.css"  media="all">
<script src="<%=basePath%>/assets/loading/load-min.js" charset="utf-8"></script>

<!-- arcgis api from javascript -->
<link rel="stylesheet" type="text/css" href="http://11.10.51.103:8080/arcgis_js_api/library/3.18/3.18/dijit/themes/claro/claro.css" />
<link rel="stylesheet" type="text/css" href="http://11.10.51.103:8080/arcgis_js_api/library/3.18/3.18/esri/css/esri.css" />
<script src="http://11.10.51.103:8080/arcgis_js_api/library/3.18/3.18/init.js" djConfig="parseOnLoad:true"></script>



<!-- 相关js文件 -->
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/renderer.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/showLabel.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/legend.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/widget.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/showmap.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/gpRiskWarning.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/removeBaseMap.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/removeAllLayer.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/susceptibility.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/showDisasterPoint.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/custom_js/showDisasterPointByArea.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/custom_css/mystyle.css">



<style type="text/css">
#searchFeature {
	display: block;
	position: absolute;
	z-index: 2;
	top: 20px;
	left: 74px;
}

#HomeButton {
	position: absolute;
	top: 95px;
	left: 20px;
	z-index: 50;
}

#accordion {
	border: 1px solid #e2e2e2;
	border-radius: 8px;
	right: 0px;
	position: absolute;
	z-index: 1000;
	top: 0px;
}
</style>
<%
	String path1=basePath+"/gis.config";
	String gisconfig=FileUtil.getFileContent(basePath+"/gis.config");
	String selectedTime = "2018-11-19";
	String uploadResult = null;
	String area = null;
	String toggleArea = "";
	if(request.getParameterMap().containsKey("uploadResult")){
		uploadResult = request.getParameter("uploadResult");
		if("".equals(uploadResult)){
			uploadResult = "0";
		}
	}else{
		uploadResult = "0";
	}
	
	if(request.getParameterMap().containsKey("area")){
		area = request.getParameter("area");
		if("".equals(area)){
			area = "nujiang";
		}
	}else{
		area = "nujiang";
	}
	if("nujiang".equals(area)){
		toggleArea = "honghe";
	}else{
		toggleArea = "nujiang";
	}
	//获取发布服务的文件目录
	String dicpath;
	if("nujiang".equals(area)){
		dicpath = "C:\\arcgisserver\\directories\\arcgisoutput\\nujiangBaseMap";
	}else{
		dicpath = "C:\\arcgisserver\\directories\\arcgisoutput\\hongheBaseMap";
	}
	ArrayList<String> list = LayerServicesUtil.getLayerServices(dicpath);
	Collections.sort(list);
	//示范区拼音映射
	Map<String , String> areaMap = new HashMap<String , String>();
	areaMap.put("nujiang", "怒江");
	areaMap.put("honghe", "红河");
	//图层映射
	Map<String , String> layerMap = new HashMap<String ,String>();
	layerMap.put("honghe1cun", "红河村级底图");
	layerMap.put("honghe3xian", "红河县级底图");
	layerMap.put("honghe2xiangzhen", "红河乡镇级底图");
	layerMap.put("nujiang3xian", "怒江县级底图");
	layerMap.put("nujiang2xiangzhen", "怒江乡镇级底图");
	layerMap.put("nujiang1cun", "怒江村级底图");
%>
</head>
<body>
	<div id="dialog">
	  <p id="dlgMsg"></p>
	</div>
	<div id="mapDiv">
		<div id="HomeButton"></div>
		<!-- 渲染颜色图例 -->
		<div id="leg" class="panel panel-default legendPanel" style="margin-bottom: 0px; border-color: white;display:none;">
			<div id="legendDiv" style="top: 10px; left: 10px; font-size: 16px;"></div>
		</div>
		<!-- 图层选择 -->
		<div id="layersPanel">
			<span></span>
		</div>
		<form class="form-inline" action="ParamServlet" method="get" style="padding: 20px 50px 50px 60px; height: 5px; width: 760px; z-index: 60; position: absolute;">
			<div class="form-group">
				<select name="address" class="form-control"  style="width:196px;" id="warningOption" onchange="gradeChange()">
					<option value="2" selected="selected">危险性预警</option>
					<option value="1" >易发性评价</option>
					<option value="3">风险预警</option>
				</select>
			</div><br>
			<div class="form-group">
				<input type="text" class="form-control" id="time" value=<%=selectedTime%> name="time" >
			</div>
	 		<button type="button" id="timeButton" class="btn btn-default">确定</button>
	 		<button type="button" id="toggleArea" class="btn btn-default">切换至<%=areaMap.get(toggleArea) %>示范区</button>
		</form>
		<!--<div class="jsonDisplayDiv" id="jsonDisplay"></div>   -->		
	</div>
	<div hidden id="container" style="width: 200px; height: 500px; position: absoulte; right: 1px; z-index: 100; padding-top: 0px">
		<div id="accordion"">
			<h3>图层叠加</h3>
			<div>
				<div class="checkbox" id="yunjinglayers" style="padding-left:10%;">
					<%for(int i=0;i<list.size();i++){
						if("nujiang3xian".equals(list.get(i)) || "honghe3xian".equals(list.get(i))){%>
							<div style="padding-top:5px;">
								<input type="checkbox" checked="checked" value=<%=list.get(i) %> name="layers"><b><%=layerMap.get(list.get(i)) %></b>
							</div>
					<% 
							continue;
						}%>	
					 
						<div style="padding-top:5px;">
							<input type="checkbox" value=<%=list.get(i) %> name="layers"><b><%=layerMap.get(list.get(i)) %></b>
						</div>
					
					<%} %>
				</div>

				<button type="button" class="btn btn-primary" id="save_layers" >提交</button>
			</div>
			<h3>录入降雨数据</h3>
			<div>
				<div class="panel">
					<br>
						<b>填入降雨数据TXT文件绝对路径:</b>
						<input type="text" id="raindataTxt" style="width: 100% ; margin: 10px auto 0 auto" name="raindataTxt">
					<br/>
					<div>
						<input id="uploadRainData" type="button" class="btn btn-default" style="width:100% ; margin: 10px auto 0 auto" value="录入数据" />
					</div>
					<div id="displayFile"></div>
				</div>
				
				<div class="panel">
					<br>
						<form action="servlet/SingleUploadData" method="post" enctype="multipart/form-data">
							 <b>选择txt：</b> 
							 <input type="file" name="rainFile" id="rainTxt">  
							 <input id="singleUploadRainData" type="submit" class="btn btn-default" style="width:100% ; margin: 10px auto 0 auto" value="上传数据" />
					    </form>
					<br/>
				</div>
			</div>
		</div>
	</div>
	<div id="opentoolBtn">
		<button type="button" class="btn btn-default" style="position: absolute; right: 10px; top: 20px; z-index: 60;" onclick="showTool()">
			<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span> 工具箱
		</button>
	</div>
	<div hidden id="closetoolBtn">
		<button type="button" class="btn btn-default" style="position: absolute; right: 318px; top: 0px; z-index: 100;" onclick="closeTool()">
			<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
		</button>
	</div>
	<script type="text/javascript">
		var map;
		//叠加图层的基路径
		var baseMapPath;
		//风险性gp服务路径
		var riskgpUrl;
		//风险性gp服务mapserver路径
		var mapServerUrl;
		//危险性gp服务路径
		var weixianGpUrl;
		//危险性gp服务mapserver路径
		var weixianmapServerUrl;
		//输入图层查询路径
		var raindataUrl;
		//易发性图层路径
		var susceptibilityUrl;
		//图例
		var legendDijit;
		//初始范围
		var extent;
		
		//易发等级描述
		var yifaRankLabels = ["低易发","较低易发","中等易发","较高易发","高易发"];
		//危险等级描述
		var weixianRankLabels = ["无危险","危险等级低","危险等级中等","危险等级较高","危险等级高"];
		//风险等级描述
		var fengxianRankLabels = ["无风险","风险低","风险中等","风险较高","风险高"];
		//危险性gp等级分界
		var hazardRanks = [];
		//风险性gp等级分界
		var riskRanks = [];
		//字段名称代号
		var fieldNameId;
		//历史地质灾害点url
		var disasterPointUrl = "http://11.10.51.103:6080/arcgis/rest/services/historyDisaster/disasterPoints/MapServer/0";
		//查询历史灾害点所使用示范区
		var areaDisasterPoint;
		if("<%=area%>" === "nujiang"){
			baseMapPath = "http://11.10.51.103:6080/arcgis/rest/services/nujiangBaseMap";
			riskgpUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/NJriskWarning/GPServer/NJriskWarning";
			mapServerUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/NJriskWarning/MapServer/jobs/";
			weixianGpUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/NJhazardWarning/GPServer/NJhazardWarning";
			weixianmapServerUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/NJhazardWarning/MapServer/jobs/";
			raindataUrl = "http://11.10.51.103:6080/arcgis/rest/services/nujiang/nujiangrain/FeatureServer/0";
			susceptibilityUrl = "http://11.10.51.103:6080/arcgis/rest/services/nujiang/nujiang_yifa/FeatureServer/0";
			extent = [17823389.063100, 2841725.551400, 17911180.286600, 3160911.848600];
			hazardRanks = [0.05 , 0.3 , 0.5 , 0.6];
			riskRanks = [0.009795407 , 0.04 , 0.06 , 0.15];
			fieldNameId = "nujiang";
			areaDisasterPoint = "怒江州";
		}else{
			baseMapPath = "http://11.10.51.103:6080/arcgis/rest/services/hongheBaseMap";
			riskgpUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/HHriskWarning/GPServer/HHriskWarning";
			mapServerUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/HHriskWarning/MapServer/jobs/";
			weixianGpUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/HHhazardWarning/GPServer/HHhazardWarning";
			weixianmapServerUrl = "http://11.10.51.103:6080/arcgis/rest/services/gp/HHhazardWarning/MapServer/jobs/";
			raindataUrl = "http://11.10.51.103:6080/arcgis/rest/services/honghe/hongherain/FeatureServer/0";
			susceptibilityUrl = "http://11.10.51.103:6080/arcgis/rest/services/honghe/honghe_yifa/FeatureServer/0";
			extent = [18245773.837200, 2484111.559400, 18425938.615800, 2588385.675200];
			hazardRanks = [0.252636057 , 0.413768444 , 0.516539387 , 0.677671774];
			riskRanks = [0.01 , 0.03 , 0.04 , 0.05];
			fieldNameId = "honghe";
			areaDisasterPoint = "红河州";
		}
		
		var resultLayer = [];
		//map中包含的图层，用来获取已经显示图层
		var dispaliedLayers = [];
		//获取选中的图层,用来叠加图层
		
		//弹出对话框，参数是标题与具体信息
		function showDialog(title,msg){
			 $( "#dialog" ).dialog({
				 title:title
			 })
			 $("#dlgMsg").text(msg);
		}
		
		function getSelectedLayer(){
			var $selectlayers=$("#yunjinglayers input:checkbox:checked");
		    var checkedLayerArr = [];
		    $selectlayers.each(function(index, element) {
		    	var checkedlayer = $(this).val();
	        	checkedLayerArr.push(checkedlayer);
		    })
		    return checkedLayerArr;
		}
		
		var checkedLayerArr = getSelectedLayer();

		//地图的一系列初始化操作，设置范围，定义home按键等
		require(
				["esri/map","esri/SpatialReference",
					"esri/layers/ArcGISDynamicMapServiceLayer" ,
					"esri/geometry/Extent" ,"esri/dijit/HomeButton" , 
					"esri/layers/FeatureLayer" ,"esri/dijit/Legend",
					"dojo/domReady!"],
				function(Map , SpatialReference , ArcGISDynamicMapServiceLayer , Extent , HomeButton,
						FeatureLayer,Legend){
						map = new Map("mapDiv",{
							showLabels : true
						});
						var startExtent = new Extent(extent[0], extent[1], extent[2], extent[3],
						          new SpatialReference({wkid:4496}));
						//设置初始范围
						map.setExtent(startExtent);
						var home = new HomeButton({
			                map: map
			              }, "HomeButton");
						home.startup();
						
						
						legendDijit = new Legend({
		                    map: map,
		                    layerInfos:[]
		                }, "legendDiv");
					}
				);
		
		//页面加载时，执行一次showMap
		window.onload = function() {
			dispaliedLayers = showMap(map , baseMapPath , checkedLayerArr);
			
			
			//如果访问者ip不是本机ip那么将禁用批量上传雨量数据框
			if ("<%=ip%>" == "11.10.51.103") {
				
			}else {
				$("#raindataTxt").val("远程用户请使用下面文件域单文件上传数据！");
				$("#raindataTxt").attr("disabled", true);
				$("#uploadRainData").attr("disabled", true);
			}
			//判断是否有雨量数据上传结果，如果有结果弹出对应框
			if("<%=uploadResult%>" == "1"){
				showDialog("提示","数据上传成功！");
			}else if("<%=uploadResult%>" == "2"){
				showDialog("提示","文件名不符合！");
			}else if("<%=uploadResult%>" == "3"){
				showDialog("提示","数据已经上传过了！");
			}
			
		}
		//隐藏与显示工具栏
		function getQueryString(name) { 
	        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	        var r = window.location.search.substr(1).match(reg); 
	        if (r != null) return unescape(r[2]); 
	        return null; 
	    } 
		$(function() {
			$("#accordion").accordion({
				collapsible : true,
				heightStyle : "fill"
			});
		});
		function showTool() {
			$("#container").show();
			$("#closetoolBtn").show();
			$("#opentoolBtn").hide();
		}
		function closeTool() {
			$("#container").hide();
			$("#closetoolBtn").hide();
			$("#opentoolBtn").show();
		}
		
		//上传数据文件域
		$("#uploadRainData").click(function(){
			
			var path = $("#raindataTxt").val();
			if(path === ""){
				showDialog("提示","路径不能为空！");
			}else{
				//定义ajax访问url
				var url = "UploadDataServlet";
				//通过ajax发送传入的数据文件路径至后台，后台实现数据入库
			    $.ajax({
			        type: "post",
			        timeout:0,
			        url: url,
			        data: {"filePath":path},
			        cache: false,
			        async : false,
			        dataType: "text",
			        success: function (result){
			        	if(result == "ok"){
							showDialog("提示","上传完成！");
			        	}else{
			        		showDialog("提示",result);
			        	}
			        },
			        error:function (XMLHttpRequest, textStatus, errorThrown) {  
						showDialog("提示","请求失败！");
			        }
			     });
			}

		})
		
		//点击切换示范区时
		$("#toggleArea").click(function(){
		    var url = 'timechange.jsp?area='+ "<%=toggleArea%>";
		    url=encodeURI(encodeURI(url));
		    console.log(url);
		    window.location.href=url;
		})
		
		//加载层-全屏
		function mask_fullscreen(duration){
			$.mask_fullscreen(duration);
			
		}
		//关闭所有加载层
		function mask_close_all(){
			$.mask_close_all();
		}
		//点击"确定"时间按钮执行操作
		
		$("#timeButton").click(function(){
			$("#leg").show();
 			var selectTime = $("#time").val();
 			var times = selectTime.split("-");
			var area ="<%=area%>";
			var warningOption = $("#warningOption").val();
			
			if(warningOption == 1){
				mask_fullscreen(5000);
				checkedLayerArr = getSelectedLayer();
    			removeAllLayer(map);
    			dispaliedLayers = showMap(map, baseMapPath, checkedLayerArr);
    			susceptibility(map , susceptibilityUrl , legendDijit , yifaRankLabels);
    			//展示当前示范区的所有灾害点
    			showDisasterPointByArea(map , disasterPointUrl , areaDisasterPoint);
			}else if(warningOption == 2){
				//定义ajax访问url
				
				var url = "TimeChangeServlet";
				//通过ajax发送传入的数据文件路径至后台，后台实现数据入库
			    $.ajax({
			        type: "post",
			        url: url,
			        timeout:0,
			        data: {"selectTime":selectTime,
			        	"area":area},
			        cache: false,
			        async : false,
			        dataType: "text",
			        success: function (result){
			        	console.log(result);
			        	if(result == "true"){
			        		
			        		checkedLayerArr = getSelectedLayer();
			    			removeAllLayer(map);
			    			mask_fullscreen(22000);
			    			resultLayer = excuteRiskWarningGp(map , weixianGpUrl , weixianmapServerUrl , raindataUrl ,legendDijit , weixianRankLabels, fieldNameId , hazardRanks ,"危险等级" , times[0] , times[1] , times[2]);
			    			dispaliedLayers = showMap(map, baseMapPath, checkedLayerArr);
			        	}else if(result == "false"){
			        		
			        		$("#leg").hide();
			        		checkedLayerArr = getSelectedLayer();
			    			removeAllLayer(map);
			    			dispaliedLayers = showMap(map, baseMapPath, checkedLayerArr);
			        		mask_close_all();
			        		showDialog("提示","没有该天的降雨数据！");
			        		/* alert("没有该天的降雨数据！") */
			        	}
			        	
			        	//展示当天当前示范区的灾害点
						showDisasterPoint(map , disasterPointUrl , times[0] , times[1] , times[2] ,areaDisasterPoint);
			        },
			        
			        error:function (XMLHttpRequest, textStatus, errorThrown) {   
			        	showDialog("提示","请求失败！");
			        }
			     });
			}else if(warningOption == 3){
				
				//定义ajax访问url
				var url = "TimeChangeServlet";
				//通过ajax发送传入的数据文件路径至后台，后台实现数据入库
			    $.ajax({
			        type: "post",
			        timeout:0,
			        url: url,
			        data: {"selectTime":selectTime,
			        	"area":area},
			        cache: false,
			        async : false,
			        dataType: "text",
			        success: function (result){
			        	console.log(result);
			        	if(result == "true"){
			        		checkedLayerArr = getSelectedLayer();
			    			removeAllLayer(map);
			    			mask_fullscreen(22000);
			    			resultLayer = excuteRiskWarningGp(map , riskgpUrl , mapServerUrl , raindataUrl,legendDijit , fengxianRankLabels , fieldNameId , riskRanks ,"风险等级", times[0] , times[1] , times[2]);
			    			dispaliedLayers = showMap(map, baseMapPath, checkedLayerArr);
			        	}else if(result == "false"){
			        		
			        		$("#leg").hide();
			        		checkedLayerArr = getSelectedLayer();
			    			removeAllLayer(map);
			    			dispaliedLayers = showMap(map, baseMapPath, checkedLayerArr);
			        		mask_close_all();
			        		showDialog("提示","没有该天的降雨数据！");
			        		/* alert("没有该天的降雨数据！") */
			        	}
			        	
			        	//展示当天当前示范区的灾害点
						showDisasterPoint(map , disasterPointUrl , times[0] , times[1] , times[2] ,areaDisasterPoint);
			        },
			        error:function (XMLHttpRequest, textStatus, errorThrown) {   
			        	showDialog("提示","请求失败！");
			        }
			     });
			}
		})

		//点击"确定"保存图层按钮时执行
		$("#save_layers").click(function() {
			checkedLayerArr = getSelectedLayer();
			removeBaseMap(map, dispaliedLayers);
			dispaliedLayers = showMap(map, baseMapPath, checkedLayerArr);
		})
		
		//日期控件
		$(function() {
			$("#time").datepicker({
				showButtonPanel : true,
				defaultDate : "+1w",
				changeYear : true,
				changeMonth : true,
				dateFormat : 'yy-mm-dd',
				numberOfMonths : 1,
			});
		});

		function gradeChange() {
			var objS = document.getElementById("warningOption");
			var grade = objS.options[objS.selectedIndex].value;
			if (grade == 1) {
				$("#time").attr("disabled", true);
			} else {
				$("#time").attr("disabled", false);
			}
		}
		
		function verify(){
			var fileName = $("#rainTxt").val();
	        if(fileName == '') {
	        	showDialog("提示","请选择文件！");
	        	return false;
	        } 
		}
		//雨量数据上传表单，表单提交时触发
		$("#singleUploadRainData").on('click', verify); 
	</script>
</body>
</html>