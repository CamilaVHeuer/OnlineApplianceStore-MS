package com.camicompany.auth_service.reposiroty;

import com.camicompany.auth_service.model.UserApp;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserAppRepository extends JpaRepository<UserApp, Long> {
    public Optional<UserApp> findByUsername(String username);

    public boolean existsByUsername(@NotBlank(message = "Username is required") String username);
}
