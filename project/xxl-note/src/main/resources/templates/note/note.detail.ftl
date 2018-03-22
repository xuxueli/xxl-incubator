
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title>菜单栏</title>

    <link rel="stylesheet" href="${request.contextPath}/static/plugins/normalize/normalize.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/editor-md/main/editormd.preview2.css">

    <style type="text/css">
        body {
            margin:0;
            padding:0;
            font-family: 'Helvetica Neue',Arial,'Hiragino Sans GB',STHeiti,'Microsoft YaHei','WenQuanYi Micro Hei',SimSun,Song,sans-serif;
            background: #F6F6F6;
        }
        #markdown_sidebar {
            width: 200px;
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            overflow: hidden;
            z-index: 100;
            padding: 0;
            border: 1px solid #ddd;
            border-top: none;
            border-bottom: none;
            background:#F6F6F6;
            padding-right:20px;
        }
        #markdown_sidebar:hover {
            overflow: auto;
        }
        #markdown_sidebar > .markdown-body{
            background:none;
        }
        #markdown_sidebar h1 {
            margin:0;
            padding:0;
            font-size: 1.6em;
            text-align: center;
            font-weight: bold;
            color:#999;
            border-bottom:1px dashed #CCC;
            line-height:80px;
        }
        #custom_toc_container {
            padding-left: 0;
        }
        #markdown_title{
            padding:0 40px 0 260px;
            font-size:1.8em;
            line-height:80px;
            margin:0;
            width:auto;
            text-align: center;
            color:#2c3f51;
            border-bottom:1px solid #CCC;
            font-weight:bold;
            max-width:900px;
            background: #FFF;
            border-right:1px solid #EEE;
        }
        #markdown_view {
            padding:40px 40px 40px 260px;
            margin: 0;
            width:auto;
            max-width:900px;
            background: #FFF;
            border-right:1px solid #EEE;
        }
    </style>
</head>
<body>

<div id="markdown_sidebar">
    <h1>目录</h1>
    <div class="markdown-body editormd-preview-container" id="custom_toc_container"></div>
</div>
<div id="markdown_title">
    菜单</div>
<div id="markdown_view">
    <textarea style="display:none;">


### hello world


    </textarea>
</div>
<script src="${request.contextPath}/static/plugins/jquery/jquery.2.1.4.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/marked.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/prettify.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/raphael.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/underscore.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/sequence-diagram.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/flowchart.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/jquery.flowchart.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/lib/marked.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor-md/main/editormd.min.js"></script>
<script type="text/javascript">
    $(function () {
        editormd.markdownToHTML("markdown_view", {
            htmlDecode : "style,script,iframe|on*",
            tocContainer: "#custom_toc_container",
            markdownSourceCode: false,
            emoji: false,
            taskList: true,
            tex: true,
            flowChart: true,
            sequenceDiagram: true,
            onload: function(div){
                div.find('.sequence-diagram > svg, .flowchart > svg').each(function(i,o){
                    try{
                        var width = $(o).attr('width'),
                                height = $(o).attr('height');
                        //$(o).attr('viewBox','0 0 '+height+' '+width);
                        o.setAttribute('viewBox','0 0 '+width+' '+height);
                    }catch(e){}
                });
            }
        });
    });
</script>
</body>
</html>