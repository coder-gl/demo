package com.example.demo;

import com.example.demo.service.LockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class DemoApplicationTests {

	CountDownLatch countDownLatch = new CountDownLatch(100);

	@Test
	void contextLoads() {
	}


	@Autowired
	private LockService lockService;

	@Test
	public void lockResourceTest(){
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		for (int i = 0 ; i< 100 ; i++){
			lockService.lockResource();
		}

	}

}
