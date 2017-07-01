<?php
include 'conn.php';
$id = $_GET [id];
$query = "SELECT * FROM message WHERE id =" . $id;
$result = mysql_query ( $query );
while ( $rs = mysql_fetch_array ( $result ) ) {
	?>
<FORM METHOD="POST" ACTION="postEdit.php">
	<input type="hidden" name="id" value="<?=$rs[id]?>"> 用户：<INPUT
		TYPE="text" NAME="user" value="<?=$rs[user]?>" /><br /> 标题：<INPUT
		TYPE="text" NAME="title" value="<?=$rs[title]?>" /><br /> 内容：
	<TEXTAREA NAME="content" ROWS="8" COLS="30"><?=$rs[content]?></TEXTAREA>
	<br /> <INPUT TYPE="submit" name="submit" value="edit" />
</FORM>
<?php }?> 