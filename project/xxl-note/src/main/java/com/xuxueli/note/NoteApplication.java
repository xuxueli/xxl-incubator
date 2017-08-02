package com.xuxueli.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxueli 2017-08-01 21:39:47
 */
@EnableAutoConfiguration
@SpringBootApplication
public class NoteApplication {

	public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
	}

}