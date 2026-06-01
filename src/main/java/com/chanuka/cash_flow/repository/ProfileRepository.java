package com.chanuka.cash_flow.repository;

import com.chanuka.cash_flow.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    // SELECT * FROM tbl_profiles WHERE email = ?;
    Optional<ProfileEntity> findByEmail(String email);
}
