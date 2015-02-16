package tw.org.by37.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class UserData {

        private final static String TAG = "UserData";

        // /** 使用者的資料物件 **/
        // public static UserData userData = new UserData();

        /** 使用者登入狀態 **/
        public static boolean userStatus = false;
        /** 使用者編號 **/
        public static String id = "";
        /** 使用者帳號 **/
        public static String account = "";
        /** 使用者密碼 **/
        public static String password = "";
        /** 0=一般註冊, 1=Facebook, 2=Google+ **/
        public static String login_type = "";
        /** 社群平台ID,僅社群登入使用 **/
        public static String social_id = "";
        /** 建立時間(秒數) **/
        public static String createtime = "";

        /** 大頭貼路徑 **/
        public static String avatar = "";
        /** 電子郵件地址 **/
        public static String email = "";
        /** 性別, 1=男, 2=女 **/
        public static String sex = "";
        /** 生日：yyyy-mm-dd **/
        public static String birthdate = "";
        /** 暱稱 **/
        public static String nickname = "";
        /** 姓名 **/
        public static String name = "";
        /** 聯絡電話 **/
        public static String phone = "";

        /** 使用者登入狀態 **/
        public static void setUserStatus(boolean status) {
                userStatus = status;
        }

        /** 使用者登入狀態 **/
        public static boolean getUserStatus() {
                return userStatus;
        }

        /** 使用者編號 **/
        public static void setId(String string) {
                id = string;
        }

        /** 使用者編號 **/
        public static String getId() {
                return id;
        }

        /** 使用者帳號 **/
        public static void setAccount(String string) {
                account = string;
        }

        /** 使用者帳號 **/
        public static String getAccount() {
                return account;
        }

        /** 使用者密碼 **/
        public static void setPassword(String string) {
                password = string;
        }

        /** 使用者密碼 **/
        public static String getPassword() {
                return password;
        }

        /** 0=一般註冊, 1=Facebook, 2=Google+ **/
        public static void setLogin_Type(String string) {
                login_type = string;
        }

        /** 0=一般註冊, 1=Facebook, 2=Google+ **/
        public static String getLogin_Type() {
                return login_type;
        }

        /** 社群平台ID,僅社群登入使用 **/
        public static void setSocial_Id(String string) {
                social_id = string;
        }

        /** 社群平台ID,僅社群登入使用 **/
        public static String getSocial_Id() {
                return social_id;
        }

        /** 建立時間(秒數) **/
        public static void setCreatetime(String string) {
                createtime = string;
        }

        /** 建立時間(秒數) **/
        public static String getCreatetime() {
                return createtime;
        }

        /** 大頭貼路徑 **/
        public static void setAvatar(String string) {
                avatar = string;
        }

        /** 大頭貼路徑 **/
        public static String getAvatar() {
                return avatar;
        }

        /** 電子郵件地址頭貼路徑 **/
        public static void setEmail(String string) {
                email = string;
        }

        /** 電子郵件地址 **/
        public static String getEmail() {
                return email;
        }

        /** 性別, 1=男, 2=女 **/
        public static void setSex(String string) {
                sex = string;
        }

        /** 性別, 1=男, 2=女 **/
        public static String getSex() {
                return sex;
        }

        /** 生日：yyyy-mm-dd **/
        public static void setBirthdate(String string) {
                birthdate = string;
        }

        /** 生日：yyyy-mm-dd **/
        public static String getBirthdate() {
                return birthdate;
        }

        /** 暱稱 **/
        public static void setNickname(String string) {
                nickname = string;
        }

        /** 暱稱 **/
        public static String getNickname() {
                return nickname;
        }

        /** 姓名 **/
        public static void setName(String string) {
                name = string;
        }

        /** 姓名 **/
        public static String getName() {
                return name;
        }

        /** 聯絡電話 **/
        public static void setPhone(String string) {
                phone = string;
        }

        /** 聯絡電話 **/
        public static String getPhone() {
                return phone;
        }

        public static void showUserData() {
                StringBuffer sb = new StringBuffer();
                // sb.append("{");
                // sb.append("userStatus : ").append(userStatus).append("},{");
                // sb.append("id : ").append(id).append("},{");
                // sb.append("account : ").append(account).append("},{");
                // sb.append("password : ").append(password).append("},{");
                // sb.append("login_type : ").append(login_type).append("},{");
                // sb.append("social_id : ").append(social_id).append("},{");
                // sb.append("createtime : ").append(createtime).append("},{");
                // sb.append("avatar : ").append(avatar).append("},{");
                // sb.append("email : ").append(email).append("},{");
                // sb.append("sex : ").append(sex).append("},{");
                // sb.append("birthdate : ").append(birthdate).append("},{");
                // sb.append("nickname : ").append(nickname).append("},{");
                // sb.append("name : ").append(name).append("},{");
                // sb.append("phone : ").append(phone);
                // sb.append("}");

                sb.append("userStatus : ").append(userStatus).append("\n");
                sb.append("id : ").append(id).append("\n");
                sb.append("account : ").append(account).append("\n");
                sb.append("password : ").append(password).append("\n");
                sb.append("login_type : ").append(login_type).append("\n");
                sb.append("social_id : ").append(social_id).append("\n");
                sb.append("createtime : ").append(createtime).append("\n");
                sb.append("avatar : ").append(avatar).append("\n");
                sb.append("email : ").append(email).append("\n");
                sb.append("sex : ").append(sex).append("\n");
                sb.append("birthdate : ").append(birthdate).append("\n");
                sb.append("nickname : ").append(nickname).append("\n");
                sb.append("name : ").append(name).append("\n");
                sb.append("phone : ").append(phone);

                Log.i(TAG, sb.toString());
        }

        /** 登出, 清除使用者資料 **/
        public static void clearUserData() {
                userStatus = false;
                id = "";
                account = "";
                password = "";
                login_type = "";
                social_id = "";
                createtime = "";
                avatar = "";
                email = "";
                sex = "";
                birthdate = "";
                nickname = "";
                name = "";
                phone = "";
        }

        public static boolean setUserData(Context context, String result) {

                String status = "";

                try {
                        JSONObject jObj = new JSONObject(result);
                        status = jObj.getString("status");

                        if (status.equals("success")) {
                                Log.i(TAG, "setUserData Result : success");
                                UserData.setUserStatus(true);
                                UserData.setId(jObj.getString("id"));
                                UserData.setAccount(jObj.getString("account"));
                                UserData.setPassword(jObj.getString("password"));
                                UserData.setLogin_Type(jObj.getString("login_type"));
                                UserData.setCreatetime(jObj.getString("createtime"));
                                UserData.setAvatar(jObj.getString("avatar"));
                                UserData.setEmail(jObj.getString("email"));
                                UserData.setSex(jObj.getString("gender"));
                                UserData.setBirthdate(jObj.getString("birthdate"));
                                UserData.setNickname(jObj.getString("nickname"));
                                UserData.setName(jObj.getString("name"));
                                UserData.setPhone(jObj.getString("phone"));
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

        public static void saveUserResultPreferences(Context context, String result) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("UserResult", result);
                editor.commit();
        }

        public static String getUserResultPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String result = "";
                try {
                        result = sp.getString("UserResult", null);
                } catch (Exception e) {
                        result = null;
                        Log.e(TAG, "getUserResultPerferences Exception");
                }
                return result;
        }

        public static void clearUserResultPreferences(Context context) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("UserResult");
                editor.commit();
        }

}
