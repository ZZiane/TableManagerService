package org.zzach.tmservice.web;

import org.zzach.tmservice.entity.User;
import org.zzach.tmservice.models.EditDataRequest;
import org.zzach.tmservice.models.ManageTableRequest;
import org.zzach.tmservice.service.TableManagerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.zzach.tmservice.utils.ServiceHelper.authenticationUserBuilder;


@RestController
@RequestMapping("manage")
public class TableManagerController {


    private TableManagerService tableManagerService;

    public TableManagerController(TableManagerService tableManagerService) {
        this.tableManagerService = tableManagerService;
    }

    @GetMapping("/{id}")
    public List<Map<String, Object>> showTable(Authentication authentication, @PathVariable long id){
        User owner = authenticationUserBuilder(authentication);
        return tableManagerService.getData(new ManageTableRequest(owner.getEmail(),id));
    }

    @PutMapping
    public List<Map<String, Object>> updateValue(Authentication authentication,@RequestBody EditDataRequest editDataRequest){
        System.out.println(
                editDataRequest.toString()
        );
        User owner = authenticationUserBuilder(authentication);
        ManageTableRequest manageTableRequest = new ManageTableRequest(owner.getEmail(),editDataRequest.genericTableId());
        tableManagerService.editData(manageTableRequest,editDataRequest);
        return showTable(authentication,editDataRequest.genericTableId());
    }

    //dev methods
    @GetMapping("/{email}/{id}")
    public List<Map<String, Object>> showTable(@PathVariable String email, @PathVariable long id){
        return tableManagerService.getData(new ManageTableRequest(email,id));
    }

    @PutMapping("/{email}/{id}")
    public List<Map<String, Object>> updateValue(@PathVariable String email,@RequestBody EditDataRequest editDataRequest,@PathVariable int id){
        User owner = User.builder().email(email).build();
        ManageTableRequest manageTableRequest = new ManageTableRequest(owner.getEmail(),editDataRequest.genericTableId());
        tableManagerService.editData(manageTableRequest,editDataRequest);
        return showTable(email,editDataRequest.genericTableId());
    }


}
