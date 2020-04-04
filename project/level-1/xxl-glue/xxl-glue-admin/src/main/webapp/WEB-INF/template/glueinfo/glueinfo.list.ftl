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
	<@netCommon.commonLeft "glueinfo" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>GLUE管理<small></small></h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">项目</span>
                        <select class="form-control" id="projectId" >
                            <option value="0" >全部</option>
                            <#list projectList as project>
                                <option value="${project.id}" >${project.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">Glue名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <button class="btn btn-block btn-info" id="searchBtn">搜索</button>
                </div>
                <div class="col-xs-2">
                    <button class="btn btn-block btn-success add" type="button">+新增GLUE</button>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">GLUE列表</h3>
                        </div>
                        <div class="box-body" >
                            <table id="glue_list" class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th name="id" >id</th>
                                    <th name="projectId" >项目</th>
                                    <th name="name" >GlueName</th>
                                    <th name="about" >描述</th>
                                    <th name="source" >源码</th>
                                    <th name="addTime" >新增时间</th>
                                    <th name="updateTime" >更新时间</th>
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
                <h4 class="modal-title" >新增GLUE</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">项目<font color="red">*</font></label>
                        <div class="col-sm-7">
                            <select class="form-control" name="projectId" >
                                <#list projectList as project>
                                    <option value="${project.id}" >${project.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">GLUE<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="name" placeholder="请输入“GLUE名称”" maxlength="50" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">描述<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="about" placeholder="请输入“描述”" maxlength="20" ></div>
                    </div>

                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="submit" class="btn btn-primary"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        </div>
                    </div>

<textarea name="source" style="display: none;">
package com.xxl.glue.example.handler;

import com.xxl.glue.core.handler.GlueHandler;

import java.util.HashSet;
import java.util.Map;

/**
 * 示例Glue
 *
 * @author xuxueli 2016-4-14 15:36:37
 */
public class DemoGlueHandler01 implements GlueHandler {

	@Override
	public Object handle(Map<String, Object> params) {

		// 手机号码黑名单列表
		HashSet<String> blackTelephones = new HashSet<String>();
		blackTelephones.add("15000000000");
		blackTelephones.add("15000000001");
		blackTelephones.add("15000000002");

		return blackTelephones;
	}

}
</textarea>

                </form>
            </div>
        </div>
    </div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >更新GLUE基础信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">项目<font color="red">*</font></label>
                        <div class="col-sm-7">
                            <select class="form-control" name="projectId" disabled  >
                            <#list projectList as project>
                                <option value="${project.id}" >${project.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">GLUE<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="name" placeholder="请输入“GLUE名称”" maxlength="50" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">描述<font color="red">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="about" placeholder="请输入“描述”" maxlength="20" ></div>
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

<!-- 清理缓存.模态框 -->
<div class="modal fade" id="clearCacheModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >清理GLUE缓存</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-3 control-label">Which App?<font color="black">*</font></label>
                        <div class="col-sm-7"><input type="text" class="form-control" name="appNames" placeholder="请输入待清理GLUE缓存项目的AppName，多个逗号分隔，为空则全站清理。" maxlength="20" ></div>
                    </div>

                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="submit" class="btn btn-primary"  >确认</button>
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
<script src="${request.contextPath}/static/js/glueinfo.list.js"></script>
</body>
</html>
