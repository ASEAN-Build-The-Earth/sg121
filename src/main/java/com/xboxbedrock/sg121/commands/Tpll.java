package com.xboxbedrock.sg121.commands;

import com.xboxbedrock.sg121.SG121;
import com.xboxbedrock.sg121.TerraConnector;
import io.papermc.lib.PaperLib;
import net.buildtheearth.terraminusminus.generator.CachedChunkData;
import net.buildtheearth.terraminusminus.generator.ChunkDataLoader;
import net.buildtheearth.terraminusminus.generator.EarthGeneratorSettings;
import net.buildtheearth.terraminusminus.projection.GeographicProjection;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import net.buildtheearth.terraminusminus.substitutes.ChunkPos;
import net.buildtheearth.terraminusminus.util.geo.CoordinateParseUtils;
import net.buildtheearth.terraminusminus.util.geo.LatLng;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Tpll implements CommandExecutor {

    private final ChunkDataLoader loader;
    private final SG121 plugin;

    public Tpll(@NotNull EarthGeneratorSettings settings, @NotNull SG121 plugin) {
        
        this.loader = new ChunkDataLoader(settings);
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!commandSender.hasPermission("SG121.tpll") && !commandSender.isOp()) {
            TextComponent textComponent = Component.text("You do not have permission to use that command.")
                    .color(NamedTextColor.DARK_RED);
            plugin.adventure().sender(commandSender).sendMessage(textComponent);
            return true;
        }
        if (!(commandSender instanceof Player)) {
            TextComponent textComponent = Component.text("Only players can use this command.")
                    .color(NamedTextColor.DARK_RED);
            plugin.adventure().sender(commandSender).sendMessage(textComponent);
            return true;
        }
        try {
            Player player = (Player) commandSender;
            Location l = player.getLocation();
            LatLng defaultCoords = CoordinateParseUtils.parseVerbatimCoordinates(this.getRawArguments(args).trim());

            if (defaultCoords == null) {
                LatLng possiblePlayerCoords = CoordinateParseUtils.parseVerbatimCoordinates(this.getRawArguments(this.selectArray(args)));
                if (possiblePlayerCoords != null) {
                    defaultCoords = possiblePlayerCoords;
                }
            }

            LatLng possibleHeightCoords = CoordinateParseUtils.parseVerbatimCoordinates(this.getRawArguments(this.inverseSelectArray(args, args.length - 1)));
            if (possibleHeightCoords != null) {
                defaultCoords = possibleHeightCoords;
            }

            LatLng possibleHeightNameCoords = CoordinateParseUtils.parseVerbatimCoordinates(this.getRawArguments(this.inverseSelectArray(this.selectArray(args), this.selectArray(args).length - 1)));
            if (possibleHeightNameCoords != null) {
                defaultCoords = possibleHeightNameCoords;
            }

            if (defaultCoords == null) {
                TextComponent textComponent = Component.text("Invalid coordinates. </tpll latitude longitude>")
                        .color(NamedTextColor.RED);
                plugin.adventure().sender(commandSender).sendMessage(textComponent);
                return true;
            }

                try {
                    EarthGeneratorSettings bteSettings = EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS);
                    GeographicProjection projection = bteSettings.projection();

                    double[] c = {defaultCoords.getLng(), defaultCoords.getLat()};
                    try {
                        c = projection.fromGeo(c[0], c[1]);
                    } catch (OutOfProjectionBoundsException e) {
                        e.printStackTrace();
                    }
                    if (args.length >= 3 && isInteger(args[2])) {
                        int y = Integer.parseInt(args[2]);
                        TextComponent textComponent = Component.text("Teleporting to ")
                                .color(NamedTextColor.GRAY)
                                .append(Component.text(defaultCoords.getLat(), NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                                .append(Component.text(", ", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, true))
                                .append(Component.text(defaultCoords.getLng(), NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true));
                        plugin.adventure().sender(commandSender).sendMessage(textComponent);
                        PaperLib.teleportAsync(player, new Location(player.getWorld(), c[0], y, c[1], l.getYaw(), l.getPitch()));
                        return true;
                    } else {

                            Location location = new Location(player.getWorld(), c[0], 0, c[1], l.getYaw(), l.getPitch());

                            boolean isChunkLoaded = PaperLib.isChunkGenerated(location);

                            if (isChunkLoaded) {
                                int y = player.getWorld().getHighestBlockYAt((int) c[0], (int) c[1]) + 1;
                                TextComponent textComponent = Component.text("Teleporting to ")
                                        .color(NamedTextColor.GRAY)
                                        .append(Component.text(defaultCoords.getLat(), NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                                        .append(Component.text(", ", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, true))
                                        .append(Component.text(defaultCoords.getLng(), NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true));
                                plugin.adventure().sender(commandSender).sendMessage(textComponent);
                                PaperLib.teleportAsync(player, new Location(player.getWorld(), c[0], y, c[1], l.getYaw(), l.getPitch()));
                            } else {
                                double y = new TerraConnector().getHeightGeo(defaultCoords.getLng(), defaultCoords.getLat()).get() + 1;
                                TextComponent textComponent = Component.text("Teleporting to ")
                                        .color(NamedTextColor.GRAY)
                                        .append(Component.text(defaultCoords.getLat(), NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                                        .append(Component.text(", ", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, true))
                                        .append(Component.text(defaultCoords.getLng(), NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true));
                                plugin.adventure().sender(commandSender).sendMessage(textComponent);
                                PaperLib.teleportAsync(player, new Location(player.getWorld(), c[0], y, c[1], l.getYaw(), l.getPitch()));
                            }
                        return true;


                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    /**
     * Gets a space separated string from an array
     *
     * @param args A string array
     * @return The space separated String
     */
    private String getRawArguments(String[] args) {
        if (args.length == 0) {
            return "";
        }
        if (args.length == 1) {
            return args[0];
        }

        StringBuilder arguments = new StringBuilder(args[0].replace((char) 176, (char) 32).trim());

        for (int x = 1; x < args.length; x++) {
            arguments.append(" ").append(args[x].replace((char) 176, (char) 32).trim());
        }

        return arguments.toString();
    }

    /**
     * Gets all objects in a string array above a given index
     *
     * @param args  Initial array
     * @return Selected array
     */
    private String[] selectArray(String[] args) {
        List<String> array = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
        return array.toArray(array.toArray(new String[0]));
    }

    private String[] inverseSelectArray(String[] args, int index) {
        List<String> array = new ArrayList<>(Arrays.asList(args).subList(0, index));
        return array.toArray(array.toArray(new String[0]));
    }

    public static boolean isInteger(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), 10) < 0) return false;
        }
        return true;
    }
}
