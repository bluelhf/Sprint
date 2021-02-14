package io.github.bluelhf.sprintpls.io;

import io.github.bluelhf.sprintpls.renderer.UserInterface;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Configurator {

    private final UserInterface.Model target;
    private final Configuration config;

    public Configurator(Configuration config, UserInterface.Model target) {
        this.config = config;
        this.target = target;
        load();
    }

    public void load() {
        config.load();
        target.hue = config.getFloat("hue", CATEGORY_GENERAL, 1, 0, 1, "The indicator colour's hue");
        target.saturation = config.getFloat("saturation", CATEGORY_GENERAL, 1, 0, 1, "The indicator colour's saturation");
        target.brightness = config.getFloat("brightness", CATEGORY_GENERAL, 1, 0, 1, "The indicator colour's brightness");

        target.chroma = config.getBoolean("isChroma", CATEGORY_GENERAL, true, "Whether Chroma is enabled or not.");
        target.chromaSpeed = config.getFloat("chromaSpeed", CATEGORY_GENERAL, 50, 0, 100, "How fast the chroma should be.");
        target.dropShadow = config.getBoolean("dropShadow", CATEGORY_GENERAL, true, "Whether the indicator has a drop shadow or not.");

        target.viewX = config.getInt("viewX", CATEGORY_GENERAL, 10, 0, Minecraft.getMinecraft().displayWidth, "The x-coordinate of the indicator.");
        target.viewY = config.getInt("viewY", CATEGORY_GENERAL, 10, 0, Minecraft.getMinecraft().displayHeight, "The y-coordinate of the indicator.");
    }


    public void save() {
        config.get(CATEGORY_GENERAL, "hue", 1).set(target.hue);
        config.get(CATEGORY_GENERAL, "saturation", 1).set(target.saturation);
        config.get(CATEGORY_GENERAL, "brightness", 1).set(target.brightness);

        config.get(CATEGORY_GENERAL, "isChroma", 1).set(target.chroma);
        config.get(CATEGORY_GENERAL, "chromaSpeed", 1).set(target.chromaSpeed);
        config.get(CATEGORY_GENERAL, "dropShadow", 1).set(target.dropShadow);

        config.get(CATEGORY_GENERAL, "viewX", 10).set(target.viewX);
        config.get(CATEGORY_GENERAL, "viewY", 10).set(target.viewY);
        config.save();
    }
}
