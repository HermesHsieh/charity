package tw.org.by37.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import static tw.org.by37.config.SysConfig.*;

/** 全域靜態使用者帳號資料的類別 **/
public class UserData2 {

        private final static String TAG = "UserData";

        /** 使用者登入狀態 **/
        public static boolean userStatus = false;
        /** 使用者編號 **/
        public static String user_id = "";
        /** 帳號(電子郵件地址) **/
        public static String email = "";
        /** by37=一般註冊, facebook=Facebook, google=Google+ **/
        public static String source = "";
        /** 使用者密碼 **/
        public static String password = "";
        /** 社群平台ID,僅社群登入使用 **/
        public static String social_id = "";
        /** 建立時間(秒數) **/
        public static String create_time = "";

        /** 大頭貼路徑 **/
        public static String image = "";
        /** 性別, 1=男, 2=女 **/
        public static String sex = "";
        /** 生日：yyyy-mm-dd **/
        public static String birthday = "";
        /** 暱稱 **/
        public static String nickname = "";
        /** 姓名 **/
        public static String name = "";
        /** 聯絡電話 **/
        public static String phone = "";
        /** 設定居住城市 **/
        public static String address = "";
        /** 關於我 **/
        public static String about_me = "";

        public static String enable = "";

        public static String role = "";

        public static String organization_ids = "";

        public void setUserStatus(boolean status) {
                this.userStatus = status;
        }

        public boolean getUserStatus() {
                return userStatus;
        }

        /** 登出, 清除使用者帳號資料 **/
        public static void clearUserData(Context context) {
                clearUserResultPreferences(context);
                userStatus = false;
                user_id = "";
                password = "";
                source = "";
                social_id = "";
                create_time = "";
                image = "";
                email = "";
                sex = "";
                birthday = "";
                nickname = "";
                name = "";
                phone = "";
                address = "";
                about_me = "";
                enable = "";
                role = "";
                organization_ids = "";
        }

        /** 設定伺服器回傳的使用者帳號資料(json格式) **/
        public static boolean setUserData(Context context, String result) {
                String status = "";
                try {
                        JSONObject jObj = new JSONObject(result);
                        status = jObj.getString("status");

                        if (status.equals("success")) {
                                Log.i(TAG, "setUserData Result : success");

                                saveUserResultPreferences(context, result);

                                UserData2.userStatus = true;
                                try {
                                        UserData2.email = jObj.getJSONObject("user").getString("email");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.source = jObj.getJSONObject("user").getString("source");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.enable = jObj.getJSONObject("user").getString("enable");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.role = jObj.getJSONObject("user").getString("role");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.user_id = jObj.getJSONObject("user").getJSONObject("info").getString("user_id");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.address = jObj.getJSONObject("user").getJSONObject("info").getString("address");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.birthday = jObj.getJSONObject("user").getJSONObject("info").getString("birthday");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.image = jObj.getJSONObject("user").getJSONObject("info").getString("image");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.about_me = jObj.getJSONObject("user").getJSONObject("info").getString("about_me");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                try {
                                        UserData2.organization_ids = jObj.getJSONObject("user").getJSONObject("info").getString("organization_ids");
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                                showUserData();
                                return true;
                        } else {
                                Log.e(TAG, "setUserData Result : fail");
                                Log.e(TAG, "Register : 此帳號已存在");
                                Log.e(TAG, "NormalLogin : 帳號或密碼錯誤");
                                return false;
                        }

                } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "setUserData JSONException");
                }

                return false;
        }

        /** 將伺服器回傳的使用者帳號資料儲存於手機內存Preferences **/
        private static void saveUserResultPreferences(Context context, String result) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(k_UserData_1, result);
                editor.commit();
        }

        /** 獲取儲存於手機內存Preferences的使用者帳號資料 **/
        public static void getUserResultPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String result = "";
                try {
                        result = sp.getString(k_UserData_1, null);
                } catch (Exception e) {
                        result = null;
                        Log.e(TAG, "getUserResultPerferences Exception");
                }

                if (result != null) {
                        setUserData(context, result);
                } else {
                        Log.i(TAG, "getUserResultPerferences == null");
                }
        }

        /** 清除儲存於手機內存Preferences的使用者帳號資料 **/
        private static void clearUserResultPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k_UserData_1);
                editor.commit();
        }

        /** log 使用者帳號資料訊息 **/
        public static String showUserData() {
                StringBuffer sb = new StringBuffer();
                sb.append("userStatus : ").append(userStatus).append("\n");
                sb.append("user_id : ").append(user_id).append("\n");
                sb.append("password : ").append(password).append("\n");
                sb.append("source : ").append(source).append("\n");
                sb.append("social_id : ").append(social_id).append("\n");
                sb.append("create_time : ").append(create_time).append("\n");
                sb.append("image : ").append(image).append("\n");
                sb.append("email : ").append(email).append("\n");
                sb.append("sex : ").append(sex).append("\n");
                sb.append("birthdate : ").append(birthday).append("\n");
                sb.append("nickname : ").append(nickname).append("\n");
                sb.append("name : ").append(name).append("\n");
                sb.append("phone : ").append(phone).append("\n");
                sb.append("address : ").append(address).append("\n");
                sb.append("about_me : ").append(about_me).append("\n");
                sb.append("enable : ").append(enable).append("\n");
                sb.append("role : ").append(role).append("\n");
                sb.append("organization_ids : ").append(organization_ids).append("\n");
                Log.i(TAG, sb.toString());
                return sb.toString();
        }

}
