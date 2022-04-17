package ru.otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
    private final List<HwListener<K, V>> listeners;
    private final Map<K, V> cache = new WeakHashMap<>();

    public MyCache() {
        listeners = new ArrayList<>();
    }

    public MyCache(List<HwListener<K, V>> listeners) {
        this.listeners = listeners;
    }

    @Override
    public long cacheSize() {
        return cache.size();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        // listeners.forEach(listener -> listener.notify(key, value, "PUT")); // очень много логов
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        listeners.forEach(listener -> listener.notify(key, value, "REMOVE"));
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        //listeners.forEach(listener -> listener.notify(key, value, "GET")); //очень много логов
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
