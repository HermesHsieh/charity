package tw.org.by37.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import tw.org.by37.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestPostFragment extends Fragment {

        private final static String TAG = "TestFragment";

        private Context mContext;

        private String url = "http://charity.gopagoda.io/organizationsType";

        private String urlPostUser = "http://charity.gopagoda.io/users";

        private EditText edt_url;
        private Button btn_url;
        private Button btn_connect;
        private TextView tv_result;

        private ProgressDialog psDialog;

        public TestPostFragment() {
                super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_test_post, container, false);

                findView(view);

                getActivity().setTitle(R.string.title_test_post);

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                edt_url = (EditText) view.findViewById(R.id.edt_url);
                btn_url = (Button) view.findViewById(R.id.btn_url);
                btn_connect = (Button) view.findViewById(R.id.btn_connect);
                tv_result = (TextView) view.findViewById(R.id.tv_result);

                checkUrlPreferData();

                btn_connect.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                new getDataAsyncTask().execute(edt_url.getText().toString(), null, null);
                                // new getDataAsyncTask().execute(url, null,
                                // null);
                        }
                });

                btn_url.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                edt_url.setText(urlPostUser);
                        }
                });
        }

        class getDataAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {
                        String result = null;
                        try {
                                result = getWebData(param[0]);
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        // postUsers();
                        Log.i(TAG, "Result : " + result);
                        return getUrlResult(param[0]);
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (psDialog != null) {
                                psDialog.dismiss();
                        }
                        tv_result.setText("The Server Result : \n" + result);
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                        // 儲存EditText的資料
                        saveUrlPreferences(mContext, edt_url.getText().toString());
                        tv_result.setText("The Server Result : null");
                        psDialog = ProgressDialog.show(mContext, "訊息", "請稍候...");
                }
        }

        public String getUrlResult(String url) {
                // 若線上資料為單筆資料，則使用JSONObject
                // JSONObject jsonObj = null;
                String strResult = "";

                // 透過HTTP連線取得回應
                try {
                        // for port 80 requests!
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(url);
                        HttpGet httpget = new HttpGet(url);
                        HttpResponse response = httpclient.execute(httpget);

                        /* 取出回應字串 */
                        strResult = EntityUtils.toString(response.getEntity(), "UTF-8");
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return strResult;
        }

        // 取得網路資料
        public String getWebData(String mUrl) throws IOException {
                URL url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                reader.close();
                return jsonString;
        }

        /* Post User To Server */
        public String postUsers() {
                Log.i(TAG, "postUsers");

                /* 建立HTTP Post連線 */
                HttpPost httpRequest = new HttpPost(urlPostUser);

                /*
                 * Post運作傳送變數必須用NameValuePair[]陣列儲存
                 */
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", "test1"));
                params.add(new BasicNameValuePair("password", "test1"));
                params.add(new BasicNameValuePair("source", "FB"));
                params.add(new BasicNameValuePair("email", "test1@yahoo.com.tw"));
                params.add(new BasicNameValuePair("enable", "true"));

                try {

                        /* 發出HTTP request */
                        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                        /* 取得HTTP response */
                        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                        /* 若狀態碼為200 ok */
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {

                                /* 取出回應字串 */
                                String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

                                Log.i(TAG, "Data : " + strResult);

                                return strResult;
                        } else {
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return "postUsers ConnectFail";
        }

        /** 判斷 Preferences 內是否有儲存的資料,若有即上次使用者有登入過,則設定使用者資料 **/
        private void checkUrlPreferData() {
                Log.i(TAG, "checkUrlPreferData");
                String result = getUrlPreferences(mContext);
                Log.i(TAG, "UrlPreferData Result : " + result);

                if (result != null) {
                        // 有上一次的資料,直接設定在EditText上
                        edt_url.setText(result);
                } else { // 沒有上一次的資料,設定預設的Url
                        edt_url.setText(url);
                }
        }

        private void saveUrlPreferences(Context context, String result) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("UrlPath", result);
                editor.commit();
        }

        private String getUrlPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String result = "";
                try {
                        result = sp.getString("UrlPath", null);
                } catch (Exception e) {
                        result = null;
                        Log.e(TAG, "getUrlPreferences Exception");
                }
                return result;
        }

        private void clearUrlPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("UrlPath");
                editor.commit();
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
