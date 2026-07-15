package com.nestly.auth.repository;

import com.nestly.auth.entity.OtpCode;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

    Optional<OtpCode> findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);
}
