package tw.org.by37;

import static tw.org.by37.config.RequestCode.*;

import tw.org.by37.data.UserData2;
import tw.org.by37.member.LoginFragment;
import tw.org.by37.member.MemberFragment;
import tw.org.by37.member.SignupFragment;
import tw.org.by37.menu.BottomMenuFragment;
import tw.org.by37.menu.RightMenuFragment;
import tw.org.by37.menu.SlidingMenuFragment;
import tw.org.by37.search.SearchFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MemberActivity extends SlidingFragmentActivity {

        private Context mContext;

        private final static String TAG = "MemberActivity";

        /** 滑動Menu **/
        private SlidingMenu mSlidingMenu;

        private boolean mSlidingMenuShow = false;

        /** Sliding Menu Fragment **/
        private SlidingMenuFragment mSlidingMenuFragment;

        /** Right Sliding Menu Fragment **/
        private RightMenuFragment mRightMenuFragment;

        /** Bottom Menu Fragment **/
        private BottomMenuFragment mBottomMenuFragment;

        /** MemberFragment Fragment **/
        private MemberFragment mMemberFragment;

        /** SignupFragment Fragment **/
        private SignupFragment mSignupFragment;

        /** MemberLogin Fragment **/
        private LoginFragment mLoginFragment;

        /** Search Fragment **/
        private SearchFragment mSearchFragment;

        /** 加入會員按鈕 **/
        private Button btn_join;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                mContext = this;

                setContentView(R.layout.activity_member);

                initSlidingMenu();

                // 判別會員的登入狀態
                if (MainActivity.mUserApplication.userData != null) {
                        // 登入狀態 -> 會員中心的Fragment
                        setTitle(R.string.title_member);
                        switchMemberFragment();
                } else {
                        setTitle(R.string.title_member_login);
                        // 未登入狀態 -> 登入的Fragment
                        switchLoginFragment();
                }
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
                        Log.i(TAG, "Click Position : " + position);
                        mSlidingMenu.toggle();
                }
        };

        AdapterView.OnItemClickListener mRightListViewItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        Log.i(TAG, "Click Position : " + position);
                        mSlidingMenu.toggle();
                }
        };

        public boolean onKeyDown(int keyCode, KeyEvent event) {
                return super.onKeyDown(keyCode, event);
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
         * switchMemberFragment 介面
         */
        public void switchMemberFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);
                FragmentTransaction ft = manager.beginTransaction();

                if (mMemberFragment == null)
                        mMemberFragment = new MemberFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mMemberFragment);
                } else {
                        ft.replace(R.id.main_content, mMemberFragment);
                }
                ft.commit();
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
        }

        /**
         * switchSignupFragment 介面
         */
        public void switchSignupFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSignupFragment == null)
                        mSignupFragment = new SignupFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mSignupFragment);
                } else {
                        ft.replace(R.id.main_content, mSignupFragment);
                }
                ft.commit();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);

                if (mLoginFragment != null) {
                        mLoginFragment.onActivityResult(requestCode, resultCode, data);
                }

                if (resultCode == REGISTER_SUCCESS_CODE) {
                        // 會員註冊成功
                        RegisterSuccess();
                }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case android.R.id.home:
                        toggle();
                        return true;
                case R.id.action_person:
                        switchLoginFragment();
                        return true;
                case R.id.action_search:
                        switchSearchFragment();
                        return true;
                case R.id.action_back:
                        this.finish();
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        private void RegisterSuccess() {
                Intent intent = this.getIntent();
                this.setResult(REGISTER_SUCCESS_CODE, intent); // 回傳RESULT_OK
                this.finish();
        }

        @Override
        public void onResume() {
                super.onResume();
        }

        @Override
        public void onPause() {
                super.onPause();
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle savedState) {
                super.onSaveInstanceState(savedState);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getSupportMenuInflater().inflate(R.menu.member, menu);
                return true;
        }
}
