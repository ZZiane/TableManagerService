package org.zzach.tmservice.service.impls;

import org.zzach.tmservice.entity.MetaDatabase;
import org.zzach.tmservice.service.JdbcPoolService;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.zzach.tmservice.utils.ServiceHelper.buildDataSourceFromMetaDatabaseHelper;


@Service
public class JdbcPoolDefault implements JdbcPoolService {
    private Map<Long, JdbcTemplate> dataSourceMap;
    private static final Logger logger = LoggerFactory.getLogger(JdbcPoolDefault.class);
    private static final Long TIME_OUT_DATASOURCE = 500000L;

    public JdbcPoolDefault(){
        this.dataSourceMap = new HashMap<>();
    }

    public boolean jdbcStart(MetaDatabase metaDatabase){
        if(dataSourceMap.containsKey(metaDatabase.getId())){
            logger.warn("JDBC Pool already contains a datasource with id : %d".formatted(metaDatabase.getId()));
            return false;
        }
        HikariDataSource dataSource = (HikariDataSource)buildDataSourceFromMetaDatabaseHelper(metaDatabase);
        dataSource.setIdleTimeout(TIME_OUT_DATASOURCE);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dataSourceMap.put(metaDatabase.getId(),jdbcTemplate);
        logger.info("Datasource with id {%d} was created in JDBC Pool".formatted(metaDatabase.getId()));
        return true;
    }

    public JdbcTemplate getJdbc(MetaDatabase metaDatabase){
        if(dataSourceMap.containsKey(metaDatabase.getId())){
            logger.info("Datasource with id : %d founded".formatted(metaDatabase.getId()));
            return dataSourceMap.get(metaDatabase.getId());
        }
        logger.warn("Datasource not founded id : %d".formatted(metaDatabase.getId()));
        jdbcStart(metaDatabase);
        return dataSourceMap.get(metaDatabase.getId());
    }



}
