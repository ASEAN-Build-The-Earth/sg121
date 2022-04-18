package com.xboxbedrock.sg121.biomes;

import com.xboxbedrock.sg121.SG121;
import com.xboxbedrock.sg121.TerraConnector;
import com.xboxbedrock.sg121.datasets.KoppenData;
import net.buildtheearth.terraminusminus.generator.ChunkDataLoader;
import net.buildtheearth.terraminusminus.generator.EarthGeneratorSettings;
import net.buildtheearth.terraminusminus.projection.GeographicProjection;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BiomeGenerator extends BiomeProvider {

    EarthGeneratorSettings bteSettings = EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS);
    GeographicProjection projection = bteSettings.projection();
    private final KoppenData loader = new KoppenData();
    private final SG121 plugin;

    public BiomeGenerator(@NotNull SG121 plugin) {
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        double[] latLong = null;
        try {
            latLong = projection.toGeo(x, z);
        } catch (OutOfProjectionBoundsException e) {
            return Biome.PLAINS;
        }
        try {
            double biomeDouble = this.loader.getAsync(latLong[0], latLong[1]).get();
            return KoppenFilter.koppenToBiome(biomeDouble);
        } catch (OutOfProjectionBoundsException | ExecutionException | InterruptedException | NullPointerException e) {
            return Biome.PLAINS;

        }
    }

    @NotNull
    @Override
    public List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return KoppenFilter.biomes;
    }
}
