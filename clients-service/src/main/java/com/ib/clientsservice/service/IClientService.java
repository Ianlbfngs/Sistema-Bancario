package com.ib.clientsservice.service;

import com.ib.clientsservice.response.ClientCreationResponse;
import com.ib.clientsservice.response.ClientDeleteResponse;
import com.ib.clientsservice.response.ClientSpecificGetResponse;
import com.ib.clientsservice.response.ClientUpdateResponse;
import com.ib.clientsservice.entity.Client;

import java.util.List;

public interface IClientService {

    public List<Client> getAllClients();
    public ClientSpecificGetResponse getClientByDNI(String dni);
    public ClientCreationResponse addClient(Client client);
    public ClientDeleteResponse logicDeleteClient(String dni);
    public ClientUpdateResponse updateClient(Client client);
    public Integer deleteClient(String dni);
}
