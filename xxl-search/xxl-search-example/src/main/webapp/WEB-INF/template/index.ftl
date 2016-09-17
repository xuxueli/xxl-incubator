<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Elasticsearch</title>

    <!-- bootstrap -->
    <link rel="stylesheet" href="${request.contextPath}/static/plugin/bootstrap-3.3.4/css/bootstrap.min.css" >
    <link rel="stylesheet" href="${request.contextPath}/static/plugin/bootstrap-3.3.4/css/bootstrap-theme.min.css" >

</head>
<body>

    <#-- 导航 -->
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <nav class="navbar navbar-default" role="navigation">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="#">XXLSearch</a>
                    </div>
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav">
                            <li class="active">
                                <a href="#">ES</a>
                            </li>
                            <li>
                                <a href="${request.contextPath}/lucene">Lucene</a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
        </div>
    </div>

    <#-- 搜索条件 -->
    <div class="container">
        <div class="row clearfix">

            <div class="panel panel-default">
                <div class="panel-body">


                    <form role="form" class="form-inline">
                    <#-- 商户名 -->
                        <div class="form-group col-md-12 column">
                            <label class="col-md-2">商户:</label>
                            <input type="shopname" class="form-control" />
                            <button type="submit" class="btn btn-default">查询</button>
                        </div>

                    <#-- 城市 -->
                        <div class="btn-group col-md-12 column">
                            <label class="col-md-2">城市:</label>
                            <div>
                            <#list cityEnum as city>
                                <label class="checkbox-inline">
                                    <input type="checkbox" id="inlineCheckbox1" value="${city.cityid}">${city.cityname}
                                </label>
                            </#list>
                            </div>
                        </div>

                        <div class="btn-group col-md-12 column">
                            <label class="col-md-2">标签:</label>
                            <div>
                            <#list tagEnum as tag>
                                <label class="checkbox-inline">
                                    <input type="checkbox" id="inlineCheckbox1" value="${tag.tagid}">${tag.tagname}
                                </label>
                            </#list>
                            </div>
                        </div>

                    </form>

                </div>
            </div>

        </div>
    </div>


    <#-- 搜索列表 -->
    <hr>
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <table class="table table-bordered" id="shopSearchData">
                    <thead>
                    <tr>
                        <th>商户ID</th>
                        <th>商户名</th>
                        <th>城市ID</th>
                        <th>标签列表</th>
                        <th>业务分数</th>
                        <th>热门分数</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list shopOriginMapVal as shop>
                            <tr class="<#if shop_index%3==0>success<#elseif shop_index%3==1>error<#elseif shop_index%3==2>warning<#elseif shop_index%4==3>info</#if>" >
                                <td>${shop.shopid}</td>
                                <td>${shop.shopname}</td>
                                <td>${shop.cityid}</td>
                                <td>${shop.taglist?size}</td>
                                <td>${shop.score}</td>
                                <td>${shop.hotscore}</td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <#-- 商户列表 -->
    <hr>
    <div class="container" id="shopOriginData" >
        <div class="row clearfix">
            <div class="col-md-12 column">
                <label class="col-md-2">商户原始数据(更新、删除操作, 将会触发索引操作):</label>&nbsp;&nbsp;
                <a href="javascript:;" class="index_all_update">索引全量更新</a>&nbsp;&nbsp;
                <a href="javascript:;" class="add_one_line">新增一行</a>
                <table class="table table-bordered" >
                    <thead>
                    <tr>
                        <th>商户ID</th>
                        <th>商户名</th>
                        <th>城市ID</th>
                        <th>标签列表</th>
                        <th>业务分数</th>
                        <th>热门分数</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list shopOriginMapVal as shop>
                    <tr class="info" >
                        <td><input name="shopid" value="${shop.shopid}" readonly /></td>
                        <td><input name="shopname" value="${shop.shopname}" /></td>
                        <td><input name="cityid" value="${shop.cityid}" /></td>
                        <td><input name="taglist" value="${shop.taglist?size}" /></td>
                        <td><input name="score" value="${shop.score}" /></td>
                        <td><input name="hotscore" value="${shop.hotscore}" /></td>
                        <td><a href="javascript:;" class="save" >更新</a>&nbsp;&nbsp;<a href="javascript:;" class="delete" >删除</a></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

<#-- jquery -->
<script type="text/javascript" src="${request.contextPath}/static/plugin/jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/plugin/jquery/jquery.validate.min.js"></script>
<#-- bootstrap -->
<script type="text/javascript" src="${request.contextPath}/static/plugin/bootstrap-3.3.4/js/bootstrap.min.js"></script>
<script>
var base_url = '${request.contextPath}';
$(function(){
    // 索引全量更新
    $("#shopOriginData .index_all_update").click(function(){

    });

    // 新增一行
    $("#shopOriginData .add_one_line").click(function(){
        var temp = '<tr class="info" >' +
            '<td><input name="shopid" value="" readonly /></td>' +
            '<td><input name="shopname" value="" /></td>' +
            '<td><input name="cityid" value="" /></td>' +
            '<td><input name="taglist" value="" /></td>' +
            '<td><input name="score" value="" /></td>' +
            '<td><input name="hotscore" value="" /></td>' +
            '<td><a href="javascript:;" class="save" >更新</a>&nbsp;&nbsp;<a href="javascript:;" class="delete" >删除</a></td>' +
            '</tr>';

        $("#shopOriginData").find('tbody').append(temp);
    });

    // 更新
    $("#shopOriginData .save").click(function(){
        alert('更新一条索引');
    });


});
</script>

</body>
</html>