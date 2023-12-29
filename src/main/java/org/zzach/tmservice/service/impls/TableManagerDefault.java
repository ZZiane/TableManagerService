package org.zzach.tmservice.service.impls;

import org.zzach.tmservice.entity.GenericTable;
import org.zzach.tmservice.models.EditDataRequest;
import org.zzach.tmservice.models.ManageTableRequest;
import org.zzach.tmservice.service.JdbcPoolService;
import org.zzach.tmservice.service.MetaTableService;
import org.zzach.tmservice.service.TableManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.zzach.tmservice.utils.ServiceHelper.*;


@Service
public class TableManagerDefault implements TableManagerService {


    private JdbcPoolService jdbcPoolService;
    private MetaTableService metaTableService;
    private static final Logger logger = LoggerFactory.getLogger(TableManagerDefault.class);

    public TableManagerDefault(JdbcPoolService jdbcPoolService, MetaTableService metaTableService) {
        this.jdbcPoolService = jdbcPoolService;
        this.metaTableService = metaTableService;
    }


    public Optional<GenericTable> prepareAndVerify(ManageTableRequest manageTableRequest) {
        try {
            Optional<GenericTable> optionalGenericTable = metaTableService.getGenericById(manageTableRequest.genericTableId());
            if (optionalGenericTable.isPresent() && checkAuthorityHelper(optionalGenericTable.get(), manageTableRequest.userEmail())) {
                logger.warn("Table founded");
                return optionalGenericTable;
            }
            logger.warn("User %s not able to view this content".formatted(manageTableRequest.userEmail()));
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();

        }
    }

    public boolean editData(ManageTableRequest manageTableRequest, EditDataRequest editDataRequest) {
        Optional<GenericTable> optionalGenericTable  = prepareAndVerify(manageTableRequest);
        if(optionalGenericTable.isPresent()) {
            GenericTable genericTable = optionalGenericTable.get();
            if(canEdit(genericTable,editDataRequest.newData().keySet())){
                String query = columnsFormattingHelperUpdateQuery(editDataRequest.oldData(),
                                                   editDataRequest.newData(),
                                                   genericTable.getTableName());

                logger.warn(query);
                JdbcTemplate jdbcTemplate = jdbcPoolService.getJdbc(genericTable.getDatabase());
                jdbcTemplate.execute(query);
                logger.info("Updating table %s ".formatted(genericTable.getTableName()));
                return true;
            }
            logger.info("You can't edit the column of this table %s ".formatted(genericTable.getTableName()));

        }
        return false;
    }

    private boolean checkAuthorityHelper(GenericTable genericTable, String email){
       return genericTable.getAssignedUsers().stream().filter((auth) -> email.equalsIgnoreCase(auth.getEmail())).count() > 0
                ||
                genericTable.getDatabase().getOwner().getEmail().equalsIgnoreCase(email);
    }

    public boolean editData(ManageTableRequest manageTableRequest, String newValue, String editedColumn, Map<String, String> row) {
        Optional<GenericTable> optionalGenericTable  = prepareAndVerify(manageTableRequest);
        if(optionalGenericTable.isPresent()) {
            GenericTable genericTable = optionalGenericTable.get();
            if (canEdit(genericTable, editedColumn)) {
                JdbcTemplate jdbcTemplate = jdbcPoolService.getJdbc(genericTable.getDatabase());
                logger.info("Updating table %s ".formatted(genericTable.getTableName()));
                return true;
            }
        }
        return false;
    }

    public boolean editData(ManageTableRequest manageTableRequest, String newValue, String editedColumn, String uniqueValue) {
        Optional<GenericTable> optionalGenericTable  = prepareAndVerify(manageTableRequest);
        if(optionalGenericTable.isPresent()) {
            GenericTable genericTable = optionalGenericTable.get();
            if(canEdit(genericTable,editedColumn)){
                return true;
            }
        }
        return false;
    }

    public boolean canEdit(GenericTable genericTable, String columnName) {
        try{
            return genericTable.getMetaColumns()
                    .stream()
                    .filter((p) -> columnName.equalsIgnoreCase(p.getColumnName()))
                    .findFirst()
                    .orElseThrow()
                    .isEditable();
        }
        catch (Exception e){
            logger.error("Error columnName not founded");
            return false;
        }
    }
    public boolean canEdit(GenericTable genericTable, Set<String> columns) {
        for(String columnName : columns){
            if(!canEdit(genericTable, columnName)){
                return false;
            }
        }
        return true;
    }

    public List<Map<String, Object>> getData(ManageTableRequest manageTableRequest) {
        Optional<GenericTable> optionalGenericTable  = prepareAndVerify(manageTableRequest);
        if(optionalGenericTable.isPresent()){
            GenericTable genericTable = optionalGenericTable.get();
            JdbcTemplate jdbcTemplate = jdbcPoolService.getJdbc(genericTable.getDatabase());
            return  jdbcTemplate.queryForList("SELECT * FROM %s".formatted(genericTable.getTableName()));
        }
        return List.of();
    }



}
