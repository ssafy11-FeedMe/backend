package com.todoslave.feedme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeedmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedmeApplication.class, args);
		Hello hello = new Hello();
		hello.setData("yoyoyo");
		String tmp = hello.getData();

		System.out.println(tmp);
	}

}
