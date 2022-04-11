package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM " + entityClassMetaData.getName()
                + " WHERE " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        int amount = fields.size();
        return "INSERT INTO " + entityClassMetaData.getName()
                + fields.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(",", "(", ")"))
                + " VALUES (?" + (amount > 1 ? ", ?".repeat(amount - 1) + ")" : ")");
    }

    @Override
    public String getUpdateSql() {
        return "UPDATE " + entityClassMetaData.getName()
                + " SET " + entityClassMetaData.getFieldsWithoutId().stream()
                                .skip(1)
                                .map(field -> field.getName() + " = ?")
                                .collect(Collectors.joining(","))
                + " WHERE ID = " + entityClassMetaData.getIdField();
    }
}
