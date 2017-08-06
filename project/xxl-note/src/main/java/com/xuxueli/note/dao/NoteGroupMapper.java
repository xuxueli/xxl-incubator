package com.xuxueli.note.dao;

import com.xuxueli.note.model.NoteGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxueli 2017-08-02 22:34:53
 */
@Mapper
public interface NoteGroupMapper {

	int insert(@Param("noteGroup") NoteGroup noteGroup);

	int delete(@Param("id") int id);

	int update(@Param("noteGroup") NoteGroup noteGroup);

	List<NoteGroup> findAll();

    NoteGroup loadById(int id);

}
