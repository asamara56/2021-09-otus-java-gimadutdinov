package ru.otus.crm.service;

import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    long cacheSize();

    Client saveClient(Client client);

    Optional<Client> getClientWithCache(long id);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
