package io.github.bluelhf.sprint;

import io.github.bluelhf.sprint.command.SprintCommand;
import io.github.bluelhf.sprint.io.Configurator;
import io.github.bluelhf.sprint.listener.HitStopper;
import io.github.bluelhf.sprint.listener.MoveSubscriber;
import io.github.bluelhf.sprint.renderer.UserInterface;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;

@Mod(modid = Sprint.MODID, version = Sprint.VERSION, clientSideOnly = true)
public class Sprint {
    public static final String MODID = "sprint";
    public static final String VERSION = "1.0";

    public static Sprint INSTANCE;

    public boolean shouldSprint = false;
    public boolean previousSprintState = false;
    private UserInterface userInterface;
    private Configurator configurator;
    private KeyBinding keyBindToggle;

    private final HitStopper hitStopper = new HitStopper();



    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        keyBindToggle = new KeyBinding("Sprint Toggle", Keyboard.KEY_LCONTROL, "Sprint");
        ClientRegistry.registerKeyBinding(keyBindToggle);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // This instance won't be nulled when the mod is unloaded, which is problematic, but Forge doesn't allow us to run code when our mod unloads...
        INSTANCE = this;

        userInterface = new UserInterface(this);
        configurator = new Configurator(
                new Configuration(new File("config/sprint.cfg")),
                userInterface.getModel()
        );

        MinecraftForge.EVENT_BUS.register(new MoveSubscriber());
        MinecraftForge.EVENT_BUS.register(hitStopper);
        SprintCommand command = new SprintCommand();
        MinecraftForge.EVENT_BUS.register(command);
        ClientCommandHandler.instance.registerCommand(command);
    }

    public Configurator getConfigurator() {
        return configurator;
    }

    public HitStopper getHitStopper() {
        return hitStopper;
    }

    public UserInterface getState() {
        return userInterface;
    }

    public KeyBinding getKeyBindToggle() {
        return keyBindToggle;
    }

    public boolean toggleSprint() {
        return this.shouldSprint = !this.shouldSprint;
    }
}
