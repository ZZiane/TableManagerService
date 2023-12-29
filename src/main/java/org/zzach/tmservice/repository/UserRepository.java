package org.zzach.tmservice.repository;

import org.zzach.tmservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}