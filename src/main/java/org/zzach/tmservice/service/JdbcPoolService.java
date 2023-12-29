package org.zzach.tmservice.service;

import org.zzach.tmservice.entity.MetaDatabase;
import org.springframework.jdbc.core.JdbcTemplate;

public interface JdbcPoolService {

    public boolean jdbcStart(MetaDatabase metaDatabase);
    public JdbcTemplate getJdbc(MetaDatabase metaDatabase);
}
