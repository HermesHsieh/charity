package tw.org.by37;

import android.os.Bundle;
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

        /** Screen Param **/
        public static int myScreenWidth = 0;
        public static int myScreenHeight = 0;
        public static float myScreenDensity = 0;
        public static float myScreenDensityDpi = 0;
        /** End of Screen Param **/

        private SlidingMenu mSlidingMenu;

        private boolean mSlidingMenuShow = false;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                getDisplayMetrics();

                setContentView(R.layout.activity_main);

                initSlidingMenu();
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
                return super.onKeyDown(keyCode, event);
        }

        /**
         * ��l�Ƴ]�wSlidingMenu
         */
        public void initSlidingMenu() {
                setBehindContentView(R.layout.fragment_menu);

                // customize the SlidingMenu
                mSlidingMenu = getSlidingMenu();
                mSlidingMenu.setMode(SlidingMenu.LEFT);// �]�m������
                mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// �]�m���e��,���:�Ѿl���e��(dp)
                mSlidingMenu.setFadeDegree(0.35f);// �]�m�H�J�H�X�����
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// �]�m��ռҦ�
                mSlidingMenu.setShadowDrawable(R.drawable.shadow);// �]�m���\����v�Ϥ�
                mSlidingMenu.setFadeEnabled(true);// �]�m�ưʮɵ�檺�O�_�H�J�H�X
                mSlidingMenu.setBehindScrollScale(0.333f);// �]�m�ưʮɩ���ĪG
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

        // /**
        // * replaceSlidingMenu ����
        // */
        // public void replaceSlidingMenu() {
        // FragmentManager manager = getSupportFragmentManager();
        // FragmentSlidingMenu mFragmentMenu = new FragmentSlidingMenu();
        //
        // FragmentTransaction transaction = manager.beginTransaction();
        // transaction.replace(R.id.fragment_slidingmenu, mFragmentMenu,
        // "mFragmentMenu");
        //
        // transaction.commit();
        // }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case android.R.id.home:
                        toggle();
                        return true;

                }
                return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getSupportMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

}
