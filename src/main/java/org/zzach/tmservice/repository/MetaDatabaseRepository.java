package org.zzach.tmservice.repository;

import org.zzach.tmservice.entity.MetaDatabase;
import org.zzach.tmservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaDatabaseRepository extends JpaRepository<MetaDatabase, Long> {

    public List<MetaDatabase> findByOwner(User owner);
}
