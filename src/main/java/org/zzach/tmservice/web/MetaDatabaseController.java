package org.zzach.tmservice.web;

import org.zzach.tmservice.entity.MetaDatabase;
import org.zzach.tmservice.entity.User;
import org.zzach.tmservice.service.MetaDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.zzach.tmservice.utils.ServiceHelper.authenticationUserBuilder;

@RestController
@RequestMapping("databases")
public class MetaDatabaseController {

    private MetaDatabaseService metaDatabaseService;

    public MetaDatabaseController(MetaDatabaseService metaDatabaseService){
        this.metaDatabaseService = metaDatabaseService;
    }

    @PostMapping("/test")
    public Boolean testConnection(@RequestBody MetaDatabase metaDatabase){
        return metaDatabaseService.checkConnection(metaDatabase);
    }

    @GetMapping
    public ResponseEntity<List<MetaDatabase>> showDatabases(Authentication authentication){
        User owner = authenticationUserBuilder(authentication);
        return ResponseEntity.of(metaDatabaseService.getMetaDatabases(owner));
    }

    @PostMapping
    public ResponseEntity<MetaDatabase> linkDatabase(Authentication authentication, @RequestBody MetaDatabase metaDatabase){
        User owner = authenticationUserBuilder(authentication);
        metaDatabase.setOwner(owner);
        return ResponseEntity.of(metaDatabaseService.addMetaDatabase(metaDatabase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaDatabase> showDatabaseInformation(Authentication authentication, @PathVariable long id){
        User owner = authenticationUserBuilder(authentication);
        return ResponseEntity.of(metaDatabaseService.getMetaDatabaseById(owner,id));
    }

    @DeleteMapping("/{id}")
    public Boolean dropDatabase(Authentication authentication, @PathVariable long id){
        User owner = authenticationUserBuilder(authentication);
        return metaDatabaseService.removeDatabaseById(owner,id);
    }

    @GetMapping("/{id}/tables")
    public ResponseEntity<List<String>> showTablesofDatabase(Authentication authentication, @PathVariable long id){
        User owner = authenticationUserBuilder(authentication);
        return ResponseEntity.of(metaDatabaseService.getTablesNameByDatabaseId(owner,id));
    }
    @GetMapping("/{id}/tables/{tableName}")
    public ResponseEntity<List<String>> showColumnsOfTable(Authentication authentication, @PathVariable long id, @PathVariable String tableName){
        User owner = authenticationUserBuilder(authentication);
        return ResponseEntity.of(metaDatabaseService.getColumnsNameOfTable(owner,id, tableName));
    }


    //dev methodes
    @PostMapping("/{email}")
    public ResponseEntity<MetaDatabase> linkDatabase(@PathVariable String email, @RequestBody MetaDatabase metaDatabase){
        metaDatabase.setOwner(User.builder().email(email).build());
        return ResponseEntity.of(metaDatabaseService.addMetaDatabase(metaDatabase));
    }

    @DeleteMapping ("/{email}/{id}")
    public Boolean dropDatabase(@PathVariable String email, @PathVariable long id){
        User owner = User.builder().email(email).build();
        return metaDatabaseService.removeDatabaseById(owner,id);
    }
}
