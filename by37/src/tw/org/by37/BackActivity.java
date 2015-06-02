package tw.org.by37;

import static tw.org.by37.config.RequestCode.MEMBER_ACTIVITY_CODE;
import tw.org.by37.data.SelectingData;
import tw.org.by37.menu.RightMenuFragment;
import tw.org.by37.menu.SlidingMenuFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BackActivity extends SlidingFragmentActivity {

        private Context mContext;

        /** 滑動Menu **/
        private SlidingMenu mSlidingMenu;

        /** Sliding Menu Fragment **/
        private SlidingMenuFragment mSlidingMenuFragment;

        /** Right Sliding Menu Fragment **/
        private RightMenuFragment mRightMenuFragment;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                mContext = this;
                initSlidingMenu();
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
                        }
                });
                mSlidingMenu.setOnCloseListener(new OnCloseListener() {
                        @Override
                        public void onClose() {
                        }
                });

                /** 監聽右側菜單 **/
                mSlidingMenu.setSecondaryOnOpenListner(new OnOpenListener() {
                        @Override
                        public void onOpen() {

                        }
                });
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

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.action_back:
                        this.finish();
                        return true;
                case R.id.action_person:
                        gotoMemberActivity();
                        return true;
                case R.id.action_search:
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        public void gotoMemberActivity() {
                Intent intent = new Intent();
                intent.setClass(this, MemberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, MEMBER_ACTIVITY_CODE);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getSupportMenuInflater().inflate(R.menu.back_main, menu);
                // getMenuInflater().inflate(R.menu.back_main, menu);
                return true;
        }
}
