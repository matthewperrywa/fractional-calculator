// computes arithmetic operations with integers and fractions and outputs the result as a reduced mixed fraction

import java.util.Scanner;

public class FractionalCalculator {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Fractional Calculator!\n");
        System.out.println("Please format your operands like so:\n");
        System.out.println("For numbers that aren't fractions, only whole numbers are supported (64, -5, 0).\n");
        System.out.println("For fractions, mixed numbers, improper fractions, and proper fractions are supported.\n");
        System.out.println("Please format mixed numbers by typing the whole number and then an underline to separate the whole number from the fraction (1_3/7, -2_4/6, 33_1/2).\n");
        System.out.println("For fractions, please separate the numerator and denominator with a slash (8/10, -64/7, 1/5).\n");
        System.out.println("Please separate your operator and operands by putting a space on both sides of the operator (1/2 * 83, 2 / 1/2, 83 + -9/10).\n");
        System.out.println("At this time, only two operands can be evaluated.\n");
        System.out.println("Valid operators include +, -, *, and /\n");
        System.out.println("Type \"quit\" at anytime to leave.\n");
        System.out.print("Enter an expression: ");

        String input = scan.nextLine().toLowerCase(); // input is the user's entered expression
        while (!input.equals("quit")) {
            if (input.equals("test")) {
                System.out.print(runTests());
            }
            else {
                try {
                    System.out.print(produceAnswer(input));
                }
                catch(Exception e) {
                    System.out.print("Please format your expression correctly.");
                }
            }
            System.out.println();
            System.out.println();
            System.out.print("Enter an expression: ");
            input = scan.nextLine().toLowerCase();
        }

