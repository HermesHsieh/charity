package tw.org.by37.util;

import static tw.org.by37.config.SysConfig.*;

import java.io.File;

import tw.org.by37.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class FunctionUtil {

        private final static String TAG = FunctionUtil.class.getName();

        public static void createFileRoot() {
                /** App SD Database 內存路徑 **/
                File mSDFile = new File(SD_DB_PATH);
                Log.v(TAG, SD_DB_PATH + ", SD_DB_PATH Exist : " + mSDFile.exists());
                if (!mSDFile.exists()) {
                        mSDFile.mkdirs();
                        Log.v(TAG, "create ---* : " + SD_DB_PATH);
                }
                /** App Database 內存路徑 **/
                File mFile = new File(DB_PATH);
                Log.v(TAG, DB_PATH + ", DB_PATH Exist : " + mFile.exists());
                if (!mFile.exists()) {
                        mFile.mkdirs();
                        Log.v(TAG, "create ---* : " + DB_PATH);
                }
                /** App照片內存路徑 **/
                File mPicFile = new File(PIC_PATH);
                Log.v(TAG, PIC_PATH + ", PIC_PATH Exist : " + mFile.exists());
                if (!mPicFile.exists()) {
                        mPicFile.mkdirs();
                        Log.v(TAG, "create ---* : " + PIC_PATH);
                }
                /** SD卡Picture路徑 **/
                File mSDPicFile = new File(SD_PIC_PATH);
                Log.v(TAG, SD_PIC_PATH + ", SD_PIC_PATH Exist : " + mSDPicFile.exists());
                if (!mSDPicFile.exists()) {
                        mSDPicFile.mkdirs();
                        Log.v(TAG, "create ---* : " + SD_PIC_PATH);
                }
                /** temp Picture路徑 **/
                File mTempFile = new File(SD_TEMP_PIC_PATH);
                Log.v(TAG, SD_TEMP_PIC_PATH + ", TEMP_PIC_PATH Exist : " + mTempFile.exists());
                if (!mTempFile.exists()) {
                        mTempFile.mkdirs();
                        Log.v(TAG, "create ---* : " + SD_TEMP_PIC_PATH);
                }
        }

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

        /** 將bitmap裁成圓形 **/
        public static Bitmap roundCorners(final Bitmap source) {
                int width = source.getWidth();
                int height = source.getHeight();
                float mRadius = Math.max(width, height) / 2;

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(android.graphics.Color.WHITE);

                Bitmap clipped = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(clipped);
                canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius, paint);

                paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(source, 0, 0, paint);

                source.recycle();

                return clipped;
        }

        /** 將圖片進行縮放,取得該scale **/
        public static float scaleSize(float oldWidth, float oldHeight, float newWidth, float newHeight) {
                float scaleH = oldHeight / newHeight;
                float scaleW = oldWidth / newWidth;
                float scale = Math.max(scaleH, scaleW);
                return scale;
        }

        public static Bitmap PicZoom(Bitmap bmp, float width, float height) {
                int bmpWidth = bmp.getWidth();
                int bmpHeght = bmp.getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);
                return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
        }

        /** 提示Dialog **/
        public static AlertDialog AlertDialogCheck(Context mContext, String title, String message) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                // 設定Dialog的標題
                builder.setTitle(title);
                // 設定Dialog的內容
                builder.setMessage(message);
                // 設定Positive按鈕資料
                builder.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                });

                // 利用Builder物件建立AlertDialog
                return builder.create();
        }

        /** 判別伺服器是否在睡覺 **/
        public static boolean isSleepServer(String result) {
                int checkReg = result.indexOf("<html");
                if (checkReg >= 0) {
                        return true;
                } else {
                        return false;
                }
        }

        /** 開啟鍵盤 **/
        public static void openInputKeyBoard(Context context, View view) {
                if (context != null && view != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
        }

        /** 收起鍵盤 **/
        public static void closeInputKeyBoard(Context context, Activity activity) {
                if (context != null && activity != null) {
                        try {
                                // 收起鍵盤
                                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        } catch (Exception e) {

                        }
                }
        }
}
