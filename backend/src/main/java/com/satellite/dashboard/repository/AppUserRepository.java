
package com.satellite.dashboard.repository;

import com.satellite.dashboard.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppUserRepository
        extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
