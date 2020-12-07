package com.example.demo;

import com.example.demo.service.LockService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DemoApplicationTests {




	@Autowired
	private LockService lockService;

	@Test
	public void lockResourceTest() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		CountDownLatch countDownLatch = new CountDownLatch(100);

		CountDownLatch countDownLatch2 = new CountDownLatch(100);
		for (int i = 0 ; i< 100 ; i++){
			Callable callable = new Callable() {
				@Override
				public Object call() throws Exception {
					countDownLatch.await();
					lockService.lockResource();
					countDownLatch2.countDown();
					return null;
				}
			};
			executorService.submit(callable);
			countDownLatch.countDown();
		}
		countDownLatch2.await();

	}

	@Test
	public  void TestMain(){
		Set<Long> set = new HashSet<>(3);
		System.out.println(set.size());
	}

}
