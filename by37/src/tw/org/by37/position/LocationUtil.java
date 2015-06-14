package tw.org.by37.position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

/**
 * 經緯度轉換成地址
 * 
 * @author Tellasy
 * 
 */
public class LocationUtil {

        private final static String TAG = LocationUtil.class.getName();

        public static String getAddressByLocation(Context context, Double latitude, Double longitude) {
                Log.i(TAG, "getAddressByLocation");
                String returnAddress = "";
                try {
                        Geocoder gc = new Geocoder(context, Locale.TRADITIONAL_CHINESE); // 地區:台灣

                        // 自經緯度取得地址,資料比數
                        List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);

                        if (!Geocoder.isPresent()) {
                                returnAddress = "Sorry! Geocoder service not Present.";
                        }
                        returnAddress = lstAddress.get(0).getAddressLine(0); // 取得第一筆完整的地址

                        for (int i = 0; i < lstAddress.size(); i++) {
                                Log.v(TAG, "List Address : " + lstAddress.get(i).getAddressLine(0));
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "getAddressByLocation Exception");
                }
                return returnAddress;
        }

        public static ArrayList<LocationResult> getLocationFromStringList(String address) {

                BufferedReader sr = null;

                try {
                        URL url = new URL("http://maps.google.com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF-8") + "&language=zh-TW&sensor=true");
                        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                        httpConn.setDoInput(true);
                        sr = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                        String line;
                        String result = "";
                        while ((line = sr.readLine()) != null) {
                                result += line;
                        }
                        sr.close();

                        JSONObject jsonObject = new JSONObject();
                        try {
                                jsonObject = new JSONObject(result);

                                String status = jsonObject.getString("status");

                                if (status.equals("OK")) {

                                        int count = ((JSONArray) jsonObject.get("results")).length();

                                        ArrayList<LocationResult> mList = new ArrayList<LocationResult>();

                                        for (int i = 0; i < count; i++) {
                                                LocationResult item = new LocationResult();
                                                item.address = ((JSONArray) jsonObject.get("results")).getJSONObject(i).getString("formatted_address");
                                                item.lng = String.valueOf(((JSONArray) jsonObject.get("results")).getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                                                item.lat = String.valueOf(((JSONArray) jsonObject.get("results")).getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                                mList.add(item);
                                                Log.i(TAG, i + ", Address : " + item.address);
                                                Log.i(TAG, i + ", Lat : " + item.lat);
                                                Log.i(TAG, i + ", Lng : " + item.lng);
                                        }

                                        return mList;
                                } else {
                                        Log.e(TAG, "status != OK");
                                        Log.e(TAG, "Perhaps address error!");
                                }
                        } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } finally {
                        if (sr != null) {
                                try {
                                        sr.close();
                                } catch (IOException e) {
                                        // TODO Auto-generated catch
                                        // block
                                        e.printStackTrace();
                                }
                        }
                }
                return null;
        }
}
