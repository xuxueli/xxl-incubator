package com.xxl.service.impl;


import org.springframework.stereotype.Service;

import com.xxl.service.IDpsfService;

@Service("dpsfService")
public class DpsfServiceImpl implements IDpsfService {

	@Override
	public String sayHi(String name) {
		int asd = 10/0;
		return "hi, i am pegin. to " + name;
	}
	
}
