package com.example.wcc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

public class CompoundConditionalCalculatorTest {

    private CompoundConditionalCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new CompoundConditionalCalculator();
    }

    @Test
    public void testCountFor_NoForLoops() {
        String code = "public class Test { public static void main(String[] args) { int x = 5; System.out.println(x); } }";
        int iterations = calculator.countFor(code);
        assertEquals(0, iterations);
    }

    @Test
    public void testCountFor_SingleForLoop() {
        String code = "public class Test { public static void main(String[] args) { for (int i = 0; i < 10; i++) { System.out.println(i); } } }";
        int iterations = calculator.countFor(code);
        assertEquals(10, iterations);
    }

// @Test
// public void testCountFor_NestedForLoops() {
// String code = "public class Test { public static void main(String[] args) { for (int i = 0; i < 5; i++) { for (int j = 0; j < 3; j++) { System.out.println(i + j); } } } }";
// int iterations = calculator.countFor(code);
// assertEquals(15, iterations);
// }

// @Test
// public void testCountFor_InvalidForLoop() {
// String code = "public class Test { public static void main(String[] args) { for (int i = 0; i < 5; i++) { System.out.println(i); }";
// int iterations = calculator.countFor(code);
// assertEquals(0, iterations);
// }
}
