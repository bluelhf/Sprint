package io.github.bluelhf.sprintpls.listener;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class HitStopper {
    private long nextSprintTime = -1L;
    @SubscribeEvent
    public void onHit(AttackEntityEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null) return;
        if (minecraft.thePlayer == null) return;
        if (event.entityPlayer != minecraft.thePlayer) return;
        if (!event.hasResult() || !event.getResult().equals(Event.Result.ALLOW))  return;
        nextSprintTime = System.currentTimeMillis() + 100;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && nextSprintTime > System.currentTimeMillis()) {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft == null) return;
            if (minecraft.thePlayer == null) return;
            minecraft.thePlayer.setSprinting(false);
        }
    }

    public boolean canSprint() {
        return nextSprintTime < System.currentTimeMillis();
    }
}
