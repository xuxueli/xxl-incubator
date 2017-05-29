<#import "/common/common.macro.ftl" as netCommon>
<!DOCTYPE html>
<html>
<head>
  	<!-- Standard Meta -->
  	<meta charset="utf-8" />
  	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  	<!-- Site Properities -->
  	<title>登陆</title>
	<@netCommon.commonStyle />
	<style type="text/css">
    	body {
      		background-color: #DADADA;
    	}
    	body > .grid {
      		height: 100%;
    	}
		.image {
      		margin-top: -100px;
		}
    	.column {
      		max-width: 450px;
    	}
	</style>
</head>
<body>

	<div class="ui middle aligned center aligned grid">
		<div class="column">
			<h2 class="ui teal image header">
				<img src="${request.contextPath}/static/image/logo.png" class="image">
				<div class="content">Glue</div>
		    </h2>
		    <form class="ui large form" id="loginForm">
		      	<div class="ui stacked segment">
					<div class="field">
			          	<div class="ui left icon input">
			            	<i class="user icon"></i>
			            	<input type="text" name="userName" placeholder="请输入用户名" value="admin" >
			          	</div>
					</div>
			        <div class="field">
			          	<div class="ui left icon input">
			            	<i class="lock icon"></i>
			            	<input type="password" name="password" placeholder="请输入密码" value="123456" >
			          	</div>
					</div>
			        <div class="ui fluid large teal submit button">Login</div>
		      	</div>
		      	<div class="ui error message"></div>
		    </form>
		    <!--
			<div class="ui message"> 第一次? <a href="#">注册</a></div>
			-->
		</div>
	</div>

<@netCommon.commonScript />
<script src="${request.contextPath}/static/js/login.js" ></script>

</script>
</body>
</html>
