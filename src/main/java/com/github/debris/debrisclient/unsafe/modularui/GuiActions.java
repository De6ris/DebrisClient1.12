package com.github.debris.debrisclient.unsafe.modularui;

import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.github.debris.debrisclient.event.malilib.InputListener;

public class GuiActions {
    public static class MousePressed implements IGuiAction.MousePressed {
        @Override
        public boolean press(int button) {
            return InputListener.Instance.handleButtonDown(button);
        }
    }

    public static class MouseReleased implements IGuiAction.MouseReleased {
        @Override
        public boolean release(int button) {
            return InputListener.Instance.handleButtonUp(button);
        }
    }

    public static class MouseDrag implements IGuiAction.MouseDrag {
        @Override
        public boolean drag(int button, long timeSinceClick) {
            InputListener.Instance.onMouseMoved();
            return false;
        }
    }

    public static class MouseScroll implements IGuiAction.MouseScroll {
        @Override
        public boolean scroll(ModularScreen.UpOrDown direction, int amount) {
            return InputListener.Instance.handleScroll(direction.isUp(), amount);
        }
    }
}
