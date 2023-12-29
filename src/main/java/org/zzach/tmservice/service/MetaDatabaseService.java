package org.zzach.tmservice.service;



import org.zzach.tmservice.entity.MetaDatabase;
import org.zzach.tmservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface MetaDatabaseService {

    public Optional<List<MetaDatabase>> getMetaDatabases(User owner);

    public Optional<MetaDatabase> getMetaDatabaseById(User owner, long databaseId);
    public Optional<MetaDatabase> addMetaDatabase(MetaDatabase metaDatabase);
    public Optional<List<String>> getTablesNameByDatabaseId(User owner, long databaseId);
    public Optional<List<String>> getColumnsNameOfTable(User owner, long databaseId, String tableName);
    public Boolean checkConnection(MetaDatabase metaDatabase);
    public Boolean removeDatabaseById(User owner, Long id);

}
