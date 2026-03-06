package com.github.debris.debrisclient.feat;

import com.github.debris.debrisclient.ModReference;
import com.github.debris.debrisclient.config.DCConfig;
import com.github.debris.debrisclient.feat.task.Task;
import com.github.debris.debrisclient.feat.task.TaskQueue;
import com.github.debris.debrisclient.feat.task.TaskResult;
import com.github.debris.debrisclient.unsafe.mod.FishingMBAccess;
import com.github.debris.debrisclient.util.AccessorUtil;
import com.github.debris.debrisclient.util.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class AutoFish {
    private static final int RETHROW_DELAY_TICKS = 10;
    private static boolean rethrowScheduled = false;

    public static void onTick(Minecraft client) {
        if (!ModReference.hasMod(ModReference.FISHINGMADEBETTER)) return;

        if (!DCConfig.AutoFish.getBooleanValue() || !Predicates.inGameNoGui(client)) {
            FishingMBAccess.releasePressing(client);
            return;
        }

        FishingMBAccess.onTick(client);
    }

    public static void onRetrieve() {
        Minecraft client = Minecraft.getMinecraft();
        if (!Predicates.inGameNoGui(client)) return;
        if (!DCConfig.AutoFish.getBooleanValue()) return;
        if (!ModReference.hasMod(ModReference.FISHINGMADEBETTER)) return;
        if (rethrowScheduled) return;

        rethrowScheduled = true;
        TaskQueue.schedule(getRethrowTask(), RETHROW_DELAY_TICKS);// wait for fish to land
    }

    private static Task getRethrowTask() {
        return client -> {
            rethrowScheduled = false;

            if (!Predicates.inGameNoGui(client)) return TaskResult.FAIL;
            if (client.player == null) return TaskResult.FAIL;

            ItemStack stack = client.player.getHeldItemMainhand();
            if (FishingMBAccess.isFishingRod(stack)) {
                AccessorUtil.use(client);
                return TaskResult.SUCCESS;
            }
            return TaskResult.FAIL;
        };
    }
}
