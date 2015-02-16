package tw.org.by37;

import static tw.org.by37.data.RequestCode.FBLOGIN_REQUEST_CODE;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import tw.org.by37.config.SysConfig;
import tw.org.by37.fragment.member.MemberLoginFragment;
import tw.org.by37.fragment.search.SearchFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

        private final static String TAG = "MainActivity";

        private Context mContext;

        /** Screen Param **/
        public static int myScreenWidth = 0;
        public static int myScreenHeight = 0;
        public static float myScreenDensity = 0;
        public static float myScreenDensityDpi = 0;
        /** End of Screen Param **/

        /** 滑動Menu **/
        private SlidingMenu mSlidingMenu;

        private boolean mSlidingMenuShow = false;

        /** MemberLogin Fragment **/
        private MemberLoginFragment mMemberLoginFragment;

        /** Search Fragment **/
        private SearchFragment mSearchFragment;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                mContext = this;

                getDisplayMetrics();

                setContentView(R.layout.activity_main);

                initSlidingMenu();

                initActionBar();

                getFacebookKeyHash();
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
                return super.onKeyDown(keyCode, event);
        }

        /**
         * 初始化 Action Bar
         */
        private void initActionBar() {

                // 設定ActionBar左上角的圖示
                // getSupportActionBar().setIcon(R.drawable.ic_action_person);

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

                // customize the SlidingMenu
                mSlidingMenu = getSlidingMenu();
                mSlidingMenu.setMode(SlidingMenu.LEFT);// 設置是左滑還是右滑，還是左右都可以滑，我這裡只做了左滑
                mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 設置菜單寬度
                mSlidingMenu.setFadeDegree(0.35f);// 設置淡入淡出的比例
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 設置手勢模式
                mSlidingMenu.setShadowDrawable(R.drawable.shadow);// 設置左功能表陰影圖片
                mSlidingMenu.setFadeEnabled(true);// 設置滑動時菜單的是否淡入淡出
                mSlidingMenu.setBehindScrollScale(0.333f);// 設置滑動時拖拽效果
                mSlidingMenu.setOnOpenListener(new OnOpenListener() {
                        @Override
                        public void onOpen() {
                                // TODO Auto-generated method stub
                                mSlidingMenuShow = true;
                        }
                });
                mSlidingMenu.setOnCloseListener(new OnCloseListener() {
                        @Override
                        public void onClose() {
                                // TODO Auto-generated method stub
                                mSlidingMenuShow = false;
                        }
                });
        }

        /** 獲取手機螢幕資訊 **/
        public void getDisplayMetrics() {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                myScreenWidth = metrics.widthPixels;
                myScreenHeight = metrics.heightPixels;

                myScreenDensity = metrics.density;
                myScreenDensityDpi = metrics.densityDpi;

                Log.i(TAG, "Screen Width : " + myScreenWidth);
                Log.i(TAG, "Screen Height : " + myScreenHeight);
                Log.i(TAG, "Screen Density : " + myScreenDensity);
                Log.i(TAG, "Screen DensityDpi : " + myScreenDensityDpi);

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
                        switchMemberLoginFragment();
                        return true;
                case R.id.action_search:
                        switchSearchFragment();
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        /**
         * switchMemberLoginFragment 介面
         */
        public void switchMemberLoginFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);
                FragmentTransaction ft = manager.beginTransaction();

                if (mMemberLoginFragment == null)
                        mMemberLoginFragment = new MemberLoginFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mMemberLoginFragment);
                } else {
                        ft.replace(R.id.fragment_content, mMemberLoginFragment);
                }
                ft.commit();

                setTitle(getString(R.string.fragment_title_member_login));
        }

        /**
         * switchSearchFragment 介面
         */
        public void switchSearchFragment() {
                FragmentManager manager = getSupportFragmentManager();
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

                setTitle(getString(R.string.fragment_title_search));
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);

                switch (requestCode) {
                case FBLOGIN_REQUEST_CODE:
                        if (resultCode == Activity.RESULT_OK) {
                                // 執行Fragment的onActivityResult
                                if (mMemberLoginFragment != null) {
                                        mMemberLoginFragment.onActivityResult(requestCode, resultCode, data);
                                }
                        }
                        break;
                }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getSupportMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

}
