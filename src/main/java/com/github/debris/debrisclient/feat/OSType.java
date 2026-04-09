package com.github.debris.debrisclient.feat;

public enum OSType {
    NONE,
    WIN_X32,
    WIN_X64,
    ;

    public boolean isWindows() {
        return this == WIN_X32 || this == WIN_X64;
    }

    public static OSType get() {
        String osName = System.getProperty("os.name").toLowerCase();
        String osArch = System.getProperty("os.arch").toLowerCase();

        if (osName.contains("windows")) {
            if (osArch.contains("64")) {
                return WIN_X64;
            } else if (osArch.contains("86")) {
                return WIN_X32;
            }
        }
        return NONE;
    }
}
