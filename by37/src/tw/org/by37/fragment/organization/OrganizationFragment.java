package tw.org.by37.fragment.organization;

import static tw.org.by37.config.SysConfig.k_Supplies_Data_UpdateTime;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.R;
import tw.org.by37.data.OrganizationData;
import tw.org.by37.data.SupplyData;
import tw.org.by37.fragment.organization.DBControlOrganization;
import tw.org.by37.fragment.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.fragment.test.TestFragment;
import tw.org.by37.service.OrganizationApiService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static tw.org.by37.config.SysConfig.*;

public class OrganizationFragment extends Fragment {

        private final static String TAG = "OrganizationFragment";

        private Context mContext;

        private OrganizationData mOrganizationData = null;

        private SupplyData mSupplyData = null;

        private boolean db_modify = false;

        private float data_updateTime = 0;

        private TextView tv_info;

        private MapFragment mMapFragment;

        public void setOrganizationData(OrganizationData data) {
                this.mOrganizationData = data;
        }

        public void setSupplyData(SupplyData data) {
                this.mSupplyData = data;
        }

        public OrganizationFragment() {
                super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_organization, container, false);

                findView(view);

                getOrganizationData();
                
                switchMapFragment();

                if (mSupplyData != null) {
                        /** 有物資需求 **/
                } else {
                        /** 沒有物資需求,僅基構簡介 **/
                }

