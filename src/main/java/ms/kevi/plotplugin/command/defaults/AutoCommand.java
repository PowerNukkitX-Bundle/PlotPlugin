/*
 * Copyright 2022 KCodeYT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ms.kevi.plotplugin.command.defaults;

import cn.nukkit.Player;
import ms.kevi.plotplugin.PlotPlugin;
import ms.kevi.plotplugin.command.PlotCommand;
import ms.kevi.plotplugin.command.SubCommand;
import ms.kevi.plotplugin.event.PlotClaimEvent;
import ms.kevi.plotplugin.event.PlotPreClaimEvent;
import ms.kevi.plotplugin.lang.TranslationKey;
import ms.kevi.plotplugin.manager.PlotManager;
import ms.kevi.plotplugin.util.Plot;

/**
 * @author Kevims KCodeYT
 * @version 1.0
 */
public class AutoCommand extends SubCommand {

    public AutoCommand(PlotPlugin plugin, PlotCommand parent) {
        super(plugin, parent, "auto", "a");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        PlotManager plotManager = this.plugin.getPlotManager(player.getLevel());
        if(plotManager == null && this.plugin.getDefaultPlotLevel() == null || plotManager == null && (plotManager = this.plugin.getPlotManager(this.plugin.getDefaultPlotLevel())) == null) {
            player.sendMessage(this.translate(player, TranslationKey.NO_PLOT_WORLD));
            return false;
        }

        final int ownedPlots = plotManager.getPlotsByOwner(player.getUniqueId()).size();
        if(!player.hasPermission("plot.limit.unlimited")) {
            int maxLimit = -1;
            for(String permission : player.getEffectivePermissions().keySet()) {
                if(permission.startsWith("plot.limit.")) {
                    try {
                        final String limitStr = permission.substring("plot.limit.".length());
                        if(limitStr.isBlank()) continue;
                        final int limit = Integer.parseInt(limitStr);

                        if(limit > maxLimit) maxLimit = limit;
                    } catch(NumberFormatException ignored) {
                    }
                }
            }

            if(maxLimit > 0 && ownedPlots >= maxLimit) {
                player.sendMessage(this.translate(player, TranslationKey.AUTO_FAILURE_TOO_MANY, ownedPlots));
                return false;
            }
        }

        final Plot plot = plotManager.getNextFreePlot();

        if(plot == null) {
            player.sendMessage(this.translate(player, TranslationKey.AUTO_FAILURE));
            return false;
        }

        final PlotPreClaimEvent plotPreClaimEvent = new PlotPreClaimEvent(player, plot, true, true, true);
        this.plugin.getServer().getPluginManager().callEvent(plotPreClaimEvent);

        if(plotPreClaimEvent.isCancelled()) {
            if(plotPreClaimEvent.isShowCancelMessage())
                player.sendMessage(this.translate(player, TranslationKey.AUTO_FAILURE));
            return false;
        }

        plot.setOwner(player.getUniqueId());
        if(plotPreClaimEvent.isBorderChanging())
            plotManager.changeBorder(plot, plotManager.getLevelSettings().getClaimPlotState());
        plotManager.savePlot(plot);

        final PlotClaimEvent plotClaimEvent = new PlotClaimEvent(player, plot, true);
        this.plugin.getServer().getPluginManager().callEvent(plotClaimEvent);

        plotManager.teleportPlayerToPlot(player, plot, false);
        player.sendMessage(this.translate(player, TranslationKey.AUTO_SUCCESS));
        return true;
    }

}
