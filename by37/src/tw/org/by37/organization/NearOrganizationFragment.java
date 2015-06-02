package tw.org.by37.organization;

import static tw.org.by37.config.RequestCode.ORGANIZATION_ACTIVITY_CODE;

import java.util.ArrayList;
import java.util.List;

import tw.org.by37.OrganizationActivity;
import tw.org.by37.R;
import tw.org.by37.config.SysConfig;
import tw.org.by37.data.SelectingData;
import tw.org.by37.emergency.EmergencyFragment.MainBroadcastReceiver;
import tw.org.by37.supplieshelp.DBControlSupplies;
import tw.org.by37.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.supplieshelp.SuppliesListAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NearOrganizationFragment extends Fragment {
        private final static String TAG = "NearOrganizationFragment";

        private Context mContext;

        /** 廣播必備參數 **/
        private IntentFilter mGetNearOrganizationIntentFilter;
        private MainBroadcastReceiver mMainBroadcastReceiverr;

        /** 是否為簡易模式(即首頁的顯示狀態) **/
        private boolean index_view = false;

        private TextView tv_organization_total;

        private ListView mListView;

        private NearOrganizationListAdapter mNearOrganizationListAdapter;

        /** 機構類別 Spinner **/
        private Spinner sp_category;

        private ArrayList<OrganizationData> mNearOrganizationData;

        public NearOrganizationFragment() {
                super();
        }

        /** 設定在首頁顯示與否的參數 **/
        public void setIndexView(boolean view) {
                index_view = view;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_near_organization, container, false);

                findView(view);

                initBroadcast();

                getNearOrganizationData();

                return view;
        }

        /** 廣播初始參數 **/
        private void initBroadcast() {
                mGetNearOrganizationIntentFilter = new IntentFilter(SysConfig.GET_NEAR_ORGANIZATION_CONTENT_BROADCAST);
                mMainBroadcastReceiverr = new MainBroadcastReceiver();
        }

        private void getNearOrganizationData() {
                Log.i(TAG, "getNearOrganizationData");
                if (SuppliesHelpFragment.wifi_latitude > 0 && SuppliesHelpFragment.wifi_longitude > 0) {
                        String[] mPosition = { String.valueOf(SuppliesHelpFragment.wifi_latitude), String.valueOf(SuppliesHelpFragment.wifi_longitude) };
                        new GetNearOrganizationAsyncTask(mContext).execute(mPosition);
                } else {
                        Log.e(TAG, "Wifi Location < 0");
                }
        }

        /** 將鄰近機構的資料設定至ListView內 **/
        private void setNearOrganizationData() {
                Log.i(TAG, "setNearOrganizationData");
                mNearOrganizationData = SelectingData.mNearOrganizationData;
                SelectingData.mNearOrganizationData = null;

                tv_organization_total.setText(String.valueOf(mNearOrganizationData.size()));

                if (index_view) {
                        /** 首頁最多只顯示三筆資料 **/
                        int count = mNearOrganizationData.size();
                        if (count > 3) {
                                while (mNearOrganizationData.size() > 3) {
                                        mNearOrganizationData.remove(3);
                                }
                        }
                }

                /** 最後將ListView的資料設定到Adapter中 **/
                if (mNearOrganizationListAdapter != null) {
                        mNearOrganizationListAdapter.setListData(mNearOrganizationData);
                }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                tv_organization_total = (TextView) view.findViewById(R.id.tv_organization_total);

                // new ListView
                mListView = (ListView) view.findViewById(android.R.id.list);
                // new ListView Adapter
                mNearOrganizationListAdapter = new NearOrganizationListAdapter(getActivity());
                // 設定是否為首頁顯示
                mNearOrganizationListAdapter.setIndexView(index_view);
                // 配適ListView Adapter
                mListView.setAdapter(mNearOrganizationListAdapter);

                mListView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                                Log.i(TAG, "Click Organization Item : " + position);

                                SelectingData.organizationId = mNearOrganizationListAdapter.getOrganizationId(position);

                                gotoOrganizationActivity();
                        }
                });

                sp_category = (Spinner) view.findViewById(R.id.spinner_category);
        }

        /** 設定收到廣播後的動作 **/
        public class MainBroadcastReceiver extends BroadcastReceiver {
                @Override
                public void onReceive(Context context, Intent intent) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "action : " + intent.getAction());
                        if (intent.getAction().equals(SysConfig.GET_NEAR_ORGANIZATION_CONTENT_BROADCAST)) {
                                boolean status = intent.getExtras().getBoolean(SysConfig.GET_NEAR_ORGANIZATION_CONTENT_BROADCAST);
                                Log.d(TAG, "GetNearOrganization Data status : " + status);
                                if (status) {
                                        setNearOrganizationData();
                                }
                        }
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
        public void gotoOrganizationActivity() {
                Intent intent = new Intent();
                intent.setClass(mContext, OrganizationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, ORGANIZATION_ACTIVITY_CODE);
        }

        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        @Override
        public void onResume() {
                super.onResume();
                getActivity().registerReceiver(mMainBroadcastReceiverr, mGetNearOrganizationIntentFilter);
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
                getActivity().unregisterReceiver(mMainBroadcastReceiverr);
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
        }
}
