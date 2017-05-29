$(function () {
	// 闭包，自由变量
	$('.logoutBtn').on('click', function(){
		ComAlert.confirm("您确定要注销登陆吗?",
			function(){
				$.ajax({
					type : 'POST',
					url : base_url + 'logout',
					dataType : "json",
					success : function(data){
						if (data.code == 200) {
							ComAlert.alert("注销成功", function(){
								window.location.href = base_url;
							});
						} else {
							alert(data.msg);
						}
					}
				});
			}
		);
	});

})