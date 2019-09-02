package me.huzhiwei.zuul.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.huzhiwei.zuul.domain.Client;
import me.huzhiwei.zuul.domain.ClientAddRO;
import me.huzhiwei.zuul.domain.ClientQuery;
import me.huzhiwei.zuul.domain.Result;
import me.huzhiwei.zuul.service.ClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-23 15:47
 */
@RestController
@RequestMapping("/gateway")
@Validated
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public Result getClients(ClientQuery query) {
        PageInfo<Client> pageInfo = PageHelper.startPage(query).doSelectPageInfo(() -> clientService.getClients(query));
        return Result.success(pageInfo);
    }

    @GetMapping("/clients/{id}")
    public Result getClientsById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return Result.success(client);
    }

    @PostMapping("/clients")
    public Result addClient(@Valid @RequestBody ClientAddRO clientAddRO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientAddRO, client);
        clientService.addClient(client);
        return Result.success();
    }

    @PutMapping("/clients/{id}")
    public Result updateClient(@PathVariable Long id, @Valid @RequestBody ClientAddRO clientAddRO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientAddRO, client);
        client.setId(id);
        clientService.updateClient(client);
        return Result.success();
    }

    @PostMapping("/clients/{id}")
    public Result renewClient(@PathVariable Long id) {
        clientService.renewClient(id);
        return Result.success();
    }

    @DeleteMapping("/clients/{id}")
    public Result deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return Result.success();
    }
}
