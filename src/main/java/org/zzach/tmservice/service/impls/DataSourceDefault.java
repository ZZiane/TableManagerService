package org.zzach.tmservice.service.impls;

import org.zzach.tmservice.entity.DataSource;
import org.zzach.tmservice.repository.DataSourceRepository;
import org.zzach.tmservice.service.DataSourceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataSourceDefault implements DataSourceService {

    private DataSourceRepository dataSourceRepository;


    public DataSourceDefault(DataSourceRepository dataSourceRepository){
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public Optional<List<DataSource>> getDataSources() {
        return Optional.of(dataSourceRepository.findAll());
    }



    @Override
    public Optional<DataSource> getDataSource(int id) {
        return dataSourceRepository.findById(id);
    }

    @Override
    public Optional<DataSource> addDataSource(DataSource datasource) {
        return Optional.of(
                dataSourceRepository
                        .save(datasource));
    }




}
