package com.example.wcc;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class ComplexityCalculatorController {

    @PostMapping("/calculate-complexity")
    public int calculateComplexity(@RequestBody String code) {

        String[] lines = code.split("\n");
        int totalComplexity = 0;
        int totalIterations = 0;

        for (String line : lines) {

            List<String> extractedForLoops = extractForLoops(line);

            for (String forLoop : extractedForLoops) {
                int iterations = countIterationsForLoop(forLoop);
                totalIterations += iterations; // Add iterations to totalIterations
            }

            // Remove curly braces, brackets, and access modifiers
            String cleanedLine = line.replaceAll("[{}]", "").replaceAll("\\b(public|private|protected)\\b", "").replaceAll("\\s+", " ").trim();


            // Remove single-line and multi-line comments
            cleanedLine = cleanedLine.replaceAll("//.*|/\\*(.|[\\r\\n])*?\\*/", "");

            // Calculate Sj: size of the executable statement (count of tokens)
            int sj = cleanedLine.split("\\s+").length;

            // Calculate Wc: Type of control structure
            int wc = 0; // Default weight for sequential code

            if (cleanedLine.contains("if") || cleanedLine.contains("else if") || cleanedLine.contains("else")) {
                wc = 1; // Branching control structure
            } else if (cleanedLine.contains("for") || cleanedLine.contains("while") || cleanedLine.contains("do")) {
                wc = 2; // Iterative control structure
            } else if (cleanedLine.contains("switch")) {
                int n = cleanedLine.split("case").length - 1;
                wc = n; // Weight for switch cases
            }

            // Calculate Wn: Nesting level of control structure
            int nestingLevel = 0; // Reset nesting level for each line
            if (cleanedLine.contains("{")) {
                nestingLevel++;
            } else if (cleanedLine.contains("}")) {
                nestingLevel--;
            }

            int wn = 0; // Default weight for sequential statements
            if (nestingLevel == 0) {
                wn = 1; // Statements inside the outermost level of control structures
            } else if (nestingLevel == 1) {
                wn = 2; // Statements inside the second level of control structures
            } else if (nestingLevel == 2) {
                wn = 3; // Statements inside the third level of control structures
            }



            // Calculate Wi: Inheritance level of statements
            int wi = 1; // Default weight for statements inside derived classes

            if (cleanedLine.contains("FirstDerivedClass")) {
                wi = 2; // Set the weight to 1 for statements inside the first derived class
            } else if (cleanedLine.contains("SecondDerivedClass")) {
                wi = 3; // Set the weight to 2 for statements inside the second derived class
            }

            int ci = 0; // Default value for system input output statements
            if(cleanedLine.contains("System.out.println") || cleanedLine.contains("System.out.print")){
                ci = 1;  // set weight 1 if there is a System.out.println statement
            }else{
                ci =0;
            }


            // Calculate Cr: Recursion complexity
            int cr = 0; // Default complexity for non-recursive statements

            // Check for recursion
            if (cleanedLine.contains("return")) {
                // Check if the line contains a method call and an assignment to handle recursion
                if (cleanedLine.matches(".*=.*\\s+[a-zA-Z]+\\(.*\\).*")) {
                    cr = 1; // Assign a complexity of 1 for recursive calls
                }
                // Check for additional recursive calls
                int additionalCalls = 0;
                int index = cleanedLine.indexOf("return");
                while (index != -1) {
                    additionalCalls++;
                    index = cleanedLine.indexOf("return", index + 1);
                }
                if (additionalCalls > 1) {
                    cr += (additionalCalls - 1) * 2; // Increment Cr by (additionalCalls - 1) * 2 for additional recursive calls
                }

            }


            //Calculate Ct : try-catch complexity --Start of the code
            int tryCount = 0;
            int catchCount = 0;
            int ct = tryCount + catchCount;
            boolean insideTry = false;

            // Check for 'try' and 'catch' keywords
            if (line.contains("try")) {
                tryCount++;
                if (insideTry) {
                    tryCount++;
                }
                insideTry = true;
            }
            if (line.contains("catch")) {
                catchCount++;
                if (insideTry) {
                    catchCount++;
                }
                insideTry = false;
            }

            // If 'catch' follows 'try' in the same line, consider it as a single operator
            if (line.contains("try") && line.contains("catch")) {
                tryCount--;
                catchCount--;
            }
            // --end of the try catch code



            //check for 'if else'
            String ifPattern = "\\bif\\s*\\(.+?\\)\\s*\\{";
            String elseIfPattern = "\\belse\\s+if\\s*\\(.+?\\)\\s*\\{";

            // Combine the patterns and create a regex pattern
            String combinedPattern = "(" + ifPattern + "|" + elseIfPattern + ")";
            Pattern pattern = Pattern.compile(combinedPattern);

            // Use a Matcher to find matches in the code
            Matcher matcher = pattern.matcher(code);

            int cc = 0;

            // Count conditions in 'if' and 'if else' statements
            while (matcher.find()) {
                String match = matcher.group(); // Get the matched statement
                int openingBraceIndex = match.indexOf("{");
                if (openingBraceIndex != -1) {
                    String conditionString = match.substring(match.indexOf("(") + 1, openingBraceIndex).trim();
                    // Split the conditionString by "&&" and "||" to count conditions
                    String[] conditions = conditionString.split("\\s*&&\\s*|\\s*\\|\\|\\s*");
                    cc += conditions.length;
                }
            }

            //end of if else



            //check array declaration
            // Check if the line contains array declarations and calculate Nd value
            int ca = 0;

            if (cleanedLine.contains("=") && cleanedLine.contains("new")) {
                int ndValue = calculateNdValue(cleanedLine);

                int DdSum = 0;

                // Extract and process array sizes for multi-dimensional arrays
                int[] sizes = getArraySizes(cleanedLine);
                for (int size : sizes) {
                    DdSum += size;
                }

                System.out.println("Sj values = " + sj);
                System.out.println("Nd Value = " + ndValue);
                System.out.println("Dd Sum = " + DdSum);

                ca += ndValue * DdSum;
            }

            //end of array declaration




            // Calculate W: Combined weight
            int w = wn + wc + wi;

            // Calculate the total complexity using Sj, W, and Cr, Ci, Ct, ca
            int complexity = (sj * w) + (cr + ci + ct + cc + ca + totalIterations);


            totalComplexity += complexity;
        }
        return totalComplexity;

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

    public static List<String> extractForLoops(String line) {
        List<String> extractedForLoops = new ArrayList<>();

        // Define a regular expression for identifying for loops
        String forLoopRegex = "for\\s*\\(.*?\\)";
        Pattern pattern = Pattern.compile(forLoopRegex, Pattern.DOTALL);

        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            String forLoop = matcher.group();
            extractedForLoops.add(forLoop);
        }

        return extractedForLoops;
    }

    public int calculateNdValue(String cleanedLine) {
        int ndValue = 0;

        // Count the occurrences of "[]" in the cleanedLine
        int countBrackets = cleanedLine.split("\\[]").length - 1;

        System.out.println("Count Brackets: " + countBrackets);

        if(countBrackets == 2){
            ndValue = 1;
        }else if(countBrackets == 3){
            ndValue = 2;
        } else if (countBrackets == 4) {
            ndValue = 3;
        }

        return ndValue;
    }

    public int[] getArraySizes(String cleanedLine) {
        // Extract and parse array sizes from the cleaned line
        // Assuming the sizes are specified as integers within square brackets []
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
        Matcher matcher = pattern.matcher(cleanedLine);
        int[] sizes = new int[3]; // Assuming a maximum of two dimensions

        int dimension = 0;

        while (matcher.find() && dimension < 3) {
            sizes[dimension] = Integer.parseInt(matcher.group(1));
            dimension++;
        }

        System.out.println("Size 1 length: " + sizes[0]);
        System.out.println("Size 2 length: " + sizes[1]);

        return sizes;
    }





}
