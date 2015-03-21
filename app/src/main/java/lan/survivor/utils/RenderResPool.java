package lan.survivor.utils;

import lan.survivor.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.SparseArray;

public class RenderResPool {

	public static int PE_HALF_W = 25;
	public static int PE_HALF_H = 25;
	public static int SE_HALF_W = 20;
	public static int SE_HALF_H = 20;

	public static Paint p = new Paint();

	private static SparseArray<Bitmap> elementImg = new SparseArray<Bitmap>();

	private RenderResPool() {

	}

	public static Bitmap getImg(int speciesID) {
		return elementImg.get(speciesID);
	}

	public static void initialLoad(Context context) {
		Resources res = context.getResources();
		elementImg.put(ID.PVD_TREE_NORMAL, createImgFromRes(res, R.drawable.tree_normal, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.PVD_TREE_BANANA, createImgFromRes(res, R.drawable.tree_banana, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.PVD_TREE_BANANA_EMPTY, createImgFromRes(res, R.drawable.tree_banana_empty, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.PVD_TREE_COCONUT, createImgFromRes(res, R.drawable.tree_coconut, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.PVD_ROCK, createImgFromRes(res, R.drawable.rock, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.PVD_BUSH, createImgFromRes(res, R.drawable.bush, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.MTR_WOOD, createImgFromRes(res, R.drawable.wood, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EDB_BANANA, createImgFromRes(res, R.drawable.banana, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EDB_COCONUT, createImgFromRes(res, R.drawable.coconut, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.BLD_FIRE, createImgFromRes(res, R.drawable.fire, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.BLD_FIRE_OFF, createImgFromRes(res, R.drawable.fire_off, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.BLD_WELL, createImgFromRes(res, R.drawable.well, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.MTR_ASH, createImgFromRes(res, R.drawable.ash, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.MTR_SALT, createImgFromRes(res, R.drawable.salt, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.TOL_BOTTLE_EMPTY, createImgFromRes(res, R.drawable.bottle_empty, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.TOL_BOTTLE_FILL, createImgFromRes(res, R.drawable.bottle_water_fill, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.TOL_BOTTLE_SEA_FILL, createImgFromRes(res, R.drawable.bottle_sea_fill, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.TOL_FISHING_ROD, createImgFromRes(res, R.drawable.fishingrod, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EQP_EMPTY, createImgFromRes(res, R.drawable.empty, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EQP_KNIFE, createImgFromRes(res, R.drawable.knife, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.MST_RABBIT, createImgFromRes(res, R.drawable.rabbit, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EDB_MEAT_RAW, createImgFromRes(res, R.drawable.meat_raw, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EDB_MEAT_COOKED, createImgFromRes(res, R.drawable.meat_raw, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EDB_FISH_RAW, createImgFromRes(res, R.drawable.fish, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.EDB_FISH_COOKED, createImgFromRes(res, R.drawable.fish, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.BLD_SHELTER, createImgFromRes(res, R.drawable.shelter, PE_HALF_W, PE_HALF_H));
		elementImg.put(ID.MTR_LEAF, createImgFromRes(res, R.drawable.leaf, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.MTR_BRANCH, createImgFromRes(res, R.drawable.branch, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.MTR_STONE, createImgFromRes(res, R.drawable.stone, SE_HALF_W, SE_HALF_H));
		elementImg.put(ID.MTR_FLINT, createImgFromRes(res, R.drawable.flint, SE_HALF_W, SE_HALF_H));
		setupPaint();
	}

	public static void setupPaint() {
		p.setColor(Color.WHITE);
		p.setStyle(Style.FILL);
		p.setTextSize(10);
	}

	private static Bitmap createImgFromRes(Resources res, int id, int halfW, int halfH) {
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, id), halfW * 2, halfH * 2, true);
	}
}
