package com.xboxbedrock.sg121.commands;

import com.xboxbedrock.sg121.SG121;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Info implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(SG121.prefix + ChatColor.BOLD + ChatColor.BLUE + "By XboxBedrock and the BuildTheEarth development team.");
        return true;
    }
}
