package lan.survivor.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseHelper {

	private static String DB_PATH = "/data/data/lan.survivor/databases/";
	private static String DB_NAME = "survivorDB";

	public static void createDataBaseIfNotExist(Context context) throws IOException {
		boolean createDb = false;
		File dbDir = new File(DB_PATH);
		File dbFile = new File(DB_PATH + DB_NAME);
		if (!dbDir.exists()) {
			dbDir.mkdir();
			createDb = true;
		} else if (!dbFile.exists()) {
			createDb = true;
		} else {
			dbFile.delete();
			createDb = true;
			// boolean doUpgrade = false;
			// if (doUpgrade) {
			// dbFile.delete();
			// createDb = true;
			// }
		}
		if (createDb) {
			InputStream myInput = context.getAssets().open(DB_NAME);
			OutputStream myOutput = new FileOutputStream(dbFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}
	}

	public static SQLiteDatabase openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		return SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

}