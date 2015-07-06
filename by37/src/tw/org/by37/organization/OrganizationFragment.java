package tw.org.by37.organization;

import static tw.org.by37.config.RequestCode.SUPPLIESHELP_ACTIVITY_CODE;
import static tw.org.by37.config.SysConfig.k_Organization_Data_UpdateTime;
import tw.org.by37.R;
import tw.org.by37.SuppliesHelpActivity;
import tw.org.by37.data.SelectingData;
import tw.org.by37.service.OrganizationApiService;
import tw.org.by37.service.SuppliesApiService;
import tw.org.by37.supplieshelp.SupportData;
import tw.org.by37.util.DialogUtil;
import tw.org.by37.util.FunctionUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class OrganizationFragment extends Fragment {

        private final static String TAG = OrganizationFragment.class.getName();

        private Context mContext;

        private float data_updateTime = 0;

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
        /** 查看該機構有需求 **/
        private TextView tv_org_allneed;

        /** 動態設定機構TextView的參數,紀錄是否已經儲存過TextView的長度 **/
        private boolean isMeasured = false;

        /** 開發階段用, 顯示機構資料與物資資料的TextView **/
        private boolean showInfo = false;

        /** 機構內，儲存物資需求的物件，僅一筆 **/
        private SupportData mData;

        /** 機構的資料物件 **/
        private OrganizationData mOrganizationData;

        /** 機構內容捐款與聯絡的動態外層Layout **/
        private LinearLayout ll_org_content;

        private View mView;

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

                this.mView = view;
                mView.setVisibility(View.GONE);
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

                tv_org_name = (TextView) view.findViewById(R.id.tv_org_name);
                tv_org_name.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (mOrganizationData != null && mOrganizationData.getName() != null) {
                                        FunctionUtil.showToastMsg(mContext, mOrganizationData.getName());
                                }
                        }
                });
                img_btn_guide = (ImageButton) view.findViewById(R.id.img_btn_guide);
                img_btn_favorite = (ImageButton) view.findViewById(R.id.img_btn_favorite);
                img_btn_share = (ImageButton) view.findViewById(R.id.img_btn_share);
                tv_org_allneed = (TextView) view.findViewById(R.id.tv_org_allneed);
                tv_org_allneed.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                gotoSuppliesHelpActivity();
                        }
                });

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

                ll_org_content = (LinearLayout) view.findViewById(R.id.ll_org_content);

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
        }

        /** 設置該機構的物資需求 **/
        private void setSuppliesInfo(SupportData mData) {
                try {
                        if (mData.getName().length() > 0) {
                                tv_name.setText(mData.getName());
                        } else {
                                tv_name.setText(getString(R.string.supplies_name_empty));
                        }

                        if (mData.getDescription().length() > 0) {
                                tv_description.setText(mData.getDescription());
                        } else {
                                tv_description.setText(getString(R.string.supplies_desc_empty));
                        }

                        if (mData.getOrganizationData().length > 0) {
                                if (mData.getOrganizationData()[0].getPhone() != null && mData.getOrganizationData()[0].getPhone().length() > 0) {
                                        tv_phone.setText(mData.getOrganizationData()[0].getPhone());
                                } else {
                                        tv_phone.setText(getString(R.string.supplies_phone_empty));
                                }

                                if (mData.getOrganizationData()[0].getEmail() != null && mData.getOrganizationData()[0].getEmail().length() > 0) {
                                        tv_mail.setText(mData.getOrganizationData()[0].getEmail());
                                } else {
                                        tv_mail.setText(getString(R.string.supplies_mail_empty));
                                }
                        }
                } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                }
        }

        /** 設定機構相關訊息(標題,內容,關鍵字) **/
        private void setOrganizationInfo(final String title) {
                tv_info_title.setText(title);
                Log.d(TAG, "mOrganizationData : " + mOrganizationData);
                if (mOrganizationData != null)
                        Log.d(TAG, "mOrganizationData To String : " + mOrganizationData.toString());
                if (tv_org_name != null && mOrganizationData != null) {
                        /** 機構名稱 **/
                        String name = mOrganizationData.getName();
                        Log.i(TAG, "Org : " + name);
                        // name = name.replace("中華民國", "");
                        Log.i(TAG, "Replace(1) : " + name);
                        // name = name.replace("財團法人", "");
                        Log.i(TAG, "Replace(2) : " + name);
                        /** 地圖下面顯示的機構名稱 **/
                        tv_org_name.setText(mOrganizationData.getName());

                        String content = getString(R.string.org_data_empty);
                        String keyword = getString(R.string.string_empty);

                        Log.d(TAG, "tv_org_name : " + mOrganizationData.getName());
                        Log.d(TAG, "content : " + mOrganizationData.getContent());
                        Log.d(TAG, "keyword : " + mOrganizationData.getKeyword());

                        // 物資需求下方機構資訊的區塊

                        /** 設立宗旨 **/
                        if (title.equals(getString(R.string.org_info_main))) {
                                content = mOrganizationData.getContent();
                                keyword = mOrganizationData.getKeyword();

                                if (content.length() == 0) {
                                        content = getString(R.string.org_data_empty);
                                }

                                if (keyword.length() == 0) {
                                        keyword = getString(R.string.string_empty);
                                }

                                tv_info_content.setText(content + "\n\n關鍵字 : " + keyword);
                        }

                        /** 服務對象與項目 **/
                        if (title.equals(getString(R.string.org_info_service))) {
                                tv_info_content.setText(content + "\n\n關鍵字 : " + keyword);
                        }

                        /** 捐款與聯絡 **/
                        if (title.equals(getString(R.string.org_info_contact))) {
                                ll_org_content.removeAllViews();
                                String url = mOrganizationData.getUrl();
                                String mail = mOrganizationData.getEmail();
                                String phone = mOrganizationData.getPhone();
                                String link = null;

                                TextView tv_url = createTextStyle(getString(R.string.org_uri), mOrganizationData.getUrl(), R.color.btn_blue_color);
                                tv_url.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                DialogUtil dialog = new DialogUtil(mContext);
                                                dialog.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(mOrganizationData.getUrl())));
                                                dialog.NotificationDialog(getString(R.string.notification), getString(R.string.check_url) + mOrganizationData.getUrl() + getString(R.string.symbol_confuse));
                                        }
                                });

                                TextView tv_mail = createTextStyle(getString(R.string.org_email), mOrganizationData.getEmail(), R.color.btn_blue_color);
                                tv_mail.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                DialogUtil dialog = new DialogUtil(mContext);
                                                dialog.setIntent(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mOrganizationData.getEmail())));
                                                dialog.NotificationDialog(getString(R.string.notification), getString(R.string.check_mail) + mOrganizationData.getEmail() + getString(R.string.symbol_confuse));
                                        }
                                });

                                TextView tv_phone = createTextStyle(getString(R.string.org_phone), mOrganizationData.getPhone(), R.color.btn_blue_color);
                                tv_phone.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                DialogUtil dialog = new DialogUtil(mContext);
                                                dialog.setIntent(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mOrganizationData.getPhone())));
                                                dialog.NotificationDialog(getString(R.string.notification), getString(R.string.check_phone) + mOrganizationData.getPhone() + getString(R.string.symbol_confuse));
                                        }
                                });

                                TextView tv_link = null;
                                if (link != null) {
                                        tv_link = createTextStyle(getString(R.string.org_donation_link), mOrganizationData.getPhone(), R.color.btn_blue_color);
                                        tv_link.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                }
                                        });
                                }
                                ll_org_content.addView(tv_url);
                                ll_org_content.addView(tv_mail);
                                ll_org_content.addView(tv_phone);

                                if (link != null) {
                                        ll_org_content.addView(tv_link);
                                }

                                /** 一定要在View之後進行設定，不然會出錯 **/
                                try {
                                        setViewMargin(tv_url);
                                        setViewMargin(tv_mail);
                                        setViewMargin(tv_phone);
                                } catch (Exception e) {
                                        Log.e(TAG, "setViewMargin Exception");
                                }

                                ll_org_content.setVisibility(View.VISIBLE);
                                tv_info_content.setVisibility(View.GONE);
                        } else {
                                ll_org_content.removeAllViews();
                                ll_org_content.setVisibility(View.GONE);
                                tv_info_content.setVisibility(View.VISIBLE);
                        }
                }

                /** 設定按壓下去後狀態的Background Color **/
                rl_1.setBackgroundResource(R.drawable.item_click_white);
                rl_2.setBackgroundResource(R.drawable.item_click_white);
                rl_3.setBackgroundResource(R.drawable.item_click_white);
                if (title.equals(getString(R.string.org_info_main)))
                        rl_1.setBackgroundColor(getResources().getColor(R.color.org_light_gray_color));
                if (title.equals(getString(R.string.org_info_service)))
                        rl_2.setBackgroundColor(getResources().getColor(R.color.org_light_gray_color));
                if (title.equals(getString(R.string.org_info_contact)))
                        rl_3.setBackgroundColor(getResources().getColor(R.color.org_light_gray_color));
        }

        /** 建立固定格式的TextView Style **/
        private TextView createTextStyle(String item, String content, int color) {
                TextView textview = new TextView(mContext);
                textview.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                textview.setText(Html.fromHtml("<font color=#000000>" + item + "</font><b><u>" + content + "</b></u>"));
                textview.setTextColor(mContext.getResources().getColor(color));
                return textview;
        }

        /** 動態設定View的Margin **/
        private void setViewMargin(View view) {
                MarginLayoutParams marginParams = (MarginLayoutParams) view.getLayoutParams();
                marginParams.bottomMargin = 30;
                view.setLayoutParams(marginParams);
        }

        private void getOrganizationData() {
                // 獲取上次更新的時間
                // data_updateTime = getDataUpdateTimePreferences(mContext);

                new getOrganizationDataForIdAsyncTask().execute();

                new getSuppliesDataForIdAsyncTask().execute();

        }

        class getOrganizationDataForIdAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {
                        Log.i(TAG, "SelectingData.organizationId : " + SelectingData.organizationId);

                        String result = OrganizationApiService.getAllOrganizationDataForId(SelectingData.organizationId);

                        Log.i(TAG, "OrganizationData Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (result != null) {
                                if (!FunctionUtil.isSleepServer(result)) {
                                        try {
                                                Gson gson = new Gson();
                                                mOrganizationData = gson.fromJson(result, OrganizationData.class);
                                                setOrganizationInfo(getString(R.string.org_info_main));
                                        } catch (Exception e) {
                                                Log.e(TAG, "mOrganizationData Exception " + e);
                                        }
                                }
                        } else {
                                Log.e(TAG, "OrganizationData Result == null");
                        }

                        if (mMapFragment != null && mOrganizationData != null) {
                                mMapFragment.setOrganizationData(mOrganizationData.getLatitude(), mOrganizationData.getLongitude(), mOrganizationData.getName(), mOrganizationData.getTitle());
                                mMapFragment.setOrganizationMarker();
                        }

                        mView.setVisibility(View.VISIBLE);
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
                        String result = null;
                        if (SelectingData.suppliesId != null) {
                                /** 從物資需求點擊進入的,有物資的ID **/
                                result = SuppliesApiService.getSuppliesDataForId(SelectingData.suppliesId);

                                Log.d(TAG, "Selected Supply Id : " + SelectingData.suppliesId);
                        } else {
                                /** 當沒有物資ID時 **/
                                if (mOrganizationData != null) {
                                        /** 從鄰近機構點擊進入的,有機構資料 **/
                                        String id = null;
                                        try {
                                                /** 取得該機構的第一筆物資ID **/
                                                id = mOrganizationData.getSupportData()[0].getId();
                                                result = SuppliesApiService.getSuppliesDataForId(id);

                                                Log.d(TAG, "Selected Supply Id : " + id);
                                        } catch (Exception e) {
                                        }
                                } else {
                                        Log.d(TAG, "mOrganizationData == null");
                                }
                                Log.d(TAG, "SelectingData.suppliesId == null");
                        }

                        Log.d(TAG, "getSuppliesDataForIdAsyncTask Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        // SupplyData mData = new SupplyData();

                        if (result != null) {

                                mData = new SupportData();
                                Gson gson = new Gson();
                                mData = gson.fromJson(result, SupportData.class);

                                setSuppliesInfo(mData);

                                // try {
                                // JSONObject mJsonObject = new
                                // JSONObject(result);
                                // DBControlSupplies mDB = new
                                // DBControlSupplies(mContext);
                                // /** 開啟資料庫 **/
                                // mDB.openDatabase();
                                // try {
                                // mData.id = mJsonObject.getString("id");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.name = mJsonObject.getString("name");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.description =
                                // mJsonObject.getString("description");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.organizationId =
                                // mJsonObject.getString("organizationId");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.total = mJsonObject.getString("total");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.created_at =
                                // mJsonObject.getString("created_at");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.updated_at =
                                // mJsonObject.getString("updated_at");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.category =
                                // mJsonObject.getString("goodsTypeId");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                //
                                // Log.i(TAG, "Organization JSONArray length : "
                                // +
                                // mJsonObject.getJSONArray("organization").length());
                                //
                                // for (int j = 0; j <
                                // mJsonObject.getJSONArray("organization").length();
                                // j++) {
                                // try {
                                // mData.organization_id =
                                // mJsonObject.getJSONArray("organization").getJSONObject(j).getString("id");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.organization_name =
                                // mJsonObject.getJSONArray("organization").getJSONObject(j).getString("name");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.organization_longitude =
                                // mJsonObject.getJSONArray("organization").getJSONObject(j).getDouble("longitude");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // try {
                                // mData.organization_latitude =
                                // mJsonObject.getJSONArray("organization").getJSONObject(j).getDouble("latitude");
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // }
                                // }
                                //
                                // /** 將單一筆資料新增/更新至 supplies.db 中 **/
                                // if (!mDB.searchIDResult(mData.id)) { //
                                // 比對db中是否已有此資料
                                // // 如果沒有這筆資料,則新增資料
                                // mDB.add(mData);
                                // } else {
                                // // 如果已經有這筆資料,則更新資料
                                // mDB.update(mData.id, mData);
                                // }
                                //
                                // // 設定物資需求資料
                                // SelectingData.mSupplyData = mData;
                                //
                                // /** 關閉資料庫 **/
                                // mDB.closeDatabase();
                                // } catch (JSONException e) {
                                // e.printStackTrace();
                                // mData = null;
                                // Log.e(TAG, "mJsonObject error!!");
                                // Log.e(TAG,
                                // "The SuppliesDataForId from Server == null!!");
                                // }
                                //

                        } else {
                                Log.e(TAG, "SuppliesDataForId Result == null");
                                /** 物資資料為空,隱藏物資需求的FragmentView **/
                                View mSupply = (View) getView().findViewById(R.id.fragment_org_supply);
                                mSupply.setVisibility(View.GONE);
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
                if (mOrganizationData != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("org_name", mOrganizationData.getName());
                        intent.putExtras(bundle);
                }
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
                SelectingData.suppliesId = null;
                SelectingData.organizationId = null;
        }
}
