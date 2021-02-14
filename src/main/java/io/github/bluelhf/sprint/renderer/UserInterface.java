package io.github.bluelhf.sprint.renderer;

import io.github.bluelhf.sprint.Sprint;
import io.github.bluelhf.sprint.util.Aligns;
import io.github.bluelhf.sprint.util.Area;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class UserInterface {

    private final Model model;
    private final View view;
    private final Controller controller;
    private final Sprint registrar;

    public UserInterface(Sprint registrar) {
        this.registrar = registrar;
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

        // These are part of Model instead of View because they be stored in the configuration.
        public int viewX = 10, viewY = 10;
        public final HashMap<SprintState, String> stateTextMap = new HashMap<>();
    }

    public class View {

        public final RainbowFontRenderer renderer;

        public boolean highlight = false;

        public View() {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft == null)
                throw new IllegalStateException("Cannot instantiate View without renderer when Minecraft is not running.");
            this.renderer = new RainbowFontRenderer(minecraft.gameSettings, new ResourceLocation("textures/font/ascii.png"), minecraft.renderEngine, false);
            this.renderer.onResourceManagerReload(null);
            MinecraftForge.EVENT_BUS.register(this);
        }

        public void setText(SprintState sprintState, String text) {
            model.stateTextMap.put(sprintState, text);
        }


        @SubscribeEvent
        public void render(RenderGameOverlayEvent.Post event) {
            if (renderer == null) return;
            if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
            SprintState s = SprintState.getClientState();

            drawText(
                    model.stateTextMap.getOrDefault(s, s.getDefaultText()),
                    model.viewX,
                    model.viewY,
                    Aligns.INDICATOR_ALIGNMENT
            );
        }


        public void drawText(String text, int x, int y, Aligns.Alignment alignment) {
            Aligns.Align align = alignment.getAlignment(model.viewX, model.viewY);
            int tempX = align.mapFromLeft(x, renderer.getStringWidth(text));

            if (model.chroma) {
                int speed = Math.round(151 - model.chromaSpeed);
                renderer.drawRainbowString(text, tempX, y, speed, speed, model.dropShadow);
            } else {
                renderer.drawString(text, tempX, y, Color.HSBtoRGB(model.hue, model.saturation, model.brightness), model.dropShadow);
            }
            if (this.highlight) {
                for (Area area : alignment.getAreas()) {
                    GuiScreen.drawRect(
                            area.getMinX() + 2,
                            area.getMinY() + 2,
                            area.getMaxX() - 2,
                            area.getMaxY() - 2,
                            0x1AFFFFFF);
                }
            }
        }
    }


    public class Controller extends GuiScreen {

        private GuiSlider chromaSpeedSlider;
        private GuiButton chromaToggle;

        private final ColourPicker colourPicker = new ColourPicker(3, 0, 0, 100);

        private GuiButton dropShadowToggle;

        private GuiTextField idleText;
        private GuiTextField vanillaText;
        private GuiTextField toggledText;

        private int dragMode = -1;
        private GuiButton dragModeToggle;

        @Override
        public void initGui() {
            Sprint.INSTANCE.getConfigurator().load();
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
            idleText.setText(model.stateTextMap.getOrDefault(SprintState.NONE, SprintState.NONE.getDefaultText()));

            vanillaText = new GuiTextField(6, fontRendererObj, 0, 0, 129 /* Reserve 70 pixels for textfield name */, 20);
            vanillaText.setText(model.stateTextMap.getOrDefault(SprintState.VANILLA, SprintState.VANILLA.getDefaultText()));

            toggledText = new GuiTextField(7, fontRendererObj, 0, 0, 129 /* Reserve 70 pixels for textfield name */, 20);
            toggledText.setText(model.stateTextMap.getOrDefault(SprintState.SPRINT_PLS, SprintState.SPRINT_PLS.getDefaultText()));

            dragModeToggle = new GuiButton(8, 0, 0, "Drag Mode");

            buttonList.add(chromaSpeedSlider);
            buttonList.add(chromaToggle);
            buttonList.add(colourPicker);
            buttonList.add(dropShadowToggle);
            buttonList.add(dragModeToggle);

            colourPicker.setColour(Color.getHSBColor(model.hue, model.saturation, model.brightness));
            super.initGui();
        }

        @Override
        public void onGuiClosed() {
            dragMode = -1;
            view.highlight = false;
            Sprint.INSTANCE.getConfigurator().save();
            super.onGuiClosed();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            if (dragMode > 0) dragMode--;
            view.highlight = dragMode != -1;

            dragModeToggle.width = fontRendererObj.getStringWidth(dragModeToggle.displayString) + 10;
            dragModeToggle.xPosition = 2;
            dragModeToggle.yPosition = height - dragModeToggle.height - 2;


            if (model.viewX > dragModeToggle.xPosition && model.viewX < dragModeToggle.xPosition + dragModeToggle.width && model.viewY > dragModeToggle.yPosition - dragModeToggle.height && model.viewY < height) {
                dragModeToggle.xPosition = width - dragModeToggle.width - 2;
                dragModeToggle.yPosition = 2;
            }

            if (dragMode != -1) {

                if (!dragModeToggle.isMouseOver() && Mouse.isButtonDown(0) && dragMode == 0) {
                    model.viewX = mouseX;
                    model.viewY = mouseY;
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
            model.stateTextMap.put(SprintState.NONE, idleText.getText());
            model.stateTextMap.put(SprintState.VANILLA, vanillaText.getText());
            model.stateTextMap.put(SprintState.SPRINT_PLS, toggledText.getText());
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
