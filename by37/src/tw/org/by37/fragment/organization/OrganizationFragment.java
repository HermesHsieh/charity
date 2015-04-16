package tw.org.by37.fragment.organization;

import static tw.org.by37.config.RequestCode.SUPPLIESHELP_ACTIVITY_CODE;
import static tw.org.by37.config.SysConfig.k_Organization_Data_UpdateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.MainActivity;
import tw.org.by37.R;
import tw.org.by37.SuppliesHelpActivity;
import tw.org.by37.data.OrganizationData;
import tw.org.by37.data.SelectingData;
import tw.org.by37.data.SupplyData;
import tw.org.by37.fragment.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.service.OrganizationApiService;
import android.app.ProgressDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrganizationFragment extends Fragment {

        private final static String TAG = "OrganizationFragment";

        private Context mContext;

        private SupplyData mSupplyData = null;

        private boolean db_modify = false;

        private float data_updateTime = 0;

        private TextView tv_info;

        private MapFragment mMapFragment;

        private ProgressDialog psDialog;

        /** Organization Information Item RelativeLayout Button **/
        private RelativeLayout rl_1, rl_2, rl_3;
        /** Organization Information Content Text **/
        private TextView tv_info_title, tv_info_content;

        private TextView tv_org_name, tv_view_all_need;

        /** 動態設定機構TextView的參數 **/
        private boolean isMeasured = false;

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

                if (SelectingData.mOrganizationData != null) {
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
                tv_org_name = (TextView) view.findViewById(R.id.tv_org_name);
                tv_view_all_need = (TextView) view.findViewById(R.id.tv_view_all_need);

                /** Organization Information Item RelativeLayout Button **/
                rl_1 = (RelativeLayout) view.findViewById(R.id.rl_1);
                rl_2 = (RelativeLayout) view.findViewById(R.id.rl_2);
                rl_3 = (RelativeLayout) view.findViewById(R.id.rl_3);

                /** Organization Information Content Text **/
                tv_info_title = (TextView) view.findViewById(R.id.tv_info_title);
                tv_info_content = (TextView) view.findViewById(R.id.tv_info_content);

                rl_1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                setOrganizationInfo(getString(R.string.org_info_main));
                        }
                });
                rl_2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                setOrganizationInfo(getString(R.string.org_info_service));
                        }
                });
                rl_3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                setOrganizationInfo(getString(R.string.org_info_contact));
                        }
                });

                tv_view_all_need.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                gotoSuppliesHelpActivity();
                        }
                });
        }

        /** 設定機構相關訊息(標題,內容,關鍵字) **/
        private void setOrganizationInfo(String title) {
                tv_info_title.setText(title);
                if (tv_org_name != null && SelectingData.mOrganizationData != null) {
                        String name = SelectingData.mOrganizationData.org_name;
                        Log.i(TAG, "Org : " + name);
                        // name = name.replace("中華民國", "");
                        Log.i(TAG, "Replace(1) : " + name);
                        // name = name.replace("財團法人", "");
                        Log.i(TAG, "Replace(2) : " + name);
                        tv_org_name.setText(name);

                        /** 動態設定機構名稱,若長度超過螢幕之一半,則設定以 ... 表示 **/
                        ViewTreeObserver observer = tv_org_name.getViewTreeObserver();
                        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                public boolean onPreDraw() {
                                        if (!isMeasured) {
                                                LayoutParams mText = (LayoutParams) tv_org_name.getLayoutParams();
                                                Log.i(TAG, "mText Width : " + mText.width);
                                                Log.i(TAG, "Screen Width : " + (MainActivity.myScreenWidth) / 2);
                                                if (tv_org_name.getMeasuredWidth() > (MainActivity.myScreenWidth) / 2) {
                                                        mText.width = (MainActivity.myScreenWidth) / 2;
                                                }
                                                tv_org_name.setLayoutParams(mText);
                                                isMeasured = true;
                                        }
                                        return true;
                                }
                        });

                        String content = "";
                        String keyword = "";

                        if (title.equals(getString(R.string.org_info_main))) {
                                content = SelectingData.mOrganizationData.org_content;
                                keyword = SelectingData.mOrganizationData.org_keyword;
                        }

                        if (title.equals(getString(R.string.org_info_service))) {

                        }

                        if (title.equals(getString(R.string.org_info_contact))) {

                        }

                        if (content.length() > 0 && keyword.length() > 0) {
                                content = content + "\n\n關鍵字 : " + keyword;
                                tv_info_content.setText(content);
                        }
                }

                /** 設定按壓下去後的Background Color **/
                rl_1.setBackgroundResource(R.drawable.item_click);
                rl_2.setBackgroundResource(R.drawable.item_click);
                rl_3.setBackgroundResource(R.drawable.item_click);
                if (title.equals(getString(R.string.org_info_main)))
                        rl_1.setBackgroundColor(getResources().getColor(R.color.org_light_gray_color));
                if (title.equals(getString(R.string.org_info_service)))
                        rl_2.setBackgroundColor(getResources().getColor(R.color.org_light_gray_color));
                if (title.equals(getString(R.string.org_info_contact)))
                        rl_3.setBackgroundColor(getResources().getColor(R.color.org_light_gray_color));

        }

        private void getOrganizationData() {
                // 獲取上次更新的時間
                // data_updateTime = getDataUpdateTimePreferences(mContext);

                // new getOrganizationDataAsyncTask().execute();

                new getOrganizationDataForIdAsyncTask().execute();
        }

        class getOrganizationDataForIdAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = OrganizationApiService.getAllOrganizationDataForId(SelectingData.organizationId);

                        Log.i(TAG, "Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        OrganizationData mData = new OrganizationData();

                        if (result != null) {
                                try {
                                        JSONObject mJsonObject = new JSONObject(result);

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
                                                mData.org_title = mJsonObject.getString("title");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'title' error!!");
                                        }
                                        try {
                                                mData.org_content = mJsonObject.getString("content");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'content' error!!");
                                        }
                                        try {
                                                mData.org_keyword = mJsonObject.getString("keyword");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'keyword' error!!");
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
                                                mData.org_url = mJsonObject.getString("url");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'url' error!!");
                                        }
                                        try {
                                                mData.org_contact = mJsonObject.getString("contact");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'contact' error!!");
                                        }
                                        try {
                                                mData.org_number = mJsonObject.getString("number");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'number' error!!");
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

                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        mData = null;
                                        Log.e(TAG, "mJsonObject error!!");
                                        Log.e(TAG, "The OrganizationData from Server == null!!");
                                }

                        } else {
                                Log.e(TAG, "OrganizationData Result == null, (getOrganizationDataAsyncTask) ");
                        }

                        if (mSupplyData != null) {
                                SelectingData.mOrganizationData = mData;
                        } else {
                                Log.e(TAG, "mSupplyData == null !!");
                        }

                        if (tv_info != null) {
                                if (SelectingData.mOrganizationData != null) {
                                        setOrganizationInfo(getString(R.string.org_info_main));

                                        tv_info.setText("= Organization Data =\n" + SelectingData.mOrganizationData.getDataString() + "\n= Supply Data =\n" + mSupplyData.getDataString());

                                } else {
                                        tv_info.setText(R.string.org_data_empty);
                                }
                        } else {
                                Log.e(TAG, "TextView == null !!");
                        }

                        if (mMapFragment != null) {
                                mMapFragment.setOrganizationMarker();
                        }
                        psDialog.dismiss();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                        psDialog = ProgressDialog.show(mContext, getString(R.string.msg), getString(R.string.loading));
                }
        }

        class getSuppliesDataForIdAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = OrganizationApiService.getAllOrganizationDataForId(SelectingData.organizationId);

                        Log.i(TAG, "Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        OrganizationData mData = new OrganizationData();

                        if (result != null) {
                                try {
                                        JSONObject mJsonObject = new JSONObject(result);

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
                                                mData.org_title = mJsonObject.getString("title");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'title' error!!");
                                        }
                                        try {
                                                mData.org_content = mJsonObject.getString("content");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'content' error!!");
                                        }
                                        try {
                                                mData.org_keyword = mJsonObject.getString("keyword");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'keyword' error!!");
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
                                                mData.org_url = mJsonObject.getString("url");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'url' error!!");
                                        }
                                        try {
                                                mData.org_contact = mJsonObject.getString("contact");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'contact' error!!");
                                        }
                                        try {
                                                mData.org_number = mJsonObject.getString("number");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e(TAG, "mJsonObject get 'number' error!!");
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

                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        mData = null;
                                        Log.e(TAG, "mJsonObject error!!");
                                        Log.e(TAG, "The OrganizationData from Server == null!!");
                                }

                        } else {
                                Log.e(TAG, "OrganizationData Result == null, (getOrganizationDataAsyncTask) ");
                        }

                        if (mSupplyData != null) {
                                SelectingData.mOrganizationData = mData;
                        } else {
                                Log.e(TAG, "mSupplyData == null !!");
                        }

                        if (tv_info != null) {
                                if (SelectingData.mOrganizationData != null) {
                                        tv_info.setText("= Organization Data =\n" + SelectingData.mOrganizationData.getDataString() + "\n= Supply Data =\n" + mSupplyData.getDataString());
                                } else {
                                        tv_info.setText(R.string.org_data_empty);
                                }
                        } else {
                                Log.e(TAG, "TextView == null !!");
                        }

                        if (mMapFragment != null) {
                                mMapFragment.setOrganizationMarker();
                        }
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                }
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
                                        SelectingData.mOrganizationData = DBControlOrganization.getForId(mContext, mSupplyData.organizationId);
                                } else {
                                        Log.e(TAG, "mSupplyData == null !!");
                                }

                                if (tv_info != null) {
                                        if (SelectingData.mOrganizationData != null) {
                                                tv_info.setText("= Organization Data =\n" + SelectingData.mOrganizationData.getDataString() + "\n= Supply Data =\n" + mSupplyData.getDataString());
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

        /** Preferences **/
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

        /** End of Preferences **/

        /** GotoActivity **/
        public void gotoSuppliesHelpActivity() {
                Intent intent = new Intent();
                intent.setClass(mContext, SuppliesHelpActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SUPPLIESHELP_ACTIVITY_CODE);
        }

        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

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
                SelectingData.clearOrganizationData();
        }
}
