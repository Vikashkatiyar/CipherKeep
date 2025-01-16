package com.encription.repository;

import com.encription.model.SecretVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretVersionRepository extends JpaRepository<SecretVersion, Long> {

}
