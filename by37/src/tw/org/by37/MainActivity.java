package tw.org.by37;

import static tw.org.by37.config.RequestCode.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tw.org.by37.config.SysConfig;
import tw.org.by37.emergency.EmergencyFragment;
import tw.org.by37.main.MainFragment;
import tw.org.by37.member.LoginFragment;
import tw.org.by37.member.UserData;
import tw.org.by37.member.UserLoginAsyncTask;
import tw.org.by37.menu.BottomMenuFragment;
import tw.org.by37.menu.RightMenuFragment;
import tw.org.by37.menu.SlidingMenuFragment;
import tw.org.by37.organization.MapFragment;
import tw.org.by37.organization.OrganizationFragment;
import tw.org.by37.position.PositionFragment;
import tw.org.by37.productsell.MainProductSellFragment;
import tw.org.by37.search.SearchFragment;
import tw.org.by37.service.LocationService;
import tw.org.by37.supplieshelp.SuppliesHelpFragment;
import tw.org.by37.test.TestFragment;
import tw.org.by37.test.TestPostFragment;
import tw.org.by37.util.FunctionUtil;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

        private final static String TAG = MainActivity.class.getName();

        private Context mContext;

        /** Screen Param **/
        public static int myScreenWidth = 0;
        public static int myScreenHeight = 0;
        public static float myScreenDensity = 0;
        public static float myScreenDensityDpi = 0;
        /** End of Screen Param **/

        public static MyApplication mUserApplication;

        /** 滑動Menu **/
        private SlidingMenu mSlidingMenu;

        private boolean mSlidingMenuShow = false;

        /** Sliding Menu Fragment **/
        private MainFragment mMainFragment;

        /** Sliding Menu Fragment **/
        private SlidingMenuFragment mSlidingMenuFragment;

        /** Right Sliding Menu Fragment **/
        private RightMenuFragment mRightMenuFragment;

        /** Bottom Menu Fragment **/
        private BottomMenuFragment mBottomMenuFragment;

        /** Login Fragment **/
        private LoginFragment mLoginFragment;

        /** Search Fragment **/
        private SearchFragment mSearchFragment;

        /** SuppliesHelp Fragment **/
        private SuppliesHelpFragment mSuppliesHelpFragment;

        /** Organization Fragment **/
        private OrganizationFragment mOrganizationFragment;

        private TestFragment mTestFragment;

        private TestPostFragment mTestPostFragment;

        private EmergencyFragment mEmergencyFragment;

        /** ProductSell Fragment **/
        private MainProductSellFragment mMainProductSellFragment;

        private ListView mListView;

        private PositionFragment mPositionFragment;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                startService(new Intent(this, LocationService.class));
                setContentView(R.layout.activity_main);
                FunctionUtil.createFileRoot();
                getDisplayMetrics();
                mContext = this;
                mUserApplication = (MyApplication) mContext.getApplicationContext();

                /** 登入使用者(手機內存資料) **/
                new UserLoginAsyncTask().execute();

                initSlidingMenu();

                initActionBar();

                getFacebookKeyHash();

                // switchTestFragment();

                switchMainFragment();

                switchBottomMenuFragment();
        }

        /**
         * 初始化 Action Bar
         */
        private void initActionBar() {

                // 設定ActionBar左上角的圖示
                getSupportActionBar().setIcon(R.drawable.ic_launcher);
                // getSupportActionBar().setCustomView(R.layout.titlebar_main);

                // 設定ActionBar左上角圖示顯示狀態
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                // 設定ActionBar左上角圖示的點擊事件
                getSupportActionBar().setHomeButtonEnabled(true);

                // 给左上角圖標的左邊加上一個返回的圖標
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        }

        /**
         * 初始化 Sliding Menu
         */
        public void initSlidingMenu() {
                setBehindContentView(R.layout.fragment_menu);

                // 載入左邊Menu Fragment
                switchMenuFragment();
                // 載入右邊Menu Fragment
                switchRightMenuFragment();

                // customize the SlidingMenu
                mSlidingMenu = getSlidingMenu();
                mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 設置是左滑還是右滑，還是左右都可以滑，我這裡只做了左滑
                mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 設置菜單寬度
                mSlidingMenu.setFadeDegree(0.35f);// 設置淡入淡出的比例
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 設置手勢模式
                mSlidingMenu.setShadowDrawable(R.drawable.shadow);// 設置左功能表陰影圖片
                mSlidingMenu.setFadeEnabled(true);// 設置滑動時菜單的是否淡入淡出
                mSlidingMenu.setBehindScrollScale(0.333f);// 設置滑動時拖拽效果

                mSlidingMenu.setSecondaryMenu(R.layout.fragment_right_menu); // 設置右邊菜單的Layout
                mSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow); // 設置右邊菜單的陰影圖片

                mSlidingMenu.setOnOpenListener(new OnOpenListener() {
                        @Override
                        public void onOpen() {
                                // TODO Auto-generated method stub
                                mSlidingMenuShow = true;
                                if (mSlidingMenuFragment != null) {
                                        mSlidingMenuFragment.getListView().setOnItemClickListener(mLeftListViewItemClickListener);
                                }
                        }
                });
                mSlidingMenu.setOnCloseListener(new OnCloseListener() {
                        @Override
                        public void onClose() {
                                // TODO Auto-generated method stub
                                mSlidingMenuShow = false;
                        }
                });

                /** 監聽右側菜單 **/
                mSlidingMenu.setSecondaryOnOpenListner(new OnOpenListener() {
                        @Override
                        public void onOpen() {
                                if (mRightMenuFragment != null) {
                                        mRightMenuFragment.getListView().setOnItemClickListener(mRightListViewItemClickListener);
                                }
                        }
                });
        }

        AdapterView.OnItemClickListener mLeftListViewItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        Log.i(TAG, "Click Left Menu Position : " + position);
                        mSlidingMenu.toggle();

                        if (position == 0) {
                                // mMainFragment = null;
                                switchMainFragment();
                        }
                }
        };

        AdapterView.OnItemClickListener mRightListViewItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        Log.i(TAG, "Click Right Menu Position : " + position);
                        mSlidingMenu.toggle();

                        switch (position) {
                        case 1:
                                switchTestFragment();
                                break;
                        case 2:
                                switchTestPostFragment();
                                break;
                        case 3:
                                switchEmergencyFragment();
                                break;
                        case 4:
                                switchSuppliesHelpFragment();
                                break;
                        case 5:
                                switchMainProductFragment();
                                break;
                        case 6:
                                switchOrganizationFragment();
                                break;
                        case 7:
                                switchPositionFragment();
                                break;
                        default:
                                break;
                        }
                }
        };

        /** 獲取手機螢幕資訊 **/
        public void getDisplayMetrics() {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                myScreenWidth = metrics.widthPixels;
                myScreenHeight = metrics.heightPixels;

                myScreenDensity = metrics.density;
                myScreenDensityDpi = metrics.densityDpi;

                Log.d(TAG, "Screen Width : " + myScreenWidth);
                Log.d(TAG, "Screen Height : " + myScreenHeight);
                Log.d(TAG, "Screen Density : " + myScreenDensity);
                Log.d(TAG, "Screen DensityDpi : " + myScreenDensityDpi);
        }

        /** 獲取FB的KeyHash **/
        private void getFacebookKeyHash() {
                String mAppPackage = SysConfig.packageName;

                /**
                 * Add this code to print out the key hash, and use that KeyHash
                 */
                Log.d(TAG, "TRY\nKeyHash:");
                try {
                        PackageInfo info = getPackageManager().getPackageInfo(mAppPackage, PackageManager.GET_SIGNATURES);
                        for (Signature signature : info.signatures) {
                                MessageDigest md = MessageDigest.getInstance("SHA");
                                md.update(signature.toByteArray());
                                Log.d(TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
                        }
                } catch (NameNotFoundException e) {

                } catch (NoSuchAlgorithmException e) {

                }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case android.R.id.home:
                        toggle();
                        return true;
                case R.id.action_person:
                        gotoMemberActivity();
                        return true;
                case R.id.action_search:
                        gotoSearchActivity();
                        // switchSearchFragment();
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        /**
         * switchMainFragment 介面
         */
        public void switchMainFragment() {
                Log.i(TAG, "switchMainFragment");
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                // if (mMainFragment == null)
                mMainFragment = new MainFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mMainFragment);
                } else {
                        ft.replace(R.id.main_content, mMainFragment);
                }
                ft.commit();
        }

        /**
         * switchMenuFragment 介面
         */
        public void switchMenuFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_menu_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSlidingMenuFragment == null)
                        mSlidingMenuFragment = new SlidingMenuFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_menu_content, mSlidingMenuFragment);
                } else {
                        ft.replace(R.id.fragment_menu_content, mSlidingMenuFragment);
                }
                ft.commit();
        }

        /**
         * switchRightMenuFragment 介面
         */
        public void switchRightMenuFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_right_menu_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mRightMenuFragment == null)
                        mRightMenuFragment = new RightMenuFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_right_menu_content, mRightMenuFragment);
                } else {
                        ft.replace(R.id.fragment_right_menu_content, mRightMenuFragment);
                }
                ft.commit();
        }

        /**
         * switchBottomMenuFragment 介面
         */
        public void switchBottomMenuFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_bottom);

                FragmentTransaction ft = manager.beginTransaction();

                if (mBottomMenuFragment == null)
                        mBottomMenuFragment = new BottomMenuFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_bottom, mBottomMenuFragment);
                } else {
                        ft.replace(R.id.fragment_bottom, mBottomMenuFragment);
                }
                ft.commit();
        }

        /**
         * switchLoginFragment 介面
         */
        public void switchLoginFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);
                FragmentTransaction ft = manager.beginTransaction();

                if (mLoginFragment == null)
                        mLoginFragment = new LoginFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mLoginFragment);
                } else {
                        ft.replace(R.id.main_content, mLoginFragment);
                }
                ft.commit();
        }

        /**
         * switchSearchFragment 介面
         */
        public void switchSearchFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSearchFragment == null)
                        mSearchFragment = new SearchFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mSearchFragment);
                } else {
                        ft.replace(R.id.main_content, mSearchFragment);
                }
                ft.commit();
        }

        /**
         * switchSearchFragment 介面
         */
        public void switchSuppliesHelpFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSuppliesHelpFragment == null)
                        mSuppliesHelpFragment = new SuppliesHelpFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mSuppliesHelpFragment);
                } else {
                        ft.replace(R.id.main_content, mSuppliesHelpFragment);
                }
                ft.commit();
        }

        /**
         * switchOrganizationFragment 介面
         */
        public void switchOrganizationFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mOrganizationFragment == null)
                        mOrganizationFragment = new OrganizationFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mOrganizationFragment);
                } else {
                        ft.replace(R.id.main_content, mOrganizationFragment);
                }
                ft.commit();
        }

        /**
         * switchTestFragment 介面
         */
        public void switchTestFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mTestFragment == null)
                        mTestFragment = new TestFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mTestFragment);
                } else {
                        ft.replace(R.id.main_content, mTestFragment);
                }
                ft.commit();
        }

        /**
         * switchTestPostFragment 介面
         */
        public void switchTestPostFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mTestPostFragment == null)
                        mTestPostFragment = new TestPostFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mTestPostFragment);
                } else {
                        ft.replace(R.id.main_content, mTestPostFragment);
                }
                ft.commit();
        }

        /**
         * switchEmergencyFragment 介面
         */

        public void switchEmergencyFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mEmergencyFragment == null)
                        mEmergencyFragment = new EmergencyFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mEmergencyFragment);
                } else {
                        ft.replace(R.id.main_content, mEmergencyFragment);
                }
                ft.commit();
        }

        /**
         * switchProductFragment 介面
         */

        public void switchMainProductFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mMainProductSellFragment == null)
                        mMainProductSellFragment = new MainProductSellFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mMainProductSellFragment);
                } else {
                        ft.replace(R.id.main_content, mMainProductSellFragment);
                }
                ft.commit();
        }

        /**
         * switchPositionFragment 介面
         */
        public void switchPositionFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mPositionFragment == null)
                        mPositionFragment = new PositionFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mPositionFragment);
                } else {
                        ft.replace(R.id.main_content, mPositionFragment);
                }
                ft.commit();
        }

        private void gotoMemberActivity() {
                Intent intent = new Intent();
                intent.setClass(mContext, MemberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, MEMBER_ACTIVITY_CODE);
        }

        private void gotoSearchActivity() {
                Intent intent = new Intent();
                intent.setClass(mContext, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SEARCH_ACTIVITY_CODE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);

                switch (requestCode) {
                case MEMBER_ACTIVITY_CODE:
                        switch (resultCode) {
                        case REGISTER_SUCCESS_CODE:
                                FunctionUtil.showToastMsg(mContext, getString(R.string.register_success_hint));
                                break;
                        case LOGIN_SUCCESS_CODE:
                                FunctionUtil.showToastMsg(mContext, getString(R.string.login_success_hint));
                                break;
                        case LOGOUT_SUCCESS_CODE:
                                FunctionUtil.showToastMsg(mContext, getString(R.string.logout_success_hint));
                                break;
                        }
                        break;
                }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getSupportMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        @Override
        protected void onResume() {
                // TODO Auto-generated method stub
                super.onResume();
        }

        @Override
        protected void onPause() {
                // TODO Auto-generated method stub
                super.onPause();
        }

        @Override
        protected void onDestroy() {
                // TODO Auto-generated method stub
                super.onDestroy();
                stopService(new Intent(this, LocationService.class));
        }

}
