package tw.org.by37.position;

import static tw.org.by37.config.SysConfig.k_My_Position_Hint;

import java.util.ArrayList;

import tw.org.by37.MainActivity;
import tw.org.by37.R;
import tw.org.by37.MyApplication;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

/**
 * 會員設定位置的Fgragment
 * 
 * @author Tellasy
 * 
 */
public class PositionFragment extends Fragment {

        private final static String TAG = PositionFragment.class.getName();

        private Context mContext;

        private static PositionFragment mPositionFragment;

        private GoogleMap map;

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
        private final static int updateTime = 60 * 1000;
        /** Location Position 更新的距離限制,單位:米 **/
        private final static int updateDst = 10;
        /** End of MyLocation Param **/

        /** 確認我的位置的按鈕 **/
        private Button btn_my_position_submit;
        /** 中間的打卡Marker 圖示 **/
        private ImageView img_position;

        /** 刷新我的位置的按鈕 **/
        private Button btn_my_position_update;

        /** 搜尋用的widget **/
        private EditText edt_search;
        private Button btn_search;
        private ImageView img_clean;

        /** 提示框的Layout及關閉按鈕 **/
        private RelativeLayout rl_hint;
        private ImageView img_hint_close;

        /** 控制顯示確認我的位置的按鈕標記 **/
        public static boolean isVisibleSubmitButton = true;

        /** 搜尋時儲存的參數 **/
        private Double search_lng;
        private Double search_lat;
        private String search_address;
        private Marker search_result_marker;
        /** 暫存搜尋結果的Location ArrayList, 避免輸入一樣的地址重複搜尋 **/
        ArrayList<LocationResult> mLocationList;

        /** 移動地圖時儲存的參數 **/
        private Double move_lng;
        private Double move_lat;

        /** 使用者原先設定的參數 **/
        private Double user_setting_lng;
        private Double user_setting_lat;
        private String user_setting_address;
        private Marker user_setting_marker;

        public static PositionFragment getInstance() {
                return mPositionFragment;
        }

        /** 控制顯示確認我的位置的Button是否顯示 **/
        public void MyPositionSubmitButtonShow(boolean status) {
                if (status) {
                        btn_my_position_submit.setVisibility(View.VISIBLE);
                } else {
                        btn_my_position_submit.setVisibility(View.GONE);
                }
        }

        /**
         * 控制顯示確認我的位置的Button及Marker是否顯示 (給點擊Marker事件用)
         **/
        public void SubmitButtonAndMarkerShow(boolean status) {
                if (status) {
                        img_position.setVisibility(View.VISIBLE);
                        btn_my_position_submit.setVisibility(View.VISIBLE);
                } else {
                        img_position.setVisibility(View.GONE);
                        btn_my_position_submit.setVisibility(View.GONE);
                }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                mPositionFragment = this;

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_position, container, false);

                initialMyLocationFunction();

                initMap();

                findView(view);

