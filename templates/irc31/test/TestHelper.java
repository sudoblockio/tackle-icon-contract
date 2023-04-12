package io.contractdeployer.generics.irc31;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHelper {
    public static void expectErrorMessage(Executable contractCall, String errorMessage) {
        AssertionError e = Assertions.assertThrows(AssertionError.class, contractCall);
        assertEquals(errorMessage, e.getMessage());
    }
}