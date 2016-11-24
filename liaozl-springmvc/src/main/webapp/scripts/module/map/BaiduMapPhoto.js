Package("com.liaozl.photo.map");

com.liaozl.photo.map = {
	    
		init : function(){
			me = this;
			me.initPage();
			me.initMap();
		},

		//初始化页面元素
		initPage : function(){
			$("#citySelect").change(function(){
				var cityName = $(this).children('option:selected').val();
				if(cityName && cityName != ''){
					me.loadMapAndPhoto(cityName);
				}
			});
		},
		
		loadMapAndPhoto : function(cityName){
			window.map.centerAndZoom(cityName, 11); 
			
			$.ajax({
				url: ctx + "/map/baidu/photo/queryPhoto",
				dataType:'json',
				type:'POST',
				data : { 
					"cityName" : cityName
				},
				success : function(json){
					if(json && json.photoList){
						me.addMarker(json.photoList);
					}
				}
			});
		},
		
		//创建和初始化地图函数：
		initMap : function(){
	        me.createMap();//创建地图
	        me.setMapEvent();//设置地图事件
	        me.addMapControl();//向地图添加控件
	        me.addMarker();//向地图中添加marker
	    },
	    
	    //创建地图函数：
		createMap : function(){
	        map = new BMap.Map("mapDiv");//在百度地图容器中创建一个地图
	        
	        //var point = new BMap.Point(117.220361,40.070596);//定义一个中心点坐标
	        //map.centerAndZoom(point,12);//设定地图的中心点和坐标并将地图显示在地图容器中
	         
	        window.map = map;//将map变量存储在全局
	        
	        me.loadMapAndPhoto(currentCity);
	    },
	    
	    //地图事件设置函数：
	    setMapEvent : function(){
	        map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
	        map.enableScrollWheelZoom();//启用地图滚轮放大缩小
	        map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
	        map.enableKeyboard();//启用键盘上下左右键移动地图
	    },
	    
	    //地图控件添加函数：
	    addMapControl : function(){
	        //向地图中添加缩放控件
			var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE});
			map.addControl(ctrl_nav);
	        //向地图中添加缩略图控件
			var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,isOpen:1});
			map.addControl(ctrl_ove);
	        //向地图中添加比例尺控件
			var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
			map.addControl(ctrl_sca);
		
			map.addControl(new BMap.NavigationControl());    
			map.addControl(new BMap.ScaleControl());    
			map.addControl(new BMap.OverviewMapControl());    
			map.addControl(new BMap.MapTypeControl());    
			map.setCurrentCity(currentCity); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用
	    },
	    
	    //向地图中添加marker函数
	    addMarker : function(photoArray){
	    	me.clearAllMarker();
	    	if(photoArray && photoArray.length > 0){
	    		var markerArray = [];
	    		
	    		$.each(photoArray, function(index, pd){   
	    			var point = new BMap.Point(pd.longitude, pd.latitude);
	    			var photoOverlay = new PhotoOverlay(map, point, pd.url);
	    		    map.addOverlay(photoOverlay);
	    		    
	    		    var searchInfoWindow = me.createPhotoOverlayInfoWindow(map, pd);
	    		    photoOverlay.addEventListener("click", function(e){
	    		    	searchInfoWindow.open(point);
	    		    });
	    			
	    		    markerArray.push(photoOverlay);
	    		});

	    		/*//点汇聚
	    		var MAX = 10;
	    		var markers = [];
	    		var pt = null;
	    		var i = 0;
	    		for (; i < MAX; i++) {
	    		   pt = new BMap.Point(Math.random() * 40 + 85, Math.random() * 30 + 21);
	    		   markers.push(new BMap.Marker(pt));
	    		}
	    		
	    		try{
	    			var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markers});
	    		}catch(e){
	    			
	    		}*/
	    	}
	    },
	    
	    //创建自定义覆盖物的弹窗
	    createPhotoOverlayInfoWindow : function(map, photoData){
	    	var content = 
	    	     '<div style="margin:0;line-height:20px;padding:2px;">' +
                    '<img src="'+photoData.url+'" alt="" style="float:right;zoom:1;overflow:hidden;width:100px;height:100px;margin-left:3px;"/>' +
                    photoData.desc + "</br>" + photoData.createTimeStr
                 '</div>';

	    	var title = photoData.city + "-" + photoData.name;
				//创建检索信息窗口对象
			var infoWindow = new BMapLib.SearchInfoWindow(map, content, {
					title  : title,      	   //标题
					width  : 290,             //宽度
					height : 105,              //高度
					panel  : "panel",         //检索结果面板
					offset : {
						height : 30,
						width : -50   		  //不起作用，可以不设置
					},
					enableAutoPan : true,     //自动平移
					searchTypes   :[
						BMAPLIB_TAB_SEARCH,   //周边检索
						BMAPLIB_TAB_TO_HERE,  //到这里去
						BMAPLIB_TAB_FROM_HERE //从这里出发
					]
				});
			
			return infoWindow;
	    },
	    
	    //清除全部marker
	    clearAllMarker : function(){
	    	map.clearOverlays();   
	    }
	    
};


//自定义地图覆盖物
function PhotoOverlay(map, point, photoUrl){
  this._map = map;
  this._point = point;
  this._photoUrl = photoUrl;
}

PhotoOverlay.prototype = new BMap.Overlay();
PhotoOverlay.prototype.initialize = function(){
  var div = this._div = document.createElement("div");
  div.style.position = "absolute";
  div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);
  div.style.backgroundColor = "#D9D9D9";
  div.style.border = "1px solid #BC3B3A";
  div.style.height = "50px";
  div.style.width = "50px";
     
  var img = this._span = document.createElement("img");
  img.style.height = "50px";
  img.style.width = "50px";
  img.src = this._photoUrl;
  div.appendChild(img);
  
  var arrow = this._arrow = document.createElement("div");
  arrow.style.background = "url(http://map.baidu.com/fwmap/upload/r/map/fwmap/static/house/images/label.png) no-repeat";
  arrow.style.position = "absolute";
  arrow.style.width = "11px";
  arrow.style.height = "10px";
  arrow.style.top = "50px";
  arrow.style.left = "10px";
  arrow.style.overflow = "hidden";
  div.appendChild(arrow);
 
  var that = this;
  
  this._map.getPanes().labelPane.appendChild(div);
  
  return div;
}

PhotoOverlay.prototype.draw = function(){
  var map = this._map;
  var pixel = map.pointToOverlayPixel(this._point);
  this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + "px";
  this._div.style.top  = pixel.y - 60 + "px";
}

PhotoOverlay.prototype.addEventListener = function(event,fun){
	this._div['on' + event] = fun;
}

com.liaozl.photo.map.init();//创建和初始化地图	