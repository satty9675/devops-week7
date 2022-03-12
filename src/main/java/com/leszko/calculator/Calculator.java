package com.leszko.calculator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Added javadoc for Calculator.java.
 */
@Service
public class Calculator {
        final static int UML_NUMBER = 3;
	@Cacheable("sum")
	public int sum(int a, int b) {
		return a + b;
	}
}
