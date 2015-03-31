package tw.org.by37.service;

import static tw.org.by37.config.SysConfig.usersApi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import tw.org.by37.data.UserData;
import android.util.Log;

public class UsersApiService {

        private final static String TAG = "UsersApi";

        private static String uri = usersApi;

        /* Post User To Server */
        public static String postUsers() {
                Log.i(TAG, "postUsers");

                /* 建立HTTP Post連線 */
                HttpPost httpRequest = new HttpPost(uri);

                /*
                 * Post運作傳送變數必須用NameValuePair[]陣列儲存
                 */
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", UserData.name));
                if (UserData.password.length() > 0) {
                        params.add(new BasicNameValuePair("password", UserData.password));
                } else {
                        params.add(new BasicNameValuePair("password", "test1"));
                }
                params.add(new BasicNameValuePair("source", UserData.login_type));
                params.add(new BasicNameValuePair("email", UserData.email));

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
}
