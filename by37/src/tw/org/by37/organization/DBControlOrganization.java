package tw.org.by37.organization;

import static android.provider.BaseColumns._ID;
import static tw.org.by37.organization.DBConstantsOrganization.*;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBControlOrganization {

        private final static String TAG = "dbOrganization";

        private DBHelperOrganization mDBHelper = null;

        private Context mContext;

        public DBControlOrganization(Context context) {
                this.mContext = context;
        }

        public void openDatabase() {
                Log.i(TAG, "openDatabase");
                mDBHelper = new DBHelperOrganization(mContext);
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
        public void add(OrganizationData mData) {
                Log.v(TAG, "add OrganizationData mData");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ORG_ID, mData.org_id);
                values.put(ORG_NAME, mData.org_name);
                values.put(ORG_EMAIL, mData.org_email);
                values.put(ORG_FAX, mData.org_fax);
                values.put(ORG_ADDRESS, mData.org_address);
                values.put(ORG_PHONE, mData.org_phone);
                values.put(ORG_DIV, mData.org_div);
                values.put(ORG_CITY, mData.org_city);
                values.put(ORG_TYPE, mData.org_type);
                values.put(ORG_LNG, mData.org_lng);
                values.put(ORG_LAT, mData.org_lat);
                values.put(ORG_CREATED_AT, mData.org_created_at);
                values.put(ORG_UPDATED_AT, mData.org_updated_at);
                db.insert(TABLE_NAME, null, values);
        }

        /** 更新資料 **/
        public void update(String id, OrganizationData mData) {
                Log.v(TAG, "update OrganizationData mData");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ORG_ID, mData.org_id);
                values.put(ORG_NAME, mData.org_name);
                values.put(ORG_EMAIL, mData.org_email);
                values.put(ORG_FAX, mData.org_fax);
                values.put(ORG_ADDRESS, mData.org_address);
                values.put(ORG_PHONE, mData.org_phone);
                values.put(ORG_DIV, mData.org_div);
                values.put(ORG_CITY, mData.org_city);
                values.put(ORG_TYPE, mData.org_type);
                values.put(ORG_LNG, mData.org_lng);
                values.put(ORG_LAT, mData.org_lat);
                values.put(ORG_CREATED_AT, mData.org_created_at);
                values.put(ORG_UPDATED_AT, mData.org_updated_at);
                db.update(TABLE_NAME, values, "org_id='" + id + "'", null);
        }

        /** 刪除資料 **/
        public void delete(String id) {
                // Log.v(TAG, "delete");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                db.delete(TABLE_NAME, _ID + "=" + id, null);

                // Toast.makeText(mContext, "刪除完成",
                // Toast.LENGTH_LONG).show();
                Log.e(TAG, "delete org_id = '" + id + "' data Success");
        }

        private OrganizationData createData(Cursor cursor) {

                OrganizationData data = new OrganizationData();

                for (int i = 0; i < cursor.getColumnCount(); i++) {

                        Log.v(TAG, i + ", " + cursor.getColumnName(i) + " = " + cursor.getString(i));
                        if (cursor.getColumnName(i).equals("_id")) {
                                data._id = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_id")) {
                                data.org_id = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_name")) {
                                data.org_name = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_email")) {
                                data.org_email = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_fax")) {
                                data.org_fax = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_address")) {
                                data.org_address = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_phone")) {
                                data.org_phone = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_div")) {
                                data.org_div = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_city")) {
                                data.org_city = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_type")) {
                                data.org_type = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_lng")) {
                                data.org_lng = cursor.getDouble(i);
                        }
                        if (cursor.getColumnName(i).equals("org_lat")) {
                                data.org_lat = cursor.getDouble(i);
                        }
                        if (cursor.getColumnName(i).equals("org_created_at")) {
                                data.org_created_at = cursor.getString(i);
                        }
                        if (cursor.getColumnName(i).equals("org_updated_at")) {
                                data.org_updated_at = cursor.getString(i);
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
                cursor.getCount();
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
                Cursor cursor = db.rawQuery("SELECT org_id FROM " + TABLE_NAME + " WHERE org_id='" + id + "\'", null);

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
        public ArrayList<OrganizationData> getDataFor_id() {
                Log.v(TAG, "getDataFor_id");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 取得所有資料，以距離遞增減排序 **/
        public ArrayList<OrganizationData> getDataForDistance(Double lng, Double lat) {
                Log.v(TAG, "getDataForDistance , lng : " + lng + "lat : " + lat);

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * ,(((organization_longitude-" + lng + ")*(organization_longitude-" + lng + ") + (organization_latitude-" + lat + ")*(organization_latitude-" + lat + "))) AS distance FROM " + TABLE_NAME + " ORDER BY distance ASC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 取得待刪除的資料資料 **/
        public ArrayList<OrganizationData> getDeleteData() {
                Log.v(TAG, "getDeleteData");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE upload='2' ORDER BY _id DESC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋最新的資料 **/
        public ArrayList<OrganizationData> getLatestData(String number) {
                Log.v(TAG, "getLatestData");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC LIMIT 0," + number + "", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋指定Id資料 **/
        public OrganizationData getDataForId(String id) {
                Log.v(TAG, "getDataForId");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE org_id='" + id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                OrganizationData mData = new OrganizationData();

                while (cursor.moveToNext()) {
                        mData = createData(cursor);
                }
                cursor.close();

                return mData;
        }

        /** 搜尋指定Id資料,排除upload狀態 **/
        public ArrayList<OrganizationData> getDataForIdNoTag(String id) {
                Log.v(TAG, "getDataForIdNoTag");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id='" + id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋指定_Id資料 **/
        public ArrayList<OrganizationData> getDataFor_Id(String _id) {
                Log.v(TAG, "getDataFor_Id");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id='" + _id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 確認db中有幾筆資料 **/
        public static int getDataCount(Context context) {
                int count = -1;
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                count = db.getCousorDataCount();
                db.closeDatabase();
                return count;
        }

        /** 取得db中所有資料, 以_id 遞減排序 **/
        public static ArrayList<OrganizationData> getAllSuppliesData(Context context) {
                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                mList = db.getDataFor_id();
                db.closeDatabase();
                return mList;
        }

        /** 取得db中所有資料, 以距離(經緯度)遞增排序 **/
        public static ArrayList<OrganizationData> getSuppliesDataForDistance(Context context, Double lng, Double lat) {
                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                mList = db.getDataForDistance(lng, lat);
                db.closeDatabase();
                return mList;
        }

        /** 取得db中最新幾筆資料 **/
        public static ArrayList<OrganizationData> getForLatest(Context context, String count) {
                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                mList = db.getLatestData(count);
                db.closeDatabase();
                return mList;
        }

        /** 以Id取得db中資料 **/
        public static OrganizationData getForId(Context context, String id) {
                OrganizationData mData = new OrganizationData();
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                mData = db.getDataForId(id);
                db.closeDatabase();
                return mData;
        }

        /** 以_Id取得db中資料,排除upload狀態 **/
        public static ArrayList<OrganizationData> getForIdNoTag(Context context, String id) {
                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                mList = db.getDataForIdNoTag(id);
                db.closeDatabase();
                return mList;
        }

        /** 以_Id取得db中資料 **/
        public static ArrayList<OrganizationData> getFor_Id(Context context, String _id) {
                ArrayList<OrganizationData> mList = new ArrayList<OrganizationData>();
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                mList = db.getDataFor_Id(_id);
                db.closeDatabase();
                return mList;
        }

        /** 以_Id刪除db中資料 **/
        public static void deleteFor_Id(Context context, String _id) {
                DBControlOrganization db = new DBControlOrganization(context);
                db.openDatabase();
                db.delete(_id);
                db.closeDatabase();
        }

}
