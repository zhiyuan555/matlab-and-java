/**
 * 执行GP服务,当时间改变是执行gp服务
 * @param riskgpUrl gp服务url地址
 * @returns
 */
function excuteRiskWarningGp(map , riskgpUrl , mapServerUrl , raindataUrl ,legend , rankLabels , fieldNameId , ranks , title , year_ , month_ , day_){
	
	var resultLayer = [];
	require(
			["esri/map","esri/tasks/query","esri/tasks/QueryTask",
				"esri/tasks/Geoprocessor","esri/SpatialReference",
				"esri/tasks/FeatureSet", "esri/layers/ArcGISDynamicMapServiceLayer" ,
				"esri/geometry/Extent" ,"esri/dijit/HomeButton" , 
				"esri/layers/FeatureLayer" ,"esri/dijit/Legend" ,
				"dojo/domReady!"],
				function(Map , Query , QueryTask , Geoprocessor, SpatialReference , FeatureSet, ArcGISDynamicMapServiceLayer , Extent  , HomeButton,
						FeatureLayer , Legend){
				
					
					var query = new esri.tasks.Query();//定义查询对象
					//雨量点查询地址
					var queryTask = new esri.tasks.QueryTask(raindataUrl);
					//新建gp服务
					var riskgp = new Geoprocessor(riskgpUrl);
					
					//定义gp服务参数集
					var riskgpParams={};
					var featureSet = {};
					
					//返回几何对象
					query.returnGeometry = true;
				    //判断条件，必须，一般选择FID >-1即可
				    query.where = "YEAR_ = " + year_ + " and MONTH_ = " + month_ + " and DAY_ = " + day_;
				    //查询出来的字段，用于插值的，要保证是数字类型
				    query.outFields = ["OBJECTID , POINTID , LONGITUDE , LATITUDE , ALTITUDE , TIME , RAINEFFEC"];
				    //查询出来的时候空间参考定义
				    query.outSpatialReference = { "wkid": 4490 };
				    
				    //执行查询
				    queryTask.execute(query, function (fs) {
				    	console.log(fs);
				    	console.log(JSON.stringify(fs));
				    	
				        //结果作为gp服务的参数
				    	if(fieldNameId == "nujiang"){
				    		riskgpParams = {
						    		"DZDX_NUJIANG_RAINEFFEC":fs,
								    "rank1":ranks[0],
								    "rank2":ranks[1],
								    "rank3":ranks[2],
								    "rank4":ranks[3]
								};
				    	}else if(fieldNameId == "honghe"){
				    		riskgpParams = {
						    		"DZDX_HONGHE_RAINEFFEC":fs,
								    "rank1":ranks[0],
								    "rank2":ranks[1],
								    "rank3":ranks[2],
								    "rank4":ranks[3]
								};
				    	}
				    	riskgp.submitJob(riskgpParams , successResult);
				    });
				    
				    
				    function successResult(result){
				    	
				    	var jobUrl = mapServerUrl + result.jobId+"/0";
				    	
				    	var gpJobLayer = new FeatureLayer(jobUrl,{
				    		mode:FeatureLayer.MODE_SNAPSHOT,
				    		outFields : [ "*" ]
				    	});
				    	resultLayer.push(gpJobLayer);
				    	//渲染预警结果
				    	renderer(gpJobLayer , rankLabels);
				    	
				    	map.addLayer(gpJobLayer);
				    	
	                    //修改预警图层为最底层
				    	map.reorderLayer(gpJobLayer, 0);
				    	showLegend(gpJobLayer,legend ,title);
				    }
				}
			);
	return resultLayer;
}