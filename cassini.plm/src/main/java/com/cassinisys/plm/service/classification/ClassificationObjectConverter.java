package com.cassinisys.plm.service.classification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 6/13/17.
 */
public class ClassificationObjectConverter<T, A> {
    private final Class<T> typeClass;
    private final Class<A> attributeClass;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ClassificationObjectConverter(Class<T> typeClass, Class<A> attributeClass) {
        this.typeClass = typeClass;
        this.attributeClass = attributeClass;
    }

    public T convertToType(ObjectNode node) {
        try {
            return objectMapper.treeToValue(node, typeClass);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public List<T> convertToType(List<ObjectNode> nodes) {
        List<T> list = new ArrayList<>();
        nodes.forEach(node -> list.add(convertToType(node)));
        return list;
    }

    public A convertToTypeAttribute(ObjectNode node) {
        try {
            return objectMapper.treeToValue(node, attributeClass);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public List<A> convertToTypeAttribute(List<ObjectNode> nodes) {
        List<A> list = new ArrayList<>();
        nodes.forEach(node -> list.add(convertToTypeAttribute(node)));
        return list;
    }
}
