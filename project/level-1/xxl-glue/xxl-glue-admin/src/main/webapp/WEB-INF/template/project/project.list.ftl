<#import "/common/common.macro.ftl" as netCommon>
<!DOCTYPE html>
<html>
<head>
    <title><@netCommon.commonName /></title>

	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
	<@netCommon.commonHeader />
		<!-- left -->
	<@netCommon.commonLeft "project" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>项目管理<small></small></h1>
        </section>

        <!-- Main content -->
        <section class="content">


            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">项目列表</h3>
                            <button class="btn btn-info btn-xs pull-right" id="add">+新增项目</button>
                        </div>
                        <div class="box-body" >
                            <table id="project_list" class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th name="id" >id</th>
                                    <th name="appname" >项目AppName</th>
                                    <th name="name" >项目名称</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                                <tfoot></tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- footer -->
<@netCommon.commonFooter />
</div>

<!-- 新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >新增项目</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">项目AppName<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="appname" placeholder="请输入“项目AppName”" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">项目名称<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="name" placeholder="请输入“项目名称”" maxlength="50" ></div>
                    </div>
                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="submit" class="btn btn-primary"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >更新任务</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">项目AppName<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="appname" placeholder="请输入“项目AppName”" maxlength="20" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">项目名称<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="name" placeholder="请输入“项目名称”" maxlength="50" ></div>
                    </div>
                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="submit" class="btn btn-primary"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <input type="hidden" name="id" >
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>

<script>
    projectList = eval('('+ '${projectList}' +')');
</script>
<script src="${request.contextPath}/static/js/project.list.js"></script>
</body>
</html>
