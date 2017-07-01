<?php
include 'conn.php';
?> 
<?php
echo "<div align='center'><a href='add.php'>继续添加</a></div>";
?>

<table width=500 border="0" align="center" cellpadding="5" cellspacing="1" bgcolor="#add3ef"> 
<?php
$sql = "select * from message order by id";
$query = mysql_query ( $sql );
while ( $row = mysql_fetch_array ( $query ) ) {
?> 
	<tr bgcolor="#eff3ff">
		<td>标题：<font color="red"><?=$row[title]?></font> 用户：<font color="red"><?=$row[user] ?></font>
			<div align="right">
				<a href="preEdit.php?id=<?=$row[id]?>">编辑</a> | <a
					href="delete.php?id=<?=$row[id]?>">删除</a>
			</div>
		</td>
	</tr>
	<tr bgColor="#ffffff">
		<td>内容：<?=$row[content]?></td>
	</tr>
	<tr bgColor="#ffffff">
		<td><div align="right">发表日期：<?=$row[lastdate]?></div></td>
	</tr> 
<?php 
}
?> 
</table>
