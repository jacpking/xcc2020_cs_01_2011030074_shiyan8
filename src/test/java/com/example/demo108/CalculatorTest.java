package com.example.demo108;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.demo108.ex.Error;

public class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 3, 5",
            "5, 5, 10",
            "10, 0, 10"
    })
    void testAdd(int num1, int num2, int expectedResult) {
        int result = calculator.add(num1, num2);
        assertEquals(expectedResult, result, "Addition result is incorrect");
    }
}
