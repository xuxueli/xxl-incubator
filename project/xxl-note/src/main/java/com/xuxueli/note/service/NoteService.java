package com.xuxueli.note.service;

import com.xuxueli.note.dao.NoteGroupMapper;
import com.xuxueli.note.dao.NoteInfoMapper;
import com.xuxueli.note.model.NoteGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
	
	@Autowired
	private NoteGroupMapper noteGroupMapper;
	@Autowired
	private NoteInfoMapper noteInfoMapper;

	public List<NoteGroup> findAllNoteGroup() {
		List<NoteGroup> noteGroupList = noteGroupMapper.findAll();
		return noteGroupList;
	}
}
