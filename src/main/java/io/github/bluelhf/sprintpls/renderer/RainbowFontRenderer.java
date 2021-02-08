package io.github.bluelhf.sprintpls.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RainbowFontRenderer extends FontRenderer {
    public RainbowFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
    }

    public int drawRainbowString(String text, float x, float y, int speed, int variance, boolean dropShadow) {
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
        enableAlpha();
        int i;

        if (dropShadow) {
            i = this.renderRainbowString(text, x + 1.0F, y + 1.0F, speed, variance, true);
            i = Math.max(i, this.renderRainbowString(text, x, y, speed, variance, false));
        } else {
            i = this.renderRainbowString(text, x, y, speed, variance, false);
        }
        GL11.glPopAttrib();
        Minecraft.getMinecraft().renderEngine.bindTexture(GuiIngameForge.icons);
        return i;
    }

    private int renderRainbowString(String text, float x, float y, int speed, int variance, boolean dropShadow) {

        if (text == null) {
            return 0;
        } else {

            this.posX = x;
            this.posY = y;
            this.renderRainbowStringAtPos(text, speed, variance, dropShadow);

            return (int) this.posX;
        }
    }

    private void renderRainbowStringAtPos(String text, int speed, int variance, boolean shadow) {
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

            float f1 = j == -1 ? 0.5f : 1f;
            boolean flag = (c0 == 0 || j == -1) && shadow;

            if (flag) {
                this.posX -= f1;
                this.posY -= f1;
            }

            float f = this.renderRainbowChar(c0, speed, variance, shadow);

            if (flag) {
                this.posX += f1;
                this.posY += f1;
            }

            doDraw(f);
        }
    }

    private float renderRainbowChar(char ch, int speed, int variance, boolean shadow) {
        if (ch == 160) return 4.0F; // forge: display nbsp as space. MC-2595
        if (ch == ' ') {
            return 4.0F;
        } else {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(ch);
            if (i != -1) {
                return this.renderRainbowDefaultChar(i, speed, variance, shadow);
            } else {
                throw new RuntimeException("Unrecognized char: " + ch);
            }
        }
    }

    protected float renderRainbowDefaultChar(int ch, int speed, int variance, boolean shadow) {

        float charXPos = ch % 16 * 8f;
        float charYPos = Math.floorDiv(ch, 16) * 8f;

        bindTexture(this.locationFontTexture);
        int charWidth = this.charWidth[ch];
        float width = (float) charWidth - 0.01F;

        double off = System.currentTimeMillis();
        float wrap = speed * 10;


        Color prev = new Color(Color.HSBtoRGB((float) (off % wrap) / wrap, 1, 1));
        Color fut = new Color(Color.HSBtoRGB((float) ((off + (variance)) % wrap) / wrap, 1, 1));

        if (shadow) {
            prev = new Color(
                    Math.max(0, prev.getRed() - 127),
                    Math.max(0, prev.getGreen() - 127),
                    Math.max(0, prev.getBlue() - 127),
                    Math.max(0, prev.getAlpha() - 127)
            );
            fut = new Color(
                    Math.max(0, fut.getRed() - 127),
                    Math.max(0, fut.getGreen() - 127),
                    Math.max(0, fut.getBlue() - 127),
                    Math.max(0, fut.getAlpha() - 127)
            );
        }

        float prevRed = prev.getRed(), prevGreen = prev.getGreen(), prevBlue = prev.getBlue(), prevAlpha = prev.getAlpha();
        float futRed = fut.getRed(), futGreen = fut.getGreen(), futBlue = fut.getBlue(), futAlpha = fut.getAlpha();

        prevRed /= 255F; prevGreen /= 255F; prevBlue /= 255F; prevAlpha /= 255F;
        futRed /= 255F; futGreen /= 255F; futBlue /= 255F; futAlpha /= 255F;


        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        GlStateManager.color(prevRed, prevGreen, prevBlue, prevAlpha);
        GL11.glTexCoord2f(charXPos / 128.0F, (charYPos + 0F) / 128.0F); // 0 0
        GL11.glVertex3f(this.posX, this.posY + 0F, 0.0F);

        GlStateManager.color(futRed, futGreen, futBlue, futAlpha);
        GL11.glTexCoord2f(charXPos / 128.0F, (charYPos + 7.99F) / 128.0F); // 0 .5
        GL11.glVertex3f(this.posX, this.posY + 7.99F, 0.0F);

        GlStateManager.color(futRed, futGreen, futBlue, futAlpha);
        GL11.glTexCoord2f((charXPos + width - 1.0F) / 128.0F, (charYPos + 7.99F) / 128.0F); // 1 .5
        GL11.glVertex3f(this.posX + width - 1.0F, this.posY + 7.99F, 0.0F);

        GlStateManager.color(prevRed, prevGreen, prevBlue, prevAlpha);
        GL11.glTexCoord2f((charXPos + width - 1.0F) / 128.0F, (charYPos + 0F) / 128.0F); // 1 0
        GL11.glVertex3f(this.posX + width - 1.0F, this.posY + 0F, 0.0F);

        GL11.glEnd();
        GlStateManager.shadeModel(GL11.GL_FLAT);

        return (float) charWidth;
    }


}
