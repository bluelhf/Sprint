package io.github.bluelhf.sprint.command;

import io.github.bluelhf.sprint.Sprint;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Arrays;

import java.util.List;

public class SprintCommand extends CommandBase {
    private boolean scheduled = false;

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("spr", "sprint");
    }

    @Override
    public String getCommandName() {
        return "sprint";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sprint";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        scheduled = true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !scheduled) return;
        Minecraft.getMinecraft().displayGuiScreen(Sprint.INSTANCE.getState().getController());
        scheduled = false;
    }
}
