$(function() {
	
	// 创建任务
	var lcreateDevTaskValid = $("#createDevTaskForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
            title : {  
            	required : true ,
                minlength: 2,
                maxlength: 50
            } 
        }, 
        messages : {  
            title : {
            	required :"请输入任务标题."  ,
                minlength:"任务标题不应低于2位",
                maxlength:"任务标题不应超过50位",}
        }, 
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
			$.post(base_url + "task/createDevTask", $("#createDevTaskForm").serialize(), function(data, status) {
				if (data.code == "S") {
					$('#createDevTaskModal').modal('hide');
					
					ComAlert.show(1, "任务创建成功");
					ComAlert.callback = function (){
						window.location.reload();
					}
				} else {
					ComAlert.show(0, "登陆失败:" + data.msg);
				}
			});
		}
	});
	$("#createDevTaskModal").on('hide.bs.modal', function () {
		$("#createDevTaskForm")[0].reset()
	})
	
	// 删除任务
	$(".deleteDevTask").click(function() {
		var id = $(this).attr("id");
		$.ajax({
			type : 'post',
			url : base_url + "task/deleteDevTask/" + id,
			dataType : 'json',
			success : function(data){
				if (data.code == "S") {
					ComAlert.callback = function(){
						window.location.reload();
					}
					ComAlert.show(1, "操作成功");
				}
			}
		});
	});
	
	// 启动流程
	$(".startProcess").click(function() {
		var id = $(this).attr("id");
		$.ajax({
			type : 'post',
			url : base_url + "task/startProcess/" + id,
			dataType : 'json',
			success : function(data){
				if (data.code == "S") {
					ComAlert.callback = function(){
						window.location.reload();
					}
					ComAlert.show(1, "流程启动成功");
				}
			}
		});
	});
	
	// 完成任务
	$(".competeTask").click(function() {
		var taskId = $(this).attr('taskId');
		var status = $(this).attr('status');
		$.ajax({
			//type : 'post',
			url : base_url + "task/completeTask/" + taskId + "/" + status,
			dataType : 'json',
			success : function(data){
				if (data.code == "S") {
					ComAlert.callback = function(){
						window.location.reload();
					}
					ComAlert.show(1, "操作成功");
				} else {
					ComAlert.show(1, data.msg);
				}
			}
		});
	});
	
	// 查看流程图
	$(".showProcess").click(function() {
		var processInstanceId = $(this).attr('processInstanceId');
		window.open(base_url + "task/showProcess?processInstanceId=" + processInstanceId);
	});
	
});
