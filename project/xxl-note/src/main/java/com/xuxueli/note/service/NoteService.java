package com.xuxueli.note.service;

import com.xuxueli.note.dao.NoteGroupMapper;
import com.xuxueli.note.dao.NoteInfoMapper;
import com.xuxueli.note.model.NoteGroup;
import com.xuxueli.note.model.NoteInfo;
import com.xuxueli.note.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class NoteService {
	
	@Autowired
	private NoteGroupMapper noteGroupMapper;
	@Autowired
	private NoteInfoMapper noteInfoMapper;


	// ---------------------- note group ----------------------

	public List<NoteGroup> findAllNoteGroup() {
		List<NoteGroup> noteGroupList = noteGroupMapper.findAll();
		return noteGroupList;
	}

	public ReturnT<Integer> addGroup(NoteGroup noteGroup){
		// valid
		if (!StringUtils.hasText(noteGroup.getName())) {
			return new ReturnT<Integer>(ReturnT.FAIL_CODE, "分组名称不可为空");
		}

		int ret = noteGroupMapper.insert(noteGroup);
		return ret>0?new ReturnT<Integer>(noteGroup.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	public ReturnT<String> updateGroup(NoteGroup noteGroup){
		// valid
		if (!StringUtils.hasText(noteGroup.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "分组名称不可为空");
		}

		int ret = noteGroupMapper.update(noteGroup);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	public ReturnT<String> deleteGroup(int id){
		// valid
		List<NoteInfo> noteInfoList = noteInfoMapper.findByGroupId(id);
		if (!CollectionUtils.isEmpty(noteInfoList)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "分组使用中，拒绝删除");
		}

		int ret = noteGroupMapper.delete(id);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	// ---------------------- note info ----------------------

	public ReturnT<Integer> addNote(NoteInfo noteInfo){
		// valid
		if (noteInfo.getGroupId() > 0) {
			NoteGroup group = noteGroupMapper.loadById(noteInfo.getGroupId());
			if (group == null) {
				return new ReturnT<Integer>(ReturnT.FAIL_CODE, "文章分组ID非法");
			}
		}
		if (!StringUtils.hasText(noteInfo.getTitle())) {
			return new ReturnT<Integer>(ReturnT.FAIL_CODE, "文章标题不可为空");
		}

		int ret = noteInfoMapper.insert(noteInfo);
		return ret>0?new ReturnT<Integer>(noteInfo.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	public ReturnT<String> updateNote(NoteInfo noteInfo){
		// valid
		if (noteInfo.getGroupId() > 0) {
			NoteGroup group = noteGroupMapper.loadById(noteInfo.getGroupId());
			if (group == null) {
				return new ReturnT<String>(ReturnT.FAIL_CODE, "文章分组ID非法");
			}
		}
		if (!StringUtils.hasText(noteInfo.getTitle())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "文章标题不可为空");
		}

		int ret = noteInfoMapper.update(noteInfo);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	public ReturnT<String> deleteNote(int id){
		int ret = noteInfoMapper.delete(id);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
