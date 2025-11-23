package com.github.debris.debrisclient.unsafe.windows;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public class WindowsImManager {
    private static final int WM_INPUTLANGCHANGEREQUEST = 0x0050;
    private static final int IME_CHINESE = 0x0804;
    private static final int IME_ENGLISH = 0x0409;

    public static final boolean VALID;

    private static boolean status = true;

    private static native boolean ImmGetConversionStatus(WinNT.HANDLE himc, int lpdw, int lpdw2);

    private static native WinNT.HANDLE ImmGetContext(WinDef.HWND hwnd);

    private static native WinNT.HANDLE ImmAssociateContext(WinDef.HWND hwnd, WinNT.HANDLE himc);

    private static native boolean ImmReleaseContext(WinDef.HWND hwnd, WinNT.HANDLE himc);

    private static native WinNT.HANDLE ImmCreateContext();

    private static native boolean ImmDestroyContext(WinNT.HANDLE himc);

    static {
        boolean temp = false;
        if (Platform.isWindows()) {
            try {
                Native.register("imm32");
                temp = true;
            } catch (Throwable e) {
            }
        }

        VALID = temp;
    }

    private static final User32 u = User32.INSTANCE;

    public static void makeOn() {
        if (status) {
            return;
        }
        status = true;
        WinDef.HWND hwnd = u.GetForegroundWindow();
        WinNT.HANDLE himc = ImmGetContext(hwnd);
        if (himc == null) {
            himc = ImmCreateContext();
            ImmAssociateContext(hwnd, himc);
        }
        ImmReleaseContext(hwnd, himc);
    }

    public static void makeOff() {
        if (!status) {
            return;
        }
        status = false;
        WinDef.HWND hwnd = u.GetForegroundWindow();
        WinNT.HANDLE himc = ImmAssociateContext(hwnd, null);
        if (himc != null) {
            ImmDestroyContext(himc);
        }
        ImmReleaseContext(hwnd, himc);
    }

    public static void switchToChinese() {
        WinDef.HWND hwnd = u.GetForegroundWindow();
        WinDef.WPARAM wParam = new WinDef.WPARAM(IME_CHINESE);
        WinDef.LPARAM lParam = new WinDef.LPARAM(0);
        u.PostMessage(hwnd, WM_INPUTLANGCHANGEREQUEST, wParam, lParam);
    }

    public static void switchToEnglish() {
        WinDef.HWND hwnd = u.GetForegroundWindow();
        WinDef.WPARAM wParam = new WinDef.WPARAM(IME_ENGLISH);
        WinDef.LPARAM lParam = new WinDef.LPARAM(0);
        u.PostMessage(hwnd, WM_INPUTLANGCHANGEREQUEST, wParam, lParam);
    }
}
