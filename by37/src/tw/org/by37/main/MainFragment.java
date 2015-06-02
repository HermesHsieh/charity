package tw.org.by37.main;

import static tw.org.by37.config.RequestCode.SUPPLIESHELP_ACTIVITY_CODE;
import tw.org.by37.MainActivity;
import tw.org.by37.R;
import tw.org.by37.R.color;
import tw.org.by37.SuppliesHelpActivity;
import tw.org.by37.emergency.EmergencyFragment;
import tw.org.by37.organization.OrganizationFragment;
import tw.org.by37.productsell.AllProductActivity;
import tw.org.by37.productsell.MainProductSellFragment;
import tw.org.by37.search.SearchFragment;
import tw.org.by37.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.test.TestFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainFragment extends Fragment {

        private final static String TAG = "MainFragment";

        private Context mContext;

        /** Search Fragment **/
        private SearchFragment mSearchFragment;

        /** SuppliesHelp Fragment **/
        private SuppliesHelpFragment mSuppliesHelpFragment;

        /** Organization Fragment **/
        private OrganizationFragment mOrganizationFragment;

        /** Emergency Fragment **/
        private EmergencyFragment mEmergencyFragment;

        /** ProductSell Fragment **/
        private MainProductSellFragment mMainProductSellFragment;
        private TestFragment mTestFragment;

        private ListView mListView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Log.i(TAG, "onCreateView");

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_main, container, false);

                findView(view);

                setFragmentHeight(view);

                switchEmergencyFragment();

                switchSuppliesHelpFragment();

                switchProductSellFragment();

                ScrollView mScrollView = (ScrollView) view.findViewById(R.id.scrollView);
                Log.i(TAG, "ScrollView Height : " + mScrollView.getHeight());

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Log.i(TAG, "onCreate");
        }

        public void findView(View view) {
                /** 顯示全部物資 **/
                TextView textView = (TextView) view.findViewById(R.id.tv_supplies_all);
                /** 字體加底線 **/
                SpannableString content = new SpannableString(getString(R.string.view_all));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                textView.setText(content);

                textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // goto View All SuppliesHelp Data
                                gotoSuppliesHelpActivity();
                        }
                });

                textView.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_DOWN:
                                        break;
                                case MotionEvent.ACTION_UP:
                                        break;
                                }
                                return false;
                        }
                });
                
                /**顯示全部產品**/
                TextView showAllProdcutTv = (TextView)view.findViewById(R.id.tv_product_all);
                showAllProdcutTv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						gotoAllProductActivity();
					}
				});
        }

        /** 動態設定三個fragment的高度,比例根據螢幕的高度 **/
        public void setFragmentHeight(View view) {
                Log.i(TAG, "setFragmentHeight");
                FrameLayout mEmergency = (FrameLayout) view.findViewById(R.id.fragment_emergency);
                FrameLayout mSuppliesHelps = (FrameLayout) view.findViewById(R.id.fragment_supplieshelp);
                FrameLayout mProductSell = (FrameLayout) view.findViewById(R.id.fragment_productsell);

                LayoutParams mEMGParams = (LayoutParams) mEmergency.getLayoutParams();
                LayoutParams mSPHParams = (LayoutParams) mSuppliesHelps.getLayoutParams();
                LayoutParams mPDSParams = (LayoutParams) mProductSell.getLayoutParams();

                mEMGParams.height = (MainActivity.myScreenHeight) * 15 / 100;
                mSPHParams.height = (MainActivity.myScreenHeight) * 30 / 100;
                mPDSParams.height = (MainActivity.myScreenHeight) * 35 / 100;

                Log.i(TAG, "Screen Height : " + MainActivity.myScreenHeight);
                Log.i(TAG, "mEmergency Height : " + mEMGParams.height);
                Log.i(TAG, "mSuppliesHelps Height : " + mSPHParams.height);
                Log.i(TAG, "mProductSell Height : " + mPDSParams.height);

                mEmergency.setLayoutParams(mEMGParams);
                mSuppliesHelps.setLayoutParams(mSPHParams);
                mProductSell.setLayoutParams(mPDSParams);
        }

        /**
         * switchSearchFragment 介面
         */
        public void switchSearchFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSearchFragment == null)
                        mSearchFragment = new SearchFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mSearchFragment);
                } else {
                        ft.replace(R.id.fragment_content, mSearchFragment);
                }
                ft.commit();
        }

        /**
         * switchOrganizationFragment 介面
         */
        public void switchOrganizationFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mOrganizationFragment == null)
                        mOrganizationFragment = new OrganizationFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mOrganizationFragment);
                } else {
                        ft.replace(R.id.fragment_content, mOrganizationFragment);
                }
                ft.commit();
        }

        /**
         * switchTestFragment 介面
         */
        public void switchTestFragment() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mTestFragment == null)
                        mTestFragment = new TestFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mTestFragment);
                } else {
                        ft.replace(R.id.fragment_content, mTestFragment);
                }
                ft.commit();
        }

        /**
         * switchEmergencyFragment 介面
         */
        public void switchEmergencyFragment() {
                Log.i(TAG, "switchEmergencyFragment");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_emergency);

                FragmentTransaction ft = manager.beginTransaction();

                if (mEmergencyFragment == null)
                        mEmergencyFragment = new EmergencyFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_emergency, mEmergencyFragment);
                } else {
                        ft.replace(R.id.fragment_emergency, mEmergencyFragment);
                }
                ft.commit();
        }

        /**
         * switchSuppliesHelpFragment 介面
         */
        public void switchSuppliesHelpFragment() {
                Log.i(TAG, "switchSuppliesHelpFragment");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_supplieshelp);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSuppliesHelpFragment == null)
                        mSuppliesHelpFragment = new SuppliesHelpFragment();

                mSuppliesHelpFragment.setIndexView(true);

                if (fragment == null) {
                        ft.add(R.id.fragment_supplieshelp, mSuppliesHelpFragment);
                } else {
                        ft.replace(R.id.fragment_supplieshelp, mSuppliesHelpFragment);
                }
                ft.commit();
        }

        /**
         * switchProductSellFragment 介面
         */
        public void switchProductSellFragment() {
                Log.i(TAG, "switchProductSellFragment");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_productsell);

                FragmentTransaction ft = manager.beginTransaction();

                if (mMainProductSellFragment == null)
                        mMainProductSellFragment = new MainProductSellFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_productsell, mMainProductSellFragment);
                } else {
                        ft.replace(R.id.fragment_productsell, mMainProductSellFragment);
                }
                ft.commit();

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
        public void gotoSuppliesHelpActivity() {
                Intent intent = new Intent();
                intent.setClass(mContext, SuppliesHelpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SUPPLIESHELP_ACTIVITY_CODE);
        }

        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        private void gotoAllProductActivity(){
        	Intent i = new Intent();
        	i.setClass(mContext,AllProductActivity.class);
        	startActivity(i);
        }
        
        @Override
        public void onResume() {
                super.onResume();
                Log.i(TAG, "onResume");
        }

        @Override
        public void onStart() {
                super.onStart();
                Log.i(TAG, "onStart");
        }

        @Override
        public void onStop() {
                super.onStop();
                Log.i(TAG, "onStop");
        }

        @Override
        public void onPause() {
                super.onPause();
                Log.i(TAG, "onPause");
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
                Log.i(TAG, "onDestroy");
        }

}
