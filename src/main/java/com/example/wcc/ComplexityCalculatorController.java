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

        for (String line : lines) {

            // Remove curly braces, brackets, and access modifiers
            String cleanedLine = line.replaceAll("[{}]", "").replaceAll("\\b(public|private|protected)\\b", "").trim();

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
            int wi = 0; // Default weight for statements inside derived classes

            if (cleanedLine.contains("FirstDerivedClass")) {
                wi = 1; // Set the weight to 1 for statements inside the first derived class
            } else if (cleanedLine.contains("SecondDerivedClass")) {
                wi = 2; // Set the weight to 2 for statements inside the second derived class
            }

            int ci = 0; // Default value for system input output statements
            if(cleanedLine.contains("System.out.println") || cleanedLine.contains("System.out.print")){
                ci = 1;  // set weight 1 if there is a System.out.println statement
            }else{
                ci =0;
            }


            // Calculate Cr: Recursion complexity
            int cr = 1; // Default complexity for non-recursive statements

            // Check for recursion
            if (cleanedLine.contains("return")) {
                if (cleanedLine.contains("calculateFactorial")) {
                    cr = 3; // Complexity for return with recursive call
                }
                // Check for additional recursive calls
                int additionalCalls = 0;
                int index = cleanedLine.indexOf("return");
                while (index != -1) {
                    additionalCalls++;
                    index = cleanedLine.indexOf("return", index + 1);
                }
                if (additionalCalls > 1) {
                    cr += additionalCalls - 1; // Increment Cr by (additionalCalls - 1)
                }
            }

            // Calculate W: Combined weight
            int w = wn + wc + wi;

            // Calculate the total complexity using Sj, W, and Cr
            int complexity = (sj * w) + (cr + ci);

            totalComplexity += complexity;
        }
        return totalComplexity;
    }
}