                lms.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, updateTime, updateDst, mLocationListener);

                map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location arg0) {
                                // TODO Auto-generated method stub
                                Log.d(TAG, "onMyLocationChange Location : " + arg0);
                        }
                });

                map.setOnCameraChangeListener(new OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition arg0) {
                                // TODO Auto-generated method stub
                                Log.d(TAG, "onCameraChange Position : " + arg0);
                                move_lat = arg0.target.latitude;
                                move_lng = arg0.target.longitude;
                        }
                });

                Log.d(TAG, "getPositionHint : " + getPositionHint());
                // putPositionHint(false);

                // 判別是否第一次進入此頁面, 顯示提示框
                if (!getPositionHint()) {
                        showHintLayout();
                } else {
                        closeHintLayout();
                }

                getUserSettingPosition();

                return view;
        }

        /** 獲取使用者原本設定的位置資訊 **/
        private void getUserSettingPosition() {
                if (MainActivity.mUserApplication.userData != null) {
                        Log.d(TAG, MainActivity.mUserApplication.userData.getUser().getInfo().toString());
                        try {
                                user_setting_lat = Double.valueOf(MainActivity.mUserApplication.userData.getUser().getInfo().getLat());
                                user_setting_lng = Double.valueOf(MainActivity.mUserApplication.userData.getUser().getInfo().getLng());
                                user_setting_address = MainActivity.mUserApplication.userData.getUser().getInfo().getAddress();

                                // Log.d(TAG,
                                // MainActivity.mUserApplication.userData.getUser().getInfo().toString());
                                if (user_setting_lat != null && user_setting_lng != null && user_setting_lat > 0 && user_setting_lng > 0 && user_setting_address != null) {
                                        markerUserSettingPosition();
                                        moveCameraToUserSettingPosition();
                                }
                        } catch (Exception e) {
                                Log.e(TAG, "getUserDefaultPosition Exception");
                                // 沒有使用者之前的資料, Camera移動到使用者當前的位置
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(wifi_latitude, wifi_longitude), 16));
                        }
                } else {
                        // 沒有使用者之前的資料, Camera移動到使用者當前的位置
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(wifi_latitude, wifi_longitude), 16));
                }
        }

        /** 標示(Marker)使用者自己的位置 **/
        private void markerUserSettingPosition() {
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_search);
                LatLng mLatLng = new LatLng(user_setting_lat, user_setting_lng);
                // Add a marker in the map
                user_setting_marker = map.addMarker(new MarkerOptions().position(mLatLng).title(getString(R.string.my_setting_position)).snippet(user_setting_address).icon(bitmapDescriptor));
        }

        /** 將 Camera 移動到使用者設定的位置 **/
        private void moveCameraToUserSettingPosition() {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(user_setting_lat, user_setting_lng), 16));
                // 隱藏設定位置的Marker, Button
                isVisibleSubmitButton = false;
                SubmitButtonAndMarkerShow(isVisibleSubmitButton);
                new Handler().postDelayed(new Runnable() {
                        public void run() {
                                // 提示使用者可以滑動重新設定位置
                                Toast.makeText(mContext, getString(R.string.move_setting_position), Toast.LENGTH_LONG).show();
                        }
                }, 2500);
        }

        /** 標示(Marker)搜尋結果的位置 **/
        private void markerSearchResultPosition() {
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_search);
                LatLng mLatLng = new LatLng(search_lat, search_lng);
                // Add a marker in the map
                search_result_marker = map.addMarker(new MarkerOptions().position(mLatLng).title(edt_search.getText().toString()).snippet(search_address).icon(bitmapDescriptor));
        }

        /** 將 Camera 移動到搜尋結果的位置 **/
        private void moveCameraToSearchResultPosition() {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(search_lat, search_lng), 16));
                // 隱藏設定位置的Marker, Button
                isVisibleSubmitButton = false;
                SubmitButtonAndMarkerShow(isVisibleSubmitButton);
        }

        class MapMarkerClickListener implements OnMarkerClickListener {
                @Override
                public boolean onMarkerClick(Marker marker) {
                        // TODO Auto-generated method stub
                        isVisibleSubmitButton = false;
                        SubmitButtonAndMarkerShow(isVisibleSubmitButton);
                        return false;
                }
        }

        class MarkerInfoWindowClickListener implements OnInfoWindowClickListener {
                @Override
                public void onInfoWindowClick(Marker marker) {
                        if (user_setting_marker != null) {
                                if (marker.getId().equals(user_setting_marker.getId())) {
                                        // 如果點擊到原本使用者預設的Marker, 詢問是否要刪除
                                        cleanUserSettingPositionDialog("", user_setting_address);
                                }
                        }
                        if (search_result_marker != null) {
                                if (marker.getId().equals(search_result_marker.getId())) {
                                        // 如果是搜尋的Marker，則跳出設定完成的Dialog
                                        submitUserPositionDialog();
                                }
                        }
                }
        }

        private void findView(View view) {
                rl_hint = (RelativeLayout) view.findViewById(R.id.rl_hint);
                img_hint_close = (ImageView) view.findViewById(R.id.img_hint_close);
                img_position = (ImageView) view.findViewById(R.id.img_position);
                btn_my_position_submit = (Button) view.findViewById(R.id.btn_my_position_submit);
                btn_my_position_update = (Button) view.findViewById(R.id.btn_my_position_update);
                edt_search = (EditText) view.findViewById(R.id.edt_search);
                btn_search = (Button) view.findViewById(R.id.btn_search);
                img_clean = (ImageView) view.findViewById(R.id.img_clean);

                img_hint_close.setOnClickListener(mButtonClickListener);
                btn_my_position_submit.setOnClickListener(mButtonClickListener);
                btn_my_position_update.setOnClickListener(mButtonClickListener);
                btn_search.setOnClickListener(mButtonClickListener);
                img_clean.setOnClickListener(mButtonClickListener);

                edt_search.addTextChangedListener(textWatcher);

        }

        private void showHintLayout() {
                rl_hint.setVisibility(View.VISIBLE);
                img_position.setVisibility(View.GONE);
                btn_my_position_submit.setVisibility(View.GONE);
        }

        private void closeHintLayout() {
                rl_hint.setVisibility(View.GONE);
                img_position.setVisibility(View.VISIBLE);
                btn_my_position_submit.setVisibility(View.VISIBLE);
        }

        private OnClickListener mButtonClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                        switch (v.getId()) {
                        case R.id.img_hint_close:
                                putPositionHint(true);
                                closeHintLayout();
                                break;
                        case R.id.btn_my_position_submit:
                                new GetAddressByLocationTask().execute();
                                break;
                        case R.id.btn_my_position_update:
                                isVisibleSubmitButton = true;
                                SubmitButtonAndMarkerShow(isVisibleSubmitButton);
                                moveCamera(new LatLng(wifi_latitude, wifi_longitude));
                                break;
                        case R.id.btn_search:
                                isVisibleSubmitButton = false;
                                MyPositionSubmitButtonShow(isVisibleSubmitButton);
                                if (edt_search.getText().toString().length() == 0) {
                                        Toast.makeText(mContext, getString(R.string.address_empty), Toast.LENGTH_LONG).show();
                                } else {
                                        if (edt_search.getText().toString().length() < 3) {
                                                Toast.makeText(mContext, getString(R.string.address_short), Toast.LENGTH_LONG).show();
                                        } else {
                                                new GetPositionByAddressTask().execute();
                                        }
                                }
                                break;
                        case R.id.img_clean:
                                edt_search.setText("");
                                break;
                        default:
                                break;
                        }
                }
        };

        /** 搜尋結果, 地址轉經緯度 **/
        class GetPositionByAddressTask extends AsyncTask<String, Void, ArrayList<LocationResult>> {
                ProgressDialog psDialog;

                @Override
                protected ArrayList<LocationResult> doInBackground(String... param) {
                        if (search_address == null && mLocationList == null) {
                                search_address = edt_search.getText().toString();
                        } else {
                                if (search_address.equals(edt_search.getText().toString()) && mLocationList != null)
                                        return mLocationList;
                        }
                        return LocationUtil.getLocationFromStringList(edt_search.getText().toString());
                }

                @Override
                protected void onPostExecute(ArrayList<LocationResult> result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        psDialog.dismiss();
                        if (result != null) {
                                mLocationList = result;
                                SearchAddressResultDialog(result);
                        } else {
                                Log.e(TAG, "GetPositionByAddressTask Result == null");
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
                        /** 開啟提示Dialog **/
                        psDialog = ProgressDialog.show(mContext, "", getString(R.string.searching));
                }
        }

        /** 經緯度轉地址 **/
        class GetAddressByLocationTask extends AsyncTask<String, Void, String> {
                ProgressDialog psDialog;

                @Override
                protected String doInBackground(String... arg0) {
                        if (search_lat == move_lat && search_lng == move_lng) {
                                if (search_address != null)
                                        return search_address;
                        }
                        search_lat = move_lat;
                        search_lng = move_lng;
                        return LocationUtil.getAddressByLocation(mContext, move_lat, move_lng);
                }

                @Override
                protected void onPostExecute(String result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        psDialog.dismiss();
                        if (result.length() > 0 && result != null) {
                                search_address = result;
                                checkAddressDialog(getString(R.string.search_result), result);
                        } else {
                                noAddressDialog(getString(R.string.search_result), getString(R.string.search_address_error));
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
                        /** 開啟提示Dialog **/
                        psDialog = ProgressDialog.show(mContext, "", getString(R.string.searching));
                }
        }

        /**
         * Log顯示搜尋結果選擇的地址與經緯度
         */
        private void showMySearchResult() {
                Log.i(TAG, "search_address : " + search_address + "search_lat : " + search_lat + ", search_lng : " + search_lng);
        }

        /** 搜尋地址後，以此Dialog顯示結果資料，供使用者選擇地址 **/
        private void SearchAddressResultDialog(ArrayList<LocationResult> data) {
                final Dialog mDialog = new Dialog(mContext);
                // 指定自定義layout
                mDialog.setContentView(R.layout.item_listview);
                // 設定標題
                mDialog.setTitle(getString(R.string.search_result));
                // 是否可點擊Dialog區域外, true可以觸控外面, false不可以觸控外面
                mDialog.setCanceledOnTouchOutside(true);
                // 設定不能取消, true可以按返回鍵取消, false不可以按返回鍵取消
                mDialog.setCancelable(true);
                mDialog.setOnShowListener(new OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                        }
                });
                mDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                });

                ListView mListView = (ListView) mDialog.findViewById(android.R.id.list);
                mListView.setAdapter(getAdapter(data));
                mListView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                                search_lat = Double.valueOf(mLocationList.get(position).lat);
                                search_lng = Double.valueOf(mLocationList.get(position).lng);
                                search_address = mLocationList.get(position).address;

                                showMySearchResult();
                                // 標記搜尋結果的Marker
                                markerSearchResultPosition();
                                // 移動畫面位置
                                moveCameraToSearchResultPosition();
                                // 關閉Dialog
                                mDialog.dismiss();

                                // moveCamera(new LatLng(search_lat,
                                // search_lng));

                                // 結束當前Activity
                                // getActivity().finish();

                        }
                });

                mDialog.show();
        }

        private ArrayAdapter<String> getAdapter(ArrayList<LocationResult> data) {
                // fake data
                String[] address = new String[data.size()];
                for (int i = 0; i < data.size(); i++) {
                        address[i] = data.get(i).address;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, address);
                return adapter;
        }

        /** End of SearchAddressResultDialog **/

        /**
         * 使用者滑動地圖, 經緯度轉地址後, 確認經緯度轉換成地址的結果
         * 
         * @param title
         * @param address
         */
        private void checkAddressDialog(String title, String address) {
                // 產生一個Builder物件
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // 設定Dialog的標題
                builder.setTitle(title);
                // 設定Dialog的內容
                builder.setMessage(address);
                // 設定不能取消, true可以按返回鍵取消, false不可以按返回鍵取消
                builder.setCancelable(false);
                // 設定Positive按鈕資料
                builder.setPositiveButton(mContext.getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                });

                builder.setNegativeButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                // 跳出提示框提醒使用者設定完成
                                submitUserPositionDialog();

                                // saveUserPositionData();
                                // 結束當前Activity
                                // getActivity().finish();
                        }
                });

                // create alert dialog
                AlertDialog alertDialog = builder.create();
                // cancle touch outside, true可以觸控外面, false不可以觸控外面
                alertDialog.setCanceledOnTouchOutside(false);
                // show it
                alertDialog.show();
        }

        /** 當搜尋地址結果長度為零的時候 **/
        private void noAddressDialog(String title, String msg) {
                // 產生一個Builder物件
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // 設定Dialog的標題
                builder.setTitle(title);
                // 設定Dialog的內容
                builder.setMessage(msg);
                // 設定不能取消, true可以按返回鍵取消, false不可以按返回鍵取消
                builder.setCancelable(false);
                // 設定Positive按鈕資料
                builder.setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Positive+");
                        }
                });

                // create alert dialog
                AlertDialog alertDialog = builder.create();
                // cancle touch outside, true可以觸控外面, false不可以觸控外面
                alertDialog.setCanceledOnTouchOutside(false);
                // show it
                alertDialog.show();
        }

        private void cleanUserSettingPositionDialog(String title, String address) {
                // 產生一個Builder物件
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // 設定Dialog的標題
                builder.setTitle(title);
                // 設定Dialog的內容
                builder.setMessage(getString(R.string.delete_my_position_setting) + address);
                // 設定不能取消, true可以按返回鍵取消, false不可以按返回鍵取消
                builder.setCancelable(false);
                // 設定Positive按鈕資料
                builder.setPositiveButton(mContext.getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Positive+");
                        }
                });

                builder.setNegativeButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                Log.d(TAG, "Negative+");

                                map.clear();

                                user_setting_lng = null;
                                user_setting_lat = null;
                                user_setting_address = null;
                                user_setting_marker = null;

                                MainActivity.mUserApplication.userData.getUser().getInfo().setLat(null);
                                MainActivity.mUserApplication.userData.getUser().getInfo().setLng(null);
                                MainActivity.mUserApplication.userData.getUser().getInfo().setAddress("");

                                Gson gson = new Gson();
                                String data = gson.toJson(MainActivity.mUserApplication.userData);
                                MyApplication.getInstance().putUserResult(data);

                                Toast.makeText(mContext, getString(R.string.clean_finish), Toast.LENGTH_LONG).show();
                        }
                });

                // create alert dialog
                AlertDialog alertDialog = builder.create();
                // cancle touch outside, true可以觸控外面, false不可以觸控外面
                alertDialog.setCanceledOnTouchOutside(false);
                // show it
                alertDialog.show();
        }

        /** 提醒使用者設定完成, 儲存使用者設定資料, 結束頁面 **/
        private void submitUserPositionDialog() {
                // 產生一個Builder物件
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // 設定Dialog的內容
                builder.setMessage(getString(R.string.setting_finish));
                // 設定不能取消, true可以按返回鍵取消, false不可以按返回鍵取消
                builder.setCancelable(false);
                // 設定Negative按鈕資料
                builder.setNegativeButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                // 更新使用者的定位設定
                                saveUserPositionData();
                                // 結束
                                getActivity().finish();
                        }
                });

                // create alert dialog
                AlertDialog alertDialog = builder.create();
                // cancle touch outside, true可以觸控外面, false不可以觸控外面
                alertDialog.setCanceledOnTouchOutside(false);
                // show it
                alertDialog.show();
        }

        /** 儲存搜尋結果到手機內存使用者的資料 **/
        private void saveUserPositionData() {
                MainActivity.mUserApplication.userData.getUser().getInfo().setLat(String.valueOf(search_lat));
                MainActivity.mUserApplication.userData.getUser().getInfo().setLng(String.valueOf(search_lng));
                MainActivity.mUserApplication.userData.getUser().getInfo().setAddress(String.valueOf(search_address));

                Gson gson = new Gson();
                String data = gson.toJson(MainActivity.mUserApplication.userData);
                Log.d(TAG, "saveUserPositionData Data : " + data);
                MyApplication.getInstance().putUserResult(data);
        }

        private TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onTextChanged ---> " + s);
                        // 文字框內容正在改變
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "beforeTextChanged ---> " + s);
                        // 文字框內容改變前
                }

                @Override
                public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "afterTextChanged ---> " + s);
                        // 文字框內容改變後 基本上跟正在改變會一樣
                        if (s.length() == 0) {
                                img_clean.setVisibility(View.GONE);
                        } else {
                                img_clean.setVisibility(View.VISIBLE);
                        }
                }
        };

        private void moveCamera(LatLng site) {
                // 將 Camera 移動到指定位置
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(site, 16));
        }

        /** 點擊Map事件監聽 **/
        private class MyMapClickListener implements OnMapClickListener {
                @Override
                public void onMapClick(LatLng position) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onMapClick Position : " + position);

                        // CameraPosition cameraPosition = new
                        // CameraPosition.Builder().target(position).zoom(16).build();
                        // CameraUpdate cameraUpdate =
                        // CameraUpdateFactory.newCameraPosition(cameraPosition);
                        // map.animateCamera(cameraUpdate);
                }
        };

        /** 位置事件監聽 **/
        private LocationListener mLocationListener = new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "StatusChanged provider : " + provider + ", Status : " + status + ", extras : " + extras);

                }

                @Override
                public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "Enabled provider : " + provider);

                }

                @Override
                public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "Disabled provider : " + provider);

                }

                @Override
                public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onLocationChanged Location : " + location);
                }
        };

        /**
         * 初始化地圖設定
         */
        public void initMap() {
                // SDK version under 8
                FragmentManager myFragmentManager = getActivity().getSupportFragmentManager();
                SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
                map = mySupportMapFragment.getMap();
                map.setOnMarkerClickListener(new MapMarkerClickListener());
                map.setOnInfoWindowClickListener(new MarkerInfoWindowClickListener());
                map.setOnMapClickListener(new MyMapClickListener());
                if (map != null) {
                        try {
                                // 設置在地圖上顯示我的位置
                                map.setMyLocationEnabled(false);
                                // 關閉刷新我的位置按鈕
                                map.getUiSettings().setMyLocationButtonEnabled(false);
                                // 設置室內地圖是否開啟
                                map.setIndoorEnabled(true);
                                // 縮放地圖按鈕是否開啟
                                map.getUiSettings().setZoomControlsEnabled(true);
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
                lms = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE); // 取得系統定位服務
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

        /** 儲存提示訊息標記 **/
        public void putPositionHint(boolean status) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(k_My_Position_Hint, status);
                editor.commit();
        }

        /**
         * 獲取提示訊息標記, 判別是否第一次進入此頁面, 顯示提示框
         **/
        public boolean getPositionHint() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                try {
                        return sp.getBoolean(k_My_Position_Hint, false);
                } catch (Exception e) {
                        return false;
                }
        }

        /** End of Preferences **/

        @Override
        public void onResume() {
                super.onResume();
                // 回到頁面時開啟監聽
                if (getService) {
                        lms.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateTime, updateDst, gll);
                        lms.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, updateTime, updateDst, wll);
                }
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
                if (getService) {
                        lms.removeUpdates(gll);// 離開頁面時GPS停止更新
                        lms.removeUpdates(wll);// 離開頁面時WIFI停止更新
                }
        }

        @Override
        public void onDestroy() {
                super.onDestroy();

                mPositionFragment = null;
        }
}
