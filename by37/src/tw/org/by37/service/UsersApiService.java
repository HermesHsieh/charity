package tw.org.by37.service;

import static tw.org.by37.config.SysConfig.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import tw.org.by37.data.RegisterData;
import tw.org.by37.productsell.NewProductActivity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class UsersApiService {

        private final static String TAG = "UsersApi";

        /** User Register (addUser) **/
        private static String uri = usersApi;

        /** User Login (checkUser) **/
        private static String uriLogin = usersLoginApi;

        public static String RegisterUser() {
                Log.i(TAG, "RegisterUser");

                String result = null;
                HttpClient client = new DefaultHttpClient();

                HttpPost post = new HttpPost(uri);

                /*
                 * Post運作傳送變數必須用NameValuePair[]陣列儲存
                 */
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", RegisterData.email));
                params.add(new BasicNameValuePair("password", RegisterData.password));
                params.add(new BasicNameValuePair("source", RegisterData.source));
                params.add(new BasicNameValuePair("name", RegisterData.name));
                if (RegisterData.image != null) {
                        params.add(new BasicNameValuePair("image", RegisterData.image));
                }
                Log.d(TAG, RegisterData.showData());

                try {
                        // setup multipart entity
                        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                        for (int i = 0; i < params.size(); i++) {
                                // identify param type by Key
                                if (params.get(i).getName().equals("image")) {
                                        entity.addPart(params.get(i).getName(), new FileBody(new File(params.get(i).getValue())));
                                } else {
                                        entity.addPart(params.get(i).getName(), new StringBody(params.get(i).getValue(), Charset.forName("UTF-8")));
                                }
                        }

                        post.setEntity(entity);

                        try {
                                // execute and get response
                                result = new String(client.execute(post, new BasicResponseHandler()).getBytes(), HTTP.UTF_8);

                                Log.i(TAG, "RegisterUser Result : " + result);
                        } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "RegisterUser Result Exception!");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "RegisterUser Exception");
                }

                return result;
        }

        public static String LoginUser(String account, String pwd, String source) {
                Log.i(TAG, "LoginUser");
                Log.d(TAG, "account : " + account);
                Log.d(TAG, "password : " + pwd);
                Log.d(TAG, "source : " + source);

                String result = null;

                /* 建立HTTP Post連線 */
                HttpPost httpRequest = new HttpPost(uriLogin);

                /*
                 * Post運作傳送變數必須用NameValuePair[]陣列儲存
                 */
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", account));
                params.add(new BasicNameValuePair("password", pwd));
                params.add(new BasicNameValuePair("source", source));

                try {

                        /* 發出HTTP request */
                        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                        /* 取得HTTP response */
                        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                        /* 若狀態碼為200 ok */
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                /* 取出回應字串 */
                                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                                Log.i(TAG, "LoginUser Reslut : " + result);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "HttpRequest Exception");
                }
                return result;
        }

}
