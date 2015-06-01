package tw.org.by37.member;

import static tw.org.by37.config.RequestCode.*;
import static tw.org.by37.config.SysConfig.*;

import java.net.URL;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.MainActivity;
import tw.org.by37.R;
import tw.org.by37.SignupActivity;
import tw.org.by37.data.RegisterData;
import tw.org.by37.data.UserData2;
import tw.org.by37.service.UsersApiService;
import tw.org.by37.util.FunctionUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class LoginFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {

        private final static String TAG = "LoginFragment";

        private Context mContext;

        private ProgressDialog psDialog;

        /** Google+ **/
        private static final int RC_SIGN_IN = 0;
        // Profile pic image size in pixels
        private static final int PROFILE_PIC_SIZE = 200;
        // Google client to interact with Google API
        private GoogleApiClient mGoogleApiClient;

        /**
         * A flag indicating that a PendingIntent is in progress and prevents us
         * from starting further intents.
         */
        private boolean mIntentInProgress;
        private boolean mSignInClicked;
        private ConnectionResult mConnectionResult;
        private SignInButton btnSignIn;

        /** Google+ 的登入狀態 **/
        private boolean userGoogleStatus = false;
        /** End of Google+ **/

        /** FB Login **/
        private LoginButton btn_fb_login;
        private TextView tv_info;
        private UiLifecycleHelper uiHelper;
        /** End of FB Login **/

        /** 登入會員按鈕 **/
        private Button btn_login;
        /** 加入會員按鈕 **/
        private Button btn_join;

        private EditText edt_account;
        private EditText edt_password;

        /** 提示字 忘記密碼 **/
        private TextView tv_forget_password;

        /** 記住我 **/
        private CheckBox ckb_remember_me;
        /** 記住我的帳號 **/
        private String rm_account;
        private boolean remember_me = false;

        public static String fb_name = null;
        public static String fb_email = null;
        public static String fb_image = null;

        public static String g_name = null;
        public static String g_email = null;
        public static String g_image = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_login, container, false);
                mContext = getActivity();
                getRememberMe();
                getUserRMAccount();
                findView(view);
                initGoogleSignInButton(view);
                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
                uiHelper.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                tv_info = (TextView) view.findViewById(R.id.textView1);

                edt_account = (EditText) view.findViewById(R.id.edt_account);
                edt_password = (EditText) view.findViewById(R.id.edt_password);

                if (remember_me) {
                        if (rm_account != null)
                                edt_account.setText(rm_account);
                }

                edt_account.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                                // 假如記住我的選項有打勾
                                if (remember_me) {
                                        new Thread(new Runnable() {
                                                public void run() {
                                                        rm_account = edt_account.getText().toString();
                                                        // 當輸入帳號時都將他的值儲存於手機內
                                                        putUserRMAccount(rm_account);
                                                }
                                        }).start();
                                }
                        }
                });

                ckb_remember_me = (CheckBox) view.findViewById(R.id.ckb_remember_me);
                ckb_remember_me.setChecked(remember_me);
                ckb_remember_me.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Log.d(TAG, "onCheckedChanged : " + isChecked);
                                remember_me = isChecked;
                                putUserRememberMe();
                                if (remember_me) {
                                        rm_account = edt_account.getText().toString();
                                        putUserRMAccount(rm_account);
                                } else {
                                        removeUserRMAccount();
                                }
                        }
                });

                btn_login = (Button) view.findViewById(R.id.btn_login);
                btn_login.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                RegisterData.email = edt_account.getText().toString();
                                RegisterData.password = edt_password.getText().toString();
                                RegisterData.source = "by37";
                                MainActivity.mUserApplication.setRegisterData();
                                new UserLoginAsyncTask().execute();
                        }
                });

                btn_join = (Button) view.findViewById(R.id.btn_join);
                btn_join.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                gotoSignupActivity();
                        }
                });

                btn_fb_login = (LoginButton) view.findViewById(R.id.btn_facebook);
                // If using in a fragment
                btn_fb_login.setFragment(this);
                btn_fb_login.setReadPermissions(Arrays.asList("email"));
                btn_fb_login.setUserInfoChangedCallback(userCallback);

                /** 忘記密碼 **/
                tv_forget_password = (TextView) view.findViewById(R.id.tv_forget_password);
                /** 加底線 **/
                SpannableString content = new SpannableString(getString(R.string.forget_password));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                tv_forget_password.setText(content);
        }

        class UserLoginAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {
                        Log.d(TAG, "UserLoginAsyncTask");

                        String user = RegisterData.email;
                        String pwd = RegisterData.password;
                        String src = RegisterData.source;

                        String result = UsersApiService.LoginUser(user, pwd, src);
                        Log.d(TAG, "Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        /** 關閉提示Dialog **/
                        psDialog.dismiss();
                        String mInfo = tv_info.getText().toString();
                        String mReg = "";
                        String status = "";

                        if (result != null) {

                                /** 判別伺服器是否正常work狀態 **/
                                if (FunctionUtil.isSleepServer(result)) {
                                        mReg = "Register Info : server has been sleep." + "\n\n";
                                } else {
                                        mReg = "Register Info : " + result + "\n\n";
                                }
                                tv_info.setText(mReg + mInfo);

                                try {
                                        JSONObject mJObj = new JSONObject(result);
                                        status = mJObj.getString("status");

                                        if (status.equals("success")) {
                                                /** 儲存使用者資料於手機內存 **/
                                                MainActivity.mUserApplication.putUserLogin();
                                                /** 儲存使用者結果資料於手機內存 **/
                                                MainActivity.mUserApplication.putUserResult(result);
                                                /** 刷新使用者資料 **/
                                                MainActivity.mUserApplication.updateUserResult();
                                                /** 刷新使用者大頭照資料 **/
                                                MainActivity.mUserApplication.updateUser_Image();
                                                /** 登入成功,finish **/
                                                LoginSuccess();
                                        } else {
                                                if (status.equals("fail")) {
                                                        if (RegisterData.source.equals("by37")) {
                                                                FunctionUtil.AlertDialogCheck(mContext, "", mJObj.getString("message")).show();
                                                                Log.d(TAG, "Login fail : by37");
                                                                Log.d(TAG, "Meaage : " + mJObj.getString("message"));
                                                        } else {
                                                                if (RegisterData.source.equals("facebook")) {
                                                                        Log.d(TAG, "Login fail : facebook");
                                                                        Log.d(TAG, "Meaage : " + mJObj.getString("message"));
                                                                        gotoSignupActivity();
                                                                } else {
                                                                        if (RegisterData.source.equals("google")) {
                                                                                Log.d(TAG, "Login fail : google");
                                                                                Log.d(TAG, "Meaage : " + mJObj.getString("message"));
                                                                                gotoSignupActivity();
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                        /** 開啟提示Dialog **/
                        psDialog = ProgressDialog.show(mContext, "", "登入中，請稍候...");
                }
        }

        private Session.StatusCallback statusCallback = new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                        onSessionStateChange(session, state, exception);
                }
        };

        /** Facebook登入後的 UserCallBack **/
        private UserInfoChangedCallback userCallback = new UserInfoChangedCallback() {
                @Override
                public void onUserInfoFetched(GraphUser user) {
                        if (user != null) {
                                try {
                                        RegisterData.email = user.getInnerJSONObject().getString("email");
                                        RegisterData.password = "";
                                        RegisterData.source = "facebook";
                                        RegisterData.name = user.getName();
                                        RegisterData.image = getFacebookProfilePicturePath(user.getId());
                                } catch (JSONException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                }

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

                                callFacebookLogout();

                                new UserLoginAsyncTask().execute();
                        }
                }
        };

        private void onSessionStateChange(Session session, SessionState state, Exception exception) {
                if (state.isOpened()) {
                        Log.d(TAG, "Facebook session opened.");
                } else if (state.isClosed()) {
                        Log.d(TAG, "Facebook session closed.");
                }
        }

        private String getFacebookProfilePicturePath(String userID) {
                return "https://graph.facebook.com/" + userID + "/picture?type=large";
        }

        /**
         * Logout From Facebook
         */
        public static void callFacebookLogout() {
                Log.i(TAG, "FacebookLogout");
                if (Session.getActiveSession() != null) {
                        Session.getActiveSession().closeAndClearTokenInformation();
                }
                Session.setActiveSession(null);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);

                uiHelper.onActivityResult(requestCode, resultCode, data);

                switch (requestCode) {
                case RC_SIGN_IN:
                        if (resultCode != getActivity().RESULT_OK) {
                                mSignInClicked = false;
                        }

                        mIntentInProgress = false;

                        if (!mGoogleApiClient.isConnecting()) {
                                mGoogleApiClient.connect();
                        }
                        break;
                }
        }

        /** Google+ **/
        private void initGoogleSignInButton(View view) {
                btnSignIn = (SignInButton) view.findViewById(R.id.btn_google);
                // Button click listeners
                btnSignIn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (!userGoogleStatus) {
                                        // Signin button clicked
                                        signInWithGplus();
                                } else {
                                        signOutFromGplus();
                                }
                        }
                });

                // Initializing google plus api client
                mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, null).addScope(Plus.SCOPE_PLUS_LOGIN).build();

        }

        @Override
        public void onConnectionFailed(ConnectionResult result) {
                if (!result.hasResolution()) {
                        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(), 0).show();
                        return;
                }

                if (!mIntentInProgress) {
                        // Store the ConnectionResult for later usage
                        mConnectionResult = result;

                        if (mSignInClicked) {
                                // The user has already clicked 'sign-in' so we
                                // attempt to
                                // resolve all
                                // errors until the user is signed in, or they
                                // cancel.
                                resolveSignInError();
                        }
                }
        }

        @Override
        public void onConnected(Bundle arg0) {
                mSignInClicked = false;
                Log.i(TAG, "Google+ User is connected!");

                // Get user's information
                getProfileInformation();

                // Update the UI after signin
                updateUI(true);

                // Login Success, finish this activity to MainActivity To
                // Auto Login
        }

        @Override
        public void onConnectionSuspended(int arg0) {
                mGoogleApiClient.connect();
                updateUI(false);
        }

        /** Updating the UI, showing/hiding buttons and profile layout **/
        private void updateUI(boolean isSignedIn) {
                if (isSignedIn) {
                        userGoogleStatus = true;
                        // btnSignIn.setVisibility(View.GONE);
                        // btn_google_login.setVisibility(View.GONE);
                } else {
                        userGoogleStatus = false;

                        tv_info.setText("You logout Google+ success !");
                        // btnSignIn.setVisibility(View.VISIBLE);
                        // btn_google_login.setVisibility(View.VISIBLE);
                }
        }

        /** Sign-in into google **/
        private void signInWithGplus() {
                // 如果是登出狀態，則登入
                if (!mGoogleApiClient.isConnecting()) {
                        mSignInClicked = true;
                        resolveSignInError();
                }
        }

        /** Method to resolve any signin errors **/
        private void resolveSignInError() {
                if (mConnectionResult.hasResolution()) {
                        try {
                                mIntentInProgress = true;
                                mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
                        } catch (SendIntentException e) {
                                mIntentInProgress = false;
                                mGoogleApiClient.connect();
                        }
                }
        }

        /**
         * Fetching user's information name, email, profile pic
         * */
        private void getProfileInformation() {
                try {
                        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                                String personId = currentPerson.getId();
                                String personName = currentPerson.getDisplayName();
                                String personPhotoUrl = currentPerson.getImage().getUrl();
                                String personGooglePlusProfile = currentPerson.getUrl();

                                String personBirthday = currentPerson.getBirthday();
                                int personGender = currentPerson.getGender();
                                String personNickname = currentPerson.getNickname();

                                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                                Log.i(TAG, "Id: " + personId + ", Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email + ", Image: " + personPhotoUrl + ", Birthday: " + personBirthday + ", Gender: " + personGender + ", Nickname: " + personNickname);

                                // by default the profile url gives
                                // 50x50 px
                                // image only
                                // we can replace the value with
                                // whatever
                                // dimension we want by
                                // replacing sz=X
                                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

                                Log.e(TAG, "PhotoUrl : " + personPhotoUrl);

                                Log.i(TAG, "Finally Set UserData");
                                Log.i(TAG, "account : " + email + ", Social_Id : " + personId);

                                StringBuffer sb = new StringBuffer();
                                sb.append("Id : ").append(personId).append("\n");
                                sb.append("Name : ").append(personName).append("\n");
                                sb.append("plusProfile : ").append(personGooglePlusProfile).append("\n");
                                sb.append("Email : ").append(email).append("\n");
                                sb.append("PhotoUrl : ").append(personPhotoUrl).append("\n");
                                sb.append("Birthday : ").append(personBirthday).append("\n");
                                sb.append("Gender : ").append(personGender).append("\n");
                                sb.append("Nickname : ").append(personNickname).append("\n");

                                tv_info.setText(sb.toString());

                                signOutFromGplus();

                                /** set Google User Data **/
                                RegisterData.name = personName;
                                RegisterData.email = email;
                                RegisterData.password = "";
                                RegisterData.source = "google";
                                RegisterData.image = personPhotoUrl;

                                new UserLoginAsyncTask().execute();
                        } else {
                                Toast.makeText(mContext, "Person information is null", Toast.LENGTH_LONG).show();
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * Sign-out from google
         * */
        private void signOutFromGplus() {
                Log.e(TAG, "Google+ User is disconnected!");

                if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();
                        updateUI(false);
                }
        }

        /**
         * Revoking access from google
         * */
        @SuppressWarnings("unused")
        private void revokeGplusAccess() {
                if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status arg0) {
                                        Log.e(TAG, "User access revoked!");
                                        mGoogleApiClient.connect();
                                        updateUI(false);
                                }
                        });
                }
        }

        /** End of Google+ **/

        /** GotoActivity **/
        public void gotoSignupActivity() {
                Intent intent = new Intent();
                intent.setClass(mContext, SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SIGNUP_ACTIVITY_CODE);
        }

        private void LoginSuccess() {
                Intent intent = getActivity().getIntent();
                getActivity().setResult(LOGIN_SUCCESS_CODE, intent); // 回傳RESULT_OK
                getActivity().finish();
        }

        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        /** Preferences **/
        /** 獲取記住我資料 **/
        public void getRememberMe() {
                Log.i(TAG, "getRememberMe");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                try {
                        remember_me = sp.getBoolean(k_UserData_5, false);
                } catch (Exception e) {
                }
                Log.d(TAG, "remember_me : " + remember_me);
        }

        /** 儲存記住我資料 **/
        public void putUserRememberMe() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(k_UserData_5, remember_me);
                editor.commit();
        }

        /** 獲取記住我的帳號資料 **/
        public void getUserRMAccount() {
                Log.i(TAG, "getUserRMAccount");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                try {
                        rm_account = sp.getString(k_UserData_6, null);
                } catch (Exception e) {
                }
                Log.d(TAG, "rm_account : " + rm_account);
        }

        /** 儲存記住我的帳號資料 **/
        public void putUserRMAccount(String account) {
                Log.i(TAG, "putUserRMAccount " + account);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(k_UserData_6, account);
                editor.commit();
        }

        /** 清除記住我的帳號資料 **/
        public void removeUserRMAccount() {
                Log.i(TAG, "removeUserRMAccount");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_UserData_6);
                editor.commit();
        }

        /** End of Preferences **/

        @Override
        public void onResume() {
                super.onResume();
                uiHelper.onResume();
        }

        @Override
        public void onStart() {
                super.onStart();
                mGoogleApiClient.connect();
        }

        @Override
        public void onStop() {
                super.onStop();
                if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                }
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
