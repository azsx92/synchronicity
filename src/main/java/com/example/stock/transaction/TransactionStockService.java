package com.example.stock.transaction;

import com.example.stock.service.StockService;

public class TransactionStockService {

    private StockService stockService;

    public TransactionStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        startTransaction();

        stockService.decrease(id, quantity);
        // 종료 시점에서 문제가 발생하게 되는데 종료 시점에 다른 스레드가 decrease를 접속하면서 끝나지 않는 문제가 발생 할 수가 있다.
        endTransaction();
    }

    private void startTransaction() {
        System.out.println("Transaction Start");
    }

    private void endTransaction() {
        System.out.println("Commit");
    }
}