                if (mOrganizationData != null) {
                } else {
                        /** 沒有設定基構資料 **/
                        Log.e(TAG, "OrganizationData == null !!");
                }

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                tv_info = (TextView) view.findViewById(R.id.tv_info);
        }

        private void getOrganizationData() {
                // 獲取上次更新的時間
                data_updateTime = getDataUpdateTimePreferences(mContext);

                new getOrganizationDataAsyncTask().execute();
        }

        class getOrganizationDataAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = null;

                        /** 如果資料庫有設變,則刪除先前的資料庫 **/
                        if (db_modify) {
                                new DBHelperOrganization(mContext).deleteDataBase();
                                Log.e(TAG, "The database modify, Delete old database !!");
                                removeDataUpdateTimePreferences(mContext);
                                Log.e(TAG, "Remove the data update time preferences !!");
                        }

                        // 如果db中資料筆數大於0
                        if (DBControlOrganization.getDataCount(mContext) > 0) {
                                if (!SuppliesHelpFragment.use_dbData) {
                                        // 離上次更新時間大於兩個小時(7200秒)
                                        if (System.currentTimeMillis() - data_updateTime > 2 * 3600 * 1000) {
                                                Log.i(TAG, "Organization Data update...");
                                                // 進行更新
                                                result = OrganizationApiService.getAllOrganizationData();
                                        } else {
                                                Log.i(TAG, "Organization Data don't update...");
                                        }
                                }
                        } else {
                                // 第一次使用,手機內沒有資料,抓取伺服器資料
                                Log.i(TAG, "Organization Data update...");
                                // 進行更新
                                result = OrganizationApiService.getAllOrganizationData();
                        }

                        Log.i(TAG, "Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (result != null) {

                                try {
                                        JSONArray mJsonArray = new JSONArray(result);
                                        Log.i(TAG, "mJsonArray Length : " + mJsonArray.length());

                                        /** 儲存更新的時間 **/
                                        saveDataUpdateTimePreferences(mContext, System.currentTimeMillis());

                                        DBControlOrganization mDB = new DBControlOrganization(mContext);
                                        /** 開啟資料庫 **/
                                        mDB.openDatabase();

                                        for (int i = 0; i < mJsonArray.length(); i++) {
                                                OrganizationData mData = new OrganizationData();
                                                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                                                try {
                                                        mData.org_id = mJsonObject.getString("id");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'id' error!!");
                                                }
                                                try {
                                                        mData.org_name = mJsonObject.getString("name");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'name' error!!");
                                                }
                                                try {
                                                        mData.org_email = mJsonObject.getString("email");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'email' error!!");
                                                }
                                                try {
                                                        mData.org_fax = mJsonObject.getString("fax");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'fax' error!!");
                                                }
                                                try {
                                                        mData.org_address = mJsonObject.getString("address");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'address' error!!");
                                                }
                                                try {
                                                        mData.org_phone = mJsonObject.getString("phone");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'phone' error!!");
                                                }
                                                try {
                                                        mData.org_div = mJsonObject.getString("division");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'division' error!!");
                                                }
                                                try {
                                                        mData.org_city = mJsonObject.getString("city");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'city' error!!");
                                                }
                                                try {
                                                        mData.org_type = mJsonObject.getString("type");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'type' error!!");
                                                }
                                                try {
                                                        mData.org_lng = mJsonObject.getDouble("longitude");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'longitude' error!!");
                                                }
                                                try {
                                                        mData.org_lat = mJsonObject.getDouble("latitude");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'latitude' error!!");
                                                }
                                                try {
                                                        mData.org_created_at = mJsonObject.getString("created_at");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'created_at' error!!");
                                                }
                                                try {
                                                        mData.org_updated_at = mJsonObject.getString("updated_at");
                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e(TAG, "mJsonObject get 'updated_at' error!!");
                                                }

                                                /**
                                                 * 將單一筆資料新增/更新至 organization.db
                                                 * 中
                                                 **/
                                                if (!mDB.searchIDResult(mData.org_id)) { // 比對db中是否已有此資料
                                                        // 如果沒有這筆資料,則新增資料
                                                        mDB.add(mData);
                                                } else {
                                                        // 如果已經有這筆資料,則更新資料
                                                        mDB.update(mData.org_id, mData);
                                                }
                                        }

                                        /** 關閉資料庫 **/
                                        mDB.closeDatabase();

                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "mJsonArray error!!");
                                        Log.e(TAG, "The OrganizationData from Server == null!!");
                                }

                        } else {
                                Log.e(TAG, "OrganizationData Result == null, (getOrganizationDataAsyncTask) ");
                        }

                        // 如果db中資料筆數大於0
                        if (DBControlOrganization.getDataCount(mContext) > 0) {
                                if (mSupplyData != null) {
                                        mOrganizationData = DBControlOrganization.getForId(mContext, mSupplyData.organizationId);
                                } else {
                                        Log.e(TAG, "mSupplyData == null !!");
                                }

                                if (tv_info != null) {
                                        if (mOrganizationData != null) {
                                                tv_info.setText("= Organization Data =\n" + mOrganizationData.getDataString() + "\n= Supply Data =\n" + mSupplyData.getDataString());
                                        } else {
                                                tv_info.setText(R.string.org_data_empty);
                                        }
                                } else {
                                        Log.e(TAG, "TextView == null !!");
                                }

                        } else {
                                Log.e(TAG, "Organization Database has not any one data!!");
                        }

                        // /** 從db中取得資料 **/
                        // mList =
                        // DBControlSupplies.getSuppliesDataForDistance(mContext,
                        // wifi_longitude, wifi_latitude);
                        //
                        // /** 最後將ListView的資料設定到Adapter中 **/
                        // if (mSuppliesListAdapter != null) {
                        // mSuppliesListAdapter.setListData(mList,
                        // wifi_longitude, wifi_latitude);
                        // }
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

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);
        }

        /** Preferences **/
        /** End of Preferences **/

        /** GotoActivity **/
        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        /**
         * switchMapFragment 介面
         */
        public void switchMapFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_map);

                FragmentTransaction ft = manager.beginTransaction();

                if (mMapFragment == null)
                        mMapFragment = new MapFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_map, mMapFragment);
                } else {
                        ft.replace(R.id.fragment_map, mMapFragment);
                }
                ft.commit();
        }

        /** 儲存參數, (context,time) **/
        private void saveDataUpdateTimePreferences(Context context, float time) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat(k_Organization_Data_UpdateTime, time);
                editor.commit();
        }

        private float getDataUpdateTimePreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                float result = 0;
                try {
                        result = sp.getFloat(k_Organization_Data_UpdateTime, 0);
                } catch (Exception e) {
                        result = 0;
                        Log.e(TAG, "get k_Organization_Data_UpdateTime Preferences Exception");
                }
                return result;
        }

        private void removeDataUpdateTimePreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_Organization_Data_UpdateTime);
                editor.commit();
        }

        @Override
        public void onResume() {
                super.onResume();
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
