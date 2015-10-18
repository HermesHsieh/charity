package tw.org.by37.supplieshelp;

import static android.provider.BaseColumns._ID;
import static tw.org.by37.supplieshelp.DBConstantsSupplies.*;

import java.util.ArrayList;

import tw.org.by37.organization.OrganizationData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBControlSupplies {

        private final static String TAG = DBControlSupplies.class.getName();

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
        public void add(SupportData mData) {
                Log.v(TAG, "add SupportData mData");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ID, mData.getId());
                values.put(NAME, mData.getName());
                values.put(DESCRIPTION, mData.getDescription());
                values.put(ORGANIZATIONID, mData.getOrganizationId());
                values.put(CATEGORY, mData.getGoodsTypeId());
                values.put(TOTAL, mData.getTotal());
                values.put(CREATED_AT, mData.getCreated_At());
                values.put(UPDATED_AT, mData.getUpdated_At());
                values.put(ORGANIZATION_NAME, mData.getOrganizationData()[0].getName());
                values.put(ORGANIZATION_LONGITUDE, mData.getOrganizationData()[0].getLongitude());
                values.put(ORGANIZATION_LATITUDE, mData.getOrganizationData()[0].getLatitude());
                db.insert(TABLE_NAME, null, values);
        }

        /** 更新資料 **/
        public void update(String id, SupportData mData) {
                Log.v(TAG, "update");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ID, mData.getId());
                values.put(NAME, mData.getName());
                values.put(DESCRIPTION, mData.getDescription());
                values.put(ORGANIZATIONID, mData.getOrganizationId());
                values.put(CATEGORY, mData.getGoodsTypeId());
                values.put(TOTAL, mData.getTotal());
                values.put(CREATED_AT, mData.getCreated_At());
                values.put(UPDATED_AT, mData.getUpdated_At());
                values.put(ORGANIZATION_NAME, mData.getOrganizationData()[0].getName());
                values.put(ORGANIZATION_LONGITUDE, mData.getOrganizationData()[0].getLongitude());
                values.put(ORGANIZATION_LATITUDE, mData.getOrganizationData()[0].getLatitude());
                db.update(TABLE_NAME, values, "id='" + id + "'", null);
        }

        /** 刪除資料 **/
        public void delete(String id) {
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                db.delete(TABLE_NAME, _ID + "=" + id, null);

                Log.e(TAG, "delete ID = '" + id + "' data Success");
        }

        private SupportData createData(Cursor cursor) {
                Log.i(TAG, "createData");

                SupportData mData = new SupportData();
                OrganizationData oData = new OrganizationData();
                OrganizationData[] oArray = new OrganizationData[1];

                try {
                        int idIndex = cursor.getColumnIndexOrThrow("id");
                        int nameIndex = cursor.getColumnIndexOrThrow("name");
                        int descIndex = cursor.getColumnIndexOrThrow("description");
                        int typeIndex = cursor.getColumnIndexOrThrow("category");
                        int oIdIndex = cursor.getColumnIndexOrThrow("organizationId");
                        int createIndex = cursor.getColumnIndexOrThrow("created_at");
                        int oNameIndex = cursor.getColumnIndexOrThrow("organization_name");
                        int oLngIndex = cursor.getColumnIndexOrThrow("organization_longitude");
                        int oLatIndex = cursor.getColumnIndexOrThrow("organization_latitude");

                        String id = cursor.getString(idIndex);
                        String name = cursor.getString(nameIndex);
                        String desc = cursor.getString(descIndex);
                        String type = cursor.getString(typeIndex);
                        String create = cursor.getString(createIndex);
                        String oId = cursor.getString(oIdIndex);
                        String oName = cursor.getString(oNameIndex);
                        String oLng = cursor.getString(oLngIndex);
                        String oLat = cursor.getString(oLatIndex);

                        mData.setId(id);
                        mData.setName(name);
                        mData.setDescription(desc);
                        mData.setGoodsTypeId(type);
                        mData.setCreated_At(create);
                        mData.setOrganizationId(oId);

                        oData.setType_Id(oId);
                        oData.setName(oName);
                        oData.setLatitude(oLat);
                        oData.setLongitude(oLng);

                        oArray[0] = oData;

                        mData.setOrganizationData(oArray);

                } catch (Exception e) {
                        e.printStackTrace();
                }
                return mData;
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
        public ArrayList<SupportData> getDataFor_id() {
                Log.v(TAG, "getDataFor_id");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 取得所有資料，以距離遞增排序 (orgSingle = 資料是否有重複機構) **/
        public ArrayList<SupportData> getDataForDistance(Double lng, Double lat, boolean orgSingle) {
                Log.v(TAG, "getDataForDistance , lng : " + lng + "lat : " + lat);

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = null;
                if (!orgSingle) {
                        cursor = db.rawQuery("SELECT * ,(((organization_longitude-" + lng + ")*(organization_longitude-" + lng + ") + (organization_latitude-" + lat + ")*(organization_latitude-" + lat + "))) AS distance FROM " + TABLE_NAME + " ORDER BY distance ASC", null);
                } else {
                        cursor = db.rawQuery("SELECT * ,(((organization_longitude-" + lng + ")*(organization_longitude-" + lng + ") + (organization_latitude-" + lat + ")*(organization_latitude-" + lat + "))) AS distance FROM " + TABLE_NAME + " GROUP BY organizationId ORDER BY distance ASC", null);
                }
                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 取得所有資料，以時間遞減排序 **/
        public ArrayList<SupportData> getDataForTime(boolean orgSingle) {

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = null;
                if (!orgSingle) {
                        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id ASC", null);
                } else {
                        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " GROUP BY organizationId ORDER BY id ASC", null);
                }
                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 取得待刪除的資料資料 **/
        public ArrayList<SupportData> getDeleteData() {
                Log.v(TAG, "getDeleteData");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE upload='2' ORDER BY _id DESC", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋最新的資料 **/
        public ArrayList<SupportData> getLatestData(String number) {
                Log.v(TAG, "getLatestData");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC LIMIT 0," + number + "", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋指定Id資料 **/
        public SupportData getDataForId(String id) {
                Log.v(TAG, "getDataForId");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id='" + id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                SupportData mData = new SupportData();

                while (cursor.moveToNext()) {
                        mData = createData(cursor);
                }
                cursor.close();

                return mData;
        }

        /** 搜尋指定Id資料,排除upload狀態 **/
        public ArrayList<SupportData> getDataForIdNoTag(String id) {
                Log.v(TAG, "getDataForIdNoTag");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id='" + id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** 搜尋指定_Id資料 **/
        public ArrayList<SupportData> getDataFor_Id(String _id) {
                Log.v(TAG, "getDataFor_Id");

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id='" + _id + "\'", null);

                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

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
        public static ArrayList<SupportData> getAllSuppliesData(Context context) {
                ArrayList<SupportData> mList = new ArrayList<SupportData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataFor_id();
                db.closeDatabase();
                return mList;
        }

        /** 取得db中所有資料, 以距離(經緯度)遞增排序 **/
        public static ArrayList<SupportData> getSuppliesDataForDistance(Context context, Double lng, Double lat, boolean orgSingle) {
                ArrayList<SupportData> mList = new ArrayList<SupportData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataForDistance(lng, lat, orgSingle);
                db.closeDatabase();
                return mList;
        }

        /** 取得db中所有資料, 以時間遞減排序 **/
        public static ArrayList<SupportData> getSuppliesDataForTime(Context context, boolean orgSingle) {
                ArrayList<SupportData> mList = new ArrayList<SupportData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataForTime(orgSingle);
                db.closeDatabase();
                return mList;
        }

        /** 取得db中最新幾筆資料 **/
        public static ArrayList<SupportData> getForLatest(Context context, String count) {
                ArrayList<SupportData> mList = new ArrayList<SupportData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getLatestData(count);
                db.closeDatabase();
                return mList;
        }

        /** 以Id取得db中資料 **/
        public static SupportData getForId(Context context, String id) {
                SupportData mData = new SupportData();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mData = db.getDataForId(id);
                db.closeDatabase();
                return mData;
        }

        /** 以_Id取得db中資料,排除upload狀態 **/
        public static ArrayList<SupportData> getForIdNoTag(Context context, String id) {
                ArrayList<SupportData> mList = new ArrayList<SupportData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataForIdNoTag(id);
                db.closeDatabase();
                return mList;
        }

        /** 以_Id取得db中資料 **/
        public static ArrayList<SupportData> getFor_Id(Context context, String _id) {
                ArrayList<SupportData> mList = new ArrayList<SupportData>();
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

        /** Test **/
        public static ArrayList<SupportData> getSuppliesDataForDistanceTest(Context context, Double lng, Double lat, boolean orgSingle, int category) {
                ArrayList<SupportData> mList = new ArrayList<SupportData>();
                DBControlSupplies db = new DBControlSupplies(context);
                db.openDatabase();
                mList = db.getDataForDistanceTest(lng, lat, orgSingle, category);
                db.closeDatabase();
                return mList;
        }

        /** 取得所有資料，以距離遞增排序 (orgSingle = 資料是否有重複機構) **/
        public ArrayList<SupportData> getDataForDistanceTest(Double lng, Double lat, boolean orgSingle, int category) {
                Log.v(TAG, "getDataForDistance , lng : " + lng + "lat : " + lat);

                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                Cursor cursor = null;
                if (!orgSingle) {
                        if (category > 0)
                                cursor = db.rawQuery("SELECT * ,(((organization_longitude-" + lng + ")*(organization_longitude-" + lng + ") + (organization_latitude-" + lat + ")*(organization_latitude-" + lat + "))) AS distance FROM " + TABLE_NAME + " WHERE category = '" + category + "' ORDER BY distance ASC", null);
                        else
                                cursor = db.rawQuery("SELECT * ,(((organization_longitude-" + lng + ")*(organization_longitude-" + lng + ") + (organization_latitude-" + lat + ")*(organization_latitude-" + lat + "))) AS distance FROM " + TABLE_NAME + " ORDER BY distance ASC", null);
                } else {
                        cursor = db.rawQuery("SELECT * ,(((organization_longitude-" + lng + ")*(organization_longitude-" + lng + ") + (organization_latitude-" + lat + ")*(organization_latitude-" + lat + "))) AS distance FROM " + TABLE_NAME + " GROUP BY organizationId ORDER BY distance ASC", null);
                }
                Log.v(TAG, "getCount : " + cursor.getCount());
                Log.v(TAG, "getColumnCount : " + cursor.getColumnCount());

                ArrayList<SupportData> mList = new ArrayList<SupportData>();

                while (cursor.moveToNext()) {
                        mList.add(createData(cursor));
                }
                cursor.close();

                return mList;
        }

        /** End of Test **/

}
