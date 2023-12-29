package org.zzach.tmservice.service;

import org.zzach.tmservice.entity.GenericTable;
import org.zzach.tmservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface MetaTableService {

    public Optional<GenericTable> getGenericById(Long id);
    public Optional<List<GenericTable>> getTablesByOwner(User user);
    public Optional<GenericTable> addGenericTable(GenericTable genericTable);
    public Optional<List<GenericTable>> getTablesAssigned(User user);
    public boolean removeTable(User owner, long id);
}
