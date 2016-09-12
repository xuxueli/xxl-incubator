package com.xxl.search.example.controller;

import com.xxl.search.client2.util.LuceneUtilAiticleSearch;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/client2")
public class Client2Controller {

	@RequestMapping("/init")
	@ResponseBody
	public String init (){
		try {
			LuceneUtilAiticleSearch.initArticleIndex();
		} catch (Exception e) {
			e.printStackTrace();
			return "E";
		}
		return "S";
	}
	
	@RequestMapping("/search")
	@ResponseBody
	public List<String> search (String content){
		List<String> result;
		try {
			result = LuceneUtilAiticleSearch.search(null, content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
}
