/**
 * Created by xuxueli on 17/5/29.
 */
$(function () {

    // table data
    var tableData = {};

    // init date tables
    var projectTable = $("#project_list").dataTable({
        "data": projectList,
        "columns": [
            { "data": 'id', "bSortable": false, "visible" : false},
            { "data": 'appname', "visible" : true, "bSortable": false, "width":'30%'},
            { "data": 'name', "visible" : true, "bSortable": false, "width":'50%'},
            {
                "data": '操作' ,
                "width":'20%',
                "bSortable": false,
                "render": function ( data, type, row ) {
                    return function(){

                        // html
                        tableData['key'+row.id] = row;

                        var html = '<p id="'+ row.id +'" >'+
                            '<button class="btn btn-warning btn-xs update" >编辑</button>  '+
                            '<button class="btn btn-danger btn-xs delete" >删除</button>  '+
                            '</p>';

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

    // delete
    $("#project_list").on('click', '.delete',function() {
        var id = $(this).parent('p').attr("id");

        layer.confirm('确认删除该项目?', {icon: 3, title:'系统提示'}, function(index){
            layer.close(index);

            $.ajax({
                type : 'POST',
                url : base_url + "/project/delete",
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
                                window.location.reload();
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

    // jquery.validate 自定义校验 “长度4-20位的小写字母、数字和下划线”
    jQuery.validator.addMethod("appName", function(value, element) {
        var length = value.length;
        var valid = /^[a-z0-9_]{4,20}$/;
        return this.optional(element) || valid.test(value);
    }, "正确格式为：长度4-20位的小写字母、数字和下划线");

    // 新增
    $("#add").click(function(){
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var addModalValidate = $("#addModal .form").validate({
        errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            appname : {
                required : true,
                minlength: 4,
                maxlength: 20,
                appName: true
            },
            name : {
                required : true,
                minlength: 4,
                maxlength: 20
            }
        },
        messages : {
            appname : {
                required :"请输入“项目AppName”",
                minlength: "长度不可少于4",
                maxlength: "长度不可多余20"
            },
            name : {
                required :"请输入“项目名称”",
                minlength: "长度不可少于4",
                maxlength: "长度不可多余20"
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
            $.post(base_url + "/project/save",  $("#addModal .form").serialize(), function(data, status) {
                if (data.code == 200) {
                    layer.open({
                        title: '系统提示',
                        content: "新增成功",
                        icon: '1',
                        end: function(layero, index){
                            window.location.reload();
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
        addModalValidate.resetForm();
        $("#addModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote
    });

    // 更新
    $("#project_list").on('click', '.update',function() {

        var id = $(this).parent('p').attr("id");
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
        $("#updateModal .form input[name='appname']").val( row.appname );
        $("#updateModal .form input[name='name']").val( row.name );

        // show
        $('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var updateModalValidate = $("#updateModal .form").validate({
        errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            name : {
                required : true,
                minlength: 4,
                maxlength: 20
            }
        },
        messages : {
            name : {
                required :"请输入“项目名称”",
                minlength: "长度不可少于4",
                maxlength: "长度不可多余20"
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
            $.post(base_url + "/project/update", $("#updateModal .form").serialize(), function(data, status) {
                if (data.code == 200) {
                    layer.open({
                        title: '系统提示',
                        content: "更新失败",
                        icon: '1',
                        end: function(layero, index){
                            window.location.reload();
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

});