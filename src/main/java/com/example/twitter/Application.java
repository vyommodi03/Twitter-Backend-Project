package com.example.twitter;

import com.example.twitter.core.Consumer1;
import com.example.twitter.core.Consumer2;
import com.example.twitter.core.Producer;
import com.example.twitter.core.database.ESUtils;
import com.example.twitter.core.database.RedisUtils;
import com.example.twitter.core.utils.InterestingTweetChecker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		CountDownLatch latch = new CountDownLatch(3);
		Producer producer = new Producer(latch);
		Consumer1 consumer1 = new Consumer1(latch);
		Consumer2 consumer2 = new Consumer2(latch);

		Thread producerThread = new Thread(producer,"Producer");
		Thread consumer1Thread = new Thread(consumer1,"Consumer-1");
		Thread consumer2Thread = new Thread(consumer2,"Consumer-2");

		producerThread.start();
		consumer1Thread.start();
		consumer2Thread.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			producer.close();
			consumer2.close();
			consumer1.close();
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InterestingTweetChecker.closeMongodb();
			RedisUtils.closeJedisPool();
			ESUtils.close();
			System.out.println("-----------------------------------Application closed----------------------------------------");
		}));

		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Application got interrupted");
		}

		System.out.println("application is closing");
	}
}
