<#macro header>
<!-- header导航栏navbar-inverse  -->
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container">
		<!-- 折叠 -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#net-navbar-collapse">
				<span class="sr-only"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
	      	</button>
			<a class="navbar-brand" href="${base_url}" ><span class="glyphicon glyphicon-home"></span></a>
		</div>
		<!-- 响应式特性 -->
		<div class="collapse navbar-collapse" id="net-navbar-collapse">
			<!-- 左对齐 -->
			<ul class="nav navbar-nav navbar-left active-nav" >
				<li class="nav-click" ><a href="${base_url}task/">任务</a></li>
			</ul>
			<!--右对齐-->
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown login-false">
				    <a href="#" class="dropdown-toggle" data-toggle="dropdown">小窝 <b class="caret"></b></a>
				    <ul class="dropdown-menu">
				       <li><a href="#" data-toggle="modal" data-target="#loginModal" >登陆</a></li>
				    </ul>
				</li>
				<li class="dropdown login-true">
				    <a href="#" class="dropdown-toggle loginEmail" data-toggle="dropdown">小窝 <b class="caret"></b></a>
				    <ul class="dropdown-menu">
				       <li><a href="javascript:;" class="logout" >注销登陆</a></li>
				    </ul>
				</li>
			</ul>
	   </div>
	</div>
</nav>

<!-- 登陆.模态框 -->
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="loginModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<!--	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>	-->
            	<h4 class="modal-title" id="loginModalLabel">用户登陆</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal" role="form" id="loginForm">
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">邮箱</label>
						<div class="col-sm-10"><input type="text" class="form-control" name="email" placeholder="请输入邮箱" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-10"><input type="password" class="form-control" name="password" placeholder="请输入密码" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary"  >登陆</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>


<!-- 通用提示框.模态框Modal -->
<div class="modal fade" id="comAlert" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<!--	<div class="modal-header"><h4 class="modal-title"><strong>提示:</strong></h4></div>	-->
         	<div class="modal-body"><div class="alert alert-success"></div></div>
         	<div class="modal-footer">
         		<div class="text-center" >
            		<button type="button" class="btn btn-default" data-dismiss="modal" >确认</button>
            	</div>
         	</div>
		</div>
	</div>
</div>
</#macro>

<#macro footer>
<footer class="footer">
	<!-- footerlinks -->
	<section class="footerlinks">
		<div class="container">
			<div class="info">
				<ul>
					<li><b>关于:</b></li>
	          		<li><a href="#">pm简介</a></li>
			    	<li><a href="#">在线反馈</a></li>
			   		<li><a href="#">用户协议</a></li>
			        <li><a href="#">隐私政策</a></li>
	        	</ul>
	      	</div>
	    </div>
	</section>
	<!-- copyrightbottom -->
	<section class="copyrightbottom">
		<div class="container">
			<div class="info">《pm》是一款中小型项目管理软件。</div>
			<div class="info">Copyright © 2015 XXL</div>
		</div>
	</section>
</footer>
</#macro>
