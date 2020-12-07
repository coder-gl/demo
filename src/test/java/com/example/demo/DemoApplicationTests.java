package com.example.demo;

import com.example.demo.dao.userTest.UserInfoMapper;
import com.example.demo.service.LockService;
import org.apache.ibatis.cursor.Cursor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
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

	@Autowired
	UserInfoMapper userInfoMapper;
	@Test
	public void getInfo(){
		System.out.println(userInfoMapper.getUserInfo());
	}

	@Test
	@Transactional(transactionManager="userTestTransactionManager")
	public void getInfoV2(){
		try ( Cursor<Map<String,Object>> cursor = userInfoMapper.getUserInfoV2(  2  ) ) {
			for (Map<String, Object> map : cursor) {
				System.out.println(map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
