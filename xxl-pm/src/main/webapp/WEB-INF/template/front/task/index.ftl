<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	
	<#include "/common/common.style.ftl">
	<#import "/front/common.ftl" as netCommon>
	
	<script type="text/javascript" src="${base_url}js/task.1.js"></script>
	
</head>
<body>
<@netCommon.header />

<!-- content -->
<div class="container">
    
    <div class="row" >
	    <div class="col-xs-9">
	    	<div class="well" >
				<p>PM开发</p>
				<ul class="nav nav-tabs">
					<li><a href="#process1" data-toggle="tab">任务列表<span class="badge"><#if devTaskList?exists && devTaskList?size gt 0 >${devTaskList?size}</#if></span></a></li>
				   	<li class="active"><a href="#process2" data-toggle="tab">开发<span class="badge"><#if dev?exists && dev?size gt 0 >${dev?size}</#if></span></a></li>
				   	<li><a href="#process3" data-toggle="tab">测试<span class="badge"><#if test?exists && test?size gt 0 >${test?size}</#if></span></a></li>
				   	<li><a href="#process4" data-toggle="tab">审核<span class="badge"><#if audit?exists && audit?size gt 0 >${audit?size}</#if></span></a></li>
				</ul>
				<div class="tab-content" >
					<div class="tab-pane fade" id="process1">
						<ul class="list-group">
							<#if devTaskList?exists>
							<#list devTaskList as task>
							<li class="list-group-item">${task.title}
								<button type="button" class="btn btn-success btn-xs pull-right deleteDevTask" id="${task.id}" status="true" >删除任务</button>
								<#if task.processInstanceId?exists>
						   			&nbsp;&nbsp;
						   			<button type="button" class="btn btn-success btn-xs pull-right showProcess" processInstanceId="${task.processInstanceId}" status="true" >流程图</button>
						   			&nbsp;&nbsp;
						   		<#else>
						   			&nbsp;&nbsp;
						   			<button type="button" class="btn btn-success btn-xs pull-right startProcess" id="${task.id}" >启动流程</button>
						   			&nbsp;&nbsp;
						   		</#if>
						   	</li>
							</#list>
							</#if>
						</ul>
				   	</div>
					<div class="tab-pane fade  in active" id="process2">
						<ul class="list-group">
							<#if dev?exists>
							<#list dev as task>
							<li class="list-group-item">${task.name}-${task.processInstanceId}
						   		<button type="button" class="btn btn-success btn-xs pull-right showProcess" processInstanceId="${task.processInstanceId}" status="true" >流程图</button>
						   		<button type="button" class="btn btn-warning btn-xs pull-right competeTask" taskId="${task.id}" status="false" >驳回</button>
						   		<button type="button" class="btn btn-success btn-xs pull-right competeTask" taskId="${task.id}" status="true" >通过</button>
						   	</li>
							</#list>
							</#if>
						</ul>
				   	</div>
				   	<div class="tab-pane fade" id="process3">
						<ul class="list-group">
							<#if test?exists>
							<#list test as task>
						   	<li class="list-group-item">${task.name}-${task.processVariables.assignee}
						   		<button type="button" class="btn btn-danger btn-xs pull-right deleteProcessInstance" taskId="${task.id}" processInstanceId="${task.processInstanceId}" >删除流程</button>
						   		<button type="button" class="btn btn-warning btn-xs pull-right competeTask" taskId="${task.id}" status="false" >驳回</button>
						   		<button type="button" class="btn btn-success btn-xs pull-right competeTask" taskId="${task.id}" status="true" >通过</button>
						   		<button type="button" class="btn btn-success btn-xs pull-right showProcess" processInstanceId="${task.processInstanceId}" status="true" >流程图</button>
						   	</li>
						   	</#list>
						   	</#if>
						</ul>
				   	</div>
				   	<div class="tab-pane fade" id="process4">
						<ul class="list-group">
						   	<#if audit?exists>
							<#list audit as task>
						   	<li class="list-group-item">${task.name}
						   		<button type="button" class="btn btn-danger btn-xs pull-right deleteProcessInstance" taskId="${task.id}" processInstanceId="${task.processInstanceId}" >删除流程</button>
						   		<button type="button" class="btn btn-warning btn-xs pull-right competeTask" taskId="${task.id}" status="false" >驳回</button>
						   		<button type="button" class="btn btn-success btn-xs pull-right competeTask" taskId="${task.id}" status="true" >通过</button>
						   		<button type="button" class="btn btn-success btn-xs pull-right showProcess" processInstanceId="${task.processInstanceId}" status="true" >流程图</button>
						   	</li>
						   	</#list>
						   	</#if>
						</ul>
				   	</div>
				</div>
			</div>
	    </div>
		<div class="col-xs-3">
	    	<ul class="list-group">
				<li class="list-group-item"><a href="javascript:;" data-toggle="modal" data-target="#createDevTaskModal" >创建任务</a></li>
			</ul>
	    </div>
    </div>
	
	<div class="well">Hey Girl.</div>
	<div class="well">Hey Girl.</div>
	<div class="well">Hey Girl.</div>
    
</div>

<@netCommon.footer />

<!-- 创建任务 -->
<div class="modal fade" id="createDevTaskModal" tabindex="-1" role="dialog" aria-labelledby="loginModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<!--	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>	-->
            	<h4 class="modal-title" id="loginModalLabel">创建任务</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal" role="form" id="createDevTaskForm">
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">任务标题</label>
						<div class="col-sm-10"><input type="text" class="form-control" name="title" placeholder="请输入任务标题" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

</body>
</html>