package org.zzach.tmservice.repository;


import org.zzach.tmservice.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSourceRepository extends JpaRepository<DataSource,Integer> {
}