        System.out.println();
        System.out.print("Goodbye! Thank you for using Fractional Calculator!");
    }

    // returns the answer of the user's expression
    public static String produceAnswer(String input) {
        String firstOperand = input.substring(0, input.indexOf(" "));
        String operator = input.substring(input.indexOf(" ") + 1, input.indexOf(" ") + 2);
        String secondOperand = input.substring(input.indexOf(" ") + 3);
        int[] parsedFirstOperand = parsedOperand(firstOperand);
        int firstWhole = parsedFirstOperand[0];
        int firstNumerator = parsedFirstOperand[1];
        int firstDenominator = parsedFirstOperand[2];
        int[] parsedSecondOperand = parsedOperand(secondOperand);
        int secondWhole = parsedSecondOperand[0];
        int secondNumerator = parsedSecondOperand[1];
        int secondDenominator = parsedSecondOperand[2];
        return evaluate(firstWhole, firstNumerator, firstDenominator, secondWhole, secondNumerator, secondDenominator, operator);
    }

    // evaluates the user's expression
    // the variables starting with "first" correspond to the first operand and the variables starting with "second" correspond to the second operand
    public static String evaluate(int firstWhole, int firstNumerator, int firstDenominator, int secondWhole, int secondNumerator,
                                  int secondDenominator, String operator) {
        int newWhole = 0; // newWhole is the whole after the expression is evaluated
        int newNumerator = 0; // newNumerator is the numerator after the expression is evaluated
        firstNumerator = firstNumerator * secondDenominator;
        secondNumerator = secondNumerator * firstDenominator;
        int newDenominator = firstDenominator * secondDenominator; // newDenominator is the denominator after the expression is evaluated
        firstNumerator = numeratorNegative(firstWhole, firstNumerator);
        secondNumerator = numeratorNegative(secondWhole, secondNumerator);

        // evaluates addition
        if (operator.equals("+")) {
            newWhole = firstWhole + secondWhole;
            newNumerator = (firstNumerator + secondNumerator) + (newWhole * newDenominator);
        }
        // evaluates subtraction
        else if (operator.equals("-")) {
            newWhole = firstWhole - secondWhole;
            newNumerator = (firstNumerator - secondNumerator) + (newWhole * newDenominator);
        }
        // evaluates multiplication
        else if (operator.equals("*")) {
            newNumerator = ((firstWhole * newDenominator + firstNumerator) * (secondWhole * newDenominator + secondNumerator));
            newDenominator = newDenominator * newDenominator;
        }
        // evaluates division
        else {
            newNumerator = ((firstWhole * newDenominator + firstNumerator) * (newDenominator));
            newDenominator = newDenominator * (secondWhole * newDenominator + secondNumerator);
        }

        return simplify(newNumerator, newDenominator);
    }

    // makes the value of a numerator negative if the whole is negative
    public static int numeratorNegative(int whole, int numerator) {
        if (whole != 0 && whole == (Math.abs(whole) * -1)) {
            numerator = numerator * -1;
        }
        return numerator;
    }

    // simplifies a fraction into a whole, a proper fraction, or a mixed number
    public static String simplify(int numerator, int denominator) {

        // in order for the output to be displayed correctly, the numerator becomes negative if the denominator is negative
        if ((Math.abs(denominator) * -1) == denominator) {
            numerator = numerator * -1;
            denominator = Math.abs(denominator);
        }

        // whole is the whole number, which is determined based on the fraction. The whole is zero if the fraction is less than one
        int whole = numerator / denominator;
        // the numerator is whatever is left after the whole is removed from the fraction
        numerator = numerator % denominator;

        // finds the greatest common factor of the numerator and denominator and divides each by that
        for (int k = 1; k <= denominator; k++) {
            if (numerator % k == 0 && denominator % k == 0) {
                numerator = numerator / k;
                denominator = denominator / k;
                k = 1;
            }
        }

        // the following if, else if, and else statements determine whether the output is a whole, a proper fraction, or a mixed number
        if (numerator == 0) {
            return whole + "";
        }
        else if (whole == 0) {
            return numerator + "/" + denominator;
        }
        else {
            numerator = Math.abs(numerator);
            return whole + "_" + numerator + "/" + denominator;
        }

    }

    // parses an operand into an array that contains a whole, a numerator, and a denominator
    public static int[] parsedOperand(String operand) {
        String whole = "0"; // whole is the whole in the operand

        if (operand.contains("_")) {
            whole = operand.substring(0, operand.indexOf("_"));
        }

        if (!operand.contains("/")) {
            whole = operand;
        }

        String numerator = "0";

        if (operand.contains("/")) {
            numerator = operand.substring(0, operand.indexOf("/"));
            if (operand.contains("_")) {
                numerator = operand.substring(operand.indexOf("_") + 1, operand.indexOf("/"));
            }
        }

        String denominator = "1";

        if (operand.contains("/")) {
            denominator = operand.substring(operand.indexOf("/") + 1);
        }

        int wholeInt = Integer.parseInt(whole); // wholeInt is the whole in integer form
        int numeratorInt = Integer.parseInt(numerator); // numeratorInt is the numerator in integer form
        int denominatorInt = Integer.parseInt(denominator); // denominatorInt is the denominator in integer form
        // parsedOperand is the operand parsed into a whole, a numerator, and a denominator
        int[] parsedOperand = { wholeInt, numeratorInt, denominatorInt };
        return parsedOperand;
    }

    // tests a variety of different expressions and determines if the actual output is the same as the expected output
    public static String runTests() {
        System.out.println();
        // inputs is the test inputs
        String[] inputs = { "1/4 + 1_1/2", "8/4 + 2", "-1 * -1/2", "-11/17 - -1/17", "0 / 25_462/543", "6 / 9", "-7 / 21", "21 / 6", "-33 / 11",
                "-17/18 / -9/96", "-5/10 * -10/5", "5 + -5", "0/27 * 15/35", "-7/27 - 1/27", "0/1 / 1/2", "-5/9 / -9/5", "-3 - -3", "-3 + -3",
                "-6/2 + -12/4", "-24/8 - -24/8", "-1_5/15 + 0", "-1_5/15 * -2", "-1_5/15 * 3", "-1_5/15 / -5" };
        // expected is the expected outputs based on the test inputs
        String[] expected = { "1_3/4", "4", "1/2", "-10/17", "0", "2/3", "-1/3", "3_1/2", "-3",
                "10_2/27", "1", "0", "0", "-8/27", "0", "25/81", "0", "-6",
                "-6", "0", "-1_1/3", "2_2/3", "-4", "4/15" };
        int passCount = 0; // passCount is the amount of test expressions that are passed

        for (int k = 0; k < inputs.length; k++) {
            // if the test is passed, it is printed that it is passed
            if (produceAnswer(inputs[k]).equals(expected[k])) {
                passCount++;
                System.out.println("Test " + (k + 1) + " Passed\n");
            }
            // if the test is failed, it is printed that it is failed and displays the input, expected output, and actual output
            else {
                System.out.println("Test " + (k + 1) + " Failed");
                System.out.println("Input: " + inputs[k]);
                System.out.println("Expected: " + expected[k]);
                System.out.println("Output: " + produceAnswer(inputs[k]) + "\n");
            }
        }
        // the amount of tests passed is returned
        return "Tests: " + passCount + " of " + inputs.length + " Test Cases Passed";
    }
}