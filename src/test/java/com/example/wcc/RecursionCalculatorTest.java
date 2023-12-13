package com.example.wcc;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RecursionCalculatorTest {

    private final RecursionCalculator recursionCalculator = new RecursionCalculator();


    @Test
    public void testCalculateComplexityWithNoRecursiveCalls() {
        String code = "public class Example {\n" +
                "    public static void main(String[] args) {\n" +
                "        int x = 10;\n" +
                "        System.out.println(x);\n" +
                "    }\n" +
                "}";
        int complexity = recursionCalculator.calculateComplexity(code);
        assertEquals(0, complexity);
    }

    @Test
    public void testCalculateComplexityWithSingleRecursiveCall() {
        String code = "public static int factorial(int n) {\n" +
                "    if (n == 0) {\n" +
                "        return 1; // Complexity 1 (initial recursive call)\n" +
                "    }\n" +
                "    int result = n * factorial(n - 1); // Complexity 1 (recursive call)\n" +
                "    // System.out.println(result); // This line is removed\n" +
                "    return result; // Complexity 1 (return statement within the recursive call)\n" +
                "}\n";
        int complexity = recursionCalculator.calculateComplexity(code);
        assertEquals(2, complexity);
    }

    @Test
    public void testCalculateComplexityWithMultipleRecursiveCalls() {
        String code = "public class Example {\n" +
                "    public static int fibonacci(int n) {\n" +
                "        if (n == 0) {\n" +
                "            return 0;\n" +
                "        } else if (n == 1) {\n" +
                "            return 1;\n" +
                "        }\n" +
                "        return fibonacci(n - 1) + fibonacci(n - 2);\n" +
                "    }\n" +
                "}";
        int complexity = recursionCalculator.calculateComplexity(code);
        assertEquals(5, complexity);
    }
}
