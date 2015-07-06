package tw.org.by37;

import tw.org.by37.member.LoginFragment;
import tw.org.by37.menu.BottomMenuFragment;
import tw.org.by37.menu.RightMenuFragment;
import tw.org.by37.menu.SlidingMenuFragment;
import tw.org.by37.organization.OrganizationFragment;
import tw.org.by37.search.SearchFragment;
import tw.org.by37.supplieshelp.SuppliesHelpFragment;
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

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;

public class SuppliesHelpActivity extends BackActivity {

        private Context mContext;

        private final static String TAG = SuppliesHelpActivity.class.getName();

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

        /** SuppliesHelp Fragment **/
        private SuppliesHelpFragment mSuppliesHelpFragment;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                mContext = this;

                setContentView(R.layout.activity_supplieshelp);

                setTitle(R.string.title_supplieshelp);

                switchSuppliesHelpFragment();

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
         * switchMemberLoginFragment 介面
         */
        public void switchMemberLoginFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);
                FragmentTransaction ft = manager.beginTransaction();

                if (mMemberLoginFragment == null)
                        mMemberLoginFragment = new LoginFragment();

                if (fragment == null) {
                        ft.add(R.id.main_content, mMemberLoginFragment);
                } else {
                        ft.replace(R.id.main_content, mMemberLoginFragment);
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
         * switchSuppliesHelpFragment 介面
         */
        public void switchSuppliesHelpFragment() {
                Log.i(TAG, "switchSuppliesHelpFragment");
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mSuppliesHelpFragment == null)
                        mSuppliesHelpFragment = new SuppliesHelpFragment();

                mSuppliesHelpFragment.setIndexView(false);

                if (fragment == null) {
                        ft.add(R.id.main_content, mSuppliesHelpFragment);
                } else {
                        ft.replace(R.id.main_content, mSuppliesHelpFragment);
                }
                ft.commit();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);

        }
}
