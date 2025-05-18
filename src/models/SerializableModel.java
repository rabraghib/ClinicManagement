package models;

public abstract class SerializableModel {
    public Long id = null;

    public abstract String toFileString();
}