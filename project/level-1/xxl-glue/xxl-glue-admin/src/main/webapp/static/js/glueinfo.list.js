$(function() {
	
	// init date tables
	var glueTable = $("#glue_list").dataTable({
		"deferRender": true,
		"processing" : true,
		"serverSide": true,
		"ajax": {
			url: base_url + "/glueinfo/pageList",
			type:"post",
			data : function ( d ) {
				var obj = {};
				obj.projectId = $('#projectId').val();
				obj.name = $('#name').val();
				obj.start = d.start;
				obj.length = d.length;
				return obj;
			}
		},
		"searching": false,
		"ordering": false,
		//"scrollX": true,	// X轴滚动条，取消自适应
		"columns": [
			{ "data": 'id', "bSortable": false, "visible" : false},
			{ "data": 'projectId', "visible" : false},
			{
				"data": 'name',
				"width":'20%',
				"render": function ( data, type, row ) {
					return data;
				}
			},
			{ "data": 'about',"width":'40%'},
			{ "data": 'source', "visible" : false},
			{
				"data": 'addTime',
				"visible" : false,
				"render": function ( data, type, row ) {
					return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
				}
			},
			{
				"data": 'updateTime',
				"width":'15%',
				"render": function ( data, type, row ) {
					return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
				}
			},
			{
				"data": '操作' ,
				"width":'25%',
				"render": function ( data, type, row ) {
					return function(){
						// log url
						var glueWebIde = base_url +'/glueinfo/glueWebIde?id='+ row.id;

						// html
						tableData['key'+row.id] = row;
						var html = '<span id="'+ row.id +'" >'+
							'<button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open(\'' + glueWebIde + '\')" >WebIde</button>  '+
							'<button class="btn btn-primary btn-xs clearCache" type="button">清理缓存</button>  '+
							'<button class="btn btn-warning btn-xs update" type="button">编辑</button>  '+
							'<button class="btn btn-danger btn-xs delete" type="button">删除</button>  '+
							'</span>';

						return html;
					};
				}
			}
		],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});

	// table data
	var tableData = {};

	// 搜索按钮
	$('#searchBtn').on('click', function(){
		glueTable.fnDraw();
	});

	// delete
	$("#glue_list").on('click', '.delete',function() {
		var id = $(this).parent('span').attr("id");
		layer.confirm('确认删除该GLUE?', {icon: 3, title:'系统提示'}, function(index){
			layer.close(index);

			$.ajax({
				type : 'POST',
				url : base_url + "/glueinfo/delete",
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {

						layer.open({
							title: '系统提示',
							content: "删除成功",
							icon: '1',
							end: function(layero, index){
								glueTable.fnDraw();
							}
						});
					} else {
						layer.open({
							title: '系统提示',
							content: (data.msg || "删除失败"),
							icon: '2'
						});
					}
				},
			});
		});
	});

	// jquery.validate 自定义校验 “长度4-50位的大小写字母、数字和下划线”
	jQuery.validator.addMethod("projectName", function(value, element) {
		var length = value.length;
		var valid = /^[a-zA-Z0-9_]{4,50}$/;
		return this.optional(element) || valid.test(value);
	}, "正确格式为：长度4-20位的大小写字母、数字和下划线");

	// 新增
	$(".add").click(function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			name : {
				required : true,
				minlength: 4,
				maxlength: 50,
				projectName: true
			},
			about : {
				required : true
			}
		},
		messages : {
			name : {
				required :"请输入“GLUE名称”",
				minlength: "长度不可少于4",
				maxlength: "长度不可多余50"
			},
			about : {
				required : "请输入“GLUE描述”"
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
			$.post(base_url + "/glueinfo/add",  $("#addModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#addModal').modal('hide');
					layer.open({
						title: '系统提示',
						content: '新增任务成功',
						icon: '1',
						end: function(layero, index){
							glueTable.fnDraw();
						}
					});
				} else {
					layer.open({
						title: '系统提示',
						content: (data.msg || "新增失败"),
						icon: '2'
					});
				}
			});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
		$("#addModal .form")[0].reset();
		/*addModalValidate.resetForm();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote*/
	});

	// 更新
	$("#glue_list").on('click', '.update',function() {

		var id = $(this).parent('span').attr("id");
		var row = tableData['key'+id];
		if (!row) {
			layer.open({
				title: '系统提示',
				content: ("任务信息加载失败，请刷新页面"),
				icon: '2'
			});
			return;
		}

		// base data
		$("#updateModal .form input[name='id']").val( row.id );
		$('#updateModal .form select[name=projectId] option[value='+ row.projectId +']').prop('selected', true);
		$("#updateModal .form input[name='name']").val( row.name );
		$("#updateModal .form input[name='about']").val( row.about );

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			about : {
				required : true
			}
		},
		messages : {
			about : {
				required : "请输入“GLUE描述”"
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
			// post
			$.post(base_url + "/glueinfo/update", $("#updateModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#updateModal').modal('hide');
					layer.open({
						title: '系统提示',
						content: '更新成功',
						icon: '1',
						end: function(layero, index){
							//window.location.reload();
							glueTable.fnDraw();
						}
					});
				} else {
					layer.open({
						title: '系统提示',
						content: (data.msg || "更新失败"),
						icon: '2'
					});
				}
			});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
		$("#updateModal .form")[0].reset()
	});


	// 清理缓存
	$("#glue_list").on('click', '.clearCache',function() {

		var id = $(this).parent('span').attr("id");
		var row = tableData['key'+id];
		if (!row) {
			layer.open({
				title: '系统提示',
				content: ("任务信息加载失败，请刷新页面"),
				icon: '2'
			});
			return;
		}

		// base data
		$("#clearCacheModal .form input[name='id']").val( row.id );

		// show
		$('#clearCacheModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var clearCacheValidate = $("#clearCacheModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
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
			// post
			$.post(base_url + "/glueinfo/clearCache", $("#clearCacheModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#clearCacheModal').modal('hide');
					layer.open({
						title: '系统提示',
						content: '清理成功',
						icon: '1',
						end: function(layero, index){
							//window.location.reload();
							glueTable.fnDraw();
						}
					});
				} else {
					layer.open({
						title: '系统提示',
						content: (data.msg || "清理失败"),
						icon: '2'
					});
				}
			});
		}
	});
	$("#clearCacheModal").on('hide.bs.modal', function () {
		$("#clearCacheModal .form")[0].reset()
	});

});
