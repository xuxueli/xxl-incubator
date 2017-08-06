<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title>笔记列表</title>
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/bootstrap/bootstrap.min.css">
    <style>
    </style>
</head>
<body>

    <nav class="navbar navbar-default" >
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">Note</a>
            </div>
        </div>
    </nav>

    <div class="row">
        <#-- group -->
        <div class="col-md-2 group"  >
            <ul class="list-group">
                <li class="list-group-item active" >
                    文档分组 +
                    <span class="badge pull-right">5</span>
                </li>
                <a href="#" groupId="0" >
                    <li class="list-group-item" >
                        默认分组<span class="badge pull-right">5</span>
                    </li>
                </a>
                <#if noteGroupList??>
                    <#list noteGroupList as group>
                        <a href="#" groupId="${group.id}" >
                            <li class="list-group-item" >
                                ${group.name}
                                <span class="badge pull-right">1</span>
                            </li>
                        </a>
                    </#list>
                </#if>
            </ul>

        </div>
        <#-- note -->
        <div class="col-md-2" >

            <ul class="list-group">
                <li class="list-group-item active" >文档列表</li>
                <#-- ${request.contextPath}/note/detail?id=1 -->
                <a href="#" noteId="1" >
                    <li class="list-group-item" >
                        文档标题1
                    </li>
                </a>
            </ul>
        </div>
        <#-- editor -->
        <div class="col-md-8" >.col-md-1</div>
    </div>


</body>

<script src="${request.contextPath}/static/plugins/jquery/jquery.2.1.4.min.js"></script>
<script src="${request.contextPath}/static/plugins/bootstrap/bootstrap.min.js"></script>
<script>
    $(function () {

    });
</script>

</html>