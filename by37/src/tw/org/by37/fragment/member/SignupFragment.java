package tw.org.by37.fragment.member;

import tw.org.by37.R;
import tw.org.by37.data.UserData;
import tw.org.by37.service.UsersApiService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupFragment extends Fragment {

        private final static String TAG = "SignupFragment";

        private Context mContext;

        private Button btn_signup;

        private EditText edt_account, edt_password, edt_name;

        private TextView tv_info;

        private ProgressDialog psDialog;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_signup, container, false);

                findView(view);

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                btn_signup = (Button) view.findViewById(R.id.btn_signup);
                btn_signup.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                /** set User Data **/
                                UserData.name = edt_name.getText().toString();
                                UserData.email = edt_account.getText().toString();
                                UserData.password = edt_password.getText().toString();
                                UserData.login_type = "by37";
                                new getDataAsyncTask().execute(null, null, null);
                        }
                });

                edt_account = (EditText) view.findViewById(R.id.edt_account);
                edt_password = (EditText) view.findViewById(R.id.edt_password);
                edt_name = (EditText) view.findViewById(R.id.edt_name);
                tv_info = (TextView) view.findViewById(R.id.tv_info);

        }

        class getDataAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = UsersApiService.postUsers();

                        Log.i(TAG, "Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        int checkReg = result.indexOf("<html");
                        String mReg = "";
                        if (checkReg >= 0) {
                                mReg = "Register Info : server has been sleep." + "\n\n";
                        } else {
                                mReg = "Register Info : " + result + "\n\n";
                        }
                        tv_info.setText(mReg);
                        
                        psDialog.dismiss();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                        psDialog = ProgressDialog.show(mContext, "訊息", "註冊中，請稍候...");
                }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);
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
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
        }
}
