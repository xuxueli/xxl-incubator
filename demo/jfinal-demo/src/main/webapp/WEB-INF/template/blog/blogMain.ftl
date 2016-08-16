<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<#include "${base_include}/common/common.param.ftl" >
	<#import "${base_include}/net.common.ftl" as net >
	<title>blog</title>
</head>
<body>
	<div class="manage_container">
		<div class="manage_head">
			<div class="manage_logo">JFinal极速开发</div>
			<div id="nav">
				<ul>
					<li><a href="${base_url}"><b>首页</b></a></li>
					<li><a href="${base_url}blog"><b>Blog管理</b></a></li>
				</ul>
			</div>
		</div>
		<div class="main">
			<h1>Blog管理&nbsp;&nbsp;
				<a href="${base_url}blog/addPage">创建Blog</a>
			</h1>
			<div class="table_box">
				<table class="list">
					<tbody>
						<tr>
							<th width="4%">id</th>
							<th width="35%">标题</th>
							<th width="12%">操作</th>
						</tr>
						<#if pager.list?exists>
							<#list pager.list as blog>
								<tr>
									<td style="text-align: left;">${blog.id }</td>
									<td style="text-align: left;">${blog.title }</td>
									<td style="text-align: left;">
										&nbsp;&nbsp;<a href="${base_url}blog/delete/${blog.id }">删除</a>
										&nbsp;&nbsp;<a href="${base_url}blog/edit/${blog.id }">修改</a>
										&nbsp;&nbsp;<a href="${base_url}blog/view/${blog.id }">查看</a>
									</td>
				 				</tr>
							</#list>
						<#else>
							<tr>
								<td style="text-align:center;" colspan="3">
									暂无数据记录！
								</td>
			 				</tr>
						</#if>
					</tbody>
				</table>
				
				<!-- 分页宏定义 -->
				<@net.pager pageNumber=pager.pageNumber totalPage=pager.totalPage baseUrl=base_url+'blog' />
				
			</div>
		</div>	
	</div>
</body>
</html>