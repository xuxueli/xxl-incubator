<?php
include 'conn.php';
$query = "update message set user='$_POST[user]',title='$_POST[title]',content='$_POST[content]' where id='$_POST[id]'";
mysql_query ( $query );
?> 
<?php
// 页面跳转，实现方式为javascript
$url = "list.php";
echo "<script language='javascript' type='text/javascript'>";
echo "window.location.href='$url'";
echo "</script>";
?> 