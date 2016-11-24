<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="keywords" content="百度地图,百度地图API，百度地图自定义工具，百度地图所见即所得工具" />
<meta name="description" content="百度地图API自定义地图，帮助用户在可视化操作下生成百度地图" />
<title>百度地图API自定义地图</title>
<!--引用百度地图API-->
<style type="text/css">
    html,body{margin:0;padding:0; height:100%; width:100%}
    .iw_poi_title {color:#CC5522;font-size:14px;font-weight:bold;overflow:hidden;padding-right:13px;white-space:nowrap}
    .iw_poi_content {font:12px arial,sans-serif;overflow:visible;padding-top:4px;white-space:-moz-pre-wrap;word-wrap:break-word}
</style>

<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=fpzzgOjiOHvcRQLMeTVD8IS8"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
<script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>

<script type="text/javascript" src="${base}/scripts/common/PackageUtil.js"></script>
<script type="text/javascript" src="${base}/scripts/plugin/jquery/jquery-1.3.2.min.js"></script>
</head>

<body>
  <!--百度地图容器-->
  <div style="width:80%; height:99.8%; float:left" id="mapDiv"></div>
  <div style="width:20%; height:99.8%; float:left" >
  		<div style="width:100%;">
  			<select id="citySelect" style="width:100%; height:35px">
  				<#if cityList??>
  					<#list cityList as cityName>
  						<#if cityName == currentCity>
  							<option value="${cityName}" checked>${cityName}</option>
  						<#else>
  							<option value="${cityName}">${cityName}</option>
  						</#if>
  					</#list>
  				</#if>
  			<select>
  		</div>
  </div>
</body>
<script type="text/javascript">
	var currentCity = "${currentCity}";
	var ctx = "${ctx}";
</script>

<script type="text/javascript" src="${base}/scripts/module/map/BaiduMapPhoto.js"></script>
</html>