package com.xboxbedrock.sg121.terrain;

import com.xboxbedrock.sg121.SG121;
import net.buildtheearth.terraminusminus.substitutes.BukkitBindings;
import net.buildtheearth.terraminusminus.substitutes.ChunkPos;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import net.buildtheearth.terraminusminus.generator.CachedChunkData;
import net.buildtheearth.terraminusminus.generator.ChunkDataLoader;
import net.buildtheearth.terraminusminus.generator.EarthGeneratorSettings;
import net.buildtheearth.terraminusminus.substitutes.BlockState;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TerraChunkGenerator extends ChunkGenerator {

    private final ChunkDataLoader loader;
    private final SG121 plugin;

    public TerraChunkGenerator(@NotNull EarthGeneratorSettings settings,  @NotNull SG121 plugin) {
        this.loader = new ChunkDataLoader(settings);
        this.plugin = plugin;
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        final int minY = worldInfo.getMinHeight();
        final int maxY = worldInfo.getMaxHeight();

        Material water = Material.WATER;

        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
            CachedChunkData terraData = null;
            try {
                terraData = this.loader.load(chunkPos).get();



            for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                int groundHeight = terraData.groundHeight(x, z);
                int waterHeight = terraData.waterHeight(x, z);

                //horizontal density change is calculated using the top height rather than the ground height

                int waterTop = min(waterHeight, maxY);

                Material material = Material.GRASS_BLOCK;
                BlockState state = terraData.surfaceBlock(x, z);

                if (state != null) material = BukkitBindings.getAsBlockData(state).getMaterial();

                for (int y = minY; y <= min(maxY, groundHeight); y++) {

                    if (groundHeight < waterHeight && material == Material.GRASS_BLOCK) { //hacky workaround for underwater grass
                        material = Material.DIRT;
                    }

                    chunkData.setBlock(x, y, z, material);
                }


                //could be problematic
                for (int y = minY; y < min(maxY, groundHeight); y++) chunkData.setBlock(x, y, z, Material.STONE);
                if (groundHeight < maxY) chunkData.setBlock(x, groundHeight, z, material);

                //fill water
                //problematic if statement
                for (int y = groundHeight + 1; y <= min(maxY, waterTop); y++) {
                    chunkData.setBlock(x, y, z, water);
                }


                //for (int y = minY; y < min(maxY, groundHeight); y++) chunkData.setBlock(x, y, z, Material.STONE);
            }
        }
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        //TODO: Return when task done

    }

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull ChunkGenerator.ChunkData chunkData) {
        // Bad
    }


    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull ChunkGenerator.ChunkData chunkData) {
        // Bad
    }

    @NotNull
    public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Deprecated
    public boolean isParallelCapable() {
        //LETS SEE HOW THIS GOES YEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        return true;
    }


    public boolean shouldGenerateNoise() {
        // Bad
        return false;
    }


    public boolean shouldGenerateSurface() {
        // Bad
        return false;
    }


    public boolean shouldGenerateBedrock() {
        // Bad
        return false;
    }


    public boolean shouldGenerateCaves() {
        // Bad
        return false;
    }


    public boolean shouldGenerateDecorations() {
        // Bad
        return false;
    }


    public boolean shouldGenerateMobs() {
        // Bad
        return false;
    }

    public boolean shouldGenerateStructures() {
        // Bad
        return false;
    }

}
