package com.xboxbedrock.sg121.datasets;

import LZMA.LzmaInputStream;
import com.xboxbedrock.sg121.biomes.KoppenFilter;
import io.netty.buffer.ByteBuf;
import net.buildtheearth.terraminusminus.dataset.builtin.AbstractBuiltinDataset;
import net.buildtheearth.terraminusminus.util.RLEByteArray;
import net.daporkchop.lib.common.function.io.IOSupplier;
import net.daporkchop.lib.common.reference.cache.Cached;

import java.util.Objects;

import static net.daporkchop.lib.common.math.PMath.floorI;

/**
 * Represents a Koppen-Climate dataset
 * The returned values from 1 to 30 represent the following classes:
 * Af,Am,Aw,BWh,BWk,BSh,BSk,Csa,Csb,Csc,Cwa,Cwb,Cwc,Cfa,Cfb,Cfc,Dsa,Dsb,Dsc,Dsd,Dwa,Dwb,Dwc,Dwd,Dfa,Dfb,Dfc,Dfd,ET,EF
 *
 * @author DavixDevelop
 *
 */
public class KoppenData extends AbstractBuiltinDataset {
    protected static  final  int COLUMNS = 43200;
    protected static final int ROWS = 21600;

    public KoppenData(){
        super(COLUMNS, ROWS);
    }

    private static final Cached<RLEByteArray> CACHE = Cached.global((IOSupplier<RLEByteArray>) () -> {
        ByteBuf buffered = null;
        RLEByteArray.Builder builder = RLEByteArray.builder();
        System.out.println(Objects.requireNonNull(KoppenData.class.getResourceAsStream("/koppen_map.lzma")).available());
        try(LzmaInputStream is = new LzmaInputStream(Objects.requireNonNull(KoppenFilter.class.getResourceAsStream("/koppen_map.lzma")))){
            System.out.println(is.available());
            byte[] buffer = new byte[4096];
            int readyBytes;
            while((readyBytes = is.read(buffer, 0, 4096)) != -1){
                for(int i = 0; i < readyBytes; i++){
                    builder.append(buffer[i]);
                }
            }
        }

        /*for(int i = 0, s = buffered.readableBytes(); i < s; i++){
            builder.append(buffered.getByte(i));
        }*/

        return builder.build();
    });

    private  final  RLEByteArray data = CACHE.get();

    @Override
    protected double get(double xc, double yc) {
        int x = floorI(xc);
        int y = floorI(yc);

        if(x >= COLUMNS || x < 0 || y >= ROWS || y < 0)
            return 0;

        return  this.data.get(y * COLUMNS + x);
    }
}
