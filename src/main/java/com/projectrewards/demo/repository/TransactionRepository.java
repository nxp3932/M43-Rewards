package com.projectrewards.demo.repository;

import com.projectrewards.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query("SELECT COALESCE(SUM(t.txPoints), 0) FROM Transaction t WHERE t.user.id = :userId AND t.expired = false")
	Long sumPointsByUserId(@Param("userId") Long userId);

	@Query("SELECT COALESCE(SUM(t.availablePoints), 0) FROM Transaction t WHERE t.user.id = :userId AND t.expired = false")
	Long sumAvailablePointsByUserId(@Param("userId") Long userId);

	List<Transaction> findByUser_Id(Long userId);
	
	java.util.Optional<Transaction> findByPurchaseId(Long purchaseId);

	java.util.Optional<Transaction> findByUser_IdAndPurchaseId(Long userId, Long purchaseId);

	@Modifying
	@Transactional
	@Query("UPDATE Transaction t SET t.expired = true WHERE t.expired = false AND t.createdAt < :cutoff")
	int markExpiredBefore(@Param("cutoff") Instant cutoff);
}
