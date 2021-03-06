package tw.org.by37.organization;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import tw.org.by37.R;
import tw.org.by37.service.LocationService;
import tw.org.by37.util.FunctionUtil;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapFragment extends Fragment {

        private final static String TAG = MapFragment.class.getName();

        private Context mContext;

        private GoogleMap map;

        /**
         * 使用者的當前位置
         */
        private LatLng userPosition;

        /**
         * 基構的相關參數
         */
        private LatLng orgPosition;

        /** Destination Guide Param **/
        private String totaldistance = "0";
        private String totaltime = "0";

        private int zoomLevel = 15;
        private LatLng mid = null;

        /** End of Destination Guide Param **/

        private String org_lat;
        private String org_lng;
        private String org_name;
        private String org_title;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_map, container, false);

                initMap();

                if (LocationService.checkLocationStatus())
                        userPosition = new LatLng(LocationService.latitude, LocationService.longitude);

                // 將 Camera 移動到使用者當前位置
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 16));

                // // Camera移動到使用者當前的位置
                // if (LocationService.checkLocationStatus())
                // map.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                // LatLng(LocationService.latitude, LocationService.longitude),
                // 16));

                return view;
        }

        /**
         * 初始化地圖設定
         */
        public void initMap() {
                // SDK version under 8
                FragmentManager myFragmentManager = getActivity().getSupportFragmentManager();
                SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
                map = mySupportMapFragment.getMap();
                
//                SupportMapFragment mySupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//                map = mySupportMapFragment.getMap();

                if (map != null) {
                        try {
                                // 設置在地圖上顯示我目前的位置
                                map.setMyLocationEnabled(true);
                                // 關閉刷新我的位置按鈕
                                map.getUiSettings().setMyLocationButtonEnabled(false);
                                // 設置室內地圖是否開啟
                                map.setIndoorEnabled(true);
                                // 縮放地圖按鈕是否開啟
                                map.getUiSettings().setZoomControlsEnabled(false);
                                // 設置地圖類型三種：1：MAP_TYPE_NORMAL:
                                // Basic map with roads.
                                // 2：MAP_TYPE_SATELLITE:
                                // Satellite view with roads.
                                // 3:MAP_TYPE_TERRAIN:
                                // Terrain view without roads.
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                // map.setTrafficEnabled(true);
                        } catch (Exception e) {
                                Log.v(TAG, "initMap Exception");
                        }
                }
        }

        /** 將機構需要的資料設定至MapFragment **/
        public void setOrganizationData(String org_lat, String org_lng, String org_name, String org_title) {
                Double lat = Double.valueOf(org_lat);
                Double lng = Double.valueOf(org_lng);
                if (lat != null && lng != null) {
                        if (lat > 0 && lng > 0) {
                                this.org_lat = org_lat;
                                this.org_lng = org_lng;
                                this.org_name = org_name;
                                this.org_title = org_title;
                        }
                }
        }

        public void setOrganizationMarker() {
                Log.i(TAG, "setOrganizationMarker");
                // 如果有基構的資料,即進入基構簡介

                Double lat = Double.valueOf(org_lat);
                Double lng = Double.valueOf(org_lng);

                if (lat != null && lng != null) {
                        if (lat > 0 && lng > 0) {
                                orgPosition = new LatLng(lat, lng);
                                // new a Marker
                                MarkerOptions orgMarkerOption = new MarkerOptions().position(orgPosition).title(org_name).snippet(org_title);
                                // Add a marker in the mMap
                                map.addMarker(orgMarkerOption);
                                // 將 Camera 拉到使用者當前位置
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(orgPosition, 16));
                        } else {
                                Log.e(TAG, "Organization Location error");
                        }
                } else {
                        Log.e(TAG, "Organization Location == null");
                }
        }

        /** 取得導航訊息 **/
        public void getDestinationGuide() {
                Log.i(TAG, "取得導航訊息 getDestinationGuide");
                // 路線規劃的起始LatLng
                LatLng start = userPosition;
                // 路線規劃的終點LatLng (機構地點)
                LatLng end = orgPosition;

                if (start != null && end != null) {
                        // 計算地圖zoomLevel
                        zoomLevel = countZoomLevel(start, end);
                        Log.i(TAG, "zoomLevel : " + zoomLevel);
                        // 計算起始點與終點之終點座標
                        mid = countmidPosition(start, end);

                        String url = getDirectionsUrl(start, end);
                        Log.d(TAG, "Url : " + url);
                        new mapRouteDataFromUserAsyncTask().execute(url, null, null);
                } else {
                        Log.e(TAG, "userPosition : " + userPosition);
                        Log.e(TAG, "orgPosition : " + orgPosition);
                }

        }

        private class mapRouteDataFromUserAsyncTask extends AsyncTask<String, Void, List<LatLng>> {

                HttpClient client;
                String url;

                List<LatLng> routes = null;

                @Override
                protected List<LatLng> doInBackground(String... param) {
                        Log.i(TAG, "doInBackground");

                        url = param[0];

                        HttpGet get = new HttpGet(url);

                        try {
                                HttpResponse response = client.execute(get);
                                int statusecode = response.getStatusLine().getStatusCode();
                                System.out.println("response:" + response + "statuscode:" + statusecode);
                                if (statusecode == 200) {

                                        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                                        // Log.i(TAG,
                                        // "responseString : " +
                                        // responseString);
                                        int status = responseString.indexOf("<status>OK</status>");
                                        System.out.println("status:" + status);
                                        if (-1 != status) {
                                                int pos = responseString.indexOf("<overview_polyline>");
                                                pos = responseString.indexOf("<points>", pos + 1);
                                                int pos2 = responseString.indexOf("</points>", pos);

                                                // 找到第一個value值
                                                int pos3 = responseString.indexOf("<value>");
                                                // Log.i(TAG,
                                                // "INT POS3 1: " +
                                                // pos3);
                                                // 路徑總長
                                                int position = 0;
                                                // 路徑時間
                                                int position2 = 0;
                                                if (pos3 > 0) {
                                                        // 找到最後一個value值，總路徑
                                                        while (true) {
                                                                // position=1st<value>
                                                                position = pos3;
                                                                // 找下一個<value>值,從position+1的位置開始找
                                                                pos3 = responseString.indexOf("<value>", pos3 + 1);
                                                                // Log.i(TAG,
                                                                // "INT POS3 2 :
                                                                // "
                                                                // +
                                                                // pos3);
                                                                if (pos3 < 0) {
                                                                        break;
                                                                } else {
                                                                        position2 = position;
                                                                }
                                                        }
                                                }
                                                Log.i(TAG, "INT position: " + position);
                                                // 擷取路徑總長度
                                                int pos4 = responseString.indexOf("</value>", position);
                                                totaldistance = responseString.substring(position + 7, pos4);
                                                Log.i(TAG, "totalroute Distance : " + totaldistance);

                                                Log.i(TAG, "INT position2: " + position2);
                                                // 擷取總估計時間
                                                int pos5 = responseString.indexOf("</value>", position2);
                                                totaltime = responseString.substring(position2 + 7, pos5);
                                                Log.i(TAG, "totaltime Secs : " + totaltime);

                                                responseString = responseString.substring(pos + 8, pos2);
                                                routes = decodePoly(responseString);

                                        } else {
                                                // 錯誤代碼，
                                                return null;
                                        }

                                } else {
                                        // 請求失敗
                                        return null;
                                }

                        } catch (ClientProtocolException e) {
                                e.printStackTrace();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        System.out.println("doInBackground:" + routes);
                        return routes;
                }

                @Override
                protected void onPostExecute(List<LatLng> routes) {
                        super.onPostExecute(routes);
                        if (routes == null) {
                                // 導航失敗
                                Toast.makeText(mContext, "沒有搜索到線路", Toast.LENGTH_LONG).show();
                        } else {
                                map.clear();

                                // 地圖描點
                                PolylineOptions lineOptions = new PolylineOptions();
                                lineOptions.addAll(routes);
                                lineOptions.width(10);
                                lineOptions.color(Color.BLUE);
                                map.addPolyline(lineOptions);
                                // 定位到第0點經緯度
                                // map.animateCamera(CameraUpdateFactory.newLatLng(routes.get(0)));

                                // map.addMarker(new
                                // MarkerOptions().position(userPosition).title("我的位置"));
                                if (orgPosition != null && org_name != null) {
                                        map.addMarker(new MarkerOptions().position(orgPosition).title(org_name).snippet(getSubtitleForDisTime(totaldistance, totaltime)));
                                }
                                Log.i(TAG, "moveCamera mid : " + mid + ", ZoomLevel : " + zoomLevel);
                                // 定位到兩點距離之中點，縮放至合適的ZoomLevle
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mid, zoomLevel));

                        }
                }

                @Override
                protected void onPreExecute() {
                        client = new DefaultHttpClient();
                        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
                        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
                        super.onPreExecute();
                }
        }

        /**
         * 組合成googlemap direction所需要的url
         * 
         * @param origin
         * @param dest
         * @return url
         */
        private String getDirectionsUrl(LatLng origin, LatLng dest) {
                // Origin of route
                String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

                // Destination of route
                String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

                // Sensor enabled
                String sensor = "sensor=false";

                // Travelling Mode
                String mode = "mode=driving";

                // String waypointLatLng =
                // "waypoints="+"40.036675"+","+"116.32885";

                // 如果使用途徑點，需要添加此欄位
                // String waypoints = "waypoints=";

                String parameters = null;
                // Building the parameters to the web service

                parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
                // parameters = str_origin + "&" + str_dest + "&" + sensor +
                // "&"
                // + mode+"&"+waypoints;

                // Output format
                // String output = "json";
                String output = "xml";

                // Building the url to the web service
                String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                System.out.println("getDerectionsURL--->: " + url);
                return url;
        }

        /**
         * 計算兩點之間距離，回傳合適之zoomLevel值
         * 
         * @param point1
         *                點1之經緯度座標值
         * @param point2
         *                點2之經緯度座標值
         * @return int
         */
        public int countZoomLevel(LatLng point1, LatLng point2) {
                /**
                 * 14, 12km. 15, 6km. 16, 3km. 17, 1.5km
                 */

                int zoomLevel = 0;

                double lat1 = point1.latitude;
                double lon1 = point1.longitude;
                double lat2 = point2.latitude;
                double lon2 = point2.longitude;

                int dis = (int) FunctionUtil.Distance(lon1, lat1, lon2, lat2);
                Log.i(TAG, "countZoomLevel Distance : " + dis);
                int scale = dis / 1500;
                Log.i(TAG, "countZoomLevel Scale : " + scale);
                switch (scale) {
                case 0:
                        zoomLevel = 15;
                        break;
                case 1:
                        zoomLevel = 14;
                        break;
                case 2:
                        zoomLevel = 14;
                        break;
                case 3:
                        zoomLevel = 13;
                        break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                        zoomLevel = 12;
                        break;
                default:
                        zoomLevel = 11;
                        break;
                }

                return zoomLevel;
        }

        /**
         * 計算兩點距離之中點經緯度座標
         * 
         * @param point1
         *                點1經緯度座標
         * @param point2
         *                點2經緯度座標
         * @return LatLng 物件
         */
        public LatLng countmidPosition(LatLng point1, LatLng point2) {

                double lat1 = point1.latitude;
                double lon1 = point1.longitude;
                double lat2 = point2.latitude;
                double lon2 = point2.longitude;

                LatLng myLagLng = new LatLng((lat1 + lat2) / 2, (lon1 + lon2) / 2);

                return myLagLng;
        }

        /**
         * 解析返回xml中overview_polyline的路線編碼
         * 
         * @param encoded
         * @return List<LatLng>
         */
        private List<LatLng> decodePoly(String encoded) {
                List<LatLng> poly = new ArrayList<LatLng>();
                int index = 0, len = encoded.length();
                int lat = 0, lng = 0;
                while (index < len) {
                        int b, shift = 0, result = 0;
                        do {
                                b = encoded.charAt(index++) - 63;
                                result |= (b & 0x1f) << shift;
                                shift += 5;
                        } while (b >= 0x20);
                        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                        lat += dlat;
                        shift = 0;
                        result = 0;
                        do {
                                b = encoded.charAt(index++) - 63;
                                result |= (b & 0x1f) << shift;
                                shift += 5;
                        } while (b >= 0x20);
                        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                        lng += dlng;
                        LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
                        poly.add(p);
                }
                return poly;
        }

        private String getSubtitleForDisTime(String dis, String time) {
                double mDst = Double.valueOf(dis);
                double mTime = Double.valueOf(time);
                String disUnit = "";
                String timeUnit = "";
                if (mDst > 100) {
                        mDst = mDst / 1000;
                        disUnit = " 公里";
                } else {
                        disUnit = " 公尺";
                }
                if (mTime > 60) {
                        mTime = mTime / 60;
                        timeUnit = " 分鐘";
                } else {
                        timeUnit = " 秒";
                }
                NumberFormat formatter = new DecimalFormat("#.##");
                String sD = formatter.format(mDst); // -1234.6
                String sT = formatter.format(mTime); // -1234.6
                return "距離 : " + sD + disUnit + ", 時間 : " + sT + timeUnit;
        }

        /** Preferences **/
        /** End of Preferences **/

        /** GotoActivity **/

        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        /** End of Bottom Slide Menu **/

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
