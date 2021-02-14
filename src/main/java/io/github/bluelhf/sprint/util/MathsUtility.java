package io.github.bluelhf.sprint.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MathsUtility {
    public static Area getHotbarBounds() {
        ScaledResolution scaler = new ScaledResolution(Minecraft.getMinecraft());
        return new Area(
                scaler.getScaledWidth() / 2 - 91,
                scaler.getScaledHeight() - 22,
                scaler.getScaledWidth() / 2 + 91,
                scaler.getScaledHeight()
        );
    }

    public static int getScaledHeight() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }

    public static int getScaledWidth() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    public static int getHeight() {
        return Minecraft.getMinecraft().displayHeight;
    }

    public static int getWidth() {
        return Minecraft.getMinecraft().displayWidth;
    }

}
