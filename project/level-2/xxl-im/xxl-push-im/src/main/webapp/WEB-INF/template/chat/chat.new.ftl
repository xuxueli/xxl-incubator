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

<#--<@netCommon.commonScript/>-->
<!-- jQuery 2.1.4 -->
<script src="${request.contextPath}/static/adminlte/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<!-- Bootstrap 3.3.5 -->
<script src="${request.contextPath}/static/adminlte/bootstrap/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="${request.contextPath}/static/adminlte/plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="${request.contextPath}/static/adminlte/dist/js/app.min.js"></script>
<!-- pace -->
<#--<script src="${request.contextPath}/static/adminlte/plugins/pace/pace.min.js"></script>-->

<!-- scrollup -->
<script src="${request.contextPath}/static/plugins/scrollup/jquery.scrollUp.min.js"></script>

<#-- jquery.cookie -->
<script src="${request.contextPath}/static/plugins/jquery/jquery.cookie.js"></script>
<#-- toastr -->
<script src="${request.contextPath}/static/plugins/toastr/toastr.js"></script>

<script src="${request.contextPath}/static/js/xxl.alert.1.js"></script>
<script src="${request.contextPath}/static/js/common.1.js"></script>

<script>var base_url = '${request.contextPath}';</script>

<script type="text/javascript" src="${request.contextPath}/static/js/chat.new.js"></script>

</body>
</html>