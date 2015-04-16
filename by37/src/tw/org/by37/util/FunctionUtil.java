package tw.org.by37.util;

import android.content.Context;
import android.widget.Toast;

public class FunctionUtil {

        public static void showToastMsg(Context context, String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }

        /** 計算兩點經緯度距離公式,回傳單位:公尺 **/
        public static double Distance(double longitude1, double latitude1, double longitude2, double latitude2) {
                double radLatitude1 = latitude1 * Math.PI / 180;
                double radLatitude2 = latitude2 * Math.PI / 180;
                double l = radLatitude1 - radLatitude2;
                double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
                double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2) + Math.cos(radLatitude1) * Math.cos(radLatitude2) * Math.pow(Math.sin(p / 2), 2)));
                distance = distance * 6378137.0;
                distance = Math.round(distance * 10000) / 10000;
                return distance;
        }

        /** 判別伺服器是否正常運作,true = work, false = sleep **/
        public static boolean ServiceStatus(String string) {
                boolean status = true;
                int checkString = string.indexOf("<html");
                if (checkString >= 0) {
                        status = false;
                }
                return status;
        }
}
