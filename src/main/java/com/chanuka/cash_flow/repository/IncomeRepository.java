package com.chanuka.cash_flow.repository;

import com.chanuka.cash_flow.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

    // SELECT * FROM tbl_incomes WHERE profile_id = ? ORDER BY date DESC
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    // SELECT * FROM tbl_incomes WHERE profile_id = ? ORDER BY date DESC LIMIT 5
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    //
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM IncomeEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalByProfileId(@Param("profileId") Long profileId);

    // SELECT * FROM tbl_incomes WHERE profile_id = ?1 AND date BETWEEN ?2 AND ?3 AND name LIKE %?4%
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String name,
            Sort sort
    );

    // SELECT * FROM tbl_incomes WHERE profile_id ?1 AND date BETWEEN ?2 AND ?3
    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}
