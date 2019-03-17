
/**
 * 根据RANK值渲染预警结果
 */
function renderer(featureLayer , rankLabels) {
	require(
			[  "esri/symbols/SimpleMarkerSymbol",
	            "esri/symbols/SimpleLineSymbol",
	            "esri/symbols/SimpleFillSymbol",
	            "esri/renderers/ClassBreaksRenderer",
	            "esri/renderers/UniqueValueRenderer",
	            "dojo/_base/Color", "dojo/domReady!" ],
			function(SimpleMarkerSymbol,
	                 SimpleLineSymbol,SimpleFillSymbol,ClassBreaksRenderer,UniqueValueRenderer,Color
					) {
				
				var redSymbol, yellowSymbol, orangeSymbol, blueSymbol, greenSymbol , lineSymbol;
				
				redSymbol = new SimpleFillSymbol().setColor(new Color([40, 146, 198, 1]));
				redSymbol.setOutline(new SimpleLineSymbol().setColor(new Color([40, 146, 198, 1])));
				
				yellowSymbol = new SimpleFillSymbol().setColor(new Color([161, 193, 156, 1]));
				yellowSymbol.setOutline(new SimpleLineSymbol().setColor(new Color([161, 193, 156, 1])));
				
				orangeSymbol = new SimpleFillSymbol().setColor(new Color([250, 140, 51, 1]));
				orangeSymbol.setOutline(new SimpleLineSymbol().setColor(new Color([250, 140, 51, 1])));
				
				blueSymbol = new SimpleFillSymbol().setColor(new Color([250, 250 ,100 ,1]));
				blueSymbol.setOutline(new SimpleLineSymbol().setColor(new Color([250, 250 ,100 ,1])));
				
				greenSymbol = new SimpleFillSymbol().setColor(new Color([232, 16 ,21 ,1]));
				greenSymbol.setOutline(new SimpleLineSymbol().setColor(new Color([232, 16 ,21 ,1])));
				
				// 唯一值渲染
				var rendererPoint = new UniqueValueRenderer(
						new SimpleMarkerSymbol(), "GRIDCODE");
				rendererPoint.addValue({
					value : "1",
					symbol : redSymbol,
					label : rankLabels[0]

				});
				rendererPoint.addValue({
					value : "2",
					symbol : yellowSymbol,
					label : rankLabels[1]
				});
				rendererPoint.addValue({
					value : "3",
					symbol : blueSymbol,
					label : rankLabels[2]
				});
				rendererPoint.addValue({
					value : "4",
					symbol : orangeSymbol,
					label : rankLabels[3]
				});
				rendererPoint.addValue({
					value : "5",
					symbol : greenSymbol,
					label : rankLabels[4]
				});

				featureLayer.setRenderer(rendererPoint);
			})

}