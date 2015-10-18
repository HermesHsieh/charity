package tw.org.by37.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {
        private final static String TAG = LocationService.class.getSimpleName();

        public static final String BROADCAST_ACTION = "LocationService";
        private static final int TWO_MINUTES = 1000 * 60 * 2;
        public LocationManager locationManager;
        public MyLocationListener listener;
        public Location previousBestLocation = null;

        /** Location Position 更新的時間限制,單位:毫秒 **/
        private final static int updateTime = 20 * 1000;
        /** Location Position 更新的距離限制,單位:米 **/
        private final static int updateDst = 10;

        Intent intent;
        int counter = 0;

        /** 緯度 **/
        public static Double latitude;
        /** 經度 **/
        public static Double longitude;

        /** 預設值:台北車站 **/
        public static double defult_latitude = 25.046521;
        public static double defult_longitude = 121.517511;

        // 台北車站 25.046385, 121.517521

        /**
         * 確認Service的經緯度是否正常可以使用
         * 
         * @return ture(可以使用), false(不可使用)
         */
        public static boolean checkLocationStatus() {
                if (latitude != null && longitude != null)
                        if (latitude > 0 && longitude > 0)
                                return true;
                return false;
        }

        @Override
        public void onCreate() {
                super.onCreate();
                Log.v(TAG, "onCreate --->");
                intent = new Intent(BROADCAST_ACTION);
        }

        @Override
        public void onStart(Intent intent, int startId) {
                Log.v(TAG, "onStart --->");
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                listener = new MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, updateTime, updateDst, listener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateTime, updateDst, listener);
        }

        @Override
        public IBinder onBind(Intent intent) {
                return null;
        }

        protected boolean isBetterLocation(Location location, Location currentBestLocation) {
                if (currentBestLocation == null) {
                        // A new location is always better than no location
                        return true;
                }

                // Check whether the new location fix is newer or older
                long timeDelta = location.getTime() - currentBestLocation.getTime();
                boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
                boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
                boolean isNewer = timeDelta > 0;

                // If it's been more than two minutes since the current
                // location, use the new location
                // because the user has likely moved
                if (isSignificantlyNewer) {
                        return true;
                        // If the new location is more than two minutes older,
                        // it must be worse
                } else if (isSignificantlyOlder) {
                        return false;
                }

                // Check whether the new location fix is more or less accurate
                int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
                boolean isLessAccurate = accuracyDelta > 0;
                boolean isMoreAccurate = accuracyDelta < 0;
                boolean isSignificantlyLessAccurate = accuracyDelta > 200;

                // Check if the old and new location are from the same provider
                boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

                // Determine location quality using a combination of timeliness
                // and accuracy
                if (isMoreAccurate) {
                        return true;
                } else if (isNewer && !isLessAccurate) {
                        return true;
                } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                        return true;
                }
                return false;
        }

        /** Checks whether two providers are the same */
        private boolean isSameProvider(String provider1, String provider2) {
                if (provider1 == null) {
                        return provider2 == null;
                }
                return provider1.equals(provider2);
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
                // handler.removeCallbacks(sendUpdatesToUI);
                Log.v(TAG, "onDestroy --->");
                locationManager.removeUpdates(listener);
        }

        public static Thread performOnBackgroundThread(final Runnable runnable) {
                final Thread t = new Thread() {
                        @Override
                        public void run() {
                                try {
                                        runnable.run();
                                } finally {

                                }
                        }
                };
                t.start();
                return t;
        }

        public class MyLocationListener implements LocationListener {
                public void onLocationChanged(final Location loc) {
                        Log.i(TAG, "LocationService onLocationChanged");
                        if (isBetterLocation(loc, previousBestLocation)) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                                Log.d(TAG, "get Latitude : " + latitude);
                                Log.d(TAG, "get Longitude : " + longitude);
                                intent.putExtra("Latitude", loc.getLatitude());
                                intent.putExtra("Longitude", loc.getLongitude());
                                intent.putExtra("Provider", loc.getProvider());
                                sendBroadcast(intent);
                        }
                }

                public void onProviderDisabled(String provider) {
                        // Toast.makeText(getApplicationContext(), "Gps
                        // Disabled", Toast.LENGTH_SHORT).show();
                }

                public void onProviderEnabled(String provider) {
                        // Toast.makeText(getApplicationContext(), "Gps
                        // Enabled", Toast.LENGTH_SHORT).show();
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

        }
}
