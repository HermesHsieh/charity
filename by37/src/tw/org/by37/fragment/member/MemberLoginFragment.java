package tw.org.by37.fragment.member;

import java.util.Arrays;

import tw.org.by37.R;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MemberLoginFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {

        private final static String TAG = "MemberLoginFragment";

        private Context mContext;

        private LoginButton btn_fb_login;
        private TextView tv_info;
        private UiLifecycleHelper uiHelper;

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

        private boolean userGoogleStatus = false;

        /** End of Google+ **/

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

                initGoogleSignInButton(view);

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {

                tv_info = (TextView) view.findViewById(R.id.textView1);

                btn_fb_login = (LoginButton) view.findViewById(R.id.btn_facebook);
                btn_fb_login.setReadPermissions(Arrays.asList("email"));
                btn_fb_login.setUserInfoChangedCallback(new UserInfoChangedCallback() {
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

                if (requestCode == RC_SIGN_IN) {
                        if (resultCode != getActivity().RESULT_OK) {
                                mSignInClicked = false;
                        }

                        mIntentInProgress = false;

                        if (!mGoogleApiClient.isConnecting()) {
                                mGoogleApiClient.connect();
                        }
                }
        }

        /** Google+ **/
        private void initGoogleSignInButton(View view) {
                btnSignIn = (SignInButton) view.findViewById(R.id.btn_google);
                // Button click listeners
                btnSignIn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // Signin button clicked
                                signInWithGplus();
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

                if (userGoogleStatus) {
                        // 如果是登入狀態，則登出
                        signOutFromGplus();
                }

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

                                // FunctionUtil.showToastMsg(mContext,
                                // "Google+ Login Success");
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
