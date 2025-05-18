package services;

import models.SerializableModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataRepository<T extends SerializableModel> {
    private final String filePath;
    private final Function<String, T> parser;

    public DataRepository(String filePath, Function<String, T> parser) {
        this.filePath = filePath;
        this.parser = parser;

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    public List<T> loadAll() {
        List<T> models = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists())
            return models;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    T model = parser.apply(line);
                    if (model != null) {
                        models.add(model);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data from " + filePath + ": " + e.getMessage());
        }

        return models;
    }

    public void saveAll(List<T> models) {
        if (models == null)
            return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T model : models) {
                if (model == null)
                    continue;
                if (model.id == null) {
                    model.id = generateId();
                }
                writer.write(model.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving data to " + filePath + ": " + e.getMessage());
        }
    }

    public T save(T model) {
        if (model == null)
            return null;
        if (model.id == null) {
            model.id = generateId();
        }
        List<T> models = loadAll();
        boolean found = false;
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).id == model.id) {
                models.set(i, model);
                found = true;
                break;
            }
        }
        if (!found)
            models.add(model);
        saveAll(models);
        return model;
    }

    public T loadById(Long id) {
        List<T> models = loadAll();
        for (T model : models) {
            if (model.id == id)
                return model;
        }
        return null;
    }

    public boolean remove(Long id) {
        List<T> models = loadAll();
        boolean removed = false;
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).id == id) {
                models.remove(i);
                removed = true;
                break;
            }
        }
        if (removed)
            saveAll(models);
        return removed;
    }

    public static long generateId() {
        return System.currentTimeMillis();
    }
}