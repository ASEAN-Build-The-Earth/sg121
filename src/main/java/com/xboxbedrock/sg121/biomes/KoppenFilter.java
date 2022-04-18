package com.xboxbedrock.sg121.biomes;

import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KoppenFilter {

    public static List<Biome> biomes = new ArrayList<Biome>(Arrays.asList(Biome.OCEAN,
            Biome.JUNGLE,
            Biome.BAMBOO_JUNGLE,
            Biome.SPARSE_JUNGLE,
            Biome.SAVANNA,
            Biome.DESERT,
            Biome.PLAINS,
            Biome.SUNFLOWER_PLAINS,
            Biome.BEACH,
            Biome.WINDSWEPT_GRAVELLY_HILLS,
            Biome.FLOWER_FOREST,
            Biome.STONY_PEAKS,
            Biome.SAVANNA_PLATEAU,
            Biome.WOODED_BADLANDS,
            Biome.SNOWY_TAIGA,
            Biome.OLD_GROWTH_SPRUCE_TAIGA,
            Biome.SWAMP,
            Biome.OLD_GROWTH_PINE_TAIGA,
            Biome.FOREST,
            Biome.DARK_FOREST,
            Biome.TAIGA,
            Biome.FROZEN_PEAKS,
            Biome.SNOWY_PLAINS,
            Biome.ICE_SPIKES));


    public static Biome koppenToBiome(double koppen) {
        return switch ((int) koppen) {
            case 0 -> Biome.OCEAN;
            case 1 -> //Af - jungle
                    Biome.JUNGLE;
            case 2 -> //Am - bamboo_jungle (1.13) #ToDo Change to bamboo_jungle in future update to MC 1.17
                    Biome.BAMBOO_JUNGLE;
            case 3 -> //Aw - jungle_edge
                    Biome.SPARSE_JUNGLE;
            case 4 -> //BWh - desert
                    Biome.DESERT;
            case 5 -> //BWk - desert_hills
                    Biome.DESERT;
            case 6 -> //BSh - savanna
                    Biome.SAVANNA;
            case 7 -> //BSk - desert_lakes #ToDo Change to desert_lakes in future update to MC 1.17
                    Biome.DESERT;
            case 8 -> //Csa - plains
                    Biome.PLAINS;
            case 9 -> //Csb - sunflower_plains
                    Biome.SUNFLOWER_PLAINS;
            case 10 -> //Csc
                    Biome.BEACH;
            case 11 -> //Cwa -  modified_jungle_edge
                    Biome.SPARSE_JUNGLE;
            case 12 -> //Cwb - jungle_hills
                    Biome.JUNGLE;
            case 13 -> //Cwc - gravelly_mountains
                    Biome.WINDSWEPT_GRAVELLY_HILLS;
            case 14 -> //Cfa - flower_forest
                    Biome.FLOWER_FOREST;
            case 15 -> //Cfb - flower_forest
                    Biome.FLOWER_FOREST;
            case 16 -> //Cfc - mountains
                    Biome.STONY_PEAKS;
            case 17 -> //Dsa - savanna_plateau
                    Biome.SAVANNA_PLATEAU;
            case 18 -> //Dsb - wooded_badlands_plateau
                    Biome.WOODED_BADLANDS;
            case 19 -> //Dsc - snowy_taiga
                    Biome.SNOWY_TAIGA;
            case 20 -> //Dsd - giant_spruce_taiga_hills
                    Biome.OLD_GROWTH_SPRUCE_TAIGA;
            case 21 -> //Dwa - swamp
                    Biome.SWAMP;
            case 22 -> //Dwb - swampland_hills
                    Biome.SWAMP;
            case 23 -> //Dwc - giant_tree_taiga
                    Biome.OLD_GROWTH_PINE_TAIGA;
            case 24 -> //Dwd - giant_spruce_taiga
                    Biome.OLD_GROWTH_SPRUCE_TAIGA;
            case 25 -> //Dfa - forest
                    Biome.FOREST;
            case 26 -> //Dfb - dark_forest
                    Biome.DARK_FOREST;
            case 27 -> //Dfc - taiga
                    Biome.TAIGA;
            case 28 -> //Dfd - snowy_mountains
                    Biome.FROZEN_PEAKS;
            case 29 -> //ET - snowy_tundra
                    Biome.SNOWY_PLAINS;
            case 30 -> //EF - ice_spikes
                    Biome.ICE_SPIKES;
            default -> Biome.PLAINS;
        };

    }


}
