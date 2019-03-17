/**
 * 显示GIS地图
 */
function showMap(map , baseMapPath , checkedLayerArr) {
	var returnLayer = [];
	require(
			["esri/map","esri/tasks/query","esri/tasks/QueryTask",
				"esri/tasks/Geoprocessor","esri/SpatialReference",
				"esri/tasks/FeatureSet", "esri/layers/ArcGISDynamicMapServiceLayer" ,
				"esri/geometry/Extent" ,"esri/dijit/HomeButton" , 
				"esri/layers/FeatureLayer" ,"esri/dijit/Legend" ,
				"dojo/domReady!"],
				function(Map , Query , QueryTask , Geoprocessor, SpatialReference , FeatureSet, ArcGISDynamicMapServiceLayer , Extent  , HomeButton,
						FeatureLayer , Legend){

				for(k in checkedLayerArr){
					var baselayer = new esri.layers.FeatureLayer(baseMapPath + "/" + checkedLayerArr[k] +"/MapServer/0",{
						model:FeatureLayer.MODE_SNAPSHOT,
						outFields: ["*"]
					});
					returnLayer.push(baselayer);
					map.addLayer(baselayer);
					
					//显示标注，如果是村不显示标注，如果是乡镇与县显示标注
					if(checkedLayerArr[k] == "nujiang1cun" || checkedLayerArr[k] == "honghe1cun"){
						
					}else if(checkedLayerArr[k] == "nujiang2xiangzhen" || checkedLayerArr[k] == "honghe2xiangzhen"){
						showLabel(baselayer,"XZQMC" , 11);
					}else{
						showLabel(baselayer,"XZQMC" , 13);
					}
				}
			});
	return returnLayer;
}
