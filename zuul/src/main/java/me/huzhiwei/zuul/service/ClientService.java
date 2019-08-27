package me.huzhiwei.zuul.service;

import me.huzhiwei.zuul.domain.Client;
import me.huzhiwei.zuul.domain.ClientQuery;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-23 15:48
 */
public interface ClientService {

    List<Client> getClients(ClientQuery query);

    Client getClientById(Long id);

    int addClient(Client client);

    int updateClient(Client client);

    int renewClient(Long id);

    int deleteClient(Long id);
}
