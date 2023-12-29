package org.zzach.tmservice.utils;

import org.zzach.tmservice.entity.GenericTable;
import org.zzach.tmservice.entity.MetaColumn;
import org.zzach.tmservice.entity.MetaDatabase;
import org.zzach.tmservice.enums.TypeEntity;
import org.zzach.tmservice.repository.DataSourceRepository;
import org.zzach.tmservice.repository.GenericTableRepository;
import org.zzach.tmservice.repository.MetaDatabaseRepository;
import org.zzach.tmservice.service.MetaTableService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder {

    @Bean
    public CommandLineRunner commandLineRunner(DataSourceRepository dataSourceRepository,
                                               MetaDatabaseRepository metaDatabaseRepository,
                                               GenericTableRepository genericTableRepository,
                                               MetaTableService metaTableService
    ){
        return args -> {


            metaTableService.addGenericTable(
                    GenericTable.builder()
                            .database(
                                    MetaDatabase.builder().id(876L).build()
                            )
                            .tableName("agences")
                            .metaColumns(List.of(MetaColumn.builder().columnName("CH_COD_AGEC").editable(true).visible(true).build()))
                            .visibleBy(TypeEntity.ADMINISTRATION)
                            .entityColumnId(null)
                            .assignedUsers(List.of())
                            .modifiableBy(TypeEntity.ADMINISTRATION)
                            .build()
            );

        };
    }
}