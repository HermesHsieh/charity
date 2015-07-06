package tw.org.by37;

import static tw.org.by37.config.RequestCode.MEMBER_ACTIVITY_CODE;
import tw.org.by37.position.PositionFragment;
import tw.org.by37.util.FunctionUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class PositionActivity extends FragmentActivity {

        private final static String TAG = PositionActivity.class.getName();

        private Context mContext;

        private PositionFragment mPositionFragment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Log.v(TAG, "onCreate");
                setContentView(R.layout.activity_position);
                mContext = this;

                switchPositionFragment();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);
        }

        /**
         * switchPositionFragment 介面
         */
        public void switchPositionFragment() {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mPositionFragment == null)
                        mPositionFragment = new PositionFragment();

                if (fragment == null) {
                        ft.add(R.id.fragment_content, mPositionFragment);
                } else {
                        ft.replace(R.id.fragment_content, mPositionFragment);
                }
                ft.commit();
        }

        /**
         * 記錄手指按下時的縱坐標。
         */
        private float yDown;

        /**
         * 記錄手指移動時的縱坐標。
         */
        private float yMove;

        /** 控制滑動偵測關閉鍵盤 **/
        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                        yDown = event.getRawY();
                        break;
                case MotionEvent.ACTION_MOVE:
                        yMove = event.getRawY();

                        int distanceY = (int) Math.abs((yMove - yDown));

                        if (distanceY > 10) {
                                FunctionUtil.closeInputKeyBoard(mContext, this);
                        }

                        if (distanceY > 80) {
                                if (mPositionFragment != null) {
                                        if (!mPositionFragment.isVisibleSubmitButton) {
                                                // 如果搜尋過後,確認按鈕是消失的,則滑動大於10就讓他顯示
                                                mPositionFragment.isVisibleSubmitButton = true;
                                                mPositionFragment.SubmitButtonAndMarkerShow(true);
                                        }
                                }
                        }
                        break;
                case MotionEvent.ACTION_UP:
                        break;
                }
                return super.dispatchTouchEvent(event);
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
        protected void onStart() {
                super.onStart();
        }

        @Override
        public void onStop() {
                super.onStop();
        }

        @Override
        protected void onPause() {
                super.onPause();
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it
                // is present.
                getMenuInflater().inflate(R.menu.back_main, menu);
                return true;
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
}
