package com.example.wcc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayDeclarationCalculatorTest {

    private ArrayDeclarationCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new ArrayDeclarationCalculator();
    }
    @Test
    public void testCalculateArrayDeclarationComplexity_WithEmptyCode() {
        String code = "";
        int complexity = calculator.calculateArrayDeclarationComplexity(code);
        assertEquals(0, complexity);
    }

    @Test
    public void testCalculateArrayDeclarationComplexity_WithSingleDimensionArray() {
        String code = "int[] arr = new int[5];";
        int complexity = calculator.calculateArrayDeclarationComplexity(code);
        assertEquals(5, complexity);
    }

    @Test
    public void testCalculateArrayDeclarationComplexity_WithMultiDimensionArray() {
        String code = "int[][] arr = new int[5][10];";
        int complexity = calculator.calculateArrayDeclarationComplexity(code);
        assertEquals(30, complexity);
    }
}
