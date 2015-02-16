package tw.org.by37.fragment.member;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.R;
import tw.org.by37.data.UserData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Session.OpenRequest;
import com.facebook.model.GraphObject;

public class FacebookFragment extends Fragment {

        private final static String TAG = "FacebookFragment";

        private boolean status = false;

        private Button btn_facebook;

        private Context mContext;

        /** 當使用者儲存後,提示的ProgressDialog **/
        private ProgressDialog psDialog;

        public FacebookFragment() {
                super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Log.i(TAG, "onCreateView");

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_facebook_btn, container, false);

                findView(view);

                return view;
        }

        private void findView(View view) {
                btn_facebook = (Button) view.findViewById(R.id.btn_facebook);
                btn_facebook.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                // TODO Auto-generated method stub
                                if (!status) {
                                        facebookLoginFunction();
                                        Toast.makeText(getActivity(), "FaceBook Login ...", Toast.LENGTH_LONG).show();
                                        status = true;
                                } else {
                                        facebookLogoutFunction();
                                        Toast.makeText(getActivity(), "FaceBook Logout ...", Toast.LENGTH_LONG).show();
                                        status = false;
                                }
                        }
                });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Log.i(TAG, "onCreate");
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.v(TAG, "FacebookFragment onActivityResult");

                /** FB Login Session **/
                if (Session.getActiveSession() != null)
                        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);

                Session currentSession = Session.getActiveSession();
                if (currentSession == null || currentSession.getState().isClosed()) {
                        Session session = new Session.Builder(getActivity()).build();
                        Session.setActiveSession(session);
                        currentSession = session;
                }

                if (currentSession.isOpened()) {
                        Log.i(TAG, "currentSession.isOpened");

                        status = true;

                        // start Facebook Login
                        Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {
                                // callback when
                                // session
                                // changes state
                                @Override
                                public void call(Session session, SessionState state, Exception exception) {
                                        if (session.isOpened()) {

                                                Bundle params = new Bundle();
                                                // params.putString("fields",
                                                // "email,public_profile");
                                                params.putString("fields", "id,name,picture.width(160).height(160),email");
                                                /*
                                                 * make the API call
                                                 */
                                                new Request(session, "/me", params, HttpMethod.GET, new Request.Callback() {
                                                        public void onCompleted(Response response) {
                                                                // handle
                                                                // the
                                                                // result
                                                                GraphObject graphObject = response.getGraphObject();
                                                                JSONObject jsonUser = graphObject.getInnerJSONObject();
                                                                Log.i(TAG, "onActivityResult graphObject : " + graphObject.toString());

                                                                // setFacebookResultData(jsonUser);
                                                        }
                                                }).executeAsync();
                                        }
                                }
                        });

                }
                /** End of FB Login Session **/

        }

        private void facebookLoginFunction() {
                Log.i(TAG, "facebookLoginFunction");
                // Toast.makeText(getActivity(), "Login......",
                // Toast.LENGTH_LONG).show();
                Session currentSession = Session.getActiveSession();
                if (currentSession == null || currentSession.getState().isClosed()) {
                        Session session = new Session.Builder(getActivity()).build();
                        Session.setActiveSession(session);
                        currentSession = session;
                }

                if (currentSession.isOpened()) {
                        Log.i(TAG, "currentSession.isOpened");

                        status = true;

                        // start Facebook Login
                        Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {
                                // callback when session changes state
                                @Override
                                public void call(Session session, SessionState state, Exception exception) {
                                        if (session.isOpened()) {
                                                Bundle params = new Bundle();
                                                // params.putString("fields",
                                                // "email,public_profile");
                                                params.putString("fields", "id,name,gender,picture.width(160).height(160),email,birthday,first_name,middle_name,last_name");
                                                /* make the API call */
                                                new Request(session, "/me", params, HttpMethod.GET, new Request.Callback() {
                                                        public void onCompleted(Response response) {

                                                                GraphObject graphObject = response.getGraphObject();
                                                                JSONObject jsonUser = graphObject.getInnerJSONObject();
                                                                Log.i(TAG, "graphObject : " + graphObject.toString());

                                                                // setFacebookResultData(jsonUser);
                                                        }
                                                }).executeAsync();
                                        }
                                }
                        });
                }// Do whatever u want. User has logged in
                else if (!currentSession.isOpened()) {
                        Log.i(TAG, "!currentSession.isOpened");

                        status = false;

                        // Ask for username and password
                        OpenRequest op = new Session.OpenRequest(getActivity());

                        // 以網頁開啟登入授權
                        // op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
                        op.setCallback(null);

                        List<String> permissions = new ArrayList<String>();
                        permissions.add("publish_actions");
                        permissions.add("email");
                        permissions.add("user_birthday");
                        op.setPermissions(permissions);

                        Session session = new Session.Builder(getActivity()).build();
                        Session.setActiveSession(session);
                        session.openForPublish(op);
                }
        }

        // Facebook 登出
        private void facebookLogoutFunction() {
                Session session = Session.getActiveSession();
                if (session != null && !session.isClosed()) {
                        status = false;
                        session.closeAndClearTokenInformation();
                }
        }

        /**
         * 將Facebook回傳的JSON物件設定,設定完成後直接註冊帳號
         * 
         * @param jsonUser
         */
        private void setFacebookResultData(JSONObject jsonUser) {
                Log.v(TAG, "setFacebookResultData");
                String account = "";
                String social_id = "";
                String avatar = "";
                String name = "";
                String birthday = "";
                String gender = "";

                try {
                        avatar = jsonUser.getJSONObject("picture").getJSONObject("data").getString("url");
                } catch (JSONException e) {
                        Log.e(TAG, "avatar JSONException!");
                }
                try {
                        account = jsonUser.getString("email");
                } catch (JSONException e) {
                        Log.e(TAG, "email JSONException!");
                }
                try {
                        name = jsonUser.getString("name");
                } catch (JSONException e) {
                        Log.e(TAG, "name JSONException!");
                }
                try {
                        birthday = jsonUser.getString("birthday");
                } catch (JSONException e) {
                        Log.e(TAG, "birthday JSONException!");
                }
                try {
                        gender = jsonUser.getString("gender");
                } catch (JSONException e) {
                        Log.e(TAG, "gender JSONException!");
                }
                try {
                        social_id = jsonUser.getString("id");
                } catch (JSONException e) {
                        Log.e(TAG, "id JSONException!");
                }

                Log.i(TAG, "Finally Set UserData");
                Log.i(TAG, "account : " + account + ", Social_Id : " + social_id);
                UserData.setAccount(account);
                UserData.setSocial_Id(social_id);
                UserData.setLogin_Type("1");

                UserData.showUserData();

                // new FacebookLoginAsyncTask().execute();
        }

        // class FacebookLoginAsyncTask extends AsyncTask<String, Integer,
        // String> {
        // @Override
        // protected String doInBackground(String... param) {
        // String result = UsersApiService.FacebookLogin();
        // return result;
        // }
        //
        // @Override
        // protected void onPostExecute(String result) {
        // super.onPostExecute(result);
        // if (result != null) {
        // /** 設定會員資料至靜態物件，並回傳登入狀態 **/
        // boolean status = UserData.setUserData(mContext, result);
        //
        // Log.v(TAG, "Status : " + status);
        // if (status) {
        // UserData.saveUserResultPreferences(mContext, result);
        //
        // /** show progress dialog **/
        // psDialog = DialogUtil.showProgressDialog(mContext,
        // getString(R.string.progress_dialog_update));
        //
        // DataSynchronousService mSyn = new DataSynchronousService(mContext);
        // mSyn.UserGetSpotData();
        // mSyn.setHandler(mHandler);
        // } else {
        // AlertDialog.Builder builder = DialogUtil.DialogSingle(mContext,
        // getString(R.string.dialog_title),
        // getString(R.string.login_fail_hint_2));
        // builder.show();
        // }
        // } else {
        // Log.e(TAG, "FacebookLogin");
        // Log.e(TAG, "UsersApiService.FacebookLogin Result == null");
        // }
        // }
        //
        // @Override
        // protected void onProgressUpdate(Integer... values) {
        // super.onProgressUpdate(values);
        // }
        //
        // @Override
        // protected void onPreExecute() {
        // super.onPreExecute();
        // }
        // }
        //
        // public void backBeforeActivityFBLogin() {
        // Intent intent = new Intent();
        // ((Activity) mContext).setResult(MEMBER_FBLOGIN, intent);
        // ((Activity) mContext).finish();
        // }
        //
        // @SuppressLint("HandlerLeak")
        // public Handler mHandler = new Handler() {
        // @Override
        // public void handleMessage(Message msg) {
        // switch (msg.what) {
        // case mUserDataSynchronous:
        // /** 關閉提示資料更新中 **/
        // if (psDialog != null) {
        // psDialog.dismiss();
        // }
        // backBeforeActivityFBLogin();
        // break;
        // }
        // super.handleMessage(msg);
        // }
        // };
}