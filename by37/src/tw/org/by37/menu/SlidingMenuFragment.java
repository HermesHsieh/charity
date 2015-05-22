package tw.org.by37.menu;

import java.util.ArrayList;

import tw.org.by37.R;
import tw.org.by37.R.array;
import tw.org.by37.R.layout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class SlidingMenuFragment extends Fragment {

        private final static String TAG = "SlidingMenuFragment";

        private Context mContext;

        private Handler mHandler;

        private ListView mListView;

        private SlidingMenuAdapter mListViewAdapter;

        public SlidingMenuFragment() {
                super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_slidingmenu, container, false);

                findView(view);

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                mHandler = new Handler();

                mListView = (ListView) view.findViewById(android.R.id.list);

                mListViewAdapter = new SlidingMenuAdapter(mContext);

                mListView.setAdapter(mListViewAdapter);

                new Thread() {
                        @Override
                        public void run() {
                                Looper.prepare();
                                mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                                mListViewAdapter.addAllDatas(createMenuList());
                                        }
                                });
                                Looper.loop();
                        };
                }.start();
        }

        private ArrayList<SlidingMenuDetail> createMenuList() {

                // 設定給ListViewAdapter的資料
                ArrayList<SlidingMenuDetail> mData = new ArrayList<SlidingMenuDetail>();

                // Menu項目的名稱
                Resources res = mContext.getResources();
                String[] mListName = res.getStringArray(R.array.sliding_menu_item);

                // 設定ListView的資料
                for (int i = 0; i < mListName.length; i++) {
                        SlidingMenuDetail item = new SlidingMenuDetail();
                        item.name = mListName[i];

                        if (item.name.equals("義賣商品分類")) {
                                item.isHint = true;
                        }
                        if (item.name.equals("慈善機構分類")) {
                                item.isHint = true;
                        }
                        if (item.name.equals("設定")) {
                                item.icon = R.drawable.ic_action_settings;
                        }
                        if (item.name.equals("常見問題(FAQ)")) {
                                item.icon = R.drawable.ic_action_help;
                        }
                        if (item.name.equals("聯絡建議")) {
                                item.icon = R.drawable.ic_action_call;
                        }
                        if (item.name.equals("分享APP")) {
                                item.icon = R.drawable.ic_action_share;
                        }
                        if (item.name.equals("使用條款")) {
                                item.icon = R.drawable.ic_action_view_as_list;
                        }
                        mData.add(item);
                }

                return mData;
        }

        public ListView getListView() {
                if (mListView != null)
                        return mListView;
                else
                        return null;
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
