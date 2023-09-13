package com.example.wcc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TryCatchCalculatorTest {

    @Autowired
    private TryCatchCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new TryCatchCalculator();
    }

    @Test
    public void testCalculateComplexity_NoTryCatch() {
        String code = "public class Test { public static void main(String[] args) { System.out.println(\"Hello\"); } }";
        int complexity = calculator.calculateComplexity(code);
        assertEquals(0, complexity);
    }

    @Test
    public void testCalculateComplexity_SingleTryCatch() {
        String code = "public class Test { public static void main(String[] args) { try { int x = 10 / 0; } catch (ArithmeticException e) { } } }";
        int complexity = calculator.calculateComplexity(code);
        assertEquals(2, complexity);
    }
}