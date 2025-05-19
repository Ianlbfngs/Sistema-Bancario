package com.ib.clientsservice.service;

import com.ib.clientsservice.response.ClientCreationResponse;
import com.ib.clientsservice.response.ClientDeleteResponse;
import com.ib.clientsservice.response.ClientSpecificGetResponse;
import com.ib.clientsservice.response.ClientUpdateResponse;
import com.ib.clientsservice.entity.Client;
import com.ib.clientsservice.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements IClientService{

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }


    @Override
    public List<Client> getAllClients() {
        return clientRepository.findByActiveAndDniNot(true,"00000000");
    }

    @Override
    public ClientSpecificGetResponse getClientByDNI(String dni) {
        if(!dni.matches("^\\d+$")) return new ClientSpecificGetResponse(ClientSpecificGetResponse.Status.WRONG_DNI_FORMAT,null); //Wrong DNI format
        if(dni.equals("00000000")) return new ClientSpecificGetResponse(ClientSpecificGetResponse.Status.FORBIDDEN,null); //Bank cant be obtained
        try{
            Optional<Client> clientSearched = clientRepository.findByDniAndActive(dni,true);
            if(clientSearched.isEmpty()) return new ClientSpecificGetResponse(ClientSpecificGetResponse.Status.NOT_FOUND,null); //Client not found
            else if(!clientSearched.get().isActive()) return new ClientSpecificGetResponse(ClientSpecificGetResponse.Status.DELETED,null); //The client is deleted
            else return new ClientSpecificGetResponse(ClientSpecificGetResponse.Status.SUCCESS,clientSearched.get()); //Success
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return new ClientSpecificGetResponse(ClientSpecificGetResponse.Status.INVALID_DATA,null); //Error when doing the get
        }
    }

    @Override
    public ClientCreationResponse addClient(Client client) {
        if(!client.getDni().matches("^\\d+$") || !client.getCuil().matches("^\\d+$")){
            return new ClientCreationResponse(ClientCreationResponse.Status.WRONG_STRING_FORMAT,null); //Wrong DNI/CBU format
        }
        if(clientRepository.existsByDni(client.getDni())){
            return new ClientCreationResponse(ClientCreationResponse.Status.DNI_ALREADY_EXISTS,null); //DNI repeated
        }
        if(clientRepository.existsByCuil(client.getCuil())){
            return new ClientCreationResponse(ClientCreationResponse.Status.CUIL_ALREADY_EXISTS,null); //CBU repeated
        }
        if(clientRepository.existsByEmail(client.getEmail())){
            return new ClientCreationResponse(ClientCreationResponse.Status.EMAIL_ALREADY_EXISTS,null); //Email repeated
        }
        try{
            Client newClient = clientRepository.save(client);
            return new ClientCreationResponse(ClientCreationResponse.Status.SUCCESS,newClient); //Success
        }catch(DataIntegrityViolationException e){
            System.out.println("Error: "+e.getMessage());
            return new ClientCreationResponse(ClientCreationResponse.Status.INVALID_DATA,null); //Error when doing the post
        }


    }

    @Override
    public ClientDeleteResponse logicDeleteClient(String dni) { //Logical delete / active status becomes false
        if(dni.equals("00000000")) return new ClientDeleteResponse(ClientDeleteResponse.Status.FORBIDDEN,null); //Bank client cant be deleted
        try{
            Optional<Client> clientToDelete =  clientRepository.findByDni(dni);
            if(clientToDelete.isPresent()){
                if(!clientToDelete.get().isActive()) return new ClientDeleteResponse(ClientDeleteResponse.Status.ALREADY_DELETED,null); //Client deleted
                clientToDelete.get().setActive(false);
                clientRepository.save(clientToDelete.get());
                return new ClientDeleteResponse(ClientDeleteResponse.Status.SUCCESS,true); //Success
            }else{
                return new ClientDeleteResponse(ClientDeleteResponse.Status.NOT_FOUND,null); //Client to delete not found
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ClientDeleteResponse(ClientDeleteResponse.Status.INVALID_DATA,null); //Error when doing the put
        }

    }

    @Override
    public ClientUpdateResponse updateClient(Client client) {
        if(client.getDni().equals("00000000")) return new ClientUpdateResponse(ClientUpdateResponse.Status.FORBIDDEN,null);
        Optional<Client> oldClient = clientRepository.getClientByDniAndActive(client.getDni(),true);
        if(oldClient.isEmpty()) return new ClientUpdateResponse(ClientUpdateResponse.Status.NOT_FOUND,null);
        if(!client.getCuil().equals(oldClient.get().getCuil()) && clientRepository.existsClientByCuil(client.getCuil()))return new ClientUpdateResponse(ClientUpdateResponse.Status.CUIL_ALREADY_EXISTS,null);
        if(!client.getEmail().equals(oldClient.get().getEmail()) && clientRepository.existsClientByEmail(client.getEmail())) return new ClientUpdateResponse(ClientUpdateResponse.Status.EMAIL_ALREADY_EXISTS,null);
        try{
            Client updatedClient = clientRepository.save(client);
            return new ClientUpdateResponse(ClientUpdateResponse.Status.SUCCESS,updatedClient);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ClientUpdateResponse(ClientUpdateResponse.Status.INVALID_DATA,null);
        }
    }


    @Override
    @Transactional
    public Integer deleteClient(String dni) { //Physical delete for rollback when failing to register a credential but not a client
        if(dni.equals("00000000")) return 0;
        return clientRepository.deleteByDni(dni);

    }
}
