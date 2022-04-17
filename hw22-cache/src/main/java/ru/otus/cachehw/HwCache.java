package ru.otus.cachehw;


public interface HwCache<K, V> {

    long cacheSize();

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);
}
