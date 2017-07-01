// 处理函数

var exec = require("child_process").exec; // 异步处理
var querystring = require("querystring");

function start(response, postDate){
	console.log("Request handler 'start' wad called");
	
	var body = '<html>'+
	'<head>'+
	'<meta http-equiv="Content-Type" content="text/html;'+
	'charset=UTF-8"/>'+
	'</head>'+
	'<body>'+
	'<form action="/upload" method="post">'+
	'<textarea name="text" rows="10" cols="60"></textarea>'+
	'<input type="submit" value="Submit text" />'+
	'</form>'+
	'</body>'+
	'</html>';

	response.writeHead(200, {"Content-Type": "text/html"});
	response.write(body);
	response.end();
}

function upload(response, postDate){
	console.log("Request handler 'upload' wad called");
	response.writeHead(200, {"Content-Type": "text/plain"});
	//response.write("You've sent:" + postDate);
	response.write("You've sent:" + querystring.parse(postDate).text);
	response.end();
}

exports.start = start;
exports.upload = upload;