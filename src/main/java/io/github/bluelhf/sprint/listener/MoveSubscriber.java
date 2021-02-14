package io.github.bluelhf.sprint.listener;

import io.github.bluelhf.sprint.Sprint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MoveSubscriber {
    @SubscribeEvent
    public void onMove(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null) return;

        EntityPlayerSP player = minecraft.thePlayer;
        if (player == null) return;

        if (minecraft.gameSettings.keyBindSprint.isPressed()) {
            // toggleSprint() returns the new state of the shouldSprint boolean - we can utilise this
            boolean newState = Sprint.INSTANCE.toggleSprint();
            if (newState) {
                // We set the previous sprint state so we know what to return to should the user stop sprinting
                Sprint.INSTANCE.previousSprintState = player.isSprinting();
            } else {
                player.setSprinting(Sprint.INSTANCE.previousSprintState);
                KeyBinding.setKeyBindState(minecraft.gameSettings.keyBindSprint.getKeyCode(), false);
            }
        } else {
            if (player.motionX * player.motionX + player.motionZ * player.motionZ < 0.000025 && player.onGround) {
                player.setSprinting(false);
                return;
            }
            boolean canSprint = (float) player.getFoodStats().getFoodLevel() > 6.0F || player.capabilities.allowFlying;
            if (Sprint.INSTANCE.shouldSprint && player.movementInput.moveForward >= 0.8F && canSprint && !player.isUsingItem() && !player.isPotionActive(Potion.blindness) && Sprint.INSTANCE.getHitStopper().canSprint()) {
                player.setSprinting(true);
            }
        }


    }
}
