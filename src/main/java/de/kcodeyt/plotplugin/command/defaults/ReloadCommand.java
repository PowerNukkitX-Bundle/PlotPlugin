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

package de.kcodeyt.plotplugin.command.defaults;

import cn.nukkit.Player;
import de.kcodeyt.plotplugin.PlotPlugin;
import de.kcodeyt.plotplugin.command.SubCommand;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(PlotPlugin plugin) {
        super(plugin, "reload");
        this.setPermission("plot.command.admin.reload");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        this.plugin.reloadPlots();
        this.plugin.getLanguage().reload();
        player.sendMessage(this.translate("reload-success"));
        return true;
    }

}