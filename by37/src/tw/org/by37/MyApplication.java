package tw.org.by37;

import static tw.org.by37.config.SysConfig.PIC_PATH;
import static tw.org.by37.config.SysConfig.k_UserData_1;
import static tw.org.by37.config.SysConfig.k_UserData_2;
import static tw.org.by37.config.SysConfig.k_UserData_3;
import static tw.org.by37.config.SysConfig.k_UserData_4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tw.org.by37.data.RegisterData;
import tw.org.by37.member.UserData;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

        private final static String TAG = MyApplication.class.getSimpleName();

        public static MyApplication userApplication;

        public UserData userData;

        private String user_account;
        private String user_password;
        private String user_source;

        public String tmp_avatar_path = PIC_PATH + "image.jpg";

        public static MyApplication getInstance() {
                return userApplication;
        }

        @Override
        public void onCreate() {
                super.onCreate();
                Log.i(TAG, "onCreate --->");
                userApplication = this;

                new Thread(new Runnable() {
                        public void run() {
                                updateUserResult();
                        }
                }).start();
                
                //init UIL
                initImageLoader(getApplicationContext());
        }

        /** 獲取使用者帳號資料 UserData **/
        public UserData getUserData() {
                return userData;
        }

        /** 更新使用者資料 UserData **/
        public void updateUserResult() {
                Log.i(TAG, "updateUserResult");
                setUserData(getUserResult());
        }

        /** 設置使用者資料於 UserData 物件 **/
        private void setUserData(String result) {
                Log.i(TAG, "setUserData");
                if (result != null) {
                        // 有內存之前的資料
                        Gson gson = new Gson();
                        userData = gson.fromJson(result, UserData.class);
                        if (userData.getStatus().equals("success")) {
                                Log.d(TAG, "User Login Status : success");
                                /** 登入成功,改變使用者登入狀態 **/
                                userData.setUserStatus(true);
                                /** 更新使用者大頭貼 **/
                                updateUser_Image();
                        }
                } else {
                        // 沒有內存之前的資料
                        Log.d(TAG, "沒有內存使用者資料");
                }
                
                
        }

        /** 設定使用者帳號 **/
        public void setUser_Account(String user_account) {
                this.user_account = user_account;
        }

        /** 取得使用者帳號 **/
        public String getUser_Account() {
                return user_account;
        }

        /** 設定使用者密碼 **/
        public void setUser_Password(String user_pwd) {
                this.user_password = user_pwd;
        }

        /** 取得使用者密碼 **/
        public String getUser_Password() {
                return user_password;
        }

        /** 設定使用者來源 **/
        public void setUser_Source(String user_source) {
                this.user_source = user_source;
        }

        /** 取得使用者來源 **/
        public String getUser_Source() {
                return user_source;
        }

        /** 更新使用者大頭照 **/
        public void updateUser_Image() {
                Log.i(TAG, "updateUser_Image");
                if (userData != null) {
                        String uri = userData.getUser().getInfo().getImage();
                        if (uri != null && uri.length() > 0) {
                                new AsyncTask<String, Void, Bitmap>() {
                                        @Override
                                        protected Bitmap doInBackground(String... params) {
                                                return getBitmapFromURL(params[0]);
                                        }

                                        @Override
                                        protected void onPostExecute(Bitmap bitmap) {
                                                super.onPostExecute(bitmap);
                                                Log.d(TAG, "download avatar finish.");
                                                Log.d(TAG, "From : " + userData.getUser().getInfo().getImage());
                                                Log.d(TAG, "To : " + tmp_avatar_path);
                                                if (bitmap != null) {
                                                        saveImage(bitmap);
                                                }
                                        }

                                        @Override
                                        protected void onPreExecute() {
                                                super.onPreExecute();
                                        }
                                }.execute(uri);
                        }
                }
        }

        private void saveImage(Bitmap bitmap) {
                Log.i(TAG, "saveImage");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                try {
                        File f = new File(tmp_avatar_path);
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        fo.close();
                } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, e.toString());
                } finally {
                        Log.i(TAG, "User Avatar Save Done");
                }
        }

        // 讀取網路圖片，型態為Bitmap
        private static Bitmap getBitmapFromURL(String imageUrl) {
                Log.i(TAG, "getBitmapFromURL");
                try {
                        URL url = new URL(imageUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        BitmapFactory.Options opt = new BitmapFactory.Options();
                        opt.inPreferredConfig = Bitmap.Config.RGB_565;
                        opt.inPurgeable = true;
                        opt.inInputShareable = true;

                        InputStream input = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(input, null, opt);
                        return bitmap;
                } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                }
        }

        /** 記住我:獲取上次填寫之使用者帳號 **/
        public String getRememberMe() {
                getUserLogin();
                return user_account;
        }

        /** 使用者登出 **/
        public void LogoutUser() {
                userData = null;
                user_account = null;
                user_password = null;
                user_source = null;
                removeUserResult();
        }

        /** 從手機內存獲取使用者結果資料 **/
        public String getUserResult() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                String result = null;
                try {
                        result = sp.getString(k_UserData_1, null);
                } catch (Exception e) {
                        Log.e(TAG, "SharedPreferences UserData Exception");
                }
                Log.d(TAG, "getUserResult : " + result);
                return result;
        }

        /** 儲存使用者結果資料於手機內存, 清除註冊類別資料(RegisterData clean) **/
        public void putUserResult(String result) {
                RegisterData.clean();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(k_UserData_1, result);
                editor.commit();
        }

        /** 清除儲存於手機內存的使用者結果資料 **/
        public void removeUserResult() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_UserData_1);
                editor.commit();
        }

        /** 獲取使用者資料於手機內存 (帳號,密碼,來源),若沒有則回傳false **/
        public boolean getUserLogin() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                try {
                        user_account = sp.getString(k_UserData_2, null);
                        user_password = sp.getString(k_UserData_3, null);
                        user_source = sp.getString(k_UserData_4, null);
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        /** 儲存使用者資料於手機內存 (帳號,密碼,來源) **/
        public void putUserLogin() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(k_UserData_2, user_account);
                editor.putString(k_UserData_3, user_password);
                editor.putString(k_UserData_4, user_source);
                editor.commit();
        }

        /** 清除儲存於手機內存使用者資料 (帳號,密碼,來源) **/
        public void removeUserLogin() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_UserData_2);
                editor.remove(k_UserData_3);
                editor.remove(k_UserData_4);
                editor.commit();
        }

        public void setRegisterData() {
                user_account = RegisterData.email;
                user_password = RegisterData.password;
                user_source = RegisterData.source;
        }

        @Override
        public void onLowMemory() {
                super.onLowMemory();
        }

        @Override
        public void onTerminate() {
                super.onTerminate();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
        }
        

        public static void initImageLoader(Context context) {
            // This configuration tuning is custom. You can tune every option, you may tune some of them,
            // or you can create default configuration by
            //  ImageLoaderConfiguration.createDefault(this);
            // method.
            ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
            config.threadPriority(Thread.NORM_PRIORITY - 2);
            config.denyCacheImageMultipleSizesInMemory();
            config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
            config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
            config.tasksProcessingOrder(QueueProcessingType.LIFO);
            config.writeDebugLogs(); // Remove for release app

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config.build());
        }

}