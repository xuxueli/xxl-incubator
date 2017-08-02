package com.xuxueli.note.controller;

import com.xuxueli.note.model.NoteGroup;
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

    @RequestMapping("/addpage")
    public String addpage() {
        return "note/note.add";
    }

    @RequestMapping("/add")
    @ResponseBody
    public ReturnT<String> add() {
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/updatepage")
    public String updatepage() {
        return "note/note.update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update() {
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/detailpage")
    public String detailpage() {
        return "note/note.detail";
    }

}
