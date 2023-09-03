package com.example.wcc;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ComplexityCalculatorController {

    @PostMapping("/calculate-complexity")
    public int calculateComplexity(@RequestBody String code) {

        String[] lines = code.split("\n");
        int totalComplexity = 0;

        int wi = 1; // Inheritance level of statements
        int wn = 1; // Nesting level of control structure
        int w = wi * wn; // Combined weight

        for (String line : lines) {

            // Remove curly braces, brackets, and access modifiers
            String cleanedLine = line.replaceAll("[{}]", "").replaceAll("\\b(public|private|protected)\\b", "").trim();

            // Remove single-line and multi-line comments
            cleanedLine = cleanedLine.replaceAll("//.*|/\\*(.|[\\r\\n])*?\\*/", "");

            // Calculate Sj: size of the executable statement (count of tokens)
            int sj = cleanedLine.split("\\s+").length; // Use cleanedLine instead of line

            // Calculate Wt: total weight of the executable statement
            // For simplicity, assuming Wn = Wi = Wc = 1 (you can refine this logic)
            int wt = 1;

            // Calculate Cr: Recursion complexity
            int Cr = 1; // Default complexity for non-recursive statements

            // Check for recursion
            if (cleanedLine.contains("return")) {
                if (cleanedLine.contains("calculateFactorial")) {
                    Cr = 3; // Complexity for return with recursive call
                }
                // Check for additional recursive calls
                int additionalCalls = 0;
                int index = cleanedLine.indexOf("return");
                while (index != -1) {
                    additionalCalls++;
                    index = cleanedLine.indexOf("return", index + 1);
                }
                if (additionalCalls > 1) {
                    Cr += additionalCalls - 1; // Increment Cr by (additionalCalls - 1)
                }
            }

            // Calculate the total complexity using Sj, Wt, Wi, Wn, W, and Cr
            int complexity = sj * wt * wi * wn * w * Cr;

            totalComplexity += complexity;
        }
        return totalComplexity;
    }
}
///



