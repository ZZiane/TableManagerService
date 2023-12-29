package org.zzach.tmservice.web;

import org.zzach.tmservice.entity.GenericTable;
import org.zzach.tmservice.entity.User;
import org.zzach.tmservice.service.MetaTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.zzach.tmservice.utils.ServiceHelper.authenticationUserBuilder;

@RestController
@RequestMapping("tables")
public class MetaTableController {

    private MetaTableService metaTableService;
    public MetaTableController(MetaTableService metaTableService){
        this.metaTableService = metaTableService;
    }

    @GetMapping
    public ResponseEntity<List<GenericTable>> showTables(Authentication authentication) {
        User owner = authenticationUserBuilder(authentication);
        return ResponseEntity.of(metaTableService.getTablesByOwner(owner));
    }
    @GetMapping("/assigned")
    public ResponseEntity<List<GenericTable>> showAssignedTables(Authentication authentication) {
        User user = authenticationUserBuilder(authentication);
        return ResponseEntity.of(metaTableService.getTablesAssigned(user));
    }
    @PostMapping
    public ResponseEntity<GenericTable> linkTable(Authentication authentication, @RequestBody GenericTable genericTable){
        User owner = authenticationUserBuilder(authentication);
        genericTable.getDatabase().setOwner(owner);
        return ResponseEntity.of(metaTableService.addGenericTable(genericTable));
    }

    @DeleteMapping("/{id}")
    public boolean dropTable(Authentication authentication, @PathVariable long id){
        User owner = authenticationUserBuilder(authentication);
        return metaTableService.removeTable(owner, id);
    }

    //dev methodes
    @GetMapping("/{email}")
    public ResponseEntity<List<GenericTable>> showTables(@PathVariable String email) {
        User owner = User.builder().email(email).build();
        return ResponseEntity.of(metaTableService.getTablesByOwner(owner));
    }
    @PostMapping("/{email}")
    public ResponseEntity<GenericTable> linkTable(@PathVariable String email, @RequestBody GenericTable genericTable){
        genericTable.getDatabase().setOwner(User.builder().email(email).build());
        return ResponseEntity.of(metaTableService.addGenericTable(genericTable));
    }
    @GetMapping("/assigned/{email}")
    public ResponseEntity<List<GenericTable>> showAssignedTables(@PathVariable String email) {
        User user = User.builder().email(email).build();
        return ResponseEntity.of(metaTableService.getTablesAssigned(user));
    }

    @DeleteMapping("/{email}/{id}")
    public boolean dropTable(String email, @PathVariable long id){
        User owner = User.builder().email(email).build();
        return metaTableService.removeTable(owner, id);
    }


}
