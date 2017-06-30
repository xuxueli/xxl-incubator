$(function () {

	// dropdown
	$('.ui.dropdown')
		.dropdown()
	;

	// init date tables
	var codeTable = $("#code_list").dataTable({
		"deferRender": true,
		"processing" : true,
		"serverSide": true,
		"ajax": {
			url: base_url + "code/pageList" ,
			data : function ( d ) {
				var obj = {};
				obj.name = $('#codeName').val()
				obj.start = d.start;
				obj.length = d.length;
				return obj;
			}
		},
		"searching": false,
		"ordering": true,
		//"scrollX": true,  // X轴滚动条，强制性固定长度
		"columns": [
			{ "data": 'id', "bSortable": false, "visible" : true},
			{ "data": 'name', "bSortable": false},
			{ "data": 'about', "bSortable": false}
		],
		"columnDefs": [
			{
				"targets": 3,
				"data": "id",
				"align":"right",
				"render": function ( data, type, row ) {
					var html = '';
					html += '<button type="button" class="red ui mini button delCode" _id="'+ row.id +'" >删除</button>';
					return html;
				}
			}
		],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页 )",
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

	// 搜索按钮
	$('#searchBtn').on('click', function(){
		codeTable.fnDraw();
	});

	// 搜索按钮，回车监听
	$('#codeName').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			$('#searchBtn').click();
		}
	});

	// 清除GLUE缓存
	$('#code_list').on('click', '.clearCache', function(){
		var _id = $(this).attr('_id');
		$('#clearCacheModal .form input[name="id"]').val(_id);
		$('#clearCacheModal').modal({
			closable  : false,
			duration : 100,
			allowMultiple: true,
			onApprove : function(){	},
			onDeny : function() {	}
		}).modal('show');

	});
	// 清除GLUE缓存 form
	$('#clearCacheModal .form').form({
		onSuccess: function(){
			$.ajax({
				type : 'POST',
				url : base_url + 'code/clearCache',
				data : $("#clearCacheModal .form").serialize(),
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						ComAlert.alert('操作成功', function(){
							codeTable.fnDraw();
						});
					} else {
						$('#clearCacheModal .form').removeClass('success').addClass('error');
						$('#clearCacheModal .form .error').html('<ul class="list"><li>'+ data.msg +'</li></ul>');
					}
				}
			});
			return false;	// 避免默认return true表单提交
		}
	});

	// 删除
	$('#code_list').on('click', '.delCode', function(){
		var _id = $(this).attr('_id');
		ComAlert.confirm("确定要进行删除操作，该操作不可恢复",
			function(){
				$.ajax({
					type : 'POST',
					url : base_url + 'code/delCode',
					data : {
						id : _id
					},
					dataType : "json",
					success : function(data){
						if (data.code == 200) {
							ComAlert.alert("删除成功", function(){
								codeTable.fnDraw();
							});
						} else {
							ComAlert.alert(data.msg);
						}
					}
				});
			}
		);
	});

	// 新增
	$('#addBtn').on('click', function(){
		$('#addModal').modal({
			closable  : false,
			duration : 100,
			allowMultiple: true,
			onApprove : function(){	},
			onDeny : function() {	}
		}).modal('show');
	});

	// 新增form
	$('#addModal .form').form({
		fields: {
			name: {
				identifier  : 'name',
				rules: [
					{
						type   : 'empty',
						prompt : '请输入“GLUE名称”'
					},
					{
						type   : 'length[6]',
						prompt : '“GLUE名称”长度应该大于6位'
					},
					{
						type   : 'regExp[/^[a-zA-Z][a-zA-Z0-9_]*$/]',
						prompt : '“GLUE名称”只支持英文字母开头，且只允许由英文字母、数字和下划线组成'
					}
				]
			},
			remark: {
				identifier  : 'remark',
				rules: [
					{
						type   : 'empty',
						prompt : '请输入“简介”'
					},
					{
						type   : 'length[6]',
						prompt : '“简介”长度应该大于6位'
					}
				]
			}
		},
		onSuccess: function(){
			$.ajax({
				type : 'POST',
				url : base_url + 'code/addCode',
				data : $("#addModal .form").serialize(),
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						ComAlert.alert('保存成功', function(){
							codeTable.fnDraw();
						});
					} else {
						//ComAlert.alert(data.msg);
						$('#addModal .form').removeClass('success').addClass('error');
						$('#addModal .form .error').html('<ul class="list"><li>'+ data.msg +'</li></ul>');
					}
				}
			});
			return false;	// 避免默认return true表单提交
		}
	});
});