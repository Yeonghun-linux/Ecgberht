package bwem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

class AsserterTest {
    private Asserter asserter;
    private OutputStream outputStream;

    @BeforeEach
    void setUp() {
        asserter = new Asserter();
        outputStream = new ByteArrayOutputStream();
        asserter.setFailOutputStream(outputStream);
    }

    @Test
    void throwIllegalStateException_FailOnErrorTrue_ExceptionThrown() {
        asserter.setFailOnError(true);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            asserter.throwIllegalStateException("Test Exception");
        });
    }
}
