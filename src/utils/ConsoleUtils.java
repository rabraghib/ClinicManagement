package utils;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import models.SerializableModel;

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

    public static boolean readBool(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n) ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'");
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

    public static void printModelList(List<? extends SerializableModel> models) {
        printModelList(models, 1);
    }

    public static void printModelList(List<? extends SerializableModel> models, int startNum) {
        if (models == null || models.isEmpty()) {
            System.out.println("No data available");
            return;
        }
        var allValues = models.stream().map(m -> m.toViewListString()).toList();
        System.out.println();
        int maxIndexLength = String.valueOf(startNum + models.size()).length();
        for (int i = 0; i < models.size(); i++) {
            String paddedIndex = String.valueOf(i + startNum) + ".";
            System.out.printf("%-" + (maxIndexLength + 2) + "s %s\n", paddedIndex, allValues.get(i));
        }
        System.out.println();
    }
}