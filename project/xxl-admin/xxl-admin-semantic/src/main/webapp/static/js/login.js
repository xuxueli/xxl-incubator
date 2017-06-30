$(function () {


	$('#loginForm').form({
		fields: {
			userName: {
				identifier  : 'email',
				rules: [
					{
						type   : 'empty',
						prompt : '请输入用户名'
					},
					{
						type   : 'length[4]',		// type   : 'email',
						prompt : '请您输入长度超过4位的用户名'
					}
				]
			},
			password: {
				identifier  : 'password',
				rules: [
					{
						type   : 'empty',
						prompt : '请输入密码'
					},
					{
						type   : 'length[4]',
						prompt : '请您输入长度超过4位的密码'
					}
				]
			}
		},
		onSuccess: function(){
			$.ajax({
				type : 'POST',
				url : base_url + 'login',
				data : $("#loginForm").serialize(),
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						ComAlert.alert('登陆成功', function(){
							window.location.href = base_url;
						});
					} else {
						ComAlert.alert(data.msg);
					}
				}
			});
			return false;	// 避免默认return true表单提交
		}
	});

});