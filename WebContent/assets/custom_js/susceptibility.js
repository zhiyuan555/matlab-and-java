function susceptibility(map , susceptibilityUrl , legendDijit , rankLabels){
	require(
			["esri/map","esri/SpatialReference",
				"esri/layers/ArcGISDynamicMapServiceLayer" ,
				"esri/geometry/Extent" ,"esri/dijit/HomeButton" , 
				"esri/layers/FeatureLayer" ,
				"dojo/domReady!"],
			function(Map , SpatialReference , ArcGISDynamicMapServiceLayer , Extent , HomeButton,
					FeatureLayer) {
				var susceptibilityLayer = new esri.layers.FeatureLayer(susceptibilityUrl ,{
		    		mode:FeatureLayer.MODE_SNAPSHOT,
		    		outFields : [ "*" ]
		    	});
				map.addLayer(susceptibilityLayer);
				map.reorderLayer(susceptibilityLayer, 0);
				//展示图例
				showLegend(susceptibilityLayer,legendDijit ,"易发等级");
				//渲染预警结果
		    	renderer(susceptibilityLayer , rankLabels);
			});
}