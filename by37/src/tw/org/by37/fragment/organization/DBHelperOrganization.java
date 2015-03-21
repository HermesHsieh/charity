package tw.org.by37.fragment.organization;

import static android.provider.BaseColumns._ID;
import static tw.org.by37.config.SysConfig.*;
import static tw.org.by37.fragment.organization.DBConstantsOrganization.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperOrganization extends SQLiteOpenHelper {

        private final static String DATABASE_NAME = dbOrganization;
        private final static String ASSETS_NAME = dbOrganization;
        private final static int DATABASE_VERSION = 1;

        @SuppressWarnings("unused")
        private boolean createDatabase = true;

        private static String DATABASE_PATH = SD_DB_PATH;

        private SQLiteDatabase myDataBase = null;
        private final Context myContext;

        /**
         * 在SQLiteOpenHelper的建構子當中，必須有該構造函數
         * 
         * @param context
         *                上下文對象
         * @param name
         *                資料庫名稱
         * @param factory
         *                一般都是null
         * @param version
         *                當前資料庫版本，必須是整數並且是遞增的狀態
         */

        public DBHelperOrganization(Context context, String name, CursorFactory factory, int version) {
                // 必須通過super調用父類別當中的建構子
                super(context, name, null, version);
                this.myContext = context;
        }

        public DBHelperOrganization(Context context, String name, int version) {
                this(context, name, null, version);
        }

        public DBHelperOrganization(Context context, String name) {
                this(context, name, DATABASE_VERSION);
        }

        public DBHelperOrganization(Context context) {
                this(context, DATABASE_PATH + DATABASE_NAME);
        }

        public void createDataBase() throws IOException {
                boolean dbExist = checkDataBase();
                if (dbExist) {
                        // 資料庫已存在,不做任何事情
                        createDatabase = false;
                } else {
                        // 建立資料庫
                        try {
                                File dir = new File(DATABASE_PATH);
                                if (!dir.exists()) {
                                        dir.mkdirs();
                                }
                                File dbf = new File(DATABASE_PATH + DATABASE_NAME);
                                if (dbf.exists())
                                        dbf.delete();

                                // 複製asseets中的db文件到DATABASE_PATH下
                                if (!dbf.exists())
                                        copyDataBase();

                                SQLiteDatabase.openOrCreateDatabase(dbf, null);

                        } catch (IOException e) {
                                throw new Error("資料庫建立失敗");
                        }
                }
        }

        /** 檢查資料庫是否有效 **/
        private boolean checkDataBase() {
                SQLiteDatabase checkDB = null;
                String myPath = DATABASE_PATH + DATABASE_NAME;
                try {
                        checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
                } catch (SQLiteException e) {
                        // database does't exist yet.
                }
                if (checkDB != null) {
                        checkDB.close();
                }
                return checkDB != null ? true : false;
        }

        /**
         * Copies your database from your local assets-folder to the just
         * created empty database in the system folder, from where it can be
         * accessed and handled. This is done by transfering bytestream.
         **/

        private void copyDataBase() throws IOException {
                // Open your local db as the input stream
                InputStream myInput = myContext.getAssets().open(ASSETS_NAME);
                // Path to the just created empty db
                String outFileName = DATABASE_PATH + DATABASE_NAME;
                // Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(outFileName);
                // transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                        myOutput.write(buffer, 0, length);
                }
                // Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();
        }

        @Override
        public synchronized void close() {
                if (myDataBase != null) {
                        myDataBase.close();
                }
                super.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ORG_ID + " VARCHAR, " + ORG_NAME + " VARCHAR, " + ORG_EMAIL + " VARCHAR, " + ORG_FAX + " VARCHAR, " + ORG_ADDRESS + " VARCHAR, " + ORG_PHONE + " VARCHAR, " + ORG_DIV + " VARCHAR, " + ORG_CITY + " VARCHAR, " + ORG_TYPE + " VARCHAR, " + ORG_LNG + " VARCHAR, "+ ORG_LAT + " VARCHAR, "+ ORG_CREATED_AT + " VARCHAR, " + ORG_UPDATED_AT + " VARCHAR);";
                db.execSQL(INIT_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
                db.execSQL(DROP_TABLE);
                onCreate(db);
        }

        public void deleteDataBase() {
                File dbf = new File(DATABASE_PATH + DATABASE_NAME);
                if (dbf.exists())
                        dbf.delete();
        }

        public boolean existDataBase() {
                File dbf = new File(DATABASE_PATH + DATABASE_NAME);
                if (dbf.exists())
                        return true;
                else
                        return false;
        }

}