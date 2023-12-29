package org.zzach.tmservice.utils;


import org.zzach.tmservice.entity.MetaColumn;
import org.zzach.tmservice.entity.MetaDatabase;
import org.zzach.tmservice.entity.User;
import org.zzach.tmservice.models.UserKeyCloak;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ServiceHelper {


    public static DataSource buildDataSourceFromMetaDatabaseHelper(MetaDatabase metaDatabase){
        System.out.println(metaDatabase);
        return DataSourceBuilder.create()
                .driverClassName(metaDatabase.getDataSource().getDriver())
                .url(String.format("%s//%s/%s",
                        metaDatabase.getDataSource().getBridge(),
                        metaDatabase.getHost(),
                        metaDatabase.getSource()
                ))
                .username(metaDatabase.getUsername())
                .password(metaDatabase.getPassword())
                .build();    }

    public static User authenticationUserBuilder(Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        UserKeyCloak userKeyCloak = new UserKeyCloak(jwt);
        return User.builder().email(userKeyCloak.getEmail()).build();
    }

    private static String columnsFormattingHelperSelectQuery(List<MetaColumn> columnList, Predicate<MetaColumn> predicate) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (MetaColumn column : columnList) {
            if (predicate.test(column)) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append(column.getColumnName());
                count++;
            }
        }
        return sb.toString();
    }

    private static String columnsFormattingHelperSelectQuery(List<String> columnList) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String column : columnList) {
            if (count > 0) {
                sb.append(", ");
            }
            sb.append(column);
            count++;
        }
        return sb.toString();
    }

    public static String columnsFormattingHelperUpdateQuery(Map<String, String> oldValues, Map<String, String> newValues, String tableName) {
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE %s SET ".formatted(tableName));
        boolean isFirst = true;
        for (String newColumn : newValues.keySet()) {
            if (!isFirst) {
                sb.append(" AND ");
            }
            sb.append(newColumn).append(" = '").append(newValues.get(newColumn)).append("'");
            isFirst = false;
        }
                sb.append(" WHERE ");
        isFirst = true;
        for (String oldColumn : oldValues.keySet()) {
            if (!isFirst) {
                sb.append(" AND ");
            }
            sb.append(oldColumn).append(" = '").append(oldValues.get(oldColumn)).append("'");
            isFirst = false;
        }
        return sb.toString();
    }


    public static String columnsFormattingVisible(List<MetaColumn> columnList) {
        return columnsFormattingHelperSelectQuery(columnList,MetaColumn::isVisible);
    }
    public static String columnsFormattingEditable(List<MetaColumn> columnList) {
        return columnsFormattingHelperSelectQuery(columnList,MetaColumn::isEditable);
    }


}
