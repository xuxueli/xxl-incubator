// 服务器

var http = require("http");
var url = require("url");

// 启动服务器
function start(route, handle){
	// 请求回调函数
	function onRequest(request, response) { 
		var postData = "";
		var pathname = url.parse(request.url).pathname;
		console.log("Request for " + pathname + " received");
		
		request.setEncoding("utf-8");
		request.addListener("data", function(postDataChunk){
			postData += postDataChunk;
			console.log("Receive POST data chunk'" + postDataChunk + "'.");
		});
		
		request.addListener("end", function(){
			route(handle, pathname, response, postData);
		});
	}
	
	// 创建nodejs服务器，监听3000端口
	http.createServer(onRequest).listen(3000);
	console.log("Server at 3000 started:" +  + new Date());
}

// 导出函数
exports.start = start;