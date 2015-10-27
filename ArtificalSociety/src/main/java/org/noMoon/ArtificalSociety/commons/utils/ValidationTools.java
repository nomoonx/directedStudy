package org.noMoon.ArtificalSociety.commons.utils;

/**
 * Created by noMoon on 2015-10-18.
 */
public class ValidationTools {
    public static double clipValueToRange (double val, double min, double max) {
        // If the given number, val, is out of the range, [min, max], then clip it to the nearest boundary.
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        } else {
            return val;
        } // end if (determine min or max)
    } // end clipValueToRange()

    public static int clipValueToRange (int val, int min, int max) {
        // If the given number, val, is out of the range, [min, max], then clip it to the nearest boundary.
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        } else {
            return val;
        } // end if (determine min or max)
    } // end clipValueToRange()

    public static boolean numberIsWithin (int val, int min, int max) {
        // Check if the given number is between the given min and max parameters.
        if (val >= min && val <= max) {
            return true;
        } // end if (check if number is within range)
        return false;
    } // end numberIsWithin()

    public static boolean parseAndCheckCondition (double val, String condition) {
        // This method will parse a condition from the given string, and check whether or not the condition is true for the given number, val.
        // The expected usage of a condition has two terms, separated by a vertical line |, i.e. "=|1". The first term is the operator (<, >, <=, >=, =) and the
        // second term is a numeric threshold or value to check against. In the case of "=|1", the condition is that the given number, val, is equal to 1.
        // param val: a double number from which to check (this will typically be a value of a specific attribute from a person)
        // param condition: this is the string encoded condition for which to check (see the above explanation for the expected format of this string)
        //
        // returns: a boolean flag representing whether or not the condition is met for the given number, val

        String[] conditionTerms = condition.split("\\|"); // Divide into two terms by separating from vertical line in string. The vertical bar needs to be escaped.

        String operator = conditionTerms[0];						// Get the conditional operator.
        double threshold = Double.parseDouble(conditionTerms[1]);	// Get the numeric threshold value.

        if (operator.equals("=")) {
            return (val == threshold);
        } else if (operator.equals("<")) {
            return (val < threshold);
        } else if (operator.equals(">")) {
            return (val > threshold);
        } else if (operator.equals("<=")) {
            return (val <= threshold);
        } else if (operator.equals(">=")) {
            return (val >= threshold);
        }

        System.err.println("In ValidationTools->parseAndCheckCondition(); no valid operator was found for condition: '" + condition + "'. Ensure that the XML files are written properly.");

        return false;
    } // end parseAndCheckCondition()
}
