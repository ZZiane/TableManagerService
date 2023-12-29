package org.zzach.tmservice.service;

import org.zzach.tmservice.entity.GenericTable;
import org.zzach.tmservice.models.EditDataRequest;
import org.zzach.tmservice.models.ManageTableRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TableManagerService {


    public Optional<GenericTable> prepareAndVerify(ManageTableRequest manageTableRequest);
    public boolean editData(ManageTableRequest manageTableRequest, EditDataRequest editDataRequest);
    public boolean editData(ManageTableRequest manageTableRequest, String newValue, String editedColumn, String uniqueValue);

    public boolean canEdit(GenericTable genericTable, String columnName);

    public List<Map<String, Object>> getData(ManageTableRequest manageTableRequest);

}
