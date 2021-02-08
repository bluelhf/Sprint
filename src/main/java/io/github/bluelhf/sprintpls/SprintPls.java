package io.github.bluelhf.sprintpls;

import io.github.bluelhf.sprintpls.command.SprintPlsCommand;
import io.github.bluelhf.sprintpls.listener.HitStopper;
import io.github.bluelhf.sprintpls.listener.MoveSubscriber;
import io.github.bluelhf.sprintpls.renderer.UserInterface;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = SprintPls.MODID, version = SprintPls.VERSION, clientSideOnly = true)
public class SprintPls {
    public static final String MODID = "sprintpls";
    public static final String VERSION = "1.0";

    public static SprintPls INSTANCE;

    public boolean shouldSprint = false;
    public boolean previousSprintState = false;
    private UserInterface userInterface;
    private final HitStopper hitStopper = new HitStopper();


    @EventHandler
    public void init(FMLInitializationEvent event) {
        // This instance won't be nulled when the mod is unloaded, which is problematic, but Forge doesn't allow us to run code when our mod unloads...
        INSTANCE = this;

        userInterface = new UserInterface();
        MinecraftForge.EVENT_BUS.register(new MoveSubscriber());
        MinecraftForge.EVENT_BUS.register(hitStopper);
        SprintPlsCommand command = new SprintPlsCommand();
        MinecraftForge.EVENT_BUS.register(command);
        ClientCommandHandler.instance.registerCommand(command);
    }


    public HitStopper getHitStopper() {
        return hitStopper;
    }

    public UserInterface getState() {
        return userInterface;
    }

    public boolean toggleSprint() {
        return this.shouldSprint = !this.shouldSprint;
    }
}
