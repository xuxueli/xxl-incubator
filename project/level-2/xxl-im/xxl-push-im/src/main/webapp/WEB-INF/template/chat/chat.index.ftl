<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>消息推送中心</title>

<#import "/common/common.macro.ftl" as netCommon>
<@netCommon.commonStyle />

<style>
	#console {height: 100%;overflow: auto;}
	.username-msg {color: orange;}
	.connect-msg {color: green;}
	.disconnect-msg {color: red;}
	.send-msg {color: #888}
</style>

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">

<@netCommon.commonHeader />
<@netCommon.commonLeft />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>连接管理 <small></small></h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- 全部配置 -->
            <div class="box box-info2">
                <div class="box-body">


                    <body navKey="chat" >
                    <form class="well form-inline" onsubmit="return false;">
                        <input id="msg" class="input-xlarge" type="text" placeholder="请输入..." />
                        <button type="button" id="send" class="btn">发送</button>
                    </form>
                    <div id="console" class="well"></div>
                    </body>

                </div><!-- /.box-body -->
            </div><!-- /.box -->

        </section>
        <!-- /.content -->

    </div>
    <!-- /.content-wrapper -->

<@netCommon.commonFooter />

</div>
<!-- ./wrapper -->

<@netCommon.commonScript/>

<script type="text/javascript" src="${request.contextPath}/static/js/socket.io-1.3.5.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/chat.index.1.js"></script>

</body>
</html>