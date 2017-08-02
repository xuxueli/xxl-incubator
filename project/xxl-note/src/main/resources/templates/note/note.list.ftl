<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title>笔记列表</title>
</head>
<body>
    <#if noteGroupList??>
        <#list noteGroupList as group>
            ${group.name}
        </#list>
    </#if>
</body>
</html>