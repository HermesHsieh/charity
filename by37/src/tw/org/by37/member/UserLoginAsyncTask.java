package tw.org.by37.member;

import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.MainActivity;
import tw.org.by37.service.UsersApiService;
import tw.org.by37.util.FunctionUtil;
import android.os.AsyncTask;
import android.util.Log;

public class UserLoginAsyncTask extends AsyncTask<Void, Void, String> {

        private final static String TAG = "UserLoginAsyncTask";

        @Override
        protected String doInBackground(Void... param) {
                Log.d(TAG, "doInBackground");

                String user = MainActivity.mUserApplication.getUser_Account();
                String pwd = MainActivity.mUserApplication.getUser_Password();
                String src = MainActivity.mUserApplication.getUser_Source();

                String result = UsersApiService.LoginUser(user, pwd, src);
                Log.d(TAG, "Result : " + result);

                return result;
        }

        @Override
        protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d(TAG, "onPostExecute");
                String status = null;
                if (result != null) {
                        /** 判別伺服器是否正常work狀態 **/
                        if (!FunctionUtil.isSleepServer(result)) {
                                // 伺服器正常運作
                                try {
                                        JSONObject mJObj = new JSONObject(result);
                                        status = mJObj.getString("status");

                                        if (status.equals("success")) {
                                                Log.d(TAG, "Login Result Success");
                                                /** 儲存使用者資料於手機內存 **/
                                                MainActivity.mUserApplication.putUserLogin();
                                                /** 儲存使用者結果資料於手機內存 **/
                                                MainActivity.mUserApplication.putUserResult(result);
                                                /** 刷新使用者資料 **/
                                                MainActivity.mUserApplication.updateUserResult();
                                                /** 刷新使用者大頭照資料 **/
                                                MainActivity.mUserApplication.updateUser_Image();
                                                /** 登入成功,finish **/
                                                // LoginSuccess();
                                        } else {
                                                if (status.equals("fail")) {
                                                        Log.d(TAG, "Login Result Fail");
                                                        Log.d(TAG, "Message : " + mJObj.getString("message"));
                                                }
                                        }
                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        } else {
                                Log.d(TAG, "Server is sleep new.");
                                // 無法連接伺服器,用手機內存資料登入
                                MainActivity.mUserApplication.updateUserResult();
                        }
                } else {
                        Log.e(TAG, "Result == null");
                }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
        }

        /** 執行Async Task前 **/
        @Override
        protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "onPreExecute");
        }
}
