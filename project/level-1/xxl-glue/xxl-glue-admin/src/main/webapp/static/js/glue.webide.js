$(function() {

	var codeEditor;
	function initIde(glueSource) {
		var ideMode = "text/x-java";
		/*if ('GLUE_GROOVY'==glueType){
			ideMode = "text/x-java";
		} else if ('GLUE_SHELL'==glueType){
			ideMode = "text/x-sh";
		} else if ('GLUE_PYTHON'==glueType){
			ideMode = "text/x-python";
		}*/

		codeEditor = CodeMirror(document.getElementById("ideWindow"), {
			mode : ideMode,
			lineNumbers : true,
			matchBrackets : true,
			value: glueSource
		});
	}

	initIde($("#version_now").val());

	// code change
	$(".source_version").click(function(){
		var sourceId = $(this).attr('version');
		var temp = $( "#" + sourceId ).val();

		codeEditor.setValue('');
		initIde(temp);
	});

	// code source save
	$("#save").click(function() {
		$('#saveModal').modal({backdrop: false, keyboard: false}).modal('show');
	});

	var saveModalValidate = $("#saveModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			remark : {
				required : true,
				minlength: 4,
				maxlength: 50
			}
		},
		messages : {
			remark : {
				required :"请输入“备注”",
				minlength: "备注不可少于4",
				maxlength: "备注不可多余50"
			}
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

			var source = codeEditor.getValue();
			var remark = $("#saveModal .form input[name=remark]").val();

			$.ajax({
				type : 'POST',
				url : base_url + '/glueinfo/updateCodeSource',
				data : {
					'glueId' : id,
					'source' : source,
					'remark' : remark
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						layer.open({
							title: '系统提示',
							content: '保存成功',
							icon: '1',
							end: function(layero, index){
								//$(window).unbind('beforeunload');
								window.location.reload();
							}
						});
					} else {
						layer.open({
							title: '系统提示',
							content: (data.msg || "保存失败"),
							icon: '2'
						});
					}
				}
			});
		}
	});
	$("#saveModalValidate").on('hide.bs.modal', function () {
		$("#saveModalValidate .form")[0].reset();
	});

	// before upload
	/*$(window).bind('beforeunload',function(){
		return 'Glue尚未保存，确定离开Glue编辑器？';
	});*/
	
});
