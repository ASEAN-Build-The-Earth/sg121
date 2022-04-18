package com.xboxbedrock.sg121;

import com.xboxbedrock.sg121.biomes.BiomeGenerator;
import com.xboxbedrock.sg121.commands.Distortion;
import com.xboxbedrock.sg121.commands.Info;
import com.xboxbedrock.sg121.commands.Tpll;
import com.xboxbedrock.sg121.commands.Where;
import com.xboxbedrock.sg121.terrain.TerraChunkGenerator;
import net.buildtheearth.terraminusminus.generator.EarthGeneratorSettings;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class SG121 extends JavaPlugin {

    private BukkitAudiences adventure;

    public static String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "LionGen >> " + ChatColor.RESET;

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        Objects.requireNonNull(getCommand("tpll")).setExecutor(new Tpll(EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS), this));
        Objects.requireNonNull(getCommand("info")).setExecutor(new Info());
        Objects.requireNonNull(getCommand("where")).setExecutor(new Where(this));
        Objects.requireNonNull(getCommand("distortion")).setExecutor(new Distortion(this));
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
        return new TerraChunkGenerator(EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS), this);
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, String id) {
        return new BiomeGenerator(this);
    }
}
