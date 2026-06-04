package com.projectrewards.demo.scheduled;

import com.projectrewards.demo.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TransactionExpiryScheduler {

    private final TransactionRepository transactionRepository;
    private final Logger log = LoggerFactory.getLogger(TransactionExpiryScheduler.class);

    public TransactionExpiryScheduler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Run daily at midnight server time
    @Scheduled(cron = "0 0 0 * * *")
    public void expireOldTransactions() {
        Instant cutoff = Instant.now().minus(366, ChronoUnit.DAYS);
        int updated = transactionRepository.markExpiredBefore(cutoff);
        log.info("Expired {} transactions older than {}", updated, cutoff);
    }
}
