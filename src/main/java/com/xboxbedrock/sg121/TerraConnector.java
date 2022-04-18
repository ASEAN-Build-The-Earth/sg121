package com.xboxbedrock.sg121;


import net.buildtheearth.terraminusminus.dataset.IScalarDataset;
import net.buildtheearth.terraminusminus.generator.EarthGeneratorPipelines;
import net.buildtheearth.terraminusminus.generator.EarthGeneratorSettings;
import net.buildtheearth.terraminusminus.generator.GeneratorDatasets;
import net.buildtheearth.terraminusminus.projection.GeographicProjection;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import net.buildtheearth.terraminusminus.projection.dymaxion.BTEDymaxionProjection;

import java.util.concurrent.CompletableFuture;

/**
 * @author Noah Husby
 */

public class TerraConnector {

    private static final BTEDymaxionProjection scaleProj = new BTEDymaxionProjection();

    /**
     * Gets the geographical location from in-game coordinates
     *
     * @param x X-Axis in-game
     * @param z Z-Axis in-game
     * @return The geographical location (Long, Lat)
     */
    public static double[] toGeo(double x, double z) throws OutOfProjectionBoundsException {
        return scaleProj.toGeo(x, z);
    }

    /**
     * Gets in-game coordinates from geographical location
     *
     * @param lon Geographical Longitude
     * @param lat Geographic Latitude
     * @return The in-game coordinates (x, z)
     */
    public static double[] fromGeo(double lon, double lat) throws OutOfProjectionBoundsException {
        return scaleProj.fromGeo(lon, lat);
    }


    public CompletableFuture<Double> getHeight(double x, double z) throws OutOfProjectionBoundsException {
        double[] adjustedProj = toGeo(x, z);
        double adjustedLon = adjustedProj[0];
        double adjustedLat = adjustedProj[1];
        GeneratorDatasets datasets = new GeneratorDatasets(EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS));
        CompletableFuture<Double> altFuture;
        try {
            altFuture = datasets.<IScalarDataset>getCustom(EarthGeneratorPipelines.KEY_DATASET_HEIGHTS)
                    .getAsync(adjustedLon, adjustedLat)
                    .thenApply(a -> a + 1.0d);
        } catch (OutOfProjectionBoundsException e) {
            altFuture = CompletableFuture.completedFuture(0.0);
        }
        return altFuture;
    }

    public CompletableFuture<Double> getHeightGeo(double adjustedLon, double adjustedLat) {
        GeneratorDatasets datasets = new GeneratorDatasets(EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS));
        CompletableFuture<Double> altFuture;
        try {
            altFuture = datasets.<IScalarDataset>getCustom(EarthGeneratorPipelines.KEY_DATASET_HEIGHTS)
                    .getAsync(adjustedLon, adjustedLat)
                    .thenApply(a -> a + 1.0d);
        } catch (OutOfProjectionBoundsException e) {
            altFuture = CompletableFuture.completedFuture(0.0);
        }
        return altFuture;
    }

}
