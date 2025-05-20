package models;

import java.util.Map;

public abstract class SerializableModel {
    public Long id = null;

    public abstract String toFileString();

    public abstract String toViewListString();

    public abstract Map<String, String> toKeyValueMap();
}