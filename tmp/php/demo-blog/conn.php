<?php
$conn = @ mysql_connect ( "127.0.0.1:3306", "root", "root_pwd" ) or die ( "数据库链接错误" );
mysql_select_db ( "test", $conn );
mysql_query ( "set names 'utf-8'" );
?> 