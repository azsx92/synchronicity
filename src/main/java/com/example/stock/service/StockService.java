package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
   // @Transactional
    // synchronized 단일 서버 안에서는 레이스 컨디션 문제가 발생하지 않지만
    // 최근 에는 서버가 두개 이상 인 경우가 대부분 이므로 레이스 컨디션 문제가 발생 하기가 쉽다.
    // 그래서 다음에는 mysql 레이스 컨디션을 방지하는 방법을 알아 보도록 하자.
    public synchronized void decrease(Long id, Long quantity) {
        // Stock 조회
        // 재고를 감소한 뒤
        // 갱신된 값을 저장하도록 하겠습니다.

      Stock stock = stockRepository.findById(id).orElseThrow();
      stock.decrease(quantity);

      stockRepository.saveAndFlush(stock);
    }
}
