function removeBaseMap(map , dispaliedLayers){
	require(
			["esri/map","esri/SpatialReference",
				"esri/layers/ArcGISDynamicMapServiceLayer" ,
				"esri/geometry/Extent" ,"esri/dijit/HomeButton" , 
				"esri/layers/FeatureLayer" ,
				"dojo/domReady!"],
			function(Map , SpatialReference , ArcGISDynamicMapServiceLayer , Extent , HomeButton,
					FeatureLayer) {
				
				for(k in dispaliedLayers){
					map.removeLayer(dispaliedLayers[k]);
				}
			});
}