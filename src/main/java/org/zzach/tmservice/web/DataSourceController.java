package org.zzach.tmservice.web;

import org.zzach.tmservice.entity.DataSource;
import org.zzach.tmservice.service.DataSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("datasources")
public class DataSourceController {

    private DataSourceService dataSourceService;

    public DataSourceController(DataSourceService dataSourceService){
        this.dataSourceService = dataSourceService;
    }


    @GetMapping("/admin")
    public String getUserEntityType(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return (String) jwt.getClaims().get("entity-type");
    }

    @GetMapping
    public ResponseEntity<List<DataSource>> showDataSources(){
        return ResponseEntity.of(dataSourceService.getDataSources());
    }


    @GetMapping("/{id}")
    public ResponseEntity<DataSource> showDataSource(@PathVariable int id){
        return ResponseEntity.of(dataSourceService.getDataSource(id));
    }

    @PostMapping
    public ResponseEntity<DataSource> linkDataSource(@RequestBody DataSource datasource){
        return ResponseEntity.of(dataSourceService.addDataSource(datasource));
    }

}
