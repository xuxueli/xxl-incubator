<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Search</title>

    <!-- bootstrap -->
    <link rel="stylesheet" href="${request.contextPath}/static/plugin/bootstrap-3.3.4/css/bootstrap.min.css" >
    <link rel="stylesheet" href="${request.contextPath}/static/plugin/bootstrap-3.3.4/css/bootstrap-theme.min.css" >

</head>
<body>
    <#-- 搜索条件 -->
    <center><h1>搜索列表页</h1></center>
    <hr>
    <div class="container">
        <div class="row clearfix">

            <div class="panel panel-default">
                <div class="panel-body search-from">

                    <form class="form-inline">
                        <#-- 商户名 -->
                        <div class="form-group col-md-12 column">
                            <label class="col-md-2">商户:</label>
                            <input type="text" class="form-control" name="shopname" value="${shopname}" />
                            <input type="button" class="btn btn-default search" value="查询" >
                        </div>

                        <#-- 城市 -->
                        <div class="btn-group col-md-12 column">
                            <label class="col-md-2">城市:</label>
                            <div>
                            <#list cityEnum as city>
                                <label class="checkbox-inline">
                                    <#assign isChecked = false />
                                    <#if cityids?exists >
                                        <#list cityids as cityid>
                                            <#if cityid==city.cityid>
                                                <#assign isChecked = true />
                                            </#if>
                                        </#list>
                                    </#if>
                                    <input type="radio" name="cityid" value="${city.cityid}" <#if isChecked >checked</#if> >${city.cityname}(${city.cityid})
                                </label>
                            </#list>
                            </div>
                        </div>
                        <#-- 标签 -->
                        <div class="btn-group col-md-12 column">
                            <label class="col-md-2">标签:</label>
                            <div>
                            <#list tagEnum as tag>
                                <label class="checkbox-inline">
                                    <#assign isChecked = false />
                                    <#if tagids?exists >
                                        <#list tagids as tagid>
                                            <#if tagid==tag.tagid >
                                                <#assign isChecked = true />
                                            </#if>
                                        </#list>
                                    </#if>
                                    <input type="checkbox" name="tagid" value="${tag.tagid}" <#if isChecked>checked</#if> >${tag.tagname}(${tag.tagid})
                                </label>
                            </#list>
                            </div>
                        </div>

                        <#-- 排序 -->
                        <div class="btn-group col-md-12 column">
                            <label class="col-md-2">排序:</label>
                            <div>
                                <label class="checkbox-inline">
                                    <input type="radio" name="sortType" value="0" <#if 0 == sortType>checked</#if> >默认排序
                                </label>
                                <label class="checkbox-inline">
                                    <input type="radio" name="sortType" value="1" <#if 1 == sortType>checked</#if> >热门排序
                                </label>
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
                        <#if result?exists && result.documents?exists && result.documents?size gt 0 >
                            <#list result.documents as shop>
                                <tr class="<#if shop_index%3==0>success<#elseif shop_index%3==1>error<#elseif shop_index%3==2>warning<#elseif shop_index%4==3>info</#if>" >
                                    <td>${shop.shopid}</td>
                                    <td>${shop.shopname}</td>
                                    <td>
                                        <#list cityEnum as city>
                                            <#if shop.cityid==city.cityid >${city.cityname}</#if>
                                        </#list>
                                    </td>
                                    <td>
                                        <#-- 多标签,返回多个同名Field,需要手动聚合,麻烦 -->
                                        <#list tagEnum as tag>
                                            <#if shop.tagid == tag.tagid >${tag.tagname}</#if>
                                        </#list>
                                    </td>
                                    <td>${shop.score}</td>
                                    <td>${shop.hotscore}</td>
                                </tr>
                            </#list>
                        </#if>
                    </tbody>
                </table>

                <#if pageNum gt 0 >
                    <ul class="pagination">
                        <#list 1..pageNum as pageitem>
                            <li <#if pageitem == page>class="active disabled"</#if>  ><a href="javascript:;" class="<#if pageitem != page>search</#if>"  page="${pageitem}" >${pageitem}</a></li>
                        </#list>
                    </ul>
                </#if>

            </div>
        </div>
    </div>

    <#-- 商户列表 -->
    <hr>
    <div class="container" id="shopOriginData" >
        <div class="row clearfix">
            <div class="col-md-12 column">
                <label class="col-md-2">商户原始数据:</label>&nbsp;&nbsp;
                <a href="javascript:;" class="deleteAllIndex">清空索引库</a>&nbsp;&nbsp;
                <a href="javascript:;" class="createAllIndex">全量索引</a>&nbsp;&nbsp;
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
                        <th>操作(数据+索引)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list shopOriginMapVal as shop>
                    <tr class="info" >
                        <td><input name="shopid" value="${shop.shopid}" readonly onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></td>
                        <td><input name="shopname" value="${shop.shopname}" /></td>
                        <td>
                            <#list cityEnum as city>
                                <input type="radio" name="cityid_${shop.shopid}" value="${city.cityid}" <#if shop.cityid==city.cityid >checked</#if> />${city.cityname}
                            </#list>
                        </td>
                        <td>
                            <#list tagEnum as tag>
                                <label class="checkbox-inline">
                                    <#assign isChecked = false />
                                    <#if shop.taglist?exists && shop.taglist?seq_contains(tag.tagid) >
                                        <#assign isChecked = true />
                                    </#if>
                                    <input type="checkbox" name="tagid_${shop.shopid}" value="${tag.tagid}" <#if isChecked>checked</#if> >${tag.tagname}
                                </label>
                            </#list>
                        </td>
                        <td><input name="score" value="${shop.score}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></td>
                        <td><input name="hotscore" value="${shop.hotscore}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></td>
                        <td><a href="javascript:;" class="save" data-oldshopid="${shop.shopid}" >更新</a>&nbsp;&nbsp;<a href="javascript:;" class="delete" >删除</a></td>
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

    // 搜索按钮
    $(".search").click(function(){
        // cityid
        var cityids =[];
        $('.search-from input[name="cityid"]:checked').each(function(){
            cityids.push($(this).val());
        });

        // tagid
        var tagids =[];
        $('.search-from input[name="tagid"]:checked').each(function(){
            tagids.push($(this).val());
        });

        // shopname
        var shopname = $('.search-from input[name="shopname"]').val();
        shopname = encodeURI(shopname);
        shopname = encodeURI(shopname);

        // page
        var page = $(this).attr("page");
        if (!page) {
            page = 1;
        }

        // sort
        var sortType = $('.search-from input[name="sortType"]:checked').val();

        window.location.href = base_url + "?cityidarr=" + cityids + "&tagidarr=" + tagids + "&shopname=" + shopname + "&sortType=" + sortType + "&page=" + page;
    });


    // 清空索引库
    $("#shopOriginData .deleteAllIndex").click(function(){
        $.post(base_url + "/deleteAllIndex", function(data, status) {
            if (data == "S") {
                alert("操作成功");
                window.location.reload();
            } else {
                alert(data);
            }
        }).error(function(){
            alert("接口异常");
        });
    });

    // 全量索引
    $("#shopOriginData .createAllIndex").click(function(){
        $.post(base_url + "/createAllIndex", function(data, status) {
            if (data == "S") {
                alert("操作成功");
                window.location.reload();
            } else {
                alert(data);
            }
        }).error(function(){
            alert("接口异常");
        });
    });

    // 新增一行
    $("#shopOriginData .add_one_line").click(function(){
        var temp = '<tr class="info" >'+
                '<td><input name="shopid" value="${shop.shopid}" /></td>'+
                '<td><input name="shopname" value="${shop.shopname}" /></td>'+
                '<td>'+
                    '<#list cityEnum as city>'+
                        '<input type="radio" name="cityid_" value="${city.cityid}" />${city.cityname}'+
                    '</#list>'+
                '</td>'+
                '<td>'+
                    '<#list tagEnum as tag>'+
                    '<label class="checkbox-inline">'+
                        '<input type="checkbox" name="tagid_" value="${tag.tagid}" >${tag.tagname}'+
                    '</label>'+
                    '</#list>'+
                '</td>'+
                '<td><input name="score" value="${shop.score}"   /></td>'+
                '<td><input name="hotscore" value="${shop.hotscore}"  /></td>'+
                '<td><a href="javascript:;" class="save" data-oldshopid="" >保存</a></td>'+
                '</tr>';

        $("#shopOriginData").find('tbody').append(temp);
    });

    // 新增/更新
    $("#shopOriginData").on('click', '.save',function() {

        var oldshopid = $(this).attr("data-oldshopid");

        var taglist =[];
        $(this).parent().parent().find('input[name=tagid_'+ oldshopid +']:checked').each(function(){
            taglist.push($(this).val());
        });

        var data = {
            shopid : $(this).parent().parent().find('input[name="shopid"]').val() ,
            shopname : $(this).parent().parent().find('input[name="shopname"]').val() ,
            cityid : $(this).parent().parent().find('input[name=cityid_'+ oldshopid +']:checked').val() ,
            taglist : taglist+"" ,
            score : $(this).parent().parent().find('input[name="score"]').val() ,
            hotscore : $(this).parent().parent().find('input[name="hotscore"]').val()
        };

        $.post(base_url + "/addDocument", data, function(data, status) {
            if (data == "S") {
                alert("操作成功");
                window.location.reload();
            } else {
                alert(data);
            }
        }).error(function(){
            alert("接口异常");
        });

    });

    // 删除
    $("#shopOriginData").on('click', '.delete',function() {

        var shopid = $(this).parent().parent().find('input[name="shopid"]').val();

        $.post(base_url + "/deleteDocument", {"shopid":shopid}, function(data, status) {
            if (data == "S") {
                alert("操作成功");
                window.location.reload();
            } else {
                alert(data);
            }
        }).error(function(){
            alert("接口异常");
        });
    });


});
</script>

</body>
</html>