package com.nestly.auth.config;

import com.nestly.auth.entity.User;
import com.nestly.auth.repository.UserRepository;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedAdminRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final String seedAdminEmail;

    public SeedAdminRunner(UserRepository userRepository,
                           @Value("${nestly.seed-admin-email}") String seedAdminEmail) {
        this.userRepository = userRepository;
        this.seedAdminEmail = seedAdminEmail;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(seedAdminEmail).isEmpty()) {
            userRepository.save(User.builder()
                    .email(seedAdminEmail)
                    .role("ADMIN")
                    .displayName("Nestly Admin")
                    .createdAt(Instant.now())
                    .build());
            System.out.println("[auth-service] seeded admin account: " + seedAdminEmail);
        }
    }
}
