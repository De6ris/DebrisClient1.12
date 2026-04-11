package com.github.debris.debrisclient.unsafe.mod;

import com.github.debris.debrisclient.localization.GenericLocalizationKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.info.widget.InfoDisplayCommonWidgetFactories;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.lib.common.config.option.value.io.serialization.BuiltInConfigValueIOCodecs;

import java.io.IOException;

public class XaeroMiniMapAccess {
    public static void createWayPoint(int dimensionId, BlockPos pos, String name) {
        MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
        MinimapWorldManager waypointsManager = session.getWorldManager();
        MinimapWorld currentWorld = waypointsManager.getCurrentWorld();
        if (currentWorld == null) return;

        Waypoint waypoint = new Waypoint(pos.getX(), pos.getY(), pos.getZ(),
                name,
                name.isEmpty() ? "X" : name.substring(0, 1),
                WaypointColor.getRandom()
        );

        boolean front = !session.getModMain().getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.NEW_WAYPOINTS_TO_BOTTOM);

        currentWorld.getCurrentWaypointSet().add(waypoint, front);

        try {
            session.getWorldManagerIO().saveWorld(currentWorld);
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
    }

    public static InfoDisplay<Boolean> getSubSeasonInfo() {
        InfoDisplay.Builder<Boolean> builder = InfoDisplay.Builder.begin();
        return builder
                .setId("sub_season")
                .setName(GenericLocalizationKeys.INFO_DISPLAY_SUB_SEASON.translate())
                .setDefaultState(Boolean.TRUE)
                .setCodec(BuiltInConfigValueIOCodecs.BOOLEAN)
                .setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON)
                .setCompiler((displayInfo, compiler, session, availableWidth, playerPos) -> {
                    if (displayInfo.getEffectiveState()) {
                        ITextComponent info = SereneSeasonsAccess.getSubSeasonInfo(session.getMc());
                        if (info != null) {
                            compiler.addLine(info);
                        }


//                        ISeasonState seasonState = SeasonHelper.getSeasonState(session.getMc().world);
//                        compiler.addLine("一日长度" + seasonState.getDayDuration());
//                        compiler.addLine("子季节长度" + seasonState.getSubSeasonDuration());
//                        compiler.addLine("季节长度" + seasonState.getSeasonDuration());
//                        compiler.addLine("循环长度" + seasonState.getCycleDuration());
//                        compiler.addLine("季节循环刻" + seasonState.getSeasonCycleTicks());
//                        compiler.addLine("天数" + seasonState.getDay());
//
//                        int subSeasonDuration = seasonState.getSubSeasonDuration();
//                        double ratio = (double) (seasonState.getSeasonCycleTicks() % subSeasonDuration) / subSeasonDuration;
//                        compiler.addLine("子季节进度" + ComponentUtil.percentage(ratio));
                    }
                })
                .build();
    }
}
