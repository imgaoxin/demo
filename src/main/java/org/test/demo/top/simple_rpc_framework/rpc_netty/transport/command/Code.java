package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command;

import java.util.HashMap;
import java.util.Map;

public enum Code {
    SUCCESS(0, "SUCCESS"),
    UNKNOWN_ERROR(-1, "UNKNOWN_ERROR"),
    NO_PROVIDER(-2, "NO_PROVIDER");

    private static Map<Integer, Code> codes = new HashMap<>();
    private int code;
    private String message;

    static {
        for (Code code : Code.values()) {
            codes.put(code.code, code);
        }
    }

    Code(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Code valueOf(int code) {
        return codes.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getMessage(Object... args) {
        if (args.length < 1) {
            return message;
        }
        return String.format(message, args);
    }
}