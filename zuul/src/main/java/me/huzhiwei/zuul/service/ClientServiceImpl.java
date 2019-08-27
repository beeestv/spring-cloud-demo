package me.huzhiwei.zuul.service;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Client;
import me.huzhiwei.zuul.domain.ClientQuery;
import me.huzhiwei.zuul.mapper.ClientMapper;
import me.huzhiwei.zuul.util.StringUtil;
import me.huzhiwei.zuul.util.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-23 15:48
 */
@Service
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientMapper clientMapper;


    @Override
    public List<Client> getClients(ClientQuery query) {
        if (StringUtils.isNotBlank(query.getClientId())) {
            return clientMapper.selectListByClientId(query.getClientId() + "%");
        }
        return clientMapper.selectListByClientId(null);
    }

    @Override
    public Client getClientById(Long id) {
        return clientMapper.selectById(id);
    }

    @Override
    public int addClient(Client client) {
        client.setClientSecret(StringUtil.md5(String.valueOf(UuidUtil.getId())));
        return clientMapper.insert(client);
    }

    @Override
    public int updateClient(Client client) {
        return clientMapper.update(client);
    }

    @Override
    public int renewClient(Long id) {
        Client client = new Client();
        client.setId(id);
        client.setClientSecret(StringUtil.md5(String.valueOf(UuidUtil.getId())));
        return clientMapper.update(client);
    }

    @Override
    public int deleteClient(Long id) {
        return clientMapper.delete(id);
    }
}
