package tw.org.by37.fragment.member;

import java.util.Arrays;

import tw.org.by37.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

public class MemberLoginFragment extends Fragment {

        private final static String TAG = "MemberLoginFragment";

        private Context mContext;

        private LoginButton loginBtn;
        private TextView tv_info;
        private UiLifecycleHelper uiHelper;

        public MemberLoginFragment() {
                super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
                uiHelper.onCreate(savedInstanceState);

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_member_login, container, false);

                findView(view);

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {

                tv_info = (TextView) view.findViewById(R.id.textView1);

                loginBtn = (LoginButton) view.findViewById(R.id.fb_login_button);
                loginBtn.setReadPermissions(Arrays.asList("email"));
                loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
                        @Override
                        public void onUserInfoFetched(GraphUser user) {
                                if (user != null) {
                                        StringBuffer sb = new StringBuffer();
                                        sb.append("Id : " + user.getId() + "\n");
                                        sb.append("Name : " + user.getName() + "\n");
                                        sb.append("FirstName : " + user.getFirstName() + "\n");
                                        sb.append("LastName : " + user.getLastName() + "\n");
                                        sb.append("MiddleName : " + user.getMiddleName() + "\n");
                                        sb.append("Username : " + user.getUsername() + "\n");
                                        sb.append("Birthday : " + user.getBirthday() + "\n");
                                        sb.append("Link : " + user.getLink() + "\n");
                                        sb.append("Class : " + user.getClass() + "\n");
                                        sb.append("Location : " + user.getLocation() + "\n");
                                        sb.append("InnerJSONObject : " + user.getInnerJSONObject() + "\n");
                                        tv_info.setText("You are currently logged in as " + user.getName() + "\n" + sb.toString());
                                } else {
                                        tv_info.setText("You are not logged in.");
                                }
                        }
                });
        }

        private Session.StatusCallback statusCallback = new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                        if (state.isOpened()) {
                                Log.d(TAG, "Facebook session opened.");
                        } else if (state.isClosed()) {
                                Log.d(TAG, "Facebook session closed.");
                        }
                }
        };

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);

                /** FB Login Session **/
                uiHelper.onActivityResult(requestCode, resultCode, data);
                /** End of FB Login Session **/
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
                uiHelper.onResume();
        }

        @Override
        public void onStart() {
                super.onStart();
        }

        @Override
        public void onStop() {
                super.onStop();
        }

        @Override
        public void onPause() {
                super.onPause();
                uiHelper.onPause();
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
                uiHelper.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle savedState) {
                super.onSaveInstanceState(savedState);
                uiHelper.onSaveInstanceState(savedState);
        }
}
