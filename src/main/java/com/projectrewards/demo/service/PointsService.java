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
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setPointsDelta(points);
        tx.setPurchaseId(purchaseId);
        tx.setType(TransactionType.EARNING);
        tx.setCreatedAt(Instant.now());
        transactionRepository.save(tx);
        return transactionRepository.sumPointsByUserId(userId);
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
        tx.setPointsDelta(-points);
        tx.setType(TransactionType.REWARD_REDEEM);
        tx.setCreatedAt(Instant.now());
        transactionRepository.save(tx);
        return transactionRepository.sumPointsByUserId(userId);
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
