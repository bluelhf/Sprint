package io.github.bluelhf.sprintpls.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ColourPicker extends GuiButton {

    private boolean mouseHeld = false;

    private int cursorX = 0, cursorY = 0, brightness = 0;

    private Color colour = new Color(0, 0, 0, 0);

    public ColourPicker(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
        throw new IllegalArgumentException("Illegal constructor");
    }

    public ColourPicker(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        throw new IllegalArgumentException("Illegal constructor");
    }

    public ColourPicker(int buttonId, int x, int y, int size) {
        super(buttonId, x, y, size + 20, size, "");
    }

    public Color getColour() {
        return colour;
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {

    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return (mouseHeld = super.mousePressed(mc, mouseX, mouseY));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        mouseHeld = false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible) return;

        if (mouseHeld) this.mouseDragged(mc, mouseX, mouseY);
        int pickerWidth = width - 20;

        // Draw colour gradient
        for (int x = xPosition; x < xPosition + pickerWidth; x++) {
            int xIndex = x - xPosition;
            drawGradientRect(
                    x,
                    yPosition,
                    x+1,
                    yPosition + height,
                    Color.HSBtoRGB(xIndex / (float) pickerWidth, 1, 1),
                    Color.HSBtoRGB(xIndex / (float) pickerWidth, 0, 1)
            );
        }


        drawGradientRect(
                xPosition + pickerWidth + 10,
                yPosition,
                xPosition + pickerWidth + 20,
                yPosition + height - 15,
                Color.HSBtoRGB(cursorX / (float) pickerWidth, 1 - (cursorY / (float) height), 1),
                Color.HSBtoRGB(cursorX / (float) pickerWidth, 1, 0)
        );

        int num = Color.HSBtoRGB(cursorX / (float) pickerWidth, 1 - (cursorY / (float) height), 1 - (brightness / (float) (height - 15)));
        this.colour = new Color(num);

        drawRect(
                xPosition + pickerWidth + 10,
                yPosition + height - 10,
                xPosition + pickerWidth + 20,
                yPosition + height,
                num
        );

        drawRect(
                xPosition + pickerWidth + 10,
                (int) Math.round(yPosition + Math.max(brightness, 1.5) - 1.5),
                xPosition + pickerWidth + 20,
                (int) Math.round(yPosition + Math.max(brightness, 1.5) + 1.5),
                0xFFECECEC
        );

        // Draw pointer ( THIS IS BAD AND OLD DO NOT DO THIS I REPEAT DO NOT DO THI- )
        GlStateManager.pushAttrib();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer ren = tessellator.getWorldRenderer();
        ren.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        int delta = 12;
        for (float i = 0; i <= 360; i += delta) {
            ren.pos(
                    (xPosition + cursorX + 1 * Math.cos(Math.toRadians(i))),
                    (yPosition + cursorY + 1 * Math.sin(Math.toRadians(i))), 1.0F
            ).color(
                    Math.min(1.5F - cursorY / (float)(height), 1) * 2,
                    Math.min(1.5F - cursorY / (float)(height), 1) * 2,
                    Math.min(1.5F - cursorY / (float)(height), 1) * 2,
                    1F
            ).endVertex();

            ren.pos(
                    (xPosition + cursorX),
                    (yPosition + cursorY),
                    1.0F
            ).color(1F, 1F, 1F, 1F).endVertex();
        }
        tessellator.draw();
        GlStateManager.popAttrib();
    }


    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX > xPosition && mouseX < xPosition + width - 20 && mouseY > yPosition && mouseY < yPosition + height) {
            cursorX = mouseX - xPosition;
            cursorY = mouseY - yPosition;

        } else if (mouseX > xPosition + width - 10 && mouseX < xPosition + width && mouseY > yPosition && mouseY < yPosition + height - 15) {
            brightness = mouseY - yPosition;
        }
    }
}
