package tw.org.by37.emergency;

import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.BackActivity;
import tw.org.by37.R;
import tw.org.by37.config.SysConfig;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class EmergencyActivity extends BackActivity {

        private static final String TAG = EmergencyActivity.class.getName();

        private String id = "";

        /** 廣播必備參數 **/
        private IntentFilter mGetEmergencyContentIntentFilter;
        private MainBroadcastReceiver mMainBroadcastReceiverr;

        private EmergencyData mEmergencyData;

        private TextView mTitleTextView;
        private TextView mContentTextView;

        private ImageButton mShareToFBBtn;
        private UiLifecycleHelper uiHelper; // Facebook SDK介面幫助類別

        @Override
        public void onCreate(Bundle savedInstanceState) {
                // TODO Auto-generated method stub
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_emergency);
                id = getIntent().getExtras().getString("ID");
                Log.d(TAG, "id = " + id);
                init();
                setView();
        }

        @Override
        protected void onResume() {
                super.onResume();
                registerReceiver(mMainBroadcastReceiverr, mGetEmergencyContentIntentFilter);
                AppEventsLogger.activateApp(this);
        }

        @Override
        protected void onPause() {
                super.onPause();
                unregisterReceiver(mMainBroadcastReceiverr);
                AppEventsLogger.deactivateApp(this);
        }

        /** 初始參數 **/
        private void init() {
                mGetEmergencyContentIntentFilter = new IntentFilter(SysConfig.GET_EMERGENCY_CONTENT_BROADCAST);
                mMainBroadcastReceiverr = new MainBroadcastReceiver();
                uiHelper = new UiLifecycleHelper(this, callback);

                new GetEmergencyContentTask(this).execute(id);

        }

        private void setView() {
                mTitleTextView = (TextView) findViewById(R.id.emergency_title_textview);
                mContentTextView = (TextView) findViewById(R.id.emergency_content_textview);

                mShareToFBBtn = (ImageButton) findViewById(R.id.emergency_shrae_fb_imagebutton);
                mShareToFBBtn.setOnClickListener(new ImageButton.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                // TODO Auto-generated method stub
                                postStatus();
                        }
                });
        }

        /** 設定收到廣播後的動作 **/
        public class MainBroadcastReceiver extends BroadcastReceiver {

                @Override
                public void onReceive(Context context, Intent intent) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "action : " + intent.getAction());
                        if (intent.getAction().equals(SysConfig.GET_EMERGENCY_CONTENT_BROADCAST)) {
                                String strEmergency = intent.getExtras().getString(SysConfig.GET_EMERGENCY_CONTENT_BROADCAST);
                                Log.d(TAG, "strEmergency:" + strEmergency);
                                getEmergencyData(strEmergency);
                        }
                }
        }

        /** 解析Emergency字串 **/
        private void getEmergencyData(String str) {
                try {
                        JSONObject strJsonObject = new JSONObject(str);
                        mEmergencyData = new EmergencyData();

                        String id = strJsonObject.getString("id");
                        String title = strJsonObject.getString("title");
                        String group = strJsonObject.getString("group");
                        String content = strJsonObject.getString("content");

                        mEmergencyData.setId(id);
                        mEmergencyData.setTitle(title);
                        mEmergencyData.setGroup(group);
                        mEmergencyData.setContent(content);
                        Log.d(TAG, "id:" + id);
                        Log.d(TAG, "title:" + title);
                        Log.d(TAG, "group:" + group);
                        Log.d(TAG, "content:" + content);

                } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                show();
        }

        private void show() {
                String title = mEmergencyData.getTitle();
                String content = mEmergencyData.getContent();

                mTitleTextView.setText(title);
                mContentTextView.setText(content);
        }

        Session.StatusCallback callback = new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                }
        };

        private void postStatus() {
                // 判斷使用者安裝的Facebook App是否可以提供該功能
                if (FacebookDialog.canPresentShareDialog(this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                        // 使用FacebookDialog.ShareDialog來張貼文章
                        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setLink("http://www.aaronlife.com") // 要張貼的連結
                                        .setPicture("http://www.aaronlife.com/travel/images/travel_2014-09-16_13.jpg") // 照片連結
                                        .build();

                        uiHelper.trackPendingDialogCall(shareDialog.present()); // 切換到張貼狀態的Facebook
                                                                                // Activity
                } else {
                        Toast.makeText(this, "不支援Sharedialog", Toast.LENGTH_SHORT).show();
                }
        }

}
