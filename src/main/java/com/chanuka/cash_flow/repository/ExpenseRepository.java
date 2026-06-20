package com.chanuka.cash_flow.repository;

import com.chanuka.cash_flow.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // SELECT * FROM tbl_expenses WHERE profile_id = ? ORDER BY date DESC
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    // SELECT * FROM tbl_expenses WHERE profile_id = ? ORDER BY date DESC LIMIT 5
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    //
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalByProfileId(@Param("profileId") Long profileId);

    // SELECT * FROM tbl_expenses WHERE profile_id = ?1 AND date BETWEEN ?2 AND ?3 AND name LIKE %?4%
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String name,
            Sort sort
    );

    // SELECT * FROM tbl_expenses WHERE profile_id ?1 AND date BETWEEN ?2 AND ?3
    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    // SELECT * FROM tbl_expenses WHERE profile_id = ?1 AND date = ?2
    List<ExpenseEntity> findByProfileIdAndDate(Long profileID, LocalDate date);
}
