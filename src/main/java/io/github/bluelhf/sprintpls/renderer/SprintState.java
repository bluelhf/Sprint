package io.github.bluelhf.sprintpls.renderer;

import io.github.bluelhf.sprintpls.SprintPls;
import net.minecraft.client.Minecraft;

public enum SprintState {
    VANILLA("Sprinting (Vanilla)"),
    SPRINT_PLS("Sprinting (SprintPls)"),
    NONE("<3");

    private final String defaultText;

    SprintState(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public static SprintState getClientState() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null) return NONE;
        if (minecraft.thePlayer == null) return NONE;
        if (SprintPls.INSTANCE.shouldSprint) return SPRINT_PLS;
        if (minecraft.thePlayer.isSprinting()) return VANILLA;

        return NONE;
    }
}
