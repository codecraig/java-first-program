package com.h2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.platform.commons.util.ReflectionUtils.*;

public class Module3_Test {
    private final String classToFind = "com.h2.SavingsCalculator";
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private static Optional<Class<?>> getClass(final String className) {
        Try<Class<?>> aClass = tryToLoadClass(className);
        return aClass.toOptional();
    }

    public Optional<Class<?>> getSavingsClass() {
        return getClass(classToFind);
    }

    @Test
    public void m03_01_testSavingsCalculatorExists() {
        Optional<Class<?>> maybeClass = getSavingsClass();
        assertTrue(maybeClass.isPresent(), classToFind + " must be created.");
    }

    @Test
    public void m3_02_testExistenceOfPrivateFields() {
        final Optional<Class<?>> maybeSavingsCalculator = getSavingsClass();
        assertTrue(maybeSavingsCalculator.isPresent(), classToFind + " must exist");
        final Class<?> savingsCalculator = maybeSavingsCalculator.get();
        final Set<String> fieldNames = new HashSet<>(Arrays.asList("credits", "debits"));

        final Field[] declaredFields = savingsCalculator.getDeclaredFields();
        assertEquals(2, declaredFields.length, "2 fields (credits and debits) should be available in " + classToFind);

        for (final Field field : declaredFields) {
            assertTrue(isPrivate(field), field.getName() + " should be declared private");
            assertEquals(float[].class, field.getType(), field.getName() + " should be of type 'float[]'");
            assertTrue(fieldNames.contains(field.getName()), field.getName() + " is not a valid name. It should be either 'credits' or 'debits'");
        }
    }

    @Test
    public void m3_03_testConstructor() {
        final Optional<Class<?>> maybeSavingsCalculator = getSavingsClass();
        assertTrue(maybeSavingsCalculator.isPresent(), classToFind + " must exist");
        final Class<?> savingsCalculator = maybeSavingsCalculator.get();
        final Constructor<?>[] constructors = savingsCalculator.getDeclaredConstructors();

        assertEquals(1, constructors.length, classToFind + " should have 1 constructor");

        final Constructor<?> constructor = constructors[0];
        assertTrue(isPublic(constructor), "constructor must be declared 'public'");
        assertEquals(2, constructor.getParameterCount(), "Constructor should have 2 parameters");

        for (final Parameter parameter : constructor.getParameters()) {
            assertEquals(float[].class, parameter.getType(), "Constructor parameter should be of type 'float[]'");
        }
    }

    @Test
    public void m3_04_testFieldsValueSetWhenConstructorCalled() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final Optional<Class<?>> maybeSavingsCalculator = getSavingsClass();
        assertTrue(maybeSavingsCalculator.isPresent(), classToFind + " must exist");
        final Class<?> savingsCalculator = maybeSavingsCalculator.get();
        final Constructor<?>[] constructors = savingsCalculator.getDeclaredConstructors();

        assertEquals(1, constructors.length, classToFind + " should have 1 constructor");

        float[] credits = new float[]{10.0f, 20.0f};
        float[] debits = new float[]{5.0f};
        final Constructor<?> constructor = constructors[0];
        Object instance = constructor.newInstance(credits, debits);

        final Field[] fields = savingsCalculator.getDeclaredFields();

        for (final Field field : fields) {
            field.setAccessible(true);
            float[] fieldValues = (float[]) field.get(instance);
            if (field.getName().equals("credits")) {
                assertArrayEquals(credits, fieldValues, "credits parameter should set the value in class field name 'credits'");
            } else if (field.getName().equals("debits")) {
                assertArrayEquals(debits, fieldValues, "debits parameter should set the value in class field name 'debits'");
            }
        }
    }

    @Test
    public void m3_05_testSumOfCreditsExists() {
        final String methodName = "sumOfCredits";

        final Optional<Class<?>> maybeSavingsCalculator = getSavingsClass();
        assertTrue(maybeSavingsCalculator.isPresent(), classToFind + " must exist");
        final Class<?> savingsCalculator = maybeSavingsCalculator.get();

        final Method[] methods = savingsCalculator.getDeclaredMethods();
        final List<Method> filteredMethod = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

        assertEquals(1, filteredMethod.size(), classToFind + " should contain a method called '" + methodName + "'");

        final Method sumOfCredits = filteredMethod.get(0);
        assertTrue(isPrivate(sumOfCredits), methodName + " must be declared as 'private'");
        assertEquals(float.class, sumOfCredits.getReturnType(), methodName + " method must return a value of type 'float'");
    }

    @Test
    public void m3_06_testSumOfCreditsWorksCorrectly() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final Optional<Class<?>> maybeSavingsCalculator = getSavingsClass();
        assertTrue(maybeSavingsCalculator.isPresent(), classToFind + " must exist");
        final Class<?> savingsCalculator = maybeSavingsCalculator.get();
        final Constructor<?>[] constructors = savingsCalculator.getDeclaredConstructors();

        assertEquals(1, constructors.length, classToFind + " should have 1 constructor");

        float[] credits = new float[]{10.0f, 20.0f};
        float[] debits = new float[]{5.0f};
        final Constructor<?> constructor = constructors[0];
        Object instance = constructor.newInstance(credits, debits);

        final String methodName = "sumOfCredits";
        final Method[] methods = savingsCalculator.getDeclaredMethods();
        final List<Method> filteredMethod = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        assertEquals(1, filteredMethod.size(), classToFind + " should contain a method called '" + methodName + "'");
        Method method = filteredMethod.get(0);
        final float result = (float) invokeMethod(method, instance);

        assertEquals(30.0f, result, methodName + " is not returning sum of credits.");
    }

    @Test
    public void m3_07_testSumOfDebitsExists() {
        final String methodName = "sumOfDebits";

        final Optional<Class<?>> maybeSavingsCalculator = getSavingsClass();
        assertTrue(maybeSavingsCalculator.isPresent());
        final Class<?> savingsCalculator = maybeSavingsCalculator.get();

        final Method[] methods = savingsCalculator.getDeclaredMethods();
        final List<Method> filteredMethod = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

        assertEquals(1, filteredMethod.size(), classToFind + " should contain a method called '" + methodName + "'");

        final Method sumOfCredits = filteredMethod.get(0);
        assertTrue(isPrivate(sumOfCredits), methodName + " must be declared as 'private'");
        assertEquals(float.class, sumOfCredits.getReturnType(), methodName + " method must return a value of type 'float'");
    }

}
