/**
 * 
 */
function showLabel(featureLayer,labelStr , fontSize) {
	require([ "esri/map", "dojo/_base/Color", "esri/symbols/Font", "esri/symbols/TextSymbol", "esri/geometry/Point", "esri/layers/LabelClass",
			"dojo/domReady!" ], function(Map, Color, Font, TextSymbol, Point, LabelClass) {
		var labelSymbol = new TextSymbol().setColor(new Color("#000000"));
		labelSymbol.font.setSize(fontSize + "pt");
		labelSymbol.font.setFamily("微软雅黑");
		labelSymbol.font.setWeight(Font.WEIGHT_BOLD);
		var json = {
			"labelExpressionInfo" : {
				"value" : "{"+labelStr+"}"
			},
			"useCodedValues" : false,
			"labelPlacement" : "center-right"
		};
		var labelClass = new LabelClass(json);
		labelClass.symbol = labelSymbol;
		featureLayer.setLabelingInfo([ labelClass ]);
	})

}