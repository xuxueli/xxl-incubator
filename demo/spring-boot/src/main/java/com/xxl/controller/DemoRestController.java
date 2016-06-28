package com.xxl.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class DemoRestController {

	@RequestMapping("/rest/{name}")
	public String hello(@PathVariable("name") String name) {
		return "Hi:" + name;
	}
}
