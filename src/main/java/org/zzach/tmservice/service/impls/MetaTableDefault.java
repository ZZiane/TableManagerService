package org.zzach.tmservice.service.impls;

import org.zzach.tmservice.entity.GenericTable;
import org.zzach.tmservice.entity.MetaColumn;
import org.zzach.tmservice.entity.User;
import org.zzach.tmservice.repository.GenericTableRepository;
import org.zzach.tmservice.repository.MetaColumnRepository;
import org.zzach.tmservice.repository.UserRepository;
import org.zzach.tmservice.service.MetaTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MetaTableDefault implements MetaTableService {

    private GenericTableRepository genericTableRepository;
    private MetaColumnRepository metaColumnRepository;
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(MetaTableDefault.class);

    public MetaTableDefault(GenericTableRepository genericTableRepository, MetaColumnRepository metaColumnRepository,UserRepository userRepository){
        this.genericTableRepository = genericTableRepository;
        this.metaColumnRepository = metaColumnRepository;
        this.userRepository = userRepository;
    }


    public Optional<GenericTable> addGenericTable(GenericTable genericTable) {
        if(addMetaColumn(genericTable.getMetaColumns()).isPresent())
            return Optional.of(
                    genericTableRepository
                        .save(genericTable));
        return Optional.empty();
    }

    public Optional<List<GenericTable>> getTablesAssigned(User user) {
        return Optional.of(genericTableRepository.findByAssignedUsers(user));
    }

    public boolean removeTable(User owner, long id) {
        try{
            if(genericTableRepository.findById(id).get().getDatabase().getOwner().getEmail().equalsIgnoreCase(owner.getEmail())){
                genericTableRepository.deleteById(id);
                return true;
            }
        }
        catch(Exception e){
            logger.error("You can't drop table");
        }
        return false;
    }

    private Optional<List<MetaColumn>> addMetaColumn(List<MetaColumn> mcs){
        return Optional.of(metaColumnRepository.saveAll(mcs));
    }


    public Optional<GenericTable> getGenericById(Long id) {
        return genericTableRepository.findById(id);
    }

    public Optional<List<GenericTable>> getTablesByOwner(User user) {
        return Optional.of(genericTableRepository.findByDatabase_Owner(user));
    }

}
