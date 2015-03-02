package tw.org.by37.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetServiceUtil {

        private final static String TAG = "InternetServiceUtil";

        public static boolean getInternetStatus(Context context) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                // 如果未連線的話，mNetworkInfo會等於null
                if (mNetworkInfo != null) {
                        // 網路是否已連線(true or false)
                        mNetworkInfo.isConnected();
                        // 網路連線方式名稱(WIFI or mobile)
                        mNetworkInfo.getTypeName();
                        // 網路連線狀態(CONNECTED or DISCONNECTED)
                        mNetworkInfo.getState();
                        // 網路是否可使用(true or false)
                        mNetworkInfo.isAvailable();

                        StringBuffer sb = new StringBuffer();
                        sb.append("isConnected : ").append(mNetworkInfo.isConnected()).append("\n");
                        sb.append("TypeName : ").append(mNetworkInfo.getTypeName()).append("\n");
                        sb.append("State : ").append(mNetworkInfo.getState()).append("\n");
                        sb.append("isAvailable : ").append(mNetworkInfo.isAvailable()).append("\n");
                        Log.v(TAG, "NetworkInfo : \n" + sb.toString());

                        return mNetworkInfo.isAvailable();
                }
                return false;
        }
}
