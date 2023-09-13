package com.example.wcc;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class CompoundConditionalCalculator {
    @PostMapping("/calculate-complexity-if")
    public int countIf(@RequestBody String code) {
        // Define regular expressions to match 'if' and 'if else' statements
        String ifPattern = "\\bif\\s*\\(.+?\\)\\s*\\{";
        String elseIfPattern = "\\belse\\s+if\\s*\\(.+?\\)\\s*\\{";

        // Combine the patterns and create a regex pattern
        String combinedPattern = "(" + ifPattern + "|" + elseIfPattern + ")";
        Pattern pattern = Pattern.compile(combinedPattern);

        // Use a Matcher to find matches in the code
        Matcher matcher = pattern.matcher(code);

        int conditionCount = 0;

        // Count conditions in 'if' and 'if else' statements
        while (matcher.find()) {
            String match = matcher.group(); // Get the matched statement
            int openingBraceIndex = match.indexOf("{");
            if (openingBraceIndex != -1) {
                String conditionString = match.substring(match.indexOf("(") + 1, openingBraceIndex).trim();
                // Split the conditionString by "&&" and "||" to count conditions
                String[] conditions = conditionString.split("\\s*&&\\s*|\\s*\\|\\|\\s*");
                conditionCount += conditions.length;
            }
        }

        return conditionCount;
    }

    @PostMapping("/calculate-complexity-for")
    public int countFor(@RequestBody String code) {
        int totalIterations = 0;

        // Define regular expressions for identifying for loops
        String forLoopRegex = "for\\s*\\(.*?\\)";
        Pattern pattern = Pattern.compile(forLoopRegex, Pattern.DOTALL);

        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            String forLoop = matcher.group();
            int iterations = countIterationsForLoop(forLoop);
            totalIterations += iterations;
        }

        return totalIterations;
    }

    private int countIterationsForLoop(String forLoop) {
        try {
            // Extract the loop condition
            int startIndex = forLoop.indexOf("(");
            int endIndex = forLoop.lastIndexOf(")");
            String condition = forLoop.substring(startIndex + 1, endIndex).trim();

            // Split the condition into its three parts (initialization, condition, increment)
            String[] parts = condition.split(";");
            if (parts.length != 3) {
                return -1; // Invalid for loop
            }

            String initialization = parts[0].trim();
            String conditionExpr = parts[1].trim();
            String increment = parts[2].trim();

            // Split the condition expression to get the comparison operator ("<", "<=", ">", ">=")
            String[] comparisonParts = conditionExpr.split(" ");
            if (comparisonParts.length != 3) {
                return -1; // Invalid for loop
            }

            String comparisonOperator = comparisonParts[1].trim();
            int initial = Integer.parseInt(initialization.split("=")[1].trim());
            int target = Integer.parseInt(comparisonParts[2].trim());

            // Calculate the number of iterations based on the condition
            int iterations = 0;

            if ("<".equals(comparisonOperator)) {
                iterations = target - initial;
            } else if ("<=".equals(comparisonOperator)) {
                iterations = target - initial + 1;
            } else if (">".equals(comparisonOperator)) {
                iterations = initial - target;
            } else if (">=".equals(comparisonOperator)) {
                iterations = initial - target + 1;
            }

            // Ensure non-negative result
            iterations = Math.max(iterations, 0);

            return iterations;
        } catch (NumberFormatException e) {
            // Handle number format errors
            e.printStackTrace();
        }

        return -1; // Invalid or unparsable for loop
    }
}
