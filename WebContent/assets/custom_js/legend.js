/**
 * 
 */
function showLegend(layer,legend ,title){
	require(
			["esri/map","esri/tasks/query","esri/tasks/QueryTask",
				"esri/tasks/Geoprocessor","esri/SpatialReference",
				"esri/tasks/FeatureSet", "esri/layers/ArcGISDynamicMapServiceLayer" ,
				"esri/geometry/Extent" ,"esri/dijit/HomeButton" , 
				"esri/layers/FeatureLayer" ,"esri/dijit/Legend" ,
				"dojo/domReady!"],
				function(Map , Query , QueryTask , Geoprocessor, SpatialReference , FeatureSet, ArcGISDynamicMapServiceLayer , Extent  , HomeButton,
						FeatureLayer , Legend){
				//添加图例
		    	var layerInfo = [{
		    		layer:layer,
		    		title:title
		    	}];
		    	
		    	legend.refresh(layerInfo);

			});
	

}