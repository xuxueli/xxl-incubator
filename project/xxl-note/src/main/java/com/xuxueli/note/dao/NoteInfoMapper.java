package com.xuxueli.note.dao;

import com.xuxueli.note.model.NoteInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author xuxueli 2017-08-02 22:34:53
 */
@Mapper
public interface NoteInfoMapper {

	/*@Insert("insert into`xxl_note_info ( `group_id`, `title`, `content`) " +
			"values ( #{noteInfo.groupId}, #{noteInfo.title}, #{noteInfo.content});")*/
	int insert(@Param("noteInfo") NoteInfo noteInfo);

	int delete(@Param("id") int id);

	int update(@Param("noteInfo") NoteInfo noteInfo);

	List<NoteInfo> findByGroupId(@Param("groupId") int groupId);

}
