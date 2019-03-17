/**
 * arcgis 小控件
 */
function homeButton(map){
    require([
             "esri/map", 
             "esri/dijit/HomeButton",
             "dojo/domReady!"
           ], function(
             Map, HomeButton
           )  {
             var home = new HomeButton({
               map: map
             }, "HomeButton");
             home.startup();

           });
}

function basemapGallery(map){
    require([
      "esri/map", "esri/dijit/BasemapGallery", "esri/arcgis/utils",
      "dojo/parser",

      "dijit/layout/BorderContainer", "dijit/layout/ContentPane", "dijit/TitlePane",
      "dojo/domReady!"
    ], function(
      Map, BasemapGallery, arcgisUtils,
      parser
    ) {
      parser.parse();

      //add the basemap gallery, in this case we'll display maps from ArcGIS.com including bing maps
      var basemapGallery = new BasemapGallery({
        showArcGISBasemaps: true,
        map: map
      }, "basemapGallery");
      basemapGallery.startup();
      
      basemapGallery.on("error", function(msg) {
        console.log("basemap gallery error:  ", msg);
      });
    });
}

function basemapToggle(map){
    require([
      "esri/map", 
      "esri/dijit/BasemapToggle",
      "dojo/domReady!"
    ], function(
      Map, BasemapToggle
    )  {
            
      var toggle = new BasemapToggle({
        map: map,
        basemap: "satellite"
      }, "BasemapToggle");
      toggle.startup();

    });

}

function measureTool(map){
    require([
        "dojo/dom",
        "esri/Color",
        "dojo/keys",
        "dojo/parser",

        "esri/config",
        "esri/sniff",
        "esri/map",
        "esri/SnappingManager",
        "esri/dijit/Measurement",
        "esri/layers/FeatureLayer",
        "esri/renderers/SimpleRenderer",
        "esri/tasks/GeometryService",
        "esri/symbols/SimpleLineSymbol",
        "esri/symbols/SimpleFillSymbol",

        "esri/dijit/Scalebar",
        "dijit/layout/BorderContainer",
        "dijit/layout/ContentPane",
        "dijit/TitlePane",
        "dijit/form/CheckBox",
        "dojo/domReady!"
      ], function(
        dom, Color, keys, parser,
        esriConfig, has, Map, SnappingManager, Measurement, FeatureLayer, SimpleRenderer, GeometryService, SimpleLineSymbol, SimpleFillSymbol
      ) {
        parser.parse();
        //This sample may require a proxy page to handle communications with the ArcGIS Server services. You will need to
        //replace the url below with the location of a proxy on your machine. See the 'Using the proxy page' help topic
        //for details on setting up a proxy page.
        esriConfig.defaults.io.proxyUrl = "localhost/DotNet/proxy.ashx";
        esriConfig.defaults.io.alwaysUseProxy = false;

        var sfs = new SimpleFillSymbol(
          "solid",
          new SimpleLineSymbol("solid", new Color([195, 176, 23]), 2),
          null
        );

        var measurement = new Measurement({
          map: map
        }, dom.byId("measurementDiv"));
        measurement.startup();
      });
}

function bookMarks(map){
	require([
	          "esri/map",
	          "esri/dijit/Bookmarks",
	          "dojo/domReady!"
	        ],
	        function (Map, Bookmarks){

	          // Create the bookmark widget
	          // specify "editable" as true to enable editing
	          var bookmarks = new Bookmarks({
	            map: map,
	            bookmarks: [],
	            editable: true
	          }, "bookmarks");

	          // Bookmark data objects
	          var bookmarkJSON = {
	          };

	          // Add bookmarks to the widget
	          Object.keys(bookmarkJSON).forEach(function (bookmark){
	            bookmarks.addBookmark(bookmarkJSON[bookmark]);
	          });
	        });
}

function search(map){
    require([

             "esri/map",
             "esri/dijit/Search",
             "dojo/domReady!"

           ], function (Map, Search) {

              var search = new Search({
                 map: map
              }, "search");
              search.startup();

           });
}

function directions(map){
	require([
	         "esri/urlUtils", "esri/map", "esri/dijit/Directions",
	         "dojo/parser",
	         "dijit/layout/BorderContainer", "dijit/layout/ContentPane", "dojo/domReady!"
	       ], function(
	         urlUtils, Map, Directions,
	         parser
	       ) {
	         parser.parse();
	         //all requests to route.arcgis.com will proxy to the proxyUrl defined in this object.
	         urlUtils.addProxyRule({
	           urlPrefix: "route.arcgis.com",
	           proxyUrl: "localhost/DotNet/proxy.ashx"
	         });
	         urlUtils.addProxyRule({
	           urlPrefix: "traffic.arcgis.com",
	           proxyUrl: "localhost/DotNet/proxy.ashx"
	         });

	         //default will point to ArcGIS world routing service
	         var directions = new Directions({
	           map: map
	           // --------------------------------------------------------------------
	           // New constuctor option and property showSaveButton added at version
	           // 3.17 to allow saving route. For more information see the API Reference.
	           // https://developers.arcgis.com/javascript/3/jsapi/directions-amd.html#showsavebutton
	           //
	           // Uncomment the line below to add the save button to the Directions widget
	           // --------------------------------------------------------------------
	           // , showSaveButton: true
	         },"dir");
	         directions.startup();
	       });
}

function overviewMap(map){
    require([
      "esri/map", "esri/dijit/OverviewMap",
      "dojo/parser", 

      "dijit/layout/BorderContainer", "dijit/layout/ContentPane", "dojo/domReady!"
    ], function (
      Map, OverviewMap, 
      parser
    ) {
      parser.parse(); 

      var overviewMapDijit = new OverviewMap({
        map: map,
        visible: true
      });
      overviewMapDijit.startup();
    });

}

function scalebar(map){
    require([
      "esri/map", "esri/dijit/Scalebar",
      "dojo/parser",
      "dijit/layout/BorderContainer", "dijit/layout/ContentPane", "dojo/domReady!"
    ], function(
      Map, Scalebar,
      parser
    ) {
      parser.parse();

      var scalebar = new Scalebar({
        map: map,
        // "dual" displays both miles and kilometers
        // "english" is the default, which displays miles
        // use "metric" for kilometers
        scalebarUnit: "dual",
        attachTo:"bottom-right"
      });
    });

}
function searchFeature(map,featureLayer){
    require([
    "esri/map",
    "esri/layers/FeatureLayer",
    "esri/dijit/Search",
    "esri/InfoTemplate",
    "dojo/domReady!"
    ], function (Map, FeatureLayer, Search, InfoTemplate) {

       //Create search widget
       var search = new Search({
          map: map,
          //passing in empty source array to clear defaults such as 
          //"All" and the ArcGIS Online World Geocoding service
          sources: [],
          zoomScale: 50000
       }, "searchFeature");

       //listen for the load event and set the source properties 
       search.on("load", function () {

          var sources = search.sources;
          sources.push({
             featureLayer: featureLayer,
             placeholder: "请输入灾害体名称",
             enableLabel: false,
             searchFields: ["JFBJ01A020"],
             displayField: "JFBJ01A020",
             exactMatch: false,
             outFields: ["*"],

             //Create an InfoTemplate and include three fields
             infoTemplate: new InfoTemplate("信息", "</br>灾害体名称: ${JFBJ01A020}</br>")

          });
          //Set the sources above to the search widget
          search.set("sources", sources);
       });
       search.startup();
    })
}

function gpPopulation(map){
    var app;

    require(["esri/Color",
      "dojo/string",
      "dijit/registry",

      "esri/config",
      "esri/map",
      "esri/layers/ArcGISDynamicMapServiceLayer",
      "esri/graphic",
      "esri/tasks/Geoprocessor",
      "esri/tasks/FeatureSet",
      "esri/toolbars/draw",
      "esri/symbols/SimpleLineSymbol",
      "esri/symbols/SimpleFillSymbol"
      ],
    function(Color, string, registry, esriConfig, Map, ArcGISDynamicMapServiceLayer, Graphic, Geoprocessor, FeatureSet, Draw, SimpleLineSymbol, SimpleFillSymbol){

      var gp, toolbar;

      app = {
        "map": map,
        "toolbar": toolbar
      };

      /*Initialize map, GP & image params*/
        app.map = map
        
        map.on("load", initTools);

        //identify proxy page to use if the toJson payload to the geoprocessing service is greater than 2000 characters.
        //If this null or not available the gp.execute operation will not work.  Otherwise it will do a http post to the proxy.
        esriConfig.defaults.io.proxyUrl = "localhost/DotNet/proxy.ashx";
        esriConfig.defaults.io.alwaysUseProxy = false;

      function initTools(evtObj) {
        gp = new Geoprocessor("https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Demographics/ESRI_Population_World/GPServer/PopulationSummary");
        gp.setOutputSpatialReference({wkid:102100}); 
        gp.on("execute-complete", displayResults);

        app.toolbar = toolbar = new Draw(evtObj.map);
        toolbar.on("draw-end", computeZonalStats);
      }

      function computeZonalStats(evtObj) {
        var geometry = evtObj.geometry;
        /*After user draws shape on map using the draw toolbar compute the zonal*/
        map.showZoomSlider();
        map.graphics.clear();
        
        var symbol = new SimpleFillSymbol("none", new SimpleLineSymbol("dashdot", new Color([255,0,0]), 2), new Color([255,255,0,0.25]));
        var graphic = new Graphic(geometry,symbol);

        map.graphics.add(graphic);
        toolbar.deactivate();

        var features= [];
        features.push(graphic);

        var featureSet = new FeatureSet();
        featureSet.features = features;
        
        var params = { "inputPoly":featureSet };
        gp.execute(params);
      }

      function displayResults(evtObj) {
        var results = evtObj.results;
        var content = string.substitute("<h4>该区域的人口为： ${number:dojo.number.format} 人.</h4>",{number:results[0].value.features[0].attributes.SUM});

        registry.byId("dialog1").setContent(content);
        registry.byId("dialog1").show();
      }

    });
}

function gpBuffer(map){
	var tb;

	require(["dojo/dom",

	        "dojo/_base/array",
	        "dojo/parser",
	        "dojo/query",
	        "dojo/on",

	        "esri/Color",
	        "esri/config",
	        "esri/map",
	        "esri/graphic",

	        "esri/geometry/normalizeUtils",
	        "esri/tasks/GeometryService",
	        "esri/tasks/BufferParameters",
	  
	        "esri/toolbars/draw",
	  
	        "esri/symbols/SimpleMarkerSymbol",
	        "esri/symbols/SimpleLineSymbol",
	        "esri/symbols/SimpleFillSymbol",
	        
	        "dijit/layout/BorderContainer",
	        "dijit/layout/ContentPane",
	        "dijit/form/Button", "dojo/domReady!"
	        ],
	      function(dom, array, parser, query, on, Color, esriConfig, Map, Graphic, normalizeUtils, GeometryService, BufferParameters, Draw, SimpleMarkerSymbol, SimpleLineSymbol, SimpleFillSymbol){

	        parser.parse();


	        esriConfig.defaults.geometryService = new GeometryService("https://utility.arcgisonline.com/ArcGIS/rest/services/Geometry/GeometryServer");

	        esriConfig.defaults.io.proxyUrl = "/proxy/";
	        esriConfig.defaults.io.alwaysUseProxy = false;


	        //click handler for the draw tool buttons
	        query(".tool").on("click", function(evt){
	          if(tb){
	           tb.activate(evt.target.id);
	          }
	        });

	        map.on("load", initToolbar);
	      function initToolbar(evtObj) {
	        tb = new Draw(evtObj.map);
	        tb.on("draw-end", doBuffer);
	      }
	      
	      var tb=new Draw(map);
	      $(".drawBuffer button").click(function () {
		        var tool=null;
		        switch (this.id){
		            case "point": tool="POINT";break;
		            case "line": tool="LINE";break;
		            case "polygon": tool="POLYGON"; break;
		            case "circle": tool="CIRCLE"; break;
		            case "rectangle": tool="RECTANGLE"; break;
		            case "quxiao": tb.deactivate(); break;
		            case "remove": map.graphics.clear();break;
		            case "freehandpolyline": tool="FREEHAND_POLYLINE"; break;
		            case "freehandpolygon": tool="FREEHAND_POLYGON"; break;
		        }
		        if(tool !== null){
		        	tb.activate(Draw[tool]);   //激活对应的绘制工具
		        }
		        }
		    );
	      tb.on("draw-end",doBuffer);
	      
	      function doBuffer(evtObj) {
	        tb.deactivate();
	        var geometry = evtObj.geometry, symbol;
	        if ( geometry.type === "point" || geometry.type === "multipoint") {
	             symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_SQUARE, 10, new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color([255,0,0]), 1), new Color([0,255,0,0.25]));
	        } else if ( geometry.type === "line" || geometry.type === "polyline") {
	            symbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_DASH, new Color([255,0,0]), 1);
	        }
	        else {
	            symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_NONE, new SimpleLineSymbol(SimpleLineSymbol.STYLE_DASHDOT, new Color([255,0,0]), 2), new Color([255,255,0,0.25]));
	        }
	        
	        var graphic = new Graphic(geometry, symbol);
	        map.graphics.add(graphic);

	          //setup the buffer parameters
	          var params = new BufferParameters();
	          params.distances = [ dom.byId("distance").value ];
	          params.outSpatialReference = map.spatialReference;
	          params.unit = GeometryService[dom.byId("unit").value];
	          //normalize the geometry 

	          normalizeUtils.normalizeCentralMeridian([geometry]).then(function(normalizedGeometries){
	            var normalizedGeometry = normalizedGeometries[0];
	            if (normalizedGeometry.type === "polygon") {
	              //if geometry is a polygon then simplify polygon.  This will make the user drawn polygon topologically correct.
	              esriConfig.defaults.geometryService.simplify([normalizedGeometry], function(geometries) {
	                params.geometries = geometries;
	                esriConfig.defaults.geometryService.buffer(params, showBuffer);
	              });
	            } else {
	              params.geometries = [normalizedGeometry];
	              esriConfig.defaults.geometryService.buffer(params, showBuffer);
	            }

	          });
	        }

	        function showBuffer(bufferedGeometries) {
	          var symbol = new SimpleFillSymbol(
	            SimpleFillSymbol.STYLE_SOLID,
	            new SimpleLineSymbol(
	              SimpleLineSymbol.STYLE_SOLID,
	              new Color([255,0,0,0.65]), 2
	            ),
	            new Color([255,0,0,0.35])
	          );

	          array.forEach(bufferedGeometries, function(geometry) {
	            var graphic = new Graphic(geometry, symbol);
	            map.graphics.add(graphic);
	          });

	        }
	  });
}
function printMap(map){
    require(["esri/map",
             "dojo/dom","dojo/on","dojo/query",
             "esri/layers/ArcGISDynamicMapServiceLayer",
             "esri/symbols/SimpleMarkerSymbol",
             "esri/symbols/SimpleLineSymbol",
             "esri/symbols/SimpleFillSymbol",
             "esri/toolbars/draw",
             "esri/graphic",
             "esri/tasks/PrintTask",
             "esri/tasks/PrintTemplate",
             "esri/tasks/PrintParameters",
             "dojo/colors",
             "dojo/domReady!"],
             function (Map,dom,on,query,
                 ArcGISDynamicMapServiceLayer,
                 SimpleMarkerSymbol,
                 SimpleLineSymbol,
                 SimpleFillSymbol,
                 Draw,
                 Graphic,
                 PrintTask,PrintTemplate,PrintParameters,
                 Color) {
    			// var title=dom.byId("title").value
    			// var author=dom.byId("author").value;
    			// var copyright=dom.byId("copyright").value;
    			// var title="示例图";
    			//var title=dijit.byId("title").value
//    			 var author="MXG";
//    			 var copyright="中国地质大学（武汉）";
//    			 console.log(title);
                 //创建绘图对象
                 var toolBar = new Draw(map);    
                 //线符号
                 lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_DASH, new Color([255, 0, 0]), 3);
                 //面符号
                 polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, lineSymbol, new Color([255, 0, 0, 0.25]));

                 //给绘制polygon的按钮绑定事件
                 query(".pbtn").on("click",function(event){
                     //激活绘制多边形
                     toolBar.activate(Draw.POLYGON, {
                         showTooltips:true
                     })

                 })
                 //Setup button click handlers
                 //绘图结束绑定事件
                 on(toolBar,"draw-end",function(result){
                     //获得面形状
                     var geometry=result.geometry;
                     //创建Graphic
                     var graphicpoint= new Graphic(geometry, polygonSymbol);
                     map.graphics.add(graphicpoint); 
                     //关闭绘图工具
                     toolBar.deactivate();                   
                 })
                 //给地图打印按钮绑定事件
                 on(dom.byId("Btn"),"click",function(){
                	 var title=dom.byId("title").value
         			 var author=dom.byId("author").value;
         			 var copyright=dom.byId("copyright").value;
         			 var date=dom.byId("labelText").value;
                     //创建地图打印对象
                     var printMap = new PrintTask("http://localhost:6080/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task");
                     //创建地图打印模版
                     var template = new PrintTemplate();
                     //创建地图的打印参数，参数里面包括：模版和地图
                     var params = new PrintParameters(); 
                     //输出图片的空间参考
                     printMap.outSpatialReference = map.SpatialReference 
                     //打印图片的各种参数
                     template.exportOptions = { 
                         width: 850, 
                         height: 400, 
                         dpi: 150
                     };
                     //打印输出的格式
                     template.format = "PDF"; 
                     //输出地图的布局
                     template.layout = "A4 Portrait"; 
                     template.layoutOptions={  
                    	        titleText:title,  
                    	        authorText:author,  
                    	        copyrightText:copyright,  
                    	        scalebarUnit:"Kilometers",  
                    	    }  
                     
                     PrintTemplate 
                     //设置参数地图
                     params.map = map; 
                     //设置参数模版
                     params.template = template; 
                     //运行结果
                     printMap.execute(params, function(result){
                         if (result != null) { 
                             //网页打开生成的地图
                             window.open(result.url); 
                         } 
                     }) 
                 })

             });
}
function drawTool(map){
    var map, toolbar, symbol, geomTask;

    require([
      "esri/map", 
      "esri/toolbars/draw",
      "esri/graphic",

      "esri/symbols/SimpleMarkerSymbol",
      "esri/symbols/SimpleLineSymbol",
      "esri/symbols/SimpleFillSymbol",

      "dojo/parser", "dijit/registry",

      "dijit/layout/BorderContainer", "dijit/layout/ContentPane", 
      "dijit/form/Button", "dijit/WidgetSet", "dojo/domReady!"
    ], function(
      Map, Draw, Graphic,
      SimpleMarkerSymbol, SimpleLineSymbol, SimpleFillSymbol,
      parser, registry
    ) {
      parser.parse();
      var draw=new Draw(map);
      //定义图形样式
		draw.markerSymbol=new SimpleMarkerSymbol();
		draw.lineSymbol=new SimpleLineSymbol();
		draw.fillSymbol=new SimpleFillSymbol();
		
		 $(".drawTool button").click(function () {
		        var tool=null;
		        switch (this.id){
		            case "point": tool="POINT";break;
		            case "line": tool="LINE";break;
		            case "polygon": tool="POLYGON"; break;
		            case "circle": tool="CIRCLE"; break;
		            case "rectangle": tool="RECTANGLE"; break;
		            case "freehandpolyline": tool="FREEHAND_POLYLINE"; break;
		            case "freehandpolygon": tool="FREEHAND_POLYGON"; break;
		            case "polyline": tool="POLYLINE"; break;
		            
		            case "arrow": tool="CIRCLE";break;
		            case "downarrow": tool="DOWN_ARROW";break;
		            case "ellipse": tool="ELLIPSE"; break;
		            case "extent": tool="EXTENT"; break;
		            case "leftarrow": tool="LEFT_ARROW"; break;
		            case "multipoint": tool="MULTI_POINT"; break;
		            case "rightarrow": tool="RIGHT_ARROW"; break;
		            case "triangle": tool="TRIANGLE"; break;
		            case "uparrow": tool="UP_ARROW"; break;
		            
		            case "remove": map.graphics.clear();break;
		        }
		        if(tool !== null){
		            draw.activate(Draw[tool]);   //激活对应的绘制工具
		        }
		        
		        }
		    );
		 
		   draw.on("draw-complete",drawEndEvent);
		    function drawEndEvent(evt) {
		            //添加图形到地图
		        var symbol;
		       
		        if ( evt.geometry.type === "point" || evt.geometry.type === "multipoint") {
		             symbol = draw.markerSymbol;
		        } else if ( evt.geometry.type === "line" || evt.geometry.type === "polyline"|| evt.geometry.type === "freehandpolyline") {
		            symbol = draw.lineSymbol;
		        }
		        else {
		            symbol = draw.fillSymbol;
		        }
		        map.graphics.add(new Graphic(evt.geometry,symbol));
		        draw.deactivate();
		    }
    })	
      
}

function queryData(map,layerUrl,currentDate){
	
    require(["esri/map",
             "esri/layers/ArcGISDynamicMapServiceLayer",
             "dojo/dom",
             "dojo/on",
             "esri/tasks/QueryTask",
             "esri/toolbars/draw",
             "esri/tasks/query",
             "esri/symbols/SimpleMarkerSymbol",
             "esri/symbols/SimpleLineSymbol",
             "esri/symbols/SimpleFillSymbol", 
             "esri/graphic",
             "dojo/_base/Color",
             "dojo/domReady!"],
             function (Map, ArcGISDynamicMapServiceLayer,dom, on,
             QueryTask, Draw, Query,SimpleMarkerSymbol,
             SimpleLineSymbol,SimpleFillSymbol,Graphic,Color) {
             //定义一个绘图工具
             var toolBar = new Draw(map);
             toolBar.markerSymbol=new SimpleMarkerSymbol();
             toolBar.lineSymbol=new SimpleLineSymbol();
             toolBar.fillSymbol=new SimpleFillSymbol();
             //给button绑定事件
             $(".drawQuery button").click(function () {
                 var tool=null;
                 switch (this.id){
                     case "polygon": tool="POLYGON"; break;
                     case "circle": tool="CIRCLE"; break;
                     case "rectangle": tool="RECTANGLE"; break;
                     case "quxiao": toolBar.deactivate(); break;
                     case "remove": map.graphics.clear();dom.byId("divShowResult").innerHTML = "";break;
                 }
                 if(tool !== null){
                	 toolBar.activate(Draw[tool]);   //激活对应的绘制工具
                 }
                 }
             );
             toolBar.on("draw-complete",drawEndEvent);
             function drawEndEvent(evt) {
	                 //添加图形到地图
	             var symbol;
	             toolBar.deactivate();
	             if ( evt.geometry.type === "point" || evt.geometry.type === "multipoint") {
	                  symbol = toolBar.markerSymbol;
	             } else if ( evt.geometry.type === "line" || evt.geometry.type === "polyline") {
	                 symbol = toolBar.lineSymbol;
	             }
	             else {
	                 symbol = toolBar.fillSymbol;
	             }
	             map.graphics.add(new Graphic(evt.geometry,symbol))
	             var geometry=evt.geometry;
	             
	             queryGraphic(geometry);
	         }
             
             var date=new Date(1279756800000).getTime();
             function queryGraphic(geometry) {
                 //创建查询对象，注意：服务的后面有一个编号，代表对那一个图层进行查询
                 var queryTask = new QueryTask(layerUrl);
                 //创建查询参数对象
                 var query = new Query();
                 //空间查询的几何对象
                 query.geometry = geometry;
                 //服务器给我们返回的字段信息，*代表返回所有字段
                 
                 query.outFields = ["*"];
                 //空间参考信息
                 query.outSpatialReference = map.spatialReference;
                 //查询的标准，此处代表和geometry相交的图形都要返回
                 query.spatialRelationship = Query.SPATIAL_REL_CONTAINS;
                 //是否返回几何信息
                 query.returnGeometry = true;
                 //执行空间查询
                 queryTask.execute(query, showQueryResult);
             }

             function showQueryResult(queryResult) {
                 
                 var  greenSymbol=new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE,15, new SimpleLineSymbol(SimpleLineSymbol.STYLE_CIRCLE, new Color([0, 255, 0]), 0), new Color([0, 255, 0]));
                 if (queryResult.features.length == 0) {
                     dom.byId("divShowResult").innerHTML = "";
                     return;
                 }
                 var htmls = "";
                 if (queryResult.features.length >= 1) {
                     htmls = htmls + "<table class=\"table table-condensed table-bordered\">";
                     htmls = htmls + "<thead><tr>" +
                     		"<th>名称</th>" +
                     		"<th>预警等级</th>" +
                     		"</tr>" +
                     		"</thead>";
                     for (var i = 0; i < queryResult.features.length; i++) {
                         //得到graphic
                         var graphic = queryResult.features[i];
                         //给图形赋予符号
                         graphic.setSymbol(greenSymbol);
                         //添加到地图从而实现高亮效果
                         map.graphics.add(graphic);
                         //获得教学楼的名称信息，此处应和shp的属性表对应
                         var ptName = graphic.attributes["NAME"];
                         var ptRank = graphic.attributes["RANK"];
                         var ptDAY = graphic.attributes["DAY_"];
                         if(ptDAY==currentDate.getTime()){
                        	 if (i % 2 == 0)
                                 htmls = htmls + "<tr>";
                             else
                                 htmls = htmls + "<tr bgcolor=\"#F0F0F0\">";
                             htmls = htmls + "<td><a href=\"#\"\">" + ptName + "</a></td>"+
                             				"<td><a href=\"#\"\">" + ptRank + "</a></td>";;
                             htmls = htmls + "</tr>";
                         }
                         
                     }
                     htmls = htmls + "</table>";
                     //将教学楼的名称信息和divShowResult绑定
                     dom.byId("divShowResult").innerHTML = htmls;
                     $("#divShowResult").show();
                 }
             }

         });
}
