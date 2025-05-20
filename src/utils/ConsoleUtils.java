package utils;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUtils {
    static final int defaultLength = 50;
    private static final Scanner scanner = new Scanner(System.in);

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String readLine() {
        return scanner.nextLine();
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                // Using nextInt cause input buffer problems
                String input = scanner.nextLine();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    public static void printTitle(String title) {
        final int n = title.length();
        final int headerLength = Math.max(defaultLength, n + 10);
        printSeparator(headerLength);
        final int leftPadding = Math.max(5, (headerLength - n) / 2);
        System.out.println(" ".repeat(leftPadding) + title);
        printSeparator(headerLength);
    }

    public static void printSeparator() {
        printSeparator(defaultLength);
    }

    public static void printSeparator(int length) {
        System.out.println("=".repeat(length));
    }

    public static void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static void printModelInfo(Map<String, String> modelData) {
        if (modelData == null || modelData.isEmpty()) {
            System.out.println("No data available");
            return;
        }

        int maxKeyLength = modelData.keySet().stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        modelData.forEach((key, value) -> {
            String paddedKey = key + ":";
            System.out.printf("%-" + (maxKeyLength + 2) + "s %s%n", paddedKey, value);
        });
    }

    public static void printModelList(List<Map<String, String>> models) {
        if (models == null || models.isEmpty()) {
            System.out.println("No data available");
            return;
        }

        var allKeys = models.stream()
                .flatMap(map -> map.keySet().stream())
                .distinct()
                .toList();

        int[] columnWidths = new int[allKeys.size()];
        for (int i = 0; i < allKeys.size(); i++) {
            String key = allKeys.get(i);
            int maxWidth = Math.max(
                    key.length(),
                    models.stream()
                            .map(map -> map.getOrDefault(key, ""))
                            .mapToInt(String::length)
                            .max()
                            .orElse(0));
            columnWidths[i] = maxWidth;
        }

        StringBuilder header = new StringBuilder();
        for (int i = 0; i < allKeys.size(); i++) {
            header.append(String.format("%-" + (columnWidths[i] + 2) + "s", allKeys.get(i)));
        }
        System.out.println(header);
        printSeparator(header.length());

        for (var model : models) {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < allKeys.size(); i++) {
                String key = allKeys.get(i);
                String value = model.getOrDefault(key, "");
                row.append(String.format("%-" + (columnWidths[i] + 2) + "s", value));
            }
            System.out.println(row);
        }

        printSeparator(header.length());
    }
}