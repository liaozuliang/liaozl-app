<html>
	<head>
		<title>freemarker自定义标签替换图片路径</title>
	</head>
	<body>
		<img width="120" height="80" src="
			<#if url??>
				<@imgSrcReplaceDirective size='120x80' oldUrl=url; newUrl>
				    ${newUrl}
				</@imgSrcReplaceDirective>
			</#if>
		" />
	</body>
</html>