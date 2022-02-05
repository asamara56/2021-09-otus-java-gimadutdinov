package ru.otus.dataprocessor.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.Map;

public interface Serializer {

    void serialize(Map<String, Double> data) throws IOException;
}
