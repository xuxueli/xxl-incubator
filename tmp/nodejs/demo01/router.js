// 路由

function route(handle, pathname, response, postDate){
	console.log("About to route request for:" + pathname);
	
	if(typeof handle[pathname] === 'function'){
		handle[pathname](response, postDate);
	} else {
		console.log("No request handler found for:" + pathname);
		response.writeHead(404, {"Content-Type": "text/plain"});
		response.write("404 Not Found");
		response.end();
	}
}

exports.route = route;