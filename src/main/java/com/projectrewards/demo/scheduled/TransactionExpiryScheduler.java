package com.projectrewards.demo.scheduled;

import com.projectrewards.demo.model.Transaction;
import com.projectrewards.demo.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TransactionExpiryScheduler {

    private static final int BATCH_SIZE = 100;

    private final TransactionRepository transactionRepository;
    private final Logger log = LoggerFactory.getLogger(TransactionExpiryScheduler.class);

    public TransactionExpiryScheduler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Run daily at midnight server time
    @Scheduled(cron = "0 0 0 * * *")
    public void expireOldTransactions() {
        Instant cutoff = Instant.now().minus(366, ChronoUnit.DAYS);
        int updated = 0;
        Page<Transaction> page;
        int pageNumber = 0;

        do {
            page = transactionRepository.findByExpiredFalseAndCreatedAtBefore(
                    cutoff, PageRequest.of(pageNumber, BATCH_SIZE));
            List<Transaction> batch = page.getContent();
            batch.forEach(t -> t.setExpired(true));
            transactionRepository.saveAll(batch);
            updated += batch.size();
            pageNumber++;
        } while (page.hasNext());

        log.info("Expired {} transactions older than {}", updated, cutoff);
    }
}
