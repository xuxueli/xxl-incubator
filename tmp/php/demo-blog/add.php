<?php
include 'conn.php';
if ($_POST ['submit']) {
	$sql = "INSERT INTO message(id,user,title,content,lastdate) VALUES (NULL, '$_POST[user]', '$_POST[title]', '$_POST[content]', now())";
	mysql_query ( $sql );
	
	// 页面跳转，实现方式为javascript
	$url = "list.php";
	echo "<script language='javascript' type='text/javascript'>";
	echo "window.location.href='$url'";
	echo "</script>";
}
?>

<script type="text/javascript"> 
function checkPost(){ 
	if(addForm.user.value==""){ 
		alert("请输入用户名"); 
		addForm.user.focus(); 
		return false; 
	} 
	if(addForm.title.value.length<5){ 
		alert("标题不能少于5个字符"); 
		addForm.title.focus(); 
		return false; 
	} 
} 
</script>

<FORM name="addForm" METHOD="POST" ACTION="add.php" onsubmit="return checkPost();">
	用户：<INPUT TYPE="text" NAME="user" /><br /> 标题：<INPUT TYPE="text"
		NAME="title" /><br /> 内容：
	<TEXTAREA NAME="content" ROWS="8" COLS="30"></TEXTAREA>
	<br /> <INPUT TYPE="submit" name="submit" value="add" />
</FORM>
