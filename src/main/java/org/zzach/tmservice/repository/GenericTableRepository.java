package org.zzach.tmservice.repository;

import org.zzach.tmservice.entity.GenericTable;
import org.zzach.tmservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenericTableRepository extends JpaRepository<GenericTable, Long> {

    public List<GenericTable> findByDatabase_Owner(User user);
    public List<GenericTable> findByAssignedUsers(User user);

}
