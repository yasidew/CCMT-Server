package com.example.wcc;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RecursionCalculator {

    @PostMapping("/calculate-complexity-recursion")
    public int calculateComplexity(@RequestBody String code) {

        String[] lines = code.split("\n");
        int totalComplexity = 0;

        for (String line : lines) {

            // Remove curly braces, brackets, and access modifiers
            String cleanedLine = line.replaceAll("[{}]", "").replaceAll("\\b(public|private|protected)\\b", "").replaceAll("\\s+", " ").trim();

            // Remove single-line and multi-line comments
            cleanedLine = cleanedLine.replaceAll("//.*|/\\*(.|[\\r\\n])*?\\*/", "");

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

            totalComplexity += cr;


        }
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
