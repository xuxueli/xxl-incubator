package com.xuxueli.note.controller;

import com.xuxueli.note.model.NoteGroup;
import com.xuxueli.note.model.NoteInfo;
import com.xuxueli.note.model.ReturnT;
import com.xuxueli.note.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuxueli 2017-08-01 22:42:48
 */
@Controller
@RequestMapping("/note")
public class NoteController {

    @Resource
    private NoteService nodeService;

    @RequestMapping("")
    public String index(Model model) {
        List<NoteGroup> noteGroupList = nodeService.findAllNoteGroup();
        model.addAttribute("noteGroupList", noteGroupList);
        return "note/note.list";
    }

    // ---------------------- note group ----------------------

    @RequestMapping("/addGroup")
    @ResponseBody
    public ReturnT<Integer> addGroup(NoteGroup noteGroup) {
        return nodeService.addGroup(noteGroup);
    }

    @RequestMapping("/updateGroup")
    @ResponseBody
    public ReturnT<String> updateGroup(NoteGroup noteGroup) {
        return nodeService.updateGroup(noteGroup);
    }

    @RequestMapping("/deleteGroup")
    @ResponseBody
    public ReturnT<String> deleteGroup(int id) {
        return nodeService.deleteGroup(id);
    }

    // ---------------------- note info ----------------------

    @RequestMapping("/addNote")
    @ResponseBody
    public ReturnT<Integer> addNote(NoteInfo noteInfo) {
        return nodeService.addNote(noteInfo);
    }

    @RequestMapping("/updateNote")
    @ResponseBody
    public ReturnT<String> updateNote(NoteInfo noteInfo) {
        return nodeService.updateNote(noteInfo);
    }

    @RequestMapping("/deleteNote")
    @ResponseBody
    public ReturnT<String> deleteNote(int id) {
        return nodeService.deleteNote(id);
    }

    // ---------------------- show ----------------------

    @RequestMapping("/detail")
    public String detail(int id) {
        return "note/note.detail";
    }

}
