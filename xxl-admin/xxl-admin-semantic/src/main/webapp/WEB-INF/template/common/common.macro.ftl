<#macro commonStyle>
  	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/semantic-ui/dist/semantic.min2.css">
  	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/semantic-ui/dist/components/modal.min.css">
  	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/pace/pace-theme-minimal.css">
  	<style type="text/css">
	body {
		background-color: #FFFFFF;
  	}
  	.ui.menu .item img.logo {
    	margin-right: 1.5em;
  	}
  	.main.container {
    	margin-top: 7em;
  	}
  	.wireframe {
    	margin-top: 2em;
  	}
  	.ui.footer.segment {
		margin: 5em 0em 0em;
    	padding: 5em 0em;
  	}
  	</style>
</#macro>

<#macro commonScript>
<script src="${request.contextPath}/static/plugins/jquery/jquery.2.1.4.min.js" ></script>
<script src="${request.contextPath}/static/plugins/semantic-ui/dist/semantic.min.js" ></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.1.13.1.min.js" ></script>
<script src="${request.contextPath}/static/plugins/pace/pace.min.js" ></script>

<script> var base_url = '${request.contextPath}/'; </script>
<script src="${request.contextPath}/static/js/comalert.js" ></script>
<script src="${request.contextPath}/static/js/common.js" ></script>

</#macro>

<#macro commonHeader>
<div class="ui fixed inverted menu">
	<div class="ui container">
		<a href="${request.contextPath}/code" class="header item"><img class="logo" src="${request.contextPath}/static/image/logo.png">Glue</a>
	    <a href="${request.contextPath}/help" class="item">文档</a>
	    <#--
	    <a href="${request.contextPath}/code/demoEditor" class="item" target="_blank" >Demo编辑器</a>
	  	<div class="ui simple dropdown item">
			父菜单下拉 <i class="dropdown icon"></i>
			<div class="menu">
				<a class="item" href="#">子菜单A</a>
				<a class="item" href="#">子菜单B</a>
				<div class="divider"></div>
				<div class="header">组标题</div>
				<div class="item">
	            	<i class="dropdown icon"></i>
	            	子菜单组
		            <div class="menu">
		              <a class="item" href="#">子菜单C</a>
		              <a class="item" href="#">子菜单D</a>
		            </div>
				</div>
				<a class="item" href="#">子菜单E</a>
			</div>
		</div>
		-->
		<a href="javascript:;" class="item right logoutBtn">注销</a>
		<#--<a href="#" class="item">注册</a>-->
    </div>
</div>
</#macro>

<#macro commonFooter >
<#--
<div class="ui inverted vertical footer segment">
	<div class="ui container">
		<div class="ui stackable inverted divided equal height stackable grid">
			<div class="three wide column">
				<h4 class="ui inverted header teal">About</h4>
	          	<div class="ui inverted link list">
		            <a href="#" class="item">网站地图</a>
		            <a href="#" class="item">联系我们</a>
		            <a href="#" class="item">意见反馈</a>
		            <a href="#" class="item disabled">赞助</a>
	          	</div>
			</div>
	        <div class="three wide column">
	          	<h4 class="ui inverted header teal">服务</h4>
	          	<div class="ui inverted link list">
		            <a href="#" class="item">在线教程</a>
		            <a href="#" class="item">接入文档</a>
		            <a href="#" class="item">问题汇总</a>
	          	</div>
			</div>
			<div class="seven wide column">
				<h4 class="ui inverted header">热米饭里拌什么最好吃？</h4>
				<p>土豆烧牛肉。赫鲁晓夫说土豆烧牛肉是共产主义，此言不虚。</p>
			</div>
		</div>
	</div>
</div>
-->
</#macro>