function removeAllLayer(map){
	require(
			["esri/map","esri/SpatialReference",
				"esri/layers/ArcGISDynamicMapServiceLayer" ,
				"esri/geometry/Extent" ,"esri/dijit/HomeButton" , 
				"esri/layers/FeatureLayer" ,"esri/dijit/Legend" ,
				"dojo/domReady!"],
			function(Map , SpatialReference , ArcGISDynamicMapServiceLayer , Extent , HomeButton,
					FeatureLayer , Legend) {
				
				map.removeAllLayers();
			});
}