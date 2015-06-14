package tw.org.by37.supplieshelp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.R;
import tw.org.by37.OrganizationActivity;
import tw.org.by37.data.TypesData;
import tw.org.by37.data.GoodsTypesText;
import tw.org.by37.data.OrganizationTypesText;
import tw.org.by37.data.SelectingData;
import tw.org.by37.data.SupplyData;
import tw.org.by37.organization.DBControlOrganization;
import tw.org.by37.organization.OrganizationFragment;
import tw.org.by37.productsell.NewProductActivity;
import tw.org.by37.service.OrganizationApiService;
import tw.org.by37.service.SuppliesApiService;
import tw.org.by37.util.FunctionUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static tw.org.by37.config.RequestCode.*;
import static tw.org.by37.config.SysConfig.*;

public class SuppliesHelpFragment extends Fragment {

        private final static String TAG = "SuppliesHelpFragment";
        private final static String pTAG = "Position";

        private Context mContext;

        /** 是否為簡易模式(即首頁的顯示狀態) **/
        private boolean index_view = false;

        private TextView tv_supplies_total;

        private ListView mListView;

        private SuppliesListAdapter mSuppliesListAdapter;

        private Handler mHandler;

        private int pref_order = 0;
        /** 物資類別的代碼 **/
        private int pref_category = 0;

        private double test_lng = 121.4584833;
        private double test_lat = 25.0915245;

        /** 是否使用資料庫資料,不更新伺服器資料(測試階段用) **/
        public static final boolean use_dbData = false;

        /** 設定資料庫格式是否有設變,若有則刪除資料庫 **/
        private boolean db_modify = false;

        private float data_updateTime = 0;
        private long data_types_updateTime = 0;

        /** 物資需求更新資料的時間 **/
        private int update_hours = 2;

        /** 物資需求類別更新資料的時間 **/
        private int update_days = 1;

        /** MyLocation Param **/
        /** 預設值:台北車站 **/
        public static double defult_latitude = 25.046521;
        public static double defult_longitude = 121.517511;
        public static double wifi_latitude = -1;
        public static double wifi_longitude = -1;
        public static double gps_latitude = -1;
        public static double gps_longitude = -1;

        private LocationManager lms;
        private gpsLocationListener gll;
        private wifiLocationListener wll;
        private boolean getService = false;
        private Location gps_location;
        private Location wifi_location;

        /** Location Position 更新的時間限制,單位:毫秒 (5秒) **/
        private final static int gps_updateTime = 5000;
        /** Location Position 更新的距離限制,單位:米 **/
        private final static int gps_updateDst = 0;

        /** End of MyLocation Param **/

        /** Organization Fragment **/
        private OrganizationFragment mOrganizationFragment;

        /** 物資排序 Spinner **/
        private Spinner sp_order;

        /** 物資類別 Spinner **/
        private Spinner sp_category;

        /**
         * 目的:顯示該機構的物資需求資料. 從OrganizationFragment bundle過來的機構名稱資料,判別是否用機構名稱來篩選資料
         **/
        private String org_name;

        public SuppliesHelpFragment() {
                super();
        }

        /** 設定在首頁顯示與否的參數 **/
        public void setIndexView(boolean view) {
                index_view = view;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                mContext = getActivity();
                try {
                        Bundle bundle = getActivity().getIntent().getExtras();
                        org_name = bundle.getString("org_name", null);
                        Log.e(TAG, "Get org_name : " + org_name);
                } catch (Exception e) {
                        Log.e(TAG, "Get org_name Exception");
                }

                checkPreferStatus();

                View view = inflater.inflate(R.layout.fragment_supplieshelp, container, false);

                initLocationManager();

                // getOrganizationTypesData();

                getSuppliesHelpData();

                findView(view);

                getSuppliesTypesData();

                return view;

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                tv_supplies_total = (TextView) view.findViewById(R.id.tv_supplies_total);
                // new ListView
                mListView = (ListView) view.findViewById(android.R.id.list);
                // new ListView Adapter
                mSuppliesListAdapter = new SuppliesListAdapter(getActivity());
                // 設定是否為首頁顯示
                mSuppliesListAdapter.setIndexView(index_view);
                // 配適 ListView Adapter
                mListView.setAdapter(mSuppliesListAdapter);

                mListView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                                Log.i(TAG, "Click Supplies Item : " + position);

                                // String suppliesId =
                                // mSuppliesListAdapter.getSuppliesDataId(position);
                                SelectingData.suppliesId = mSuppliesListAdapter.getSuppliesDataId(position);

                                // String organizationId =
                                // mSuppliesListAdapter.getSuppliesDataOrganizationId(position);

                                SelectingData.organizationId = mSuppliesListAdapter.getSuppliesDataOrganizationId(position);

                                // SupplyData mData =
                                // DBControlSupplies.getForId(mContext,
                                // suppliesId);

                                SelectingData.mSupplyData = DBControlSupplies.getForId(mContext, SelectingData.suppliesId);

                                // switchOrganizationFragment(mData);

                                gotoOrganizationActivity();

                                // Log.v(TAG, "Supplies ID : " + suppliesId);
                                // Log.v(TAG, "Organizations ID : " +
                                // suppliesId);
                        }
                });

                sp_category = (Spinner) view.findViewById(R.id.spinner_category);

                sp_order = (Spinner) view.findViewById(R.id.spinner_order);
                // 建立一個ArrayAdapter物件，並放置下拉選單的內容
                ArrayAdapter<String> adapter_order = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, new String[] { "依距離", "依時間" });
                // 設定下拉選單的樣式
                adapter_order.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_order.setAdapter(adapter_order);
                // 設定上次的選項
                sp_order.setSelection(pref_order);
                sp_order.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                                pref_order = position;

                                savePreferences(mContext, pref_order, pref_category);

                                Log.i(TAG, "savePreferences pref_order = " + pref_order);

                                showSuppliesDataToListView();
                        }

                        public void onNothingSelected(AdapterView arg0) {
                                Toast.makeText(mContext, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
                        }
                });
        }

        /** 將物資資料根據排序規則及篩選結果輸出至ListView **/
        private void showSuppliesDataToListView() {
                getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                // 依照距離排序
                                if (sp_order.getSelectedItemPosition() == 0) {
                                        orderSuppliesDataForDst();
                                } else {
                                        // 依照時間排序
                                        if (sp_order.getSelectedItemPosition() == 1) {
                                                orderSuppliesDataForTime();
                                        } else {
                                                Log.e(TAG, "Set Supplies Data error !!");
                                        }
                                }
                        }
                });
        }

        /** 獲取物資需求資料 **/
        private void getSuppliesHelpData() {
                Log.i(TAG, "getSuppliesData");
                // 獲取上次更新的時間
                data_updateTime = getDataUpdateTimePreferences(mContext);
                
                new getSuppliesTotalAsyncTask().execute();

                new getSuppliesDataAsyncTask().execute();

        }

        /** 獲取物資需求類別資料 **/
        private void getSuppliesTypesData() {
                Log.i(TAG, "getSuppliesTypesData");

                // 假如檔案存在
                if (GoodsTypesText.existFile()) {
                        // 獲取上次更新的時間
                        data_types_updateTime = getDataTypesUpdateTimePreferences(mContext);

                        Log.i(TAG, "Time 1 : " + System.currentTimeMillis());
                        Log.i(TAG, "Time 2 : " + data_types_updateTime);
                        Log.i(TAG, "Time 3 : " + (System.currentTimeMillis() - data_types_updateTime));
                        Log.i(TAG, "Time 4 : " + (update_days * 24 * 3600 * 1000));

                        // 如果時間大於1天
                        if (System.currentTimeMillis() - data_types_updateTime > update_days * 24 * 3600 * 1000) {
                                Log.i(TAG, "update GoodsTypes Data");

                                /** 獲取物資需求類別資料 **/
                                new getGoodsTypesAsyncTask().execute();
                        }
                } else {
                        // 從伺服器取得資料
                        new getGoodsTypesAsyncTask().execute();
                }

                /** 是否有類別資料 **/
                if (SelectingData.mGoodsTypes.size() > 0) {
                        /** 直接設定至Spinner **/
                        setGoodsTypesInSpinner();
                } else {
                        /** 從txt檔讀取類別資料,並設定至Spinner **/
                        getGoodsTypes();
                }
        }

        /** 獲取物資需求類別資料 **/
        @SuppressWarnings("unused")
        private void getOrganizationTypesData() {
                Log.i(TAG, "getOrganizationTypesData");
                OrganizationTypesText mData = new OrganizationTypesText();
                // 假如檔案存在
                if (mData.existFile()) {
                        // 獲取上次更新的時間
                        data_types_updateTime = getDataTypesUpdateTimePreferences(mContext);
                        // 如果時間大於1天
                        if (System.currentTimeMillis() - data_types_updateTime > update_days * 24 * 3600 * 1000) {
                                // 從伺服器更新資料
                                new getOrganizationTypesAsyncTask().execute();
                        } else {
                                // 讀取txt資料
                                getGoodsTypes();
                        }
                } else {
                        // 假如檔案不存在, 從伺服器更新資料
                        new getOrganizationTypesAsyncTask().execute();
                }
        }

        class getSuppliesTotalAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = null;

                        // 撈取資料庫筆數
                        result = String.valueOf(DBControlSupplies.getDataCount(mContext));

                        Log.i(TAG, "Supplies Total : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (tv_supplies_total != null && result != null)
                                tv_supplies_total.setText(result);
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                }
        }

        class getSuppliesDataAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = null;

                        /** 如果資料庫有設變,則刪除先前的資料庫 **/
                        if (db_modify) {
                                new DBHelperSupplies(mContext).deleteDataBase();
                                Log.e(TAG, "The database modify, Delete old database !!");
                                removeDataUpdateTimePreferences(mContext);
                                Log.e(TAG, "Remove the data update time preferences !!");
                        }

                        // 如果db中資料筆數大於0
                        if (DBControlSupplies.getDataCount(mContext) > 0) {
                                // 是否使用資料庫資料,不更新伺服器資料(測試階段用)
                                Log.i(TAG, "use_dbData = " + use_dbData);
                                if (!use_dbData) {
                                        // 離上次更新時間大於兩個小時(7200秒)
                                        if (System.currentTimeMillis() - data_updateTime > update_hours * 3600 * 1000) {
                                                Log.i(TAG, "Supplies Data update...");
                                                // 進行更新
                                                result = SuppliesApiService.getAllSuppliesData();
                                        } else {
                                                Log.i(TAG, "Supplies Data don't update...");
                                        }
                                }
                        } else {
                                // 第一次使用,手機內沒有資料,抓取伺服器資料
                                Log.i(TAG, "Supplies Data update...");
                                // 進行更新
                                result = SuppliesApiService.getAllSuppliesData();
                        }
                        Log.i(TAG, "SuppliesData Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (result != null) {
                                // 跟伺服器更新資料的結果
                                try {
                                        JSONArray mJsonArray = new JSONArray(result);
                                        Log.i(TAG, "mJsonArray Length : " + mJsonArray.length());

                                        /** 儲存更新的時間 **/
                                        saveDataUpdateTimePreferences(mContext, System.currentTimeMillis());

                                        DBControlSupplies mDB = new DBControlSupplies(mContext);
                                        /** 開啟資料庫 **/
                                        mDB.openDatabase();

                                        for (int i = 0; i < mJsonArray.length(); i++) {
                                                SupplyData mData = new SupplyData();
                                                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                                                try {
                                                        mData.id = mJsonObject.getString("id");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                                try {
                                                        mData.name = mJsonObject.getString("name");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                                try {
                                                        mData.description = mJsonObject.getString("description");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                                try {
                                                        mData.organizationId = mJsonObject.getString("organizationId");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                                try {
                                                        mData.total = mJsonObject.getString("total");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                                try {
                                                        mData.created_at = mJsonObject.getString("created_at");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                                try {
                                                        mData.updated_at = mJsonObject.getString("updated_at");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                                try {
                                                        mData.category = mJsonObject.getString("goodsTypeId");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }

                                                Log.i(TAG, "Organization JSONArray length : " + mJsonObject.getJSONArray("organization").length());

                                                for (int j = 0; j < mJsonObject.getJSONArray("organization").length(); j++) {
                                                        try {
                                                                mData.organization_id = mJsonObject.getJSONArray("organization").getJSONObject(j).getString("id");
                                                        } catch (JSONException e) {
                                                                e.printStackTrace();
                                                        }
                                                        try {
                                                                mData.organization_name = mJsonObject.getJSONArray("organization").getJSONObject(j).getString("name");
                                                        } catch (JSONException e) {
                                                                e.printStackTrace();
                                                        }
                                                        try {
                                                                mData.organization_longitude = mJsonObject.getJSONArray("organization").getJSONObject(j).getDouble("longitude");
                                                        } catch (JSONException e) {
                                                                e.printStackTrace();
                                                        }
                                                        try {
                                                                mData.organization_latitude = mJsonObject.getJSONArray("organization").getJSONObject(j).getDouble("latitude");
                                                        } catch (JSONException e) {
                                                                e.printStackTrace();
                                                        }
                                                }

                                                /** 將單一筆資料新增/更新至 supplies.db 中 **/
                                                if (!mDB.searchIDResult(mData.id)) { // 比對db中是否已有此資料
                                                        // 如果沒有這筆資料,則新增資料
                                                        mDB.add(mData);
                                                } else {
                                                        // 如果已經有這筆資料,則更新資料
                                                        mDB.update(mData.id, mData);
                                                }
                                        }

                                        /** 關閉資料庫 **/
                                        mDB.closeDatabase();

                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "The SuppliesData from Server == null !!");
                                }

                        } else {
                                Log.e(TAG, "SuppliesData Result == null, (getSuppliesDataAsyncTask) ");
                        }

                        showSuppliesDataToListView();

                        new getSuppliesTotalAsyncTask().execute();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                }
        }

        class getGoodsTypesAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        // 從伺服器取得資料
                        String result = SuppliesApiService.getGoodsTypes();
                        Log.i(TAG, "GoodsTypes Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (result != null) {
                                // 跟伺服器更新資料的結果
                                try {
                                        JSONArray mJsonArray = new JSONArray(result);
                                        Log.i(TAG, "mJsonArray Length : " + mJsonArray.length());

                                        /** 儲存更新的時間 **/
                                        saveDataTypesUpdateTimePreferences(mContext, System.currentTimeMillis());

                                        /** 將資料儲存成txt檔 **/
                                        GoodsTypesText.saveText(result);

                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "The GoodsTypesData from Server == null!!");
                                }
                        }

                        /** 從txt檔讀取類別資料,並設定至Spinner **/
                        getGoodsTypes();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                }
        }

        /** 從txt檔取得物品的類別資料 **/
        private void getGoodsTypes() {
                Log.i(TAG, "getGoodsTypes");

                String result = GoodsTypesText.readText();

                if (result != null) {
                        try {
                                JSONArray mJsonArray = new JSONArray(result);
                                Log.i(TAG, "GoodsTypes Length : " + mJsonArray.length());

                                /** 清空物資類別清單 **/
                                SelectingData.clearGoodsTypesList();

                                for (int i = 0; i < mJsonArray.length(); i++) {
                                        TypesData mData = new TypesData();
                                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                                        try {
                                                mData.id = mJsonObject.getString("id");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                        try {
                                                mData.name = mJsonObject.getString("name");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                        try {
                                                mData.created_at = mJsonObject.getString("created_at");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                        try {
                                                mData.updated_at = mJsonObject.getString("updated_at");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }

                                        /** 加入到物資類別清單 **/
                                        SelectingData.addGoodsTypesListItem(mData);

                                }
                        } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "GoodsTypes JSONException!");
                        }
                } else {
                        Log.e(TAG, "Read GoodsTypes Result == null!");
                }

                setGoodsTypesInSpinner();

                Log.i(TAG, SelectingData.getGoodsTypesListItemString());
        }

        /** 依照距離排序 **/
        private void orderSuppliesDataForDst() {
                /**
                 * ListView 顯示用的資料,從db中取得資料(依距離)
                 **/
                ArrayList<SupplyData> mList = DBControlSupplies.getSuppliesDataForDistance(mContext, wifi_longitude, wifi_latitude, false);

                ArrayList<SupplyData> mList_select = new ArrayList<SupplyData>();

                ArrayList<SupplyData> mList_index = new ArrayList<SupplyData>();

                /** 篩選類別代號!=0的物資 **/
                if (pref_category != 0) {
                        for (int i = 0; i < mList.size(); i++) {
                                Log.i(TAG, "Category : " + mList.get(i).category + ", pref_category : " + pref_category);
                                /** 如果該物品的類別跟目前篩選的類別是一致的 **/
                                if (mList.get(i).category.equals(String.valueOf(pref_category))) {
                                        Log.i(TAG, "mList_select Add !");
                                        mList_select.add(mList.get(i));
                                }
                        }
                } else {
                        /** 顯示全部類別的物資 **/
                        mList_select = mList;
                }

                Log.i(TAG, "mList_select : " + mList_select.size());

                /** 顯示該機構的所有物資 **/
                if (org_name != null) {
                        for (int i = 0; i < mList_select.size(); i++) {
                                if (mList_select.get(i).organization_name.equals(org_name)) {
                                        mList_index.add(mList_select.get(i));
                                }
                        }
                        mList_select.clear();
                        mList_select = mList_index;
                } else {
                        /** 首頁顯示,最多三筆資料 **/
                        if (index_view) {
                                mList.clear();
                                mList_select.clear();
                                mList_index.clear();
                                mList = DBControlSupplies.getSuppliesDataForDistance(mContext, wifi_longitude, wifi_latitude, true);

                                /** 篩選類別代號!=0的物資 **/
                                if (pref_category != 0) {
                                        for (int i = 0; i < mList.size(); i++) {
                                                Log.i(TAG, "Category : " + mList.get(i).category + ", pref_category : " + pref_category);
                                                /** 如果該物品的類別跟目前篩選的類別是一致的 **/
                                                if (mList.get(i).category.equals(String.valueOf(pref_category))) {
                                                        Log.i(TAG, "mList_select Add !");
                                                        mList_select.add(mList.get(i));
                                                }
                                        }
                                } else {
                                        /** 顯示全部類別的物資 **/
                                        mList_select = mList;
                                }

                                /** 首頁最多只顯示三筆資料 **/
                                int count = mList_select.size();
                                if (count > 3) {
                                        while (mList_select.size() > 3) {
                                                mList_select.remove(3);
                                        }
                                }
                        }
                }

                Log.i(TAG, "mList_select : " + mList_select.size());

                /** 最後將ListView的資料設定到Adapter中 **/
                if (mSuppliesListAdapter != null) {
                        mSuppliesListAdapter.setListData(mList_select, wifi_longitude, wifi_latitude);
                }
        }

        /** 依照時間排序 **/
        private void orderSuppliesDataForTime() {
                /**
                 * ListView 顯示用的資料,從db中取得資料(依時間)
                 **/
                ArrayList<SupplyData> mList = DBControlSupplies.getSuppliesDataForTime(mContext, false);

                ArrayList<SupplyData> mList_select = new ArrayList<SupplyData>();

                ArrayList<SupplyData> mList_index = new ArrayList<SupplyData>();

                if (pref_category != 0) {
                        for (int i = 0; i < mList.size(); i++) {
                                Log.i(TAG, "Category : " + mList.get(i).category + ", pref_category : " + pref_category);
                                if (mList.get(i).category.equals(String.valueOf(pref_category))) {
                                        Log.i(TAG, "mList_select Add !");
                                        mList_select.add(mList.get(i));
                                }
                        }
                } else {
                        mList_select = mList;
                }

                Log.i(TAG, "mList_select : " + mList_select.size());

                /** 以機構名稱篩選 **/
                if (org_name != null) {
                        for (int i = 0; i < mList_select.size(); i++) {
                                if (mList_select.get(i).organization_name.equals(org_name)) {
                                        mList_index.add(mList_select.get(i));
                                }
                        }
                        mList_select.clear();
                        mList_select = mList_index;
                } else {
                        /** 首頁顯示,最多三筆資料 **/
                        if (index_view) {
                                mList.clear();
                                mList_select.clear();
                                mList_index.clear();
                                mList = DBControlSupplies.getSuppliesDataForTime(mContext, true);

                                /** 篩選類別代號!=0的物資 **/
                                if (pref_category != 0) {
                                        for (int i = 0; i < mList.size(); i++) {
                                                Log.i(TAG, "Category : " + mList.get(i).category + ", pref_category : " + pref_category);
                                                /** 如果該物品的類別跟目前篩選的類別是一致的 **/
                                                if (mList.get(i).category.equals(String.valueOf(pref_category))) {
                                                        Log.i(TAG, "mList_select Add !");
                                                        mList_select.add(mList.get(i));
                                                }
                                        }
                                } else {
                                        /** 顯示全部類別的物資 **/
                                        mList_select = mList;
                                }

                                /** 首頁最多只顯示三筆資料 **/
                                int count = mList_select.size();
                                if (count > 3) {
                                        while (mList_select.size() > 3) {
                                                mList_select.remove(3);
                                        }
                                }
                        }
                }

                Log.i(TAG, "mList_select : " + mList_select.size());
                /** 最後將ListView的資料設定到Adapter中 **/
                if (mSuppliesListAdapter != null) {
                        mSuppliesListAdapter.setListData(mList_select, wifi_longitude, wifi_latitude);
                }
        }

        /** 將物資類別設定於Spinner中 **/
        private void setGoodsTypesInSpinner() {
                String[] category = new String[SelectingData.mGoodsTypes.size()];

                for (int i = 0; i < SelectingData.mGoodsTypes.size(); i++) {
                        category[i] = SelectingData.mGoodsTypes.get(i).name;
                }
                // new String[] { "生活3C", "家庭電器", "衣物飾品", "生活用品", "嬰孩物品",
                // "大型家具", "種子、植物", "書籍", "手工藝品", "其他" }
                // 建立一個ArrayAdapter物件，並放置下拉選單的內容
                ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, category);
                // 設定下拉選單的樣式
                adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (sp_category != null) {
                        sp_category.setAdapter(adapter_category);
                        // 設定上次的選項
                        sp_category.setSelection(pref_category);
                        sp_category.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                                        pref_category = position;

                                        savePreferences(mContext, pref_order, pref_category);

                                        Log.i(TAG, "savePreferences pref_category = " + pref_category);

                                        showSuppliesDataToListView();
                                }

                                public void onNothingSelected(AdapterView arg0) {
                                        Toast.makeText(mContext, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
                                }
                        });
                }
        }

        class getOrganizationTypesAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = null;

                        // 從伺服器取得資料
                        result = OrganizationApiService.getOrganizationTypes();

                        Log.i(TAG, "GoodsTypes Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (result != null) {
                                // 跟伺服器更新資料的結果
                                try {
                                        JSONArray mJsonArray = new JSONArray(result);
                                        Log.i(TAG, "mJsonArray Length : " + mJsonArray.length());

                                        /** 儲存更新的時間 **/
                                        saveDataTypesUpdateTimePreferences(mContext, System.currentTimeMillis());

                                        /** 將資料儲存成txt檔 **/
                                        OrganizationTypesText.saveText(result);

                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "The GoodsTypesData from Server == null!!");
                                }

                        } else {
                                Log.e(TAG, "GoodsTypesData Result == null, (getGoodsTypesAsyncTask) ");

                                try {
                                        copyOrganizationTypesDataBase(mContext, OrganizationTypesText.getTextPath());
                                        Log.e(TAG, "copyOrganizationTypesDataBase from RAW source");
                                } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }

                        getOrganizationTypes();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                }
        }

        /** 從txt檔取得物品的類別資料 **/
        private void getOrganizationTypes() {
                Log.i(TAG, "getOrganizationTypes");
                String result = null;

                result = OrganizationTypesText.readText();

                if (result != null) {
                        try {
                                JSONArray mJsonArray = new JSONArray(result);
                                Log.i(TAG, "mJsonArray Length : " + mJsonArray.length());

                                /** 清空基構類別清單 **/
                                SelectingData.clearOrganizationTypesList();

                                for (int i = 0; i < mJsonArray.length(); i++) {
                                        TypesData mData = new TypesData();
                                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                                        try {
                                                mData.id = mJsonObject.getString("id");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                        try {
                                                mData.name = mJsonObject.getString("name");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                        try {
                                                mData.created_at = mJsonObject.getString("created_at");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                        try {
                                                mData.updated_at = mJsonObject.getString("updated_at");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }

                                        /** 加入到物資類別清單 **/
                                        SelectingData.addOrganizationTypesListItem(mData);

                                }
                        } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "The GoodsTypesData from Server == null!!");
                        }
                } else {
                        Log.e(TAG, "getGoodsTypes Result == null !!");
                }

                Log.i(TAG, "getOrganizationTypesListItemString\n" + SelectingData.getOrganizationTypesListItemString());
        }

        /** 複製預設物資分類的資料到手機儲存路徑 **/
        public static void copyGoodsTypesDataBase(Context context, String filePath) throws IOException {
                FileOutputStream os = new FileOutputStream(filePath);// 得到數據庫文件的寫入流
                InputStream is = context.getResources().openRawResource(R.raw.goods_types);// 得到數據庫文件的數據流
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                        os.write(buffer, 0, count);
                        os.flush();
                }
                is.close();
                os.close();
        }

        /** 複製預設基構分類的資料到手機儲存路徑 **/
        public static void copyOrganizationTypesDataBase(Context context, String filePath) throws IOException {
                FileOutputStream os = new FileOutputStream(filePath);// 得到數據庫文件的寫入流
                InputStream is = context.getResources().openRawResource(R.raw.organization_types);// 得到數據庫文件的數據流
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                        os.write(buffer, 0, count);
                        os.flush();
                }
                is.close();
                os.close();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);
        }

        /** Location Manager **/
        public void initLocationManager() {
                gll = new gpsLocationListener();
                wll = new wifiLocationListener();

                /** 取得系統定位服務 **/
                LocationManager status = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        // 如果GPS和網路定位開啟，呼叫locationServiceInitial()更新位置
                        getService = true; // 確認開啟定位服務
                        locationServiceInitial();
                } else {
                        Log.e(TAG, "LocationProvider Disable");
                        // 開啟設定頁面
                        // Toast.makeText(this, "請開啟定位服務",
                        // Toast.LENGTH_LONG).show();
                        // startActivity(new
                        // Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }

        }

        private void locationServiceInitial() {
                lms = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE); // 取得系統定位服務
                gps_location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                wifi_location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (LocationManager.GPS_PROVIDER != null) {
                        getGPSLocation(gps_location);
                } else {
                        Log.e(TAG, "LocationManager.GPS_PROVIDER == null");
                }
                if (LocationManager.NETWORK_PROVIDER != null) {
                        getWiFiLLocation(wifi_location);
                } else {
                        Log.e(TAG, "LocationManager.NETWORK_PROVIDER == null");
                }
        }

        private void getGPSLocation(Location gps_location) {
                Log.v(TAG, "getGPSLocation");
                if (gps_location != null) {
                        gps_longitude = gps_location.getLongitude();
                        gps_latitude = gps_location.getLatitude();
                } else {
                        Log.e(TAG, "GPSLocation == null");
                }
        }

        private void getWiFiLLocation(Location wifi_location) {
                Log.v(TAG, "getWiFiLLocation");
                if (wifi_location != null) {
                        wifi_longitude = wifi_location.getLongitude();
                        wifi_latitude = wifi_location.getLatitude();
                } else {
                        Log.e(TAG, "WiFiLLocation == null");
                }
        }

        class gpsLocationListener implements LocationListener {
                @Override
                public void onLocationChanged(Location gps_location) { // 當地點改變時
                        // TODO Auto-generated method stub
                        getGPSLocation(gps_location);
                        Log.i(pTAG, "GPS Location Change");
                        Log.i(pTAG, "gps_latitude : " + gps_latitude);
                        Log.i(pTAG, "gps_longitude : " + gps_longitude);
                }

                @Override
                public void onProviderDisabled(String arg0) { // 當GPS定位功能關閉時
                        // TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String arg0) { // 當GPS定位功能開啟時
                        // TODO Auto-generated method stub
                }

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) { // 定位狀態改變
                        // TODO Auto-generated method stub
                }
        }

        class wifiLocationListener implements LocationListener {

                @Override
                public void onLocationChanged(Location wifi_location) { // 當地點改變時
                        // TODO Auto-generated method stub
                        getWiFiLLocation(wifi_location);
                        Log.i(pTAG, "Wifi Location Change");
                        Log.i(pTAG, "wifi_latitude : " + wifi_latitude);
                        Log.i(pTAG, "wifi_longitude : " + wifi_longitude);
                }

                @Override
                public void onProviderDisabled(String arg0) { // 當網路定位功能關閉時
                        // TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String arg0) { // 當網路定位功能開啟
                        // TODO Auto-generated method stub
                }

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) { // 定位狀態改變
                        // TODO Auto-generated method stub
                }
        }

        /** End of Location Manager **/

        /** Preferences **/
        /** End of Preferences **/

        /** GotoActivity **/
        public void gotoOrganizationActivity() {
                Intent intent = new Intent();
                intent.setClass(mContext, OrganizationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, ORGANIZATION_ACTIVITY_CODE);
        }

        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        /**
         * switchOrganizationFragment 介面
         */
        public void switchOrganizationFragment(SupplyData mData) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mOrganizationFragment == null)
                        mOrganizationFragment = new OrganizationFragment();

                mOrganizationFragment.setSupplyData(mData);

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mOrganizationFragment);
                } else {
                        ft.replace(R.id.fragment_content, mOrganizationFragment);
                }
                ft.commit();
        }

        /** 判斷 Preferences (篩選距離,類別) 內是否有儲存的資料,若有即上次使用過,則設定上次使用時的資料狀態 **/
        private void checkPreferStatus() {
                Log.i(TAG, "checkPreferStatus");
                String result = getPreferences(mContext);
                Log.i(TAG, "checkPreferStatus Result : " + result);

                Log.i(TAG, "pref_order : " + pref_order + ", pref_category : " + pref_category);

                if (result != null) {
                        /** 有Preferences參數(篩選距離,類別), 有上一次的資料, 設定到相關元件參數上 **/
                        Log.i(TAG, "有Preferences參數, 有上一次的資料, 設定到相關元件參數上");
                } else {
                        /** 沒有Preferences參數(篩選距離,類別), 沒有上一次的資料 **/
                        Log.i(TAG, "沒有Preferences參數, 沒有上一次的資料");
                }
        }

        /** 儲存參數, (context,order,category) **/
        private void savePreferences(Context context, int order, int category) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(k_Supplies_Order, order);
                editor.putInt(k_Supplies_Category, category);
                editor.commit();
        }

        private String getPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String result = "";
                try {
                        pref_order = sp.getInt(k_Supplies_Order, 0);
                } catch (Exception e) {
                        result = null;
                        Log.e(TAG, "get k_Supplies_Order Preferences Exception");
                }
                try {
                        pref_category = sp.getInt(k_Supplies_Category, 0);
                } catch (Exception e) {
                        result = null;
                        Log.e(TAG, "get k_Supplies_Category Preferences Exception");
                }
                return result;
        }

        private void clearPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_Supplies_Order);
                editor.remove(k_Supplies_Category);
                editor.commit();
        }

        /** 儲存參數, (context,time) **/
        private void saveDataUpdateTimePreferences(Context context, float time) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat(k_Supplies_Data_UpdateTime, time);
                editor.commit();
        }

        private float getDataUpdateTimePreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                float result = 0;
                try {
                        result = sp.getFloat(k_Supplies_Data_UpdateTime, 0);
                } catch (Exception e) {
                        result = 0;
                        Log.e(TAG, "get k_Supplies_Data_UpdateTime Preferences Exception");
                }
                return result;
        }

        private void removeDataUpdateTimePreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_Supplies_Data_UpdateTime);
                editor.commit();
        }

        /** 儲存參數, (context,time) **/
        private void saveDataTypesUpdateTimePreferences(Context context, Long time) {
                Log.i(TAG, "saveDataTypesUpdateTimePreferences ");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                Log.i(TAG, "Save Time : " + time);
                editor.putLong(k_Supplies_Types_Data_UpdateTime, time);
                editor.commit();
        }

        private long getDataTypesUpdateTimePreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                long result = 0;
                try {
                        result = sp.getLong(k_Supplies_Types_Data_UpdateTime, 0);
                        Log.i(TAG, "get k_Supplies_Category Result : " + result);
                } catch (Exception e) {
                        Log.e(TAG, "get k_Supplies_Category Preferences Exception");
                }
                return result;
        }

        private void removeDataTypesUpdateTimePreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_Supplies_Types_Data_UpdateTime);
                editor.commit();
        }

        @Override
        public void onResume() {
                super.onResume();

                // 回到頁面時開啟監聽
                if (getService) {
                        lms.requestLocationUpdates(LocationManager.GPS_PROVIDER, gps_updateTime, gps_updateDst, gll);
                        lms.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, gps_updateTime, gps_updateDst, wll);
                }

                if (getPreferences(mContext) != null) {
                        if (sp_order != null) {
                                // 設定上次的選項
                                sp_order.setSelection(pref_order);
                        }
                        if (sp_category != null) {
                                // 設定上次的選項
                                sp_category.setSelection(pref_category);
                        }
                }
        }

        @Override
        public void onStart() {
                super.onStart();
        }

        @Override
        public void onStop() {
                super.onStop();
        }

        @Override
        public void onPause() {
                super.onPause();
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
        }
}
