<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<#include "${base_include}/common/common.param.ftl" >
	<title>view</title>
</head>
<body>
	<div class="manage_container">
		<div class="manage_head">
			<div class="manage_logo">
				<a href="http://www.jfinal.com">JFinal web framework</a>
			</div>
			<div id="nav">
				<ul>
					<li><a href="${base_url}"><b>首页</b></a></li>
					<li><a href="${base_url}blog"><b>Blog管理</b></a></li>
				</ul>
			</div>
		</div>
		<div class="main">
			<h1>Blog管理 ---&gt; ${blog.page_title }Blog</h1>
			<div class="form_box" >
				<fieldset class="solid">
					<legend>${blog.page_title }Blog</legend>
					<input type="hidden" name="blog.id" value="${blog.id}"/>
					
					<div>
						<label>标题</label>
						<input type="text" name="blog.title" value="${blog.title }"/>
					</div>
					<div>
						<label>内容</label>
						<textarea rows="10" cols="80" name="blog.content">${blog.content}</textarea>
					</div>
				</fieldset>
			</div>
		</div>
	</div>
</body>
</html>