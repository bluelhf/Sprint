package io.github.bluelhf.sprintpls.command;

import io.github.bluelhf.sprintpls.SprintPls;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class SprintPlsCommand extends CommandBase {
    private boolean scheduled = false;

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        //noinspection unchecked
        return Arrays.asList(new String[]{"spls", "sprintplease"});
    }

    @Override
    public String getCommandName() {
        return "sprintpls";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sprintpls";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        scheduled = true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !scheduled) return;
        Minecraft.getMinecraft().displayGuiScreen(SprintPls.INSTANCE.getState().getController());
        scheduled = false;
    }
}
