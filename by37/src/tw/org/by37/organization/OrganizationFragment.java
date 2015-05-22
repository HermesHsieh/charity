package tw.org.by37.organization;

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
import tw.org.by37.service.OrganizationApiService;
import tw.org.by37.service.SuppliesApiService;
import tw.org.by37.supplieshelp.DBControlSupplies;
import tw.org.by37.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.util.FunctionUtil;
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
import android.widget.ImageButton;
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

        /** Supplies Data Information **/
        private TextView tv_name;
        private TextView tv_description;
        private TextView tv_phone;
        private TextView tv_mail;

        /** Organization Information Item RelativeLayout Button **/
        private RelativeLayout rl_1;
        private RelativeLayout rl_2;
        private RelativeLayout rl_3;
        /** Organization Information Content Text **/
        private TextView tv_info_title;
        private TextView tv_info_content;

        private TextView tv_org_name;
        private ImageButton img_btn_guide;
        private ImageButton img_btn_favorite;
        private ImageButton img_btn_share;
        private TextView tv_view_all_need;

        /** 動態設定機構TextView的參數,紀錄是否已經儲存過TextView的長度 **/
        private boolean isMeasured = false;

        /** 開發階段用, 顯示機構資料與物資資料的TextView **/
        private boolean showInfo = false;

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
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_description = (TextView) view.findViewById(R.id.tv_description);
                tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                tv_mail = (TextView) view.findViewById(R.id.tv_mail);

                tv_info = (TextView) view.findViewById(R.id.tv_info);
                if (!showInfo) {
                        tv_info.setVisibility(View.GONE);
                }

                tv_org_name = (TextView) view.findViewById(R.id.tv_org_name);
                tv_org_name.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (SelectingData.mOrganizationData.org_name.length() > 0) {
                                        FunctionUtil.showToastMsg(mContext, SelectingData.mOrganizationData.org_name);
                                }
                        }
                });
                img_btn_guide = (ImageButton) view.findViewById(R.id.img_btn_guide);
                img_btn_favorite = (ImageButton) view.findViewById(R.id.img_btn_favorite);
                img_btn_share = (ImageButton) view.findViewById(R.id.img_btn_share);
                tv_view_all_need = (TextView) view.findViewById(R.id.tv_view_all_need);

                img_btn_guide.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (mMapFragment != null) {
                                        mMapFragment.getDestinationGuide();
                                }
                        }
                });

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

        private void setSuppliesInfo() {
                if (SelectingData.checkSupplyData()) {
                        if (SelectingData.mSupplyData.name.length() > 0) {
                                tv_name.setText(SelectingData.mSupplyData.name);
                        } else {
                                tv_name.setText(getString(R.string.supplies_name_empty));
                        }

                        if (SelectingData.mSupplyData.description.length() > 0) {
                                tv_description.setText(SelectingData.mSupplyData.description);
                        } else {
                                tv_description.setText(getString(R.string.supplies_desc_empty));
                        }

                        if (SelectingData.mSupplyData.organization_phone.length() > 0) {
                                tv_phone.setText(SelectingData.mSupplyData.organization_phone);
                        } else {
                                tv_phone.setText(getString(R.string.supplies_phone_empty));
                        }

                        if (SelectingData.mSupplyData.organization_email.length() > 0) {
                                tv_mail.setText(SelectingData.mSupplyData.organization_email);
                        } else {
                                tv_mail.setText(getString(R.string.supplies_mail_empty));
                        }
                }
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

                        if (content.length() == 0) {
                                content = getString(R.string.org_data_empty);
                        }

                        if (keyword.length() == 0) {
                                keyword = getString(R.string.string_empty);
                        }

                        tv_info_content.setText(content + "\n\n關鍵字 : " + keyword);
                }

                /** 設定按壓下去後狀態的Background Color **/
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

                new getSuppliesDataForIdAsyncTask().execute();

        }

        class getOrganizationDataForIdAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = OrganizationApiService.getAllOrganizationDataForId(SelectingData.organizationId);

                        Log.i(TAG, "OrganizationData Result : " + result);

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
                        psDialog = ProgressDialog.show(mContext, "", getString(R.string.loading));
                }
        }

        class getSuppliesDataForIdAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = SuppliesApiService.getAllSuppliesDataForId(SelectingData.suppliesId);

                        Log.i(TAG, "SuppliesData Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        SupplyData mData = new SupplyData();

                        if (result != null) {
                                try {
                                        JSONObject mJsonObject = new JSONObject(result);

                                        DBControlSupplies mDB = new DBControlSupplies(mContext);
                                        /** 開啟資料庫 **/
                                        mDB.openDatabase();

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

                                        // 設定物資需求資料
                                        SelectingData.mSupplyData = mData;

                                        /** 關閉資料庫 **/
                                        mDB.closeDatabase();
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        mData = null;
                                        Log.e(TAG, "mJsonObject error!!");
                                        Log.e(TAG, "The SuppliesDataForId from Server == null!!");
                                }

                                setSuppliesInfo();

                        } else {
                                Log.e(TAG, "SuppliesDataForId Result == null, (getSuppliesDataForIdAsyncTask) ");
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
