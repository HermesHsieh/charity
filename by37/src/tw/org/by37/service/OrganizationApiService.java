package tw.org.by37.service;

import static tw.org.by37.config.SysConfig.*;

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

import android.util.Log;

public class OrganizationApiService {

        private final static String TAG = "OrganizationApi";

        private static String uri = organizationsApi;
        
        private static String uri_types = organizationsTypeApi;

        public static String getAllOrganizationData() {
                Log.v(TAG, "getAllOrganizationData");

                // 若線上資料為單筆資料，則使用JSONObject
                // JSONObject jsonObj = null;
                String strResult = null;

                // 透過HTTP連線取得回應
                try {
                        // for port 80 requests!
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(uri);
                        HttpGet httpget = new HttpGet(uri);
                        HttpResponse response = httpclient.execute(httpget);

                        /* 取出回應字串 */
                        strResult = EntityUtils.toString(response.getEntity(), "UTF-8");
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return strResult;
        }
        
        public static String getOrganizationTypes() {
                Log.v(TAG, "getOrganizationTypes");

                // 若線上資料為單筆資料，則使用JSONObject
                // JSONObject jsonObj = null;
                String strResult = null;

                // 透過HTTP連線取得回應
                try {
                        // for port 80 requests!
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(uri_types);
                        HttpGet httpget = new HttpGet(uri_types);
                        HttpResponse response = httpclient.execute(httpget);

                        /* 取出回應字串 */
                        strResult = EntityUtils.toString(response.getEntity(), "UTF-8");
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return strResult;
        }
}
