package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.converters.LongConverter;
import com.eden.orchid.api.converters.NumberConverter;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.converters.BaseConverterTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class BooleanOptionExtractorTest extends BaseConverterTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestClass1 { @Option @BooleanDefault(true) public boolean testValue; }
    public static class TestClass2 { @Option @BooleanDefault(true) public Boolean testValue; }
    public static class TestClass3 { @Option                       public boolean testValue; }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    @BeforeEach
    public void setupTest() {
        StringConverter stringConverter = new StringConverter(new HashSet<>());
        LongConverter longConverter = new LongConverter(stringConverter);
        DoubleConverter doubleConverter = new DoubleConverter(stringConverter);
        NumberConverter numberConverter = new NumberConverter(longConverter, doubleConverter);
        BooleanConverter booleanConverter = new BooleanConverter(stringConverter, numberConverter);

        Set<OptionExtractor> extractors = new HashSet<>();
        extractors.add(new BooleanOptionExtractor(booleanConverter));

        setupTest(extractors);
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("getOptionsArguments")
    public void testExtractOption(
            final Object underTest,
            final String optionName,
            final Object sourceValue,
            final Object expectedOriginalValue,
            final Object expectedExtractedValue) throws Throwable {
        super.testExtractOption(
                underTest,
                optionName,
                sourceValue,
                expectedOriginalValue,
                expectedExtractedValue
        );
    }

    static Stream<Arguments> getOptionsArguments() {
        return Stream.of(
                Arguments.of(new TestClass1(), "testValue", 0,                false, false),
                Arguments.of(new TestClass1(), "testValue", 1,                false, true),
                Arguments.of(new TestClass1(), "testValue", 1.1,              false, true),
                Arguments.of(new TestClass1(), "testValue", true,             false, true),
                Arguments.of(new TestClass1(), "testValue", false,            false, false),
                Arguments.of(new TestClass1(), "testValue", "true",           false, true),
                Arguments.of(new TestClass1(), "testValue", "false",          false, false),
                Arguments.of(new TestClass1(), "testValue", new JSONObject(), false, false),
                Arguments.of(new TestClass1(), "testValue", "null",           false, false),
                Arguments.of(new TestClass1(), "testValue", null,             false, true),
                Arguments.of(new TestClass1(), "testValue", "_nullValue",     false, true),

                Arguments.of(new TestClass2(), "testValue", 0,                null, false),
                Arguments.of(new TestClass2(), "testValue", 1,                null, true),
                Arguments.of(new TestClass2(), "testValue", 1.1,              null, true),
                Arguments.of(new TestClass2(), "testValue", true,             null, true),
                Arguments.of(new TestClass2(), "testValue", false,            null, false),
                Arguments.of(new TestClass2(), "testValue", "true",           null, true),
                Arguments.of(new TestClass2(), "testValue", "false",          null, false),
                Arguments.of(new TestClass2(), "testValue", new JSONObject(), null, false),
                Arguments.of(new TestClass2(), "testValue", "null",           null, false),
                Arguments.of(new TestClass2(), "testValue", null,             null, true),
                Arguments.of(new TestClass2(), "testValue", "_nullValue",     null, true),

                Arguments.of(new TestClass3(), "testValue", 0,                false, false),
                Arguments.of(new TestClass3(), "testValue", 1,                false, true),
                Arguments.of(new TestClass3(), "testValue", 1.1,              false, true),
                Arguments.of(new TestClass3(), "testValue", true,             false, true),
                Arguments.of(new TestClass3(), "testValue", false,            false, false),
                Arguments.of(new TestClass3(), "testValue", "true",           false, true),
                Arguments.of(new TestClass3(), "testValue", "false",          false, false),
                Arguments.of(new TestClass3(), "testValue", new JSONObject(), false, false),
                Arguments.of(new TestClass3(), "testValue", "null",           false, false),
                Arguments.of(new TestClass3(), "testValue", null,             false, false),
                Arguments.of(new TestClass3(), "testValue", "_nullValue",     false, false)
        );
    }

}