package tw.org.by37.fragment.organization;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tw.org.by37.R;
import tw.org.by37.data.SelectingData;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

        private final static String TAG = "MapFragment";

        private Context mContext;

        private GoogleMap map;

        /**
         * 使用者的當前位置
         */
        private LatLng userPosition;
        private MarkerOptions mMarkerOption;
        private Marker userMarker;

        /**
         * 基構的相關參數
         */
        private LatLng orgPosition;
        private MarkerOptions orgMarkerOption;
        private Marker orgMarker;

        /** MyLocation Param **/
        public static double wifi_latitude = -1;
        public static double wifi_longitude = -1;
        public static double gps_latitude = -1;
        public static double gps_longitude = -1;

        private LocationManager lms;
        private gpsLocationListener gll;
        private wifiLocationListener wll;
        private boolean getService = false;
        private Location gps_location;
        private Location wifi_location;

        /** Location Position 更新的時間限制,單位:毫秒 **/
        private final static int updateTime = 60000;
        /** Location Position 更新的距離限制,單位:米 **/
        private final static int updateDst = 10;

        /** End of MyLocation Param **/

        public MapFragment() {
                super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_map, container, false);

                initialMyLocationFunction();

                Log.v(TAG, "wifi_latitude : " + wifi_latitude);
                Log.v(TAG, "wifi_longitude : " + wifi_longitude);
                Log.v(TAG, "gps_latitude : " + gps_latitude);
                Log.v(TAG, "gps_longitude : " + gps_longitude);

                // SDK version under 8
                FragmentManager myFragmentManager = getActivity().getSupportFragmentManager();
                SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
                map = mySupportMapFragment.getMap();

                initMap();

                userPosition = new LatLng(wifi_latitude, wifi_longitude);

                // 將 Camera 移動到使用者當前位置
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 16));

                return view;
        }

        /**
         * 初始化地圖設定
         */
        public void initMap() {
                if (map != null) {
                        try {
                                // 設置在地圖上顯示我的位置
                                map.setMyLocationEnabled(true);
                                // 關閉我的位置按鈕
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

        public void setOrganizationMarker() {
                Log.i(TAG, "setOrganizationMarker");
                // 如果有基構的資料,即進入基構簡介
                if (SelectingData.mOrganizationData != null) {
                        orgPosition = new LatLng(SelectingData.mOrganizationData.org_lat, SelectingData.mOrganizationData.org_lng);
                        // new a Marker
                        MarkerOptions orgMarkerOption = new MarkerOptions().position(orgPosition).title(SelectingData.mOrganizationData.org_name).snippet(SelectingData.mOrganizationData.org_title);
                        // Add a marker in the mMap
                        Marker marker = map.addMarker(orgMarkerOption);

                        // 將 Camera 拉到使用者當前位置
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(orgPosition, 16));
                }
        }

        /** Location Manager **/
        public void initialMyLocationFunction() {
                gll = new gpsLocationListener();
                wll = new wifiLocationListener();

                /** 取得系統定位服務 **/
                LocationManager status = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        // 如果GPS和網路定位開啟，呼叫locationServiceInitial()更新位置
                        getService = true; // 確認開啟定位服務
                        locationServiceInitial();
                } else {
                        Log.e(TAG, "LocationProvider Disable");
                        // 開啟設定頁面
                        // Toast.makeText(this, "請開啟定位服務",
                        // Toast.LENGTH_LONG).show();
                        // startActivity(new
                        // Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }

        }

        private void locationServiceInitial() {
                lms = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE); // 嚙踝蕭嚙緻嚙緣嚙諄定嚙踝蕭A嚙踝蕭
                gps_location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                wifi_location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (LocationManager.GPS_PROVIDER != null) {
                        getGPSLocation(gps_location);
                } else {
                        Log.e(TAG, "LocationManager.GPS_PROVIDER == null");
                }
                if (LocationManager.NETWORK_PROVIDER != null) {
                        getWiFiLLocation(wifi_location);
                } else {
                        Log.e(TAG, "LocationManager.NETWORK_PROVIDER == null");
                }
        }

        private void getGPSLocation(Location gps_location) {
                Log.v(TAG, "getGPSLocation");
                if (gps_location != null) {
                        gps_longitude = gps_location.getLongitude();
                        gps_latitude = gps_location.getLatitude();
                } else {
                        Log.e(TAG, "GPSLocation == null");
                }
        }

        private void getWiFiLLocation(Location wifi_location) {
                Log.v(TAG, "getWiFiLLocation");
                if (wifi_location != null) {
                        wifi_longitude = wifi_location.getLongitude();
                        wifi_latitude = wifi_location.getLatitude();
                } else {
                        Log.e(TAG, "WiFiLLocation == null");
                }
        }

        class gpsLocationListener implements LocationListener {
                @Override
                public void onLocationChanged(Location gps_location) { // 當地點改變時
                        // TODO Auto-generated method stub
                        getGPSLocation(gps_location);
                        Log.i(TAG, "GPS Location Change");
                        Log.i(TAG, "gps_latitude : " + gps_latitude);
                        Log.i(TAG, "gps_longitude : " + gps_longitude);
                }

                @Override
                public void onProviderDisabled(String arg0) { // 當GPS定位功能關閉時
                        // TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String arg0) { // 當GPS定位功能開啟時
                        // TODO Auto-generated method stub
                }

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) { // 定位狀態改變
                        // TODO Auto-generated method stub
                }
        }

        class wifiLocationListener implements LocationListener {

                @Override
                public void onLocationChanged(Location wifi_location) { // 當地點改變時
                        // TODO Auto-generated method stub
                        getWiFiLLocation(wifi_location);
                        Log.i(TAG, "Wifi Location Change");
                        Log.i(TAG, "wifi_latitude : " + wifi_latitude);
                        Log.i(TAG, "wifi_longitude : " + wifi_longitude);
                }

                @Override
                public void onProviderDisabled(String arg0) { // 當網路定位功能關閉時
                        // TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String arg0) { // 當網路定位功能開啟
                        // TODO Auto-generated method stub
                }

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) { // 定位狀態改變
                        // TODO Auto-generated method stub
                }
        }

        /** End of Location Manager **/

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
                Log.i(TAG, "onResume");
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
