package com.projectrewards.demo.service;

import com.projectrewards.demo.exception.InsufficientPointsException;
import com.projectrewards.demo.model.Transaction;
import com.projectrewards.demo.model.TransactionType;
import com.projectrewards.demo.model.User;
import com.projectrewards.demo.repository.TransactionRepository;
import com.projectrewards.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class PointsService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public PointsService(UserRepository userRepository,
                         TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public long earn(Long userId, int points, Long purchaseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        if (purchaseId != null && transactionRepository.findByPurchaseId(purchaseId).isPresent()) {
            throw new IllegalArgumentException("purchaseId already used");
        }
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setTxPoints(points);
        tx.setAvailablePoints(points);
        tx.setPurchaseId(purchaseId);
        tx.setType(TransactionType.EARNING);
        tx.setCreatedAt(Instant.now());
        transactionRepository.save(tx);
        return transactionRepository.sumAvailablePointsByUserId(userId);
    }

    @Transactional
    public long redeem(Long userId, int points) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));       
        long balance = transactionRepository.sumPointsByUserId(userId);
        if (balance < points) {
            throw new InsufficientPointsException("insufficient points");
        }
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setTxPoints(-points);
        tx.setAvailablePoints(-points);
        tx.setType(TransactionType.REWARD_REDEEM);
        tx.setCreatedAt(Instant.now());
        transactionRepository.save(tx);
        return transactionRepository.sumAvailablePointsByUserId(userId);
    }

    @Transactional
    public long refund(Long userId, Long purchaseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        Transaction orig = transactionRepository.findByUser_IdAndPurchaseId(userId, purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("transaction not found for user/purchase"));

        Integer origAvail = orig.getAvailablePoints();
        if (origAvail == null || origAvail <= 0) {
            throw new IllegalArgumentException("original transaction has no available points to refund");
        }

        Transaction rev = new Transaction();
        rev.setUser(user);
        rev.setTxPoints(orig.getTxPoints() == null ? 0 : -orig.getTxPoints());
        rev.setAvailablePoints(0);
        rev.setPurchaseId(null);
        rev.setType(TransactionType.REFUND);
        rev.setCreatedAt(Instant.now());
        transactionRepository.save(rev);

        orig.setAvailablePoints(0);
        transactionRepository.save(orig);

        Long avail = transactionRepository.sumAvailablePointsByUserId(userId);
        return avail == null ? 0L : avail.longValue();
    }

    @Transactional(readOnly = true)
    public long getBalance(Long userId) {
        // verifies user exists
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        // Use availablePoints sum for balance
        Long avail = transactionRepository.sumAvailablePointsByUserId(userId);
        return avail == null ? 0L : avail.longValue();
    }
}
