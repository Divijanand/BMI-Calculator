import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BMI_CSC215_Master_DivijAnand {
    private static final float BMI_CONSTANT = 703f;
    private static final float WEIGHT_INCREMENT_ENGLISH = 5.5f;
    private static final float WEIGHT_INCREMENT_METRIC = 2.5f;
    private static final String YELLOW_BACKGROUND = "\u001B[43m";
    private static final String BLACK_TEXT = "\u001B[30m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        runMasterProgram();
    }

    public static void runMasterProgram() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("\nPlease enter the version you want to try:");
            System.out.println("1. English Version");
            System.out.println("2. Metric Version");
            System.out.println("Enter '!' to exit the program.");

            choice = scanner.nextLine().trim();

            if (choice.equals("!")) {
                System.out.println("Thank you for using the BMI calculator. Goodbye!");
            } else {
                String choiceLower = choice.toLowerCase();

                if (choiceLower.contains("eng") || choiceLower.contains("lish")) {
                    runBMICalculator(true); // Run English version
                } else if (choiceLower.startsWith("m") || choiceLower.contains("tric")) {
                    runBMICalculator(false); // Run Metric version
                } else {
                    System.out.println("Invalid choice. Please enter 'English' or 'Metric'.");
                }
            }
        } while (!choice.equals("!"));

        scanner.close();
    }

    public static void runBMICalculator(boolean isEnglish) {
        Scanner input = new Scanner(System.in);

        displayWelcomeMessage(isEnglish);

        String name = getUsername();
        float height = getHeight(name, isEnglish);
        float weight = getWeight(name, isEnglish);

        float bmi = getBMI(height, weight, isEnglish);

        displaySummaryReport(name, bmi, weight, isEnglish);

        float lowWeight, highWeight;
        while (true) {
            System.out.print("\nPlease enter a LOW weight in " + (isEnglish ? "pounds" : "kilograms") + " for " + name + ": ");
            lowWeight = input.nextFloat();

            System.out.print("Please enter a HIGH weight in " + (isEnglish ? "pounds" : "kilograms") + " for " + name + ": ");
            highWeight = input.nextFloat();

            if (lowWeight < weight && highWeight > weight) {
                break;
            } else {
                System.out.println("\n!! Your weight must be between LOW and HIGH. !!");
            }
        }

        displayTable(lowWeight, highWeight, height, weight, bmi, isEnglish);

        System.out.println("\nThe SFSU Mashouf Wellness Center is at 755 Font Blvd.");

        displayOutroMessage(name, isEnglish);
    }

    public static void displayWelcomeMessage(boolean isEnglish) {
        System.out.println("-".repeat(90));
        System.out.println("-- Welcome to: ");
        System.out.println("--            BODY MASS INDEX (BMI) COMPUTATION, CSC215, " + (isEnglish ? "English" : "Metric") + " Version ");
        System.out.println("--                                                                  by Divij Anand");
        System.out.println("-".repeat(90));
    }

    public static String getUsername() {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter your full name: ");
        return input.nextLine();
    }

    public static float getHeight(String name, boolean isEnglish) {
        Scanner input = new Scanner(System.in);
        float height;
        do {
            if (isEnglish) {
                System.out.print("Please enter height in feet and inches for " + name + ": ");
                int feet = input.nextInt();
                int inches = input.nextInt();
                height = feet * 12 + inches;
            } else {
                System.out.print("Please enter height in centimeters for " + name + ": ");
                height = input.nextFloat() / 100; // Convert cm to meters
            }
            if (height <= 0) {
                System.out.println("Invalid height. Please enter a positive value.");
            }
        } while (height <= 0);
        return height;
    }

    public static float getWeight(String name, boolean isEnglish) {
        Scanner input = new Scanner(System.in);
        float weight;
        do {
            System.out.print("Please enter your weight in " + (isEnglish ? "pounds" : "kilograms") + " for " + name + ": ");
            weight = input.nextFloat();
            if (weight <= 0) {
                System.out.println("Invalid weight. Please enter a positive value.");
            }
        } while (weight <= 0);
        return weight;
    }

    public static float getBMI(float height, float weight, boolean isEnglish) {
        if (isEnglish) {
            return (weight / (height * height)) * BMI_CONSTANT;
        } else {
            return weight / (height * height);
        }
    }

    public static void displaySummaryReport(String name, float bmi, float weight, boolean isEnglish) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm:ss a");

        String weightStatus = getWeightStatus(bmi);

        System.out.println("\n-- SUMMARY REPORT FOR " + name.toUpperCase());
        System.out.println("-- Date and Time:           " + now.format(formatter));
        System.out.printf("-- BMI:                     %.6f (or %.1f if rounded)%n", bmi, bmi);
        System.out.println("-- Weight Status:           " + weightStatus);
    }

    public static String getWeightStatus(float bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "Normal weight";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obesity";
        }
    }

    public static void displayTable(float lowWeight, float highWeight, float height, float userWeight, float userBMI, boolean isEnglish) {
        System.out.println("-".repeat(53));
        System.out.println("| WEIGHT      | BMI       | WEIGHT STATUS           |");
        System.out.println("-".repeat(53));

        float weightIncrement = isEnglish ? WEIGHT_INCREMENT_ENGLISH : WEIGHT_INCREMENT_METRIC;

        for (float weight = lowWeight; weight <= highWeight; weight += weightIncrement) {
            if (weight > userWeight && (weight - weightIncrement) < userWeight) {
                // Print user's row
                printTableRow(userWeight, height, true, lowWeight, highWeight, isEnglish);
            }
            printTableRow(weight, height, weight == userWeight, lowWeight, highWeight, isEnglish);
        }

        // Ensure the high weight is always printed if it's not already included
        if (highWeight % weightIncrement != 0 && highWeight != userWeight) {
            printTableRow(highWeight, height, false, lowWeight, highWeight, isEnglish);
        }

        System.out.println("-".repeat(53));
    }

    private static void printTableRow(float weight, float height, boolean isUserWeight, float lowWeight, float highWeight, boolean isEnglish) {
        float bmi = getBMI(height, weight, isEnglish);
        String weightStatus = getWeightStatus(bmi);

        String bmiString;
        switch (weightStatus) {
            case "Underweight":
                bmiString = String.format("%.2f", bmi);
                break;
            case "Normal weight":
                bmiString = String.format("%.3f", bmi);
                break;
            case "Overweight":
                bmiString = String.format("%.4f", bmi);
                break;
            case "Obesity":
                bmiString = String.format("%.5f", bmi);
                break;
            default:
                bmiString = String.format("%.5f", bmi);
        }

        String statusString = weightStatus;
        if (weight == lowWeight) {
            statusString += " " + BLACK_TEXT + YELLOW_BACKGROUND + "(LOW)" + RESET;
        } else if (weight == highWeight) {
            statusString += " " + BLACK_TEXT + YELLOW_BACKGROUND + "(HIGH)" + RESET;
        }

        if (isUserWeight) {
            statusString += " (this)";
        }

        // Pad the status string to ensure consistent alignment
        statusString = String.format("%-23s", statusString);

        System.out.printf("| %-11.2f | %-9s | %s |%n", weight, bmiString, statusString);
    }

    public static void displayOutroMessage(String name, boolean isEnglish) {
        System.out.println("-".repeat(90));
        System.out.println("-- Thank you for using my program, " + name + "!");
        if (isEnglish) {
            if (name.equalsIgnoreCase("Otto Minion")) {
                System.out.println("-- Poopaye!!!");
            } else if (name.equalsIgnoreCase("Minnie Mouse")) {
                System.out.println("-- Ear-esistible!!!");
            }
        } else {
            if (name.equalsIgnoreCase("Baymax Hamada")) {
                System.out.println("-- Sayōnara!!!");
            } else if (name.equalsIgnoreCase("Goofy Dog")) {
                System.out.println("-- Woof Woof!!!");
            }
        }
        System.out.println("-".repeat(90));
    }
}