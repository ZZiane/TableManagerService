package org.zzach.tmservice.service;

import org.zzach.tmservice.entity.DataSource;

import java.util.List;
import java.util.Optional;

public interface DataSourceService {

    public Optional<List<DataSource>> getDataSources();

    public Optional<DataSource> getDataSource(int id);

    public Optional<DataSource> addDataSource(DataSource datasource);

}
