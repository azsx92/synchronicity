package com.example.stock.facade;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RedissonLockStockFacadeTest {
    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L,100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;

        // 스레드 요청서비스를 가능하게 해주는 API
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 스레드가 끝날 때까지 대기해주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++){
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decrease(1L,1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        // 100 - (1 * 100) = 0
        assertEquals(0, stock.getQuantity());

        // 레이스 컨디션 정리하기
    }
}