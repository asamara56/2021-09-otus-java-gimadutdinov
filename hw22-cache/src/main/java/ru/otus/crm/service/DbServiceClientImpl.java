package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = new MyCache<>(List.of(new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("Cache action:{}, key:{}, value:{}", action, key, value);
            }
        }));
    }

    @Override
    public long cacheSize() {
        return cache.cacheSize();
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                // log.info("created client: {}", createdClient); // очень много логов
                cache.put("key_" + clientId, createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            // log.info("updated client: {}", client); // очень много логов
            cache.put(String.valueOf(client.getId()), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClientWithCache(long id) {
        var cacheValue = cache.get("key_" + id);
        if (cacheValue != null) {
            return Optional.of(cacheValue);
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            // log.info("client: {}", clientOptional);  // очень много логов
            clientOptional.ifPresent(
                    client -> cache.put("key_" + client.getId(), client));
            return clientOptional;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            clientOptional.ifPresent(
                    client -> cache.put("key_" + client.getId(), client));
            // log.info("client: {}", clientOptional); // очень много логов
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        /**
         * Возможно здесь нужно реализовать что-то типа
         * if (dataTemplate.getClientCount() == cache.size()) {
         *     return cache.findAll();
         * }
         * но сомневаюсь, что в высоконагруженных системах возможен такой кейс
         */
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            /**
             * и здесь
             * clientList.forEach(
             *       client -> cache.put("key_" + client.getId(), client)
             * );
             */
            return clientList;
       });
    }
}
