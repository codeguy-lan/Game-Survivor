package lan.survivor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lan.survivor.R;
import lan.survivor.element.Terrain;
import android.content.Context;

public class MapLoader {

    public static void loadMap(Context context, Terrain map[][]) throws IOException {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            short i = 0;
            //TODO: the map file in raw folder
            in = context.getResources().openRawResource(R.raw.worldmap);//(R.raw.worldmap);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                for (short j = 0; j < Const.MAP_TERRAINS_COLUMN; j++) {
                    map[i][j] = new Terrain(Short.parseShort(data[j]), i, j);
                    Generator.generateObjectOnTerrain(map[i][j]);
                }
                i++;
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            reader.close();
            in.close();
        }
    }
}