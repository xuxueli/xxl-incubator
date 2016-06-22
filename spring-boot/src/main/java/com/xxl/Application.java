package com.xxl;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.xxl.controller.DemoRestController;

/**
 * @RestController				相当于	@Controller + @ResponseBody
 * @EnableAutoConfiguration		开启自动配置
 * 
 * @Configuration				注解bean
 * @ComponentScan("com.xxl")	扫描注册相应的bean
 *	
 */

@Configuration
@ComponentScan("com.xxl")
public class Application {

	public static void main(String[] args) {

		//《第一种方式》 ：@RestController + @EnableAutoConfiguration注解Controller;
		//然后，SpringApplication.run(DemoRestController.class);运行单个Controller;这种方式只运行一个控制器比较方便;
		SpringApplication.run(DemoRestController.class, args);

		//《第二种方式》：@RestController + @EnableAutoConfiguration注解Controller;
		//然后，@Configuration + @ComponentScan;开启注解扫描, 自动注册相应的注解Bean;
		SpringApplication.run(Application.class, args);
	}
	
}