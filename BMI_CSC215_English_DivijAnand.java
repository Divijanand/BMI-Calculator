import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BMI_CSC215_English_DivijAnand {
    private static final float BMI_CONSTANT = 703f;
    private static final float WEIGHT_INCREMENT = 5.5f;
    private static final String YELLOW_BACKGROUND = "\u001B[43m";
    private static final String BLACK_TEXT = "\u001B[30m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        displayWelcomeMessage();

        String name = getUsername();
        int height = getHeight(name);
        float weight = getWeight(name);

        float bmi = getBMI(height, weight);

        displaySummaryReport(name, bmi, weight);

        float lowWeight, highWeight;
        while (true) {
            System.out.print("\nPlease enter a LOW weight in pounds for " + name + ": ");
            lowWeight = input.nextFloat();

            System.out.print("Please enter a HIGH weight in pounds for " + name + ": ");
            highWeight = input.nextFloat();

            if (lowWeight < weight && highWeight > weight) {
                break;
            } else {
                System.out.println("\n!! Your weight must be between LOW and HIGH. !!");
            }
        }

        displayTable(lowWeight, highWeight, height, weight, bmi);

        System.out.println("\nThe SFSU Mashouf Wellness Center is at 755 Font Blvd.");

        displayOutroMessage(name);
    }

    public static void displayWelcomeMessage() {
        System.out.println("-".repeat(90));
        System.out.println("-- Welcome to: ");
        System.out.println("--            BODY MASS INDEX (BMI) COMPUTATION, CSC215, English Version ");
        System.out.println("--                                                                  by Divij Anand");
        System.out.println("-".repeat(90));
    }

    public static String getUsername() {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter your full name: ");
        return input.nextLine();
    }

    public static int getHeight(String name) {
        Scanner input = new Scanner(System.in);
        int heightFeet, heightInches;
        do {
            System.out.print("Please enter height in feet and inches for " + name + ": ");
            heightFeet = input.nextInt();
            heightInches = input.nextInt();
            if (heightFeet < 0 || heightInches < 0 || heightInches >= 12) {
                System.out.println("Invalid height. Please enter positive values, with inches less than 12.");
            }
        } while (heightFeet < 0 || heightInches < 0 || heightInches >= 12);
        return (heightFeet * 12) + heightInches;
    }

    public static float getWeight(String name) {
        Scanner input = new Scanner(System.in);
        float weight;
        do {
            System.out.print("Please enter your weight in pounds for " + name + ": ");
            weight = input.nextFloat();
            if (weight <= 0) {
                System.out.println("Invalid weight. Please enter a positive value.");
            }
        } while (weight <= 0);
        return weight;
    }

    public static float getBMI(int height, float weight) {
        return (weight / (height * height)) * BMI_CONSTANT;
    }

    public static void displaySummaryReport(String name, float bmi, float weight) {
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

    public static void displayTable(float lowWeight, float highWeight, int height, float userWeight, float userBMI) {
        System.out.println("-".repeat(53));
        System.out.println("| WEIGHT      | BMI       | WEIGHT STATUS           |");
        System.out.println("-".repeat(53));

        for (float weight = lowWeight; weight <= highWeight; weight += WEIGHT_INCREMENT) {
            if (weight > userWeight && (weight - WEIGHT_INCREMENT) < userWeight) {
                // Print user's row
                printTableRow(userWeight, height, true, lowWeight, highWeight);
            }
            printTableRow(weight, height, weight == userWeight, lowWeight, highWeight);
        }

        // Ensure the high weight is always printed if it's not already included
        if (highWeight % WEIGHT_INCREMENT != 0 && highWeight != userWeight) {
            printTableRow(highWeight, height, false, lowWeight, highWeight);
        }

        System.out.println("-".repeat(53));
    }

    private static void printTableRow(float weight, int height, boolean isUserWeight, float lowWeight, float highWeight) {
        float bmi = getBMI(height, weight);
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

        if (weight == lowWeight) {
            weightStatus += " " + BLACK_TEXT + YELLOW_BACKGROUND + "(LOW)" + RESET;
        } else if (weight == highWeight) {
            weightStatus += " " + BLACK_TEXT + YELLOW_BACKGROUND + "(HIGH)" + RESET;
        }

        if (isUserWeight) {
            weightStatus += " (this)";
        }

        System.out.printf("| %-11.2f | %-9s | %-23s |%n", weight, bmiString, weightStatus);
    }

    public static void displayOutroMessage(String name) {
        System.out.println("-".repeat(90));
        System.out.println("-- Thank you for using my program, " + name + "!");
        if (name.equalsIgnoreCase("Otto Minion")) {
            System.out.println("-- Poopaye!!!");
        } else if (name.equalsIgnoreCase("Minnie Mouse")) {
            System.out.println("-- Ear-esistible!!!");
        }
        System.out.println("-".repeat(90));
    }
}