/**
 * 
 */
//按州展示灾害点，在易发性展示时显示
function showDisasterPointByArea(map , disasterPointUrl , areaDisasterPoint) {
	 require(["esri/map",
	            "esri/layers/ArcGISDynamicMapServiceLayer",
				"dojo/dom",
				"dojo/on",
	            "esri/tasks/QueryTask",
	            "esri/toolbars/draw",
	            "esri/tasks/query",
				"esri/symbols/SimpleMarkerSymbol",
				"esri/symbols/SimpleLineSymbol",
				"esri/Color",
				"esri/graphic",
	            "dojo/domReady!"],
	            function (Map, ArcGISDynamicMapServiceLayer,dom, on,
				QueryTask, Draw, Query,
				SimpleMarkerSymbol,SimpleLineSymbol,
				Color ,Graphic) {
		 
		 			map.graphics.clear();
					var query = new esri.tasks.Query();//定义查询对象
					var queryTask = new esri.tasks.QueryTask(disasterPointUrl);
					
					var markSymbol = new SimpleMarkerSymbol(
							SimpleMarkerSymbol.STYLE_CIRCLE, 8,
							new SimpleLineSymbol(SimpleLineSymbol.STYLE_CIRCLE,
									new Color([ 255, 0, 0 ]), 0), new Color([ 255,
									0, 0 ]));
					//返回几何对象
					query.returnGeometry = true;
				    //判断条件，必须，一般选择FID >-1即可
				    query.where = "ZHOU = '" + areaDisasterPoint +"'";
				    //查询出来的字段，用于插值的，要保证是数字类型
				    query.outFields = ["*"];
				    //查询出来的时候空间参考定义
				    query.outSpatialReference = { "wkid": 4496 };
				    //执行查询
				    queryTask.execute(query, function (result) {
				    	 var graphics = result.features;
				    	 if (graphics.length == 0) {
				    		 return;
			             }else {
		                    for (var i = 0; i < graphics.length; i++) {
		                        //赋予相应的符号
		                    	graphics[i].setSymbol(markSymbol);
		                        //将graphic添加到地图中，从而实现高亮效果
		                    	map.graphics.add(graphics[i]);
		                    }
			             }
				    });
			});
}