package io.github.bluelhf.sprintpls.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class UserInterface {

    private final Model model;
    private final View view;
    private final Controller controller;

    public UserInterface() {
        model = new Model();
        view = new View();
        controller = new Controller();
    }

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }

    public Controller getController() {
        return controller;
    }





    public class Model {
        public float chromaSpeed = 50;
        public boolean chroma = true, dropShadow = true;
        public float hue = 1, saturation = 1, brightness = 1;

    }

    public class View {

        public final RainbowFontRenderer renderer;
        private final HashMap<SprintState, String> stateTextMap = new HashMap<>();
        public int x = 10, y = 10;
        public boolean highlight = false;

        public View() {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft == null) throw new IllegalStateException("Cannot instantiate View without renderer when Minecraft is not running.");
            this.renderer = new RainbowFontRenderer(minecraft.gameSettings, new ResourceLocation("textures/font/ascii.png"), minecraft.renderEngine, false);
            this.renderer.onResourceManagerReload(null);
            MinecraftForge.EVENT_BUS.register(this);
        }

        public void setText(SprintState sprintState, String text) {
            stateTextMap.put(sprintState, text);
        }

        @SubscribeEvent
        public void render(RenderGameOverlayEvent.Post event) {
            if (renderer == null) return;
            SprintState s = SprintState.getClientState();
            int d = controller.width / 3;
            int align = 0;
            if (y < 660 || x < 500 || x > 780) {
                if (x < 1.45 * d) {
                    align = 0;
                } else if (x < 1.55 * d) {
                    align = 1;
                } else if (x < 3 * d) {
                    align = 2;
                }
            } else {
                align = x < 640 ? 2 : 0;
            }
            drawText(stateTextMap.getOrDefault(s, s.getDefaultText()), x, y, align);
        }


        public void drawText(String text, int x, int y, int align) {
            int tempX = (int) (x - renderer.getStringWidth(text) * (align / 2F));

            if (model.chroma) {
                int speed = Math.round(151 - model.chromaSpeed);
                renderer.drawRainbowString(text, tempX, y, speed, speed, model.dropShadow);
            } else {
                renderer.drawString(text, tempX, y, Color.HSBtoRGB(model.hue, model.saturation, model.brightness), model.dropShadow);
            }
            if (this.highlight) {
                int d = controller.width / 3;

                GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);

                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                GlStateManager.color(1F, 1F, 1F, 1F);

                GuiScreen.drawRect((int) (1.45 * d), 0, (int) (1.45 * d) + 1, 660, 0x1AFFFFFF);
                GuiScreen.drawRect((int) (1.55 * d), 0, (int) (1.55 * d) + 1, 660, 0x1AFFFFFF);
                GuiScreen.drawRect(500, 660, 780, 659, 0x1AFFFFFF);
                GuiScreen.drawRect(500, 660, 501, controller.height, 0x1AFFFFFF);
                GuiScreen.drawRect(640, 660, 641, controller.height, 0x1AFFFFFF);
                GuiScreen.drawRect(780, 660, 781, controller.height, 0x1AFFFFFF);

                GuiScreen.drawRect(tempX - 5, y - 5, tempX + renderer.getStringWidth(text) + 5, y + renderer.FONT_HEIGHT + 5, 0x1A2E2E2E);
                GL11.glPopAttrib();
            }
        }
    }


    public class Controller extends GuiScreen {

        private GuiSlider chromaSpeedSlider;
        private GuiButton chromaToggle;

        private ColourPicker colourPicker = new ColourPicker(3, 0, 0, 100);

        private GuiButton dropShadowToggle;

        private GuiTextField idleText;
        private GuiTextField vanillaText;
        private GuiTextField toggledText;

        private int dragMode = -1;
        private GuiButton dragModeToggle;

        @Override
        public void initGui() {
            chromaSpeedSlider = new GuiSlider(new GuiPageButtonList.GuiResponder() {
                @Override
                public void func_175321_a(int p_175321_1_, boolean p_175321_2_) {
                }

                @Override
                public void func_175319_a(int p_175319_1_, String p_175319_2_) {
                }

                @Override
                public void onTick(int id, float value) {
                    getModel().chromaSpeed = value;
                }

            }, 1, 0, 0, "chromaSpeedSlider", 0, 100, getModel().chromaSpeed, (id, name, value) -> "Speed: " + Math.round(value) + "%");
            chromaToggle = new GuiButton(2, 0, 0, "Chroma: " + (model.chroma ? "\u00a7aYES" : "\u00a7cNO"));
            // ID 3 reserved for colour picker
            dropShadowToggle = new GuiButton(4, 0, 0, "Drop Shadow: " + (model.dropShadow ? "\u00a7aYES" : "\u00a7cNO"));
            idleText = new GuiTextField(5, fontRendererObj, 0, 0, 129 /* Reserve 70 pixels for textfield name */, 20);
            idleText.setText(view.stateTextMap.getOrDefault(SprintState.NONE, SprintState.NONE.getDefaultText()));

            vanillaText = new GuiTextField(6, fontRendererObj, 0, 0, 129 /* Reserve 70 pixels for textfield name */, 20);
            vanillaText.setText(view.stateTextMap.getOrDefault(SprintState.VANILLA, SprintState.VANILLA.getDefaultText()));

            toggledText = new GuiTextField(7, fontRendererObj, 0, 0, 129 /* Reserve 70 pixels for textfield name */, 20);
            toggledText.setText(view.stateTextMap.getOrDefault(SprintState.SPRINT_PLS, SprintState.SPRINT_PLS.getDefaultText()));

            dragModeToggle = new GuiButton(8, 0, 0, "Drag Mode");

            buttonList.add(chromaSpeedSlider);
            buttonList.add(chromaToggle);
            buttonList.add(colourPicker);
            buttonList.add(dropShadowToggle);
            buttonList.add(dragModeToggle);
            super.initGui();
        }

        @Override
        public void onGuiClosed() {
            dragMode = -1;
            view.highlight = false;
            super.onGuiClosed();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            if (dragMode > 0) dragMode--;
            view.highlight = dragMode != -1;

            dragModeToggle.width = fontRendererObj.getStringWidth(dragModeToggle.displayString) + 10;
            dragModeToggle.xPosition = 0;
            dragModeToggle.yPosition = height - dragModeToggle.height;



            if (view.x > 0 && view.x < dragModeToggle.width && view.y > height - dragModeToggle.height && view.y < height) {
                dragModeToggle.xPosition = width - dragModeToggle.width;
                dragModeToggle.yPosition = 0;
            }

            if (dragMode != -1) {

                if (!dragModeToggle.isMouseOver() && Mouse.isButtonDown(0) && dragMode == 0) {
                    view.x = mouseX;
                    view.y = mouseY;
                }

                dragModeToggle.drawButton(mc, mouseX, mouseY);
                return;
            }


            drawDefaultBackground();

            int y = 10;
            chromaToggle.yPosition = y;
            y += chromaToggle.height;
            chromaToggle.xPosition = width / 2 - chromaToggle.width / 2;
            chromaSpeedSlider.visible = model.chroma;
            colourPicker.visible = !model.chroma;


            if (model.chroma) {
                chromaSpeedSlider.yPosition = (y += 5);
                chromaSpeedSlider.xPosition = width / 2 - chromaSpeedSlider.width / 2;
                y += chromaSpeedSlider.height;
            } else {
                colourPicker.yPosition = (y += 5);
                colourPicker.xPosition = width / 2 - colourPicker.width / 2;
                y += colourPicker.height;

                Color c = colourPicker.getColour();
                float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                model.hue = hsb[0];
                model.saturation = hsb[1];
                model.brightness = hsb[2];
            }
            y += 5;

            dropShadowToggle.yPosition = y;
            y += dropShadowToggle.height;
            dropShadowToggle.xPosition = width / 2 - dropShadowToggle.width / 2;

            y += 5;

            idleText.drawTextBox();
            idleText.yPosition = y;
            y += idleText.height;
            idleText.xPosition = width / 2 - idleText.width / 2 + 34;

            drawString(fontRendererObj, "Idle Text", idleText.xPosition - 69, idleText.yPosition + idleText.height / 4, 0xFFFFFFFF);

            y += 5;

            vanillaText.drawTextBox();
            vanillaText.yPosition = y;
            y += vanillaText.height;
            vanillaText.xPosition = width / 2 - vanillaText.width / 2 + 34;

            drawString(fontRendererObj, "Vanilla Text", vanillaText.xPosition - 69, vanillaText.yPosition + vanillaText.height / 4, 0xFFFFFFFF);

            y += 5;

            toggledText.drawTextBox();
            toggledText.yPosition = y;
            y += toggledText.height;
            toggledText.xPosition = width / 2 - toggledText.width / 2 + 34;

            drawString(fontRendererObj, "Toggled Text", toggledText.xPosition - 69, toggledText.yPosition + toggledText.height / 4, 0xFFFFFFFF);

            for (GuiButton b : buttonList) {
                b.drawButton(this.mc, mouseX, mouseY);
            }

        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {

            if (keyCode == 15) {
                if (toggledText.isFocused()) {
                    toggledText.setFocused(false);
                    idleText.setFocused(true);
                } else if (vanillaText.isFocused()) {
                    vanillaText.setFocused(false);
                    toggledText.setFocused(true);
                } else if (idleText.isFocused()) {
                    idleText.setFocused(false);
                    vanillaText.setFocused(true);
                } else {
                    idleText.setFocused(true);
                }
            }

            idleText.textboxKeyTyped(typedChar, keyCode);
            vanillaText.textboxKeyTyped(typedChar, keyCode);
            toggledText.textboxKeyTyped(typedChar, keyCode);
            view.stateTextMap.put(SprintState.NONE, idleText.getText());
            view.stateTextMap.put(SprintState.VANILLA, vanillaText.getText());
            view.stateTextMap.put(SprintState.SPRINT_PLS, toggledText.getText());
            super.keyTyped(typedChar, keyCode);
        }




        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            idleText.mouseClicked(mouseX, mouseY, mouseButton);
            vanillaText.mouseClicked(mouseX, mouseY, mouseButton);
            toggledText.mouseClicked(mouseX, mouseY, mouseButton);
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            idleText.setFocused(false);

            if (button.id == chromaToggle.id) {
                model.chroma = !model.chroma;
                button.displayString = "Chroma: " + (model.chroma ? "\u00a7aYES" : "\u00a7cNO");
            } else if (button.id == dropShadowToggle.id) {
                model.dropShadow = !model.dropShadow;
                button.displayString = "Drop Shadow: " + (model.dropShadow ? "\u00a7aYES" : "\u00a7cNO");
            } else if (button.id == dragModeToggle.id) {
                dragMode = dragMode != -1 ? -1 : 20;
                button.displayString = dragMode != -1 ? "Option Mode" : "Drag Mode";
            }
        }
    }


}
