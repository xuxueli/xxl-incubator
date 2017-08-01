package com.xuxueli.note.controller;

import com.xuxueli.note.model.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xuxueli 2017-08-01 22:42:48
 */
@Controller("/note")
public class NoteController {

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
