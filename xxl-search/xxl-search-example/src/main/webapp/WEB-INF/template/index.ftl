<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Elasticsearch</title>
    <script>var base_url = '${request.contextPath}';</script>


</head>
<body>

<#list shoplist as shop>
    ${shop.shopname}
</#list>

</body>
</html>