package tw.org.by37;

import static tw.org.by37.config.RequestCode.FBLOGIN_REQUEST_CODE;
import tw.org.by37.data.SelectingData;
import tw.org.by37.data.SupplyData;
import tw.org.by37.fragment.member.LoginFragment;
import tw.org.by37.fragment.menu.BottomMenuFragment;
import tw.org.by37.fragment.menu.RightMenuFragment;
import tw.org.by37.fragment.menu.SlidingMenuFragment;
import tw.org.by37.fragment.organization.OrganizationFragment;
import tw.org.by37.fragment.search.SearchFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class OrganizationActivity extends SlidingFragmentActivity {

        private Context mContext;

        private final static String TAG = "OrganizationActivity";

        /** 滑動Menu **/
        private SlidingMenu mSlidingMenu;

        private boolean mSlidingMenuShow = false;

        /** Sliding Menu Fragment **/
        private SlidingMenuFragment mSlidingMenuFragment;

        /** Right Sliding Menu Fragment **/
        private RightMenuFragment mRightMenuFragment;

        /** Bottom Menu Fragment **/
        private BottomMenuFragment mBottomMenuFragment;

        /** MemberLogin Fragment **/
        private LoginFragment mMemberLoginFragment;

        /** Search Fragment **/
        private SearchFragment mSearchFragment;

        /** Organization Fragment **/
        private OrganizationFragment mOrganizationFragment;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                mContext = this;

                setContentView(R.layout.activity_organization);

                setTitle(R.string.title_organization);

                initSlidingMenu();

                switchOrganizationFragment(SelectingData.mSupplyData);

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

                        if (position == 0) {
                                // switchMainFragment();
                        }
                }
        };

        AdapterView.OnItemClickListener mRightListViewItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        Log.i(TAG, "Click Position : " + position);
                        mSlidingMenu.toggle();

                        if (position == 1) {
                                // switchTestFragment();
                        } else {
                                if (position == 4) {
                                        // switchSuppliesHelpFragment();
                                } else {
                                        if (position == 6) {
                                                // switchOrganizationFragment();
                                        }
                                }
                        }
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
         * switchMemberLoginFragment 介面
         */
        public void switchMemberLoginFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);
                FragmentTransaction ft = manager.beginTransaction();

                if (mMemberLoginFragment == null)
                        mMemberLoginFragment = new LoginFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mMemberLoginFragment);
                } else {
                        ft.replace(R.id.fragment_content, mMemberLoginFragment);
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
         * switchOrganizationFragment 介面
         */
        public void switchOrganizationFragment(SupplyData mData) {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mOrganizationFragment == null)
                        mOrganizationFragment = new OrganizationFragment();

                mOrganizationFragment.setSupplyData(mData);

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mOrganizationFragment);
                } else {
                        ft.replace(R.id.fragment_content, mOrganizationFragment);
                }
                ft.commit();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);

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
                case R.id.action_back:
                        this.finish();
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getSupportMenuInflater().inflate(R.menu.organization, menu);
                return true;
        }

}
