package com.example.wcc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RecursionCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecursionCalculator.class);

    @PostMapping("/calculate-complexity-recursion")
    public int calculateComplexity(@RequestBody String code) {
//        LOGGER.info("Received code from frontend: " + code);
        String[] lines = code.split("\n");
        int totalComplexity = 0;

        for (String line : lines) {
            // Remove curly braces, brackets, and access modifiers
            String cleanedLine = line.replaceAll("[{}]", "").replaceAll("\\b(public|private|protected)\\b", "").replaceAll("\\s+", " ").trim();

            // Remove single-line and multi-line comments
            cleanedLine = cleanedLine.replaceAll("//.*|/\\*(.|[\\r\\n])*?\\*/", "");

            LOGGER.info("Cleaned code: " + cleanedLine);

            // Calculate Cr: Recursion complexity
            int cr = 0; // Default complexity for non-recursive
            // statements

            // Check for recursion
            if (cleanedLine.contains("return")) {
                String[] returnStatements = cleanedLine.split("return");
                // The first part will be empty or whitespace, so start from index 1
                for (int i = 1; i < returnStatements.length; i++) {
                    String statement = returnStatements[i].trim();
                    // Check if the statement contains a method call
                    if (statement.matches(".*[a-zA-Z]+\\(.*\\).*")) {
                        // This is a recursive call
                        cr += 2; // Increment Cr by 2 for additional recursive calls
                    }
                }
                // Add 1 for the initial recursive call
                cr += 1;
            }

            totalComplexity += cr;
        }

        LOGGER.info("Calculated complexity: " + totalComplexity); // Log the calculated complexity

        return totalComplexity;
    }

}

//Guidline
//It assigns a complexity of 1 for the initial recursive call and increments it by 2 for each additional recursive call in a line,
// resulting in a total complexity score for the entire input code.


// sample code
/*public class Fibonacci {
    public static int fibonacci(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}
*/
