package toolrental;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class ToolRentalApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    private static final List<String> VALID_TOOL_CODES = Arrays.asList("LADW", "CHNS", "JAKD", "JAKR");

    public static void main(String[] args) {
        CheckoutService service = new CheckoutService();

        try {
            while (true) {
                System.out.print("Enter command (rent/help/quit): ");
                System.out.flush();
                String command = scanner.nextLine().trim().toLowerCase();
                switch (command) {
                    case "rent":
                        processRental(service);
                        break;
                    case "help":
                        printHelp();
                        break;
                    case "quit":
                        System.out.println("Thank you for using the Tool Rental App!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid command. Type 'help' for assistance.");
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static void processRental(CheckoutService service) {
        String toolCode = promptToolCode();
        int rentalDays = promptRentalDays();
        int discountPercent = promptDiscountPercent();
        LocalDate checkoutDate = promptCheckoutDate();

        try {
            RentalAgreement agreement = service.checkout(toolCode, rentalDays, discountPercent, checkoutDate);
            System.out.println("\nRental Agreement:");
            agreement.print();
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String promptToolCode() {
        while (true) {
            String toolCode = promptString("Enter tool code (or 'list' to see available codes): ");
            if (toolCode.equalsIgnoreCase("list")) {
                System.out.println("Available tool codes: " + String.join(", ", VALID_TOOL_CODES));
                continue;
            }
            if (VALID_TOOL_CODES.contains(toolCode.toUpperCase())) {
                return toolCode.toUpperCase();
            }
            System.out.println("Invalid tool code. Please try again.");
        }
    }

    private static int promptRentalDays() {
        while (true) {
            int days = promptInt("Enter number of rental days: ");
            if (days > 0) {
                return days;
            }
            System.out.println("Rental days must be greater than 0. Please try again.");
        }
    }

    private static int promptDiscountPercent() {
        while (true) {
            int percent = promptInt("Enter discount percent (0-100): ");
            if (percent >= 0 && percent <= 100) {
                return percent;
            }
            System.out.println("Discount percent must be between 0 and 100. Please try again.");
        }
    }

    private static LocalDate promptCheckoutDate() {
        while (true) {
            LocalDate date = promptDate("Enter checkout date (MM/DD/YY or 'today'): ");
            if (date != null) {
                return date;
            }
        }
    }

    private static String promptString(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        return scanner.nextLine().trim();
    }

    private static int promptInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                System.out.flush();
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static LocalDate promptDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                System.out.flush();
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("today")) {
                    return LocalDate.now();
                }
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use MM/DD/YY or 'today'.");
            }
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  rent  - Start a new tool rental process");
        System.out.println("  help  - Display this help message");
        System.out.println("  quit  - Exit the application");
        System.out.println("\nWhen renting a tool:");
        System.out.println("  - Enter 'list' when prompted for a tool code to see available codes");
        System.out.println("  - Rental days must be a positive integer");
        System.out.println("  - Discount percent must be between 0 and 100");
        System.out.println("  - Date format is MM/DD/YY, or enter 'today' for the current date");
    }
}