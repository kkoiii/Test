package Utils;

import java.util.Scanner;

public class Utils {
    private static final Scanner scanner = new Scanner(System.in);
    
    // Constants for table drawing
    private static final String HORIZONTAL_LINE = "─";
    private static final String VERTICAL_LINE = "│";
    private static final String TOP_LEFT = "┌";
    private static final String TOP_RIGHT = "┐";
    private static final String BOTTOM_LEFT = "└";
    private static final String BOTTOM_RIGHT = "┘";
    private static final String T_DOWN = "┬";
    private static final String T_UP = "┴";
    private static final String T_RIGHT = "├";
    private static final String T_LEFT = "┤";
    private static final String CROSS = "┼";

    // Input validation with retry
    public static String getString(String prompt, String error, String regex) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.matches(regex)) {
                return input;
            }
            System.out.println("Error: " + error);
        } while (true);
    }

    public static int getInt(String prompt, String error, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.println("Error: " + error + " (Must be between " + min + " and " + max + ")");
            } catch (NumberFormatException e) {
                System.out.println("Error: " + error + " (Must be a valid number)");
            }
        }
    }

    // Helper method to replace String.repeat()
    private static String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    // Table drawing methods
    public static void drawTableHeader(String[] headers, int[] columnWidths) {
        // Draw top border
        System.out.print(TOP_LEFT);
        for (int i = 0; i < columnWidths.length; i++) {
            System.out.print(repeatString(HORIZONTAL_LINE, columnWidths[i] + 2));
            System.out.print(i < columnWidths.length - 1 ? T_DOWN : TOP_RIGHT);
        }
        System.out.println();

        // Draw headers
        System.out.print(VERTICAL_LINE);
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" %-" + columnWidths[i] + "s ", headers[i]);
            System.out.print(VERTICAL_LINE);
        }
        System.out.println();

        // Draw separator
        System.out.print(T_RIGHT);
        for (int i = 0; i < columnWidths.length; i++) {
            System.out.print(repeatString(HORIZONTAL_LINE, columnWidths[i] + 2));
            System.out.print(i < columnWidths.length - 1 ? CROSS : T_LEFT);
        }
        System.out.println();
    }

    public static void drawTableRow(String[] data, int[] columnWidths) {
        System.out.print(VERTICAL_LINE);
        for (int i = 0; i < data.length; i++) {
            String cell = data[i];
            if (cell.length() > columnWidths[i]) {
                // Split long content into multiple lines
                String[] lines = splitString(cell, columnWidths[i]);
                for (int j = 0; j < lines.length; j++) {
                    if (j > 0) {
                        System.out.println();
                        System.out.print(VERTICAL_LINE);
                        for (int k = 0; k < i; k++) {
                            System.out.printf(" %" + columnWidths[k] + "s ", "");
                            System.out.print(VERTICAL_LINE);
                        }
                    }
                    System.out.printf(" %-" + columnWidths[i] + "s ", lines[j]);
                    if (j == lines.length - 1) {
                        for (int k = i + 1; k < columnWidths.length; k++) {
                            System.out.print(VERTICAL_LINE);
                            System.out.printf(" %" + columnWidths[k] + "s ", "");
                        }
                        System.out.print(VERTICAL_LINE);
                    }
                }
            } else {
                System.out.printf(" %-" + columnWidths[i] + "s ", cell);
                System.out.print(VERTICAL_LINE);
            }
        }
        System.out.println();
    }

    public static void drawTableFooter(int[] columnWidths) {
        System.out.print(BOTTOM_LEFT);
        for (int i = 0; i < columnWidths.length; i++) {
            System.out.print(repeatString(HORIZONTAL_LINE, columnWidths[i] + 2));
            System.out.print(i < columnWidths.length - 1 ? T_UP : BOTTOM_RIGHT);
        }
        System.out.println();
    }

    private static String[] splitString(String str, int maxWidth) {
        if (str == null || str.length() <= maxWidth) {
            return new String[]{str};
        }

        int parts = (str.length() + maxWidth - 1) / maxWidth;
        String[] result = new String[parts];
        for (int i = 0; i < parts; i++) {
            int start = i * maxWidth;
            int end = Math.min(start + maxWidth, str.length());
            result[i] = str.substring(start, end);
        }
        return result;
    }
}