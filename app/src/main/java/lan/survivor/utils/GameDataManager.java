package lan.survivor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import lan.survivor.actions.EquipManager;
import lan.survivor.actions.InventoryManager;
import lan.survivor.actions.StatusManager;
import lan.survivor.element.Survivor;
import lan.survivor.element.Terrain;
import lan.survivor.environment.SubWorldMap;
import lan.survivor.environment.World;
import lan.survivor.environment.WorldMap;
import android.content.Context;
import android.os.Environment;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GameDataManager {

	public static final String FILENAME = "survivor";
	public static GameData gd = new GameData();
	//private static Kryo kryo = new Kryo();

	public static void saveGameData(Context c) {
		getSaveData();
		// Output output = null;
		ObjectOutputStream oos = null;
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/tmp");
			dir.mkdirs();
			File file = new File(dir, FILENAME);
			if (file.exists()) {
				file.delete();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");

			FileOutputStream fos = new FileOutputStream(raf.getFD());
			oos = new ObjectOutputStream(fos);
			oos.writeObject(gd);

			// output = new Output(new FileOutputStream(raf.getFD()));
			// kryo.register(GameData.class);
			// kryo.register(Terrain.class);
			// kryo.register(EquipElement.class);
			// kryo.register(ArrayList.class);
			// kryo.writeObject(output, gd);
			// output.close();
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static GameData loadData(Context c) {
		GameData saveData = null;
		try {
			File sdCard = Environment.getExternalStorageDirectory();

			// Input input = new Input(new
			// FileInputStream(sdCard.getAbsolutePath() + "/tmp/" + FILENAME));
			// saveData = kryo.readObject(input, GameData.class);
			// input.close();

			FileInputStream fin = null;
			ObjectInputStream sin = null;
			fin = new FileInputStream(sdCard.getAbsolutePath() + "/tmp/" + FILENAME);
			sin = new ObjectInputStream(fin);
			saveData = (GameData) sin.readObject();
			sin.close();
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return saveData;
	}

	private static void getSaveData() {
		gd.map = WorldMap.map;
		gd.mapCurrentX = WorldMap.currentX;
		gd.mapCurrentY = WorldMap.currentY;
		gd.subMap = SubWorldMap.map;
		gd.subMapCurrentX = SubWorldMap.currentX;
		gd.subMapCurrentY = SubWorldMap.currentY;
		gd.days = World.days;
		gd.worldTimeH = World.worldTimeH;
		gd.worldTimeM = World.worldTimeM;
		gd.timeIntervalChange = World.timeIntervalChange;
		gd.dayNight = World.dayNight;
		gd.weather = World.weather;
		gd.visibleRange = World.visibleRange;
		gd.charaCurrentX = Survivor.currentX;
		gd.charaCurrentY = Survivor.currentY;
		gd.facing = Survivor.facing;
		gd.inventory = InventoryManager.inventory;
		gd.bagCapacity = InventoryManager.bagCapacity;
		gd.equips = EquipManager.equips;
		gd.equipStatus = StatusManager.equipStatus;
		gd.status = StatusManager.status;
		gd.STATUS_MAX = StatusManager.STATUS_MAX;
		gd.condition = StatusManager.condition;
		gd.inSleep = StatusManager.inSleep;
		gd.inShelter = StatusManager.inShelter;
	}

	public class GameDataSerializer extends Serializer<GameData> {

		@Override
		public GameData read(Kryo kryo, Input input, Class<GameData> type) {
			GameData gdata = new GameData();
			gdata.map = kryo.readObject(input, Terrain[][].class);
			gdata.mapCurrentX = input.readFloat();
			gdata.mapCurrentY = input.readFloat();
			return gdata;
		}

		@Override
		public void write(Kryo kryo, Output output, GameData gd) {
			kryo.writeObject(output, gd.map);
			output.writeFloat(gd.mapCurrentX);
			output.writeFloat(gd.mapCurrentY);
		}
	}

	public class TerrainSerializer extends Serializer<Terrain[][]> {

		@Override
		public Terrain[][] read(Kryo kryo, Input input, Class<Terrain[][]> type) {
			Terrain[][] map = new Terrain[100][100];
			for (int i = 0; i < 100; i++) {
				for (int j = 0; j < 100; j++) {
					map[i][j].terrainType = input.readShort();
					map[i][j].passable = input.readBoolean();
					map[i][j].blockRow = input.readShort();
					map[i][j].blockColumn = input.readShort();
					map[i][j].lightened = input.readShort();
					map[i][j].elements = kryo.readObject(input, ArrayList.class);
				}
			}
			return map;
		}

		@Override
		public void write(Kryo kryo, Output output, Terrain[][] map) {
			for (int i = 0; i < 100; i++) {
				for (int j = 0; j < 100; j++) {
					output.writeShort(map[i][j].terrainType);
					output.writeBoolean(map[i][j].passable);
					output.writeShort(map[i][j].blockRow);
					output.writeShort(map[i][j].blockColumn);
					output.writeShort(map[i][j].lightened);
					kryo.writeObject(output, map[i][j].elements);
				}
			}
		}
	}
}
