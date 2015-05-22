package tw.org.by37.supplieshelp;

import static android.provider.BaseColumns._ID;
import static tw.org.by37.supplieshelp.DBConstantsSupplies.*;

import java.util.ArrayList;

import tw.org.by37.data.SupplyData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBControlSupplies {

        private final static String TAG = "dbSuppliesHelps";

        private DBHelperSupplies mDBHelper = null;

        private Context mContext;

        public DBControlSupplies(Context context) {
                this.mContext = context;
        }

        public void openDatabase() {
                Log.i(TAG, "openDatabase");
                mDBHelper = new DBHelperSupplies(mContext);
        }

        public void closeDatabase() {
                Log.i(TAG, "closeDatabase");
                mDBHelper.close();
        }

        /** 新增資料 **/
        public void add() {
                Log.v(TAG, "add");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                db.insert(TABLE_NAME, null, values);
        }

        /** 從伺服器下載的資料新增至db **/
        public void add(SupplyData mData) {
                Log.v(TAG, "add SupplyData mData");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ID, mData.id);
                values.put(NAME, mData.name);
                values.put(DESCRIPTION, mData.description);
                values.put(ORGANIZATIONID, mData.organizationId);
                values.put(CATEGORY, mData.category);
                values.put(TOTAL, mData.total);
                values.put(CREATED_AT, mData.created_at);
                values.put(UPDATED_AT, mData.updated_at);
                values.put(ORGANIZATION_NAME, mData.organization_name);
                values.put(ORGANIZATION_LONGITUDE, mData.organization_longitude);
                values.put(ORGANIZATION_LATITUDE, mData.organization_latitude);
                db.insert(TABLE_NAME, null, values);
        }

        /** 更新資料 **/
        public void update(String id, SupplyData mData) {
                Log.v(TAG, "update");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ID, mData.id);
                values.put(NAME, mData.name);
                values.put(DESCRIPTION, mData.description);
                values.put(ORGANIZATIONID, mData.organizationId);
                values.put(CATEGORY, mData.category);
                values.put(TOTAL, mData.total);
                values.put(CREATED_AT, mData.created_at);
                values.put(UPDATED_AT, mData.updated_at);
                values.put(ORGANIZATION_NAME, mData.organization_name);
                values.put(ORGANIZATION_LONGITUDE, mData.organization_longitude);
                values.put(ORGANIZATION_LATITUDE, mData.organization_latitude);
                db.update(TABLE_NAME, values, "id='" + id + "'", null);
        }

        /** 刪除資料 **/
        public void delete(String id) {
                // Log.v(TAG, "delete");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                db.delete(TABLE_NAME, _ID + "=" + id, null);

                // Toast.makeText(mContext, "刪除完成",
                // Toast.LENGTH_LONG).show();
                Log.e(TAG, "delete ID = '" + id + "' data Success");
        }

        private SupplyData createData(Cursor cursor) {

                SupplyData data = new SupplyData();

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                        Log.v(TAG, i + ", " + cursor.getColumnName(i) + " = " + cursor.getString(i));
                        if (cursor.getColumnName(i).equals("_id")) {
                                data._id = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("id")) {
                                data.id = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("name")) {
                                data.name = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("description")) {
                                data.description = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("organizationId")) {
                                data.organizationId = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("category")) {
                                data.category = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("total")) {
                                data.total = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("created_at")) {
                                data.created_at = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("updated_at")) {
                                data.updated_at = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("organization_name")) {
                                data.organization_name = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("organization_longitude")) {
                                data.organization_longitude = cursor.getDouble(i);
                        }
                        if (cursor.getColumnName(i).equals("organization_latitude")) {
                                data.organization_latitude = cursor.getDouble(i);
                        }
                }
                return data;
        }

        public void resultAllData() {
                Cursor cursor = getAllCursor();
                while (cursor.moveToNext()) {
                }
                cursor.close();
        }

        public Cursor getAllCursor() {
                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
                return cursor;
        }

        /** 獲取資料筆數 **/
        public int getCousorDataCount() {
                int count = -1;
                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
                count = cursor.getCount();
                cursor.close();
                return count;
        }

        /** 搜尋資料的ID是否重複 **/
        public boolean searchIDResult(String id) {

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE id='" + id + "\'", null);

                int count = cursor.getCount();
                cursor.close();
                // count = 0 , 無重複資料
                // count = 1 , 有重複資料
                if (count == 0) {
                        // 沒有重複ID，資料不存在
                        return false;
                } else {
                        // 有重複ID，資料存在
                        return true;
                }
        }

        /** 取得所有資料，以_id 遞減排序 **/
        public ArrayList<SupplyData> getDataFor_id() {
                Log.v(TAG, "getDataFor_id");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 取得所有資料，以距離遞增排序 **/
        public ArrayList<SupplyData> getDataForDistance(Double lng, Double lat) {
                Log.v(TAG, "getDataForDistance , lng : " + lng + "lat : " + lat);

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * ,(((organization_longitude-" + lng + ")*(organization_longitude-" + lng + ") + (organization_latitude-" + lat + ")*(organization_latitude-" + lat + "))) AS distance FROM " + TABLE_NAME + " ORDER BY distance ASC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }
        
        /** 取得所有資料，以時間遞減排序 **/
        public ArrayList<SupplyData> getDataForTime() {

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id ASC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 取得待刪除的資料資料 **/
        public ArrayList<SupplyData> getDeleteData() {
                Log.v(TAG, "getDeleteData");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE upload='2' ORDER BY _id DESC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋最新的資料 **/
        public ArrayList<SupplyData> getLatestData(String number) {
                Log.v(TAG, "getLatestData");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC LIMIT 0," + number + "", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋指定Id資料 **/
        public SupplyData getDataForId(String id) {
                Log.v(TAG, "getDataForId");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id='" + id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                SupplyData mData = new SupplyData();

                while (cursor.moveToNext()) {
                        mData = createData(cursor);
                }
                cursor.close();

                return mData;
        }

        /** 搜尋指定Id資料,排除upload狀態 **/
        public ArrayList<SupplyData> getDataForIdNoTag(String id) {
                Log.v(TAG, "getDataForIdNoTag");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id='" + id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋指定_Id資料 **/
        public ArrayList<SupplyData> getDataFor_Id(String _id) {
                Log.v(TAG, "getDataFor_Id");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id='" + _id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 確認db中有幾筆資料 **/
        public static int getDataCount(Context context) {
                int count = -1;
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                count = db.getCousorDataCount();
                db.closeDatabase();
                return count;
        }

        /** 取得db中所有資料, 以_id 遞減排序 **/
        public static ArrayList<SupplyData> getAllSuppliesData(Context context) {
                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataFor_id();
                db.closeDatabase();
                return mList;
        }

        /** 取得db中所有資料, 以距離(經緯度)遞增排序 **/
        public static ArrayList<SupplyData> getSuppliesDataForDistance(Context context, Double lng, Double lat) {
                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataForDistance(lng, lat);
                db.closeDatabase();
                return mList;
        }
        
        /** 取得db中所有資料, 以時間遞減排序 **/
        public static ArrayList<SupplyData> getSuppliesDataForTime(Context context) {
                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataForTime();
                db.closeDatabase();
                return mList;
        }

        /** 取得db中最新幾筆資料 **/
        public static ArrayList<SupplyData> getForLatest(Context context, String count) {
                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getLatestData(count);
                db.closeDatabase();
                return mList;
        }

        /** 以Id取得db中資料 **/
        public static SupplyData getForId(Context context, String id) {
                SupplyData mData = new SupplyData();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mData = db.getDataForId(id);
                db.closeDatabase();
                return mData;
        }

        /** 以_Id取得db中資料,排除upload狀態 **/
        public static ArrayList<SupplyData> getForIdNoTag(Context context, String id) {
                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataForIdNoTag(id);
                db.closeDatabase();
                return mList;
        }

        /** 以_Id取得db中資料 **/
        public static ArrayList<SupplyData> getFor_Id(Context context, String _id) {
                ArrayList<SupplyData> mList = new ArrayList<SupplyData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataFor_Id(_id);
                db.closeDatabase();
                return mList;
        }

        /** 以_Id刪除db中資料 **/
        public static void deleteFor_Id(Context context, String _id) {
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                db.delete(_id);
                db.closeDatabase();
        }

}
