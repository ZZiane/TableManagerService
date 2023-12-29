package org.zzach.tmservice.service.impls;

import org.zzach.tmservice.entity.MetaDatabase;
import org.zzach.tmservice.entity.User;
import org.zzach.tmservice.repository.DataSourceRepository;
import org.zzach.tmservice.repository.MetaDatabaseRepository;
import org.zzach.tmservice.service.MetaDatabaseService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.zzach.tmservice.utils.ServiceHelper.buildDataSourceFromMetaDatabaseHelper;

@Service
public class MetaDatabaseDefault implements MetaDatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(MetaDatabaseDefault.class);

    private MetaDatabaseRepository metaDatabaseRepository;
    private DataSourceRepository dataSourceRepository;

    public MetaDatabaseDefault(MetaDatabaseRepository metaDatabaseRepository,
                               DataSourceRepository dataSourceRepository){
        this.metaDatabaseRepository = metaDatabaseRepository;
        this.dataSourceRepository = dataSourceRepository;
    }

    public Optional<List<MetaDatabase>> getMetaDatabases(User owner) {
        return Optional.of(metaDatabaseRepository.findByOwner(owner));
    }

    public Optional<MetaDatabase> getMetaDatabaseById(User owner, long id) {
        return Optional.of(metaDatabaseRepository.findById(id).get());
    }

    public Optional<MetaDatabase> addMetaDatabase(MetaDatabase metaDatabase) {
        return Optional.of(metaDatabaseRepository.save(metaDatabase));
    }

    public Optional<List<String>> getTablesNameByDatabaseId(User owner, long databaseId) {
        MetaDatabase metaDatabase = getMetaDatabase(owner,databaseId);
        DataSource dataSource = buildDataSourceFromMetaDatabaseHelper(metaDatabase);
        List<String> tableNames = new Vector<>();
        getTableNamesAndAddToArrayHelper(dataSource, metaDatabase.getSource(), tableNames );
        return Optional.of(tableNames);
    }

    public Optional<List<String>> getColumnsNameOfTable(User owner, long databaseId, String tableName) {
        MetaDatabase metaDatabase = getMetaDatabase(owner,databaseId);
        DataSource dataSource = buildDataSourceFromMetaDatabaseHelper(metaDatabase);
        List<String> columnNames = new Vector<>();
        getColumnsNameOfTable(dataSource, metaDatabase.getSource(), tableName, columnNames);
        return Optional.of(columnNames);
    }

    public Boolean checkConnection(MetaDatabase metaDatabase) {
        HikariDataSource dataSource = (HikariDataSource)buildDataSourceFromMetaDatabaseHelper(metaDatabase);
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connection successful!");
            return true;
        } catch (SQLException e) {
            logger.error("Connection failed: " + e.getMessage());
            return false;
        } finally {
            dataSource.close();
        }
    }

    public Boolean removeDatabaseById(User owner, Long id) {
        try{
            Optional<MetaDatabase> optionalMetaDatabase = metaDatabaseRepository.findById(id);
            if(optionalMetaDatabase.isPresent()){
                logger.info("Metadabase founded");
                MetaDatabase metaDatabase = optionalMetaDatabase.get();
               if(metaDatabase.getOwner().getEmail().equalsIgnoreCase(owner.getEmail())){
                   metaDatabaseRepository.deleteById(id);
                    return true;
               }
                logger.warn("Your are not the owner, you can't drop it");
            }
        }
        catch (Exception e){
            logger.error("Error can't delete the metadatabase");
        }
        return false;


    }


    private MetaDatabase getMetaDatabase(User owner, long databaseId){
        Optional<MetaDatabase> optionalMetaDatabase =getMetaDatabaseById(owner, databaseId);
        if(!optionalMetaDatabase.isPresent()){
            String errorMsg = "No Database found with this databaseId (may be a connectivity problem)";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        logger.info("Database found : %s".formatted(databaseId));
        return optionalMetaDatabase.get();
    }

    private void getTableNamesAndAddToArrayHelper(DataSource dataSource, String databaseName, List<String> tableNames){
        getTableNamesAndAddToArrayHelper(dataSource, databaseName, tableNames, null);
    }
    private void getTableNamesAndAddToArrayHelper(DataSource dataSource, String databaseName, List<String> tableNames, String schema){
        try{
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet tables = metaData.getTables(databaseName, schema, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        }
        catch (Exception e){
            String errorMsg = "We can't get a database with this informations in this database server : %s".formatted(databaseName);
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        finally {
            ((HikariDataSource) dataSource).close();
        }
    }

    private void getColumnsNameOfTable(DataSource dataSource, String databaseName, String tableName, List<String> columnNames){
        try{
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet columns = metaData.getColumns(databaseName, null, tableName, "%");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                columnNames.add(columnName);
            }
        }
        catch (Exception e){
            String errorMsg = "Error while we extract informations table from this database (may be '%s' does'n exist)".formatted(tableName);
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        finally {
            ((HikariDataSource) dataSource).close();
        }
    }
}
