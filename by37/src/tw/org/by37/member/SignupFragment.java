package tw.org.by37.member;

import static tw.org.by37.config.RequestCode.CROP_PHOTO;
import static tw.org.by37.config.RequestCode.REGISTER_SUCCESS_CODE;
import static tw.org.by37.config.RequestCode.SOURCE_CAMERA;
import static tw.org.by37.config.RequestCode.SOURCE_PHOTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.R;
import tw.org.by37.data.UserData;
import tw.org.by37.service.UsersApiService;
import tw.org.by37.util.FunctionUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SignupFragment extends Fragment {

        private final static String TAG = SignupFragment.class.getName();;

        private Context mContext;

        private Button btn_signup;

        private EditText edt_account, edt_password, edt_nick_name;

        private TextView tv_info;

        private ProgressDialog psDialog;

        /** 動態新增 機構類別 的Layout **/
        private LinearLayout ll_org_type;

        private ImageView img_avatar;
        private TextView tv_avatar_edit;
        /** 設定大頭照的大小 **/
        private final static float AVATAR_SIZE = 200;
        private Uri imageUri;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                UserData.clearUserData(mContext);

                View view = inflater.inflate(R.layout.fragment_signup, container, false);

                findView(view);

                setOrgType(view);

                initAvatar(view);

                Log.i(TAG, "DIRECTORY_PICTURES : " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                Log.i(TAG, "DIRECTORY_DCIM : " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                Log.i(TAG, "DIRECTORY_DOWNLOADS : " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                Log.i(TAG, "getDownloadCacheDirectory : " + Environment.getDownloadCacheDirectory());
                Log.i(TAG, "getRootDirectory : " + Environment.getRootDirectory());
                Log.i(TAG, "isExternalStorageEmulated : " + Environment.isExternalStorageEmulated());
                Log.i(TAG, "isExternalStorageRemovable : " + Environment.isExternalStorageRemovable());
                Log.i(TAG, "---");
                Log.i(TAG, "getDataDirectory : " + Environment.getDataDirectory());
                Log.i(TAG, "getDataDirectory getPath : " + Environment.getDataDirectory().getPath());
                Log.i(TAG, "getExternalStorageDirectory : " + Environment.getExternalStorageDirectory());
                Log.i(TAG, "getExternalStorageDirectory getAbsolutePath : " + Environment.getExternalStorageDirectory().getAbsolutePath());
                Log.i(TAG, "---");
                Log.i(TAG, "getFilesDir : " + mContext.getFilesDir());
                Log.i(TAG, "getCacheDir : " + mContext.getCacheDir());
                Log.i(TAG, "getExternalCacheDir : " + mContext.getExternalCacheDir());

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                btn_signup = (Button) view.findViewById(R.id.btn_signup);
                btn_signup.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                /** set User Data **/
                                UserData.name = edt_nick_name.getText().toString();
                                UserData.email = edt_account.getText().toString();
                                UserData.password = edt_password.getText().toString();
                                UserData.source = "by37";
                                UserData.showUserData();
                                new RegisterUserAsyncTask().execute(null, null, null);
                        }
                });

                edt_account = (EditText) view.findViewById(R.id.edt_account);
                edt_password = (EditText) view.findViewById(R.id.edt_password);
                edt_nick_name = (EditText) view.findViewById(R.id.edt_nick_name);
                tv_info = (TextView) view.findViewById(R.id.tv_info);
        }

        private void initAvatar(View view) {
                img_avatar = (ImageView) view.findViewById(R.id.img_avatar);
                img_avatar.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                pickPhotoDialog();
                        }
                });
                tv_avatar_edit = (TextView) view.findViewById(R.id.tv_avatar_edit);
                tv_avatar_edit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                pickPhotoDialog();
                        }
                });

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_personal);
                if (img_avatar != null && bitmap != null) {
                        img_avatar.setImageBitmap(bitmap);
                }
        }

        private void pickPhotoDialog() {
                // Be a Design Button Event
                final String[] choseSourceFromList = { "相機拍攝", "從相簿中挑選" };

                AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
                // 建立標題
                mDialog.setTitle(getString(R.string.photo_source));
                // 建立按下取消什麼事情都不做的事件
                DialogInterface.OnClickListener cancle_Click = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                };

                // 建立選擇的事件
                DialogInterface.OnClickListener list_Click = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                case 0:
                                        // 使用Intent調用其他服務幫忙拍照
                                        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent_camera, SOURCE_CAMERA);
                                        break;
                                case 1:
                                        Intent intent_photo = new Intent();
                                        // 用來設置 intent 裡數據的類型
                                        intent_photo.setType("image/*");
                                        intent_photo.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(intent_photo, SOURCE_PHOTO);
                                        break;
                                default:
                                        break;
                                }
                        }
                };

                mDialog.setItems(choseSourceFromList, list_Click);
                mDialog.setNeutralButton(getString(R.string.cancle), cancle_Click);
                mDialog.show();
        }

        /** 機構類別名稱陣列 **/
        private String[] mOrgType;
        /** 機構類別名稱勾選狀態陣列 **/
        private boolean[] mOrgTypeStatus;

        /** 動態設定 機構類別 的資料 **/
        private void setOrgType(View view) {
                ll_org_type = (LinearLayout) view.findViewById(R.id.ll_org_type);
                ll_org_type.removeAllViews();

                // 機構類別項目的名稱
                Resources res = mContext.getResources();
                mOrgType = res.getStringArray(R.array.org_type);
                mOrgTypeStatus = new boolean[mOrgType.length];
                for (int i = 0; i < mOrgType.length; i++) {
                        mOrgTypeStatus[i] = false;
                }
                int mOrgTypeItem = mOrgType.length;
                int viewCount = mOrgTypeItem / 2;
                if (mOrgTypeItem % 2 != 0) {
                        viewCount++;
                }
                Log.i(TAG, "OrgType Count : " + mOrgTypeItem);
                Log.i(TAG, "OrgType View Count : " + viewCount);

                int j = 0;
                for (int i = 0; i < viewCount; i++) {
                        if (j < mOrgTypeItem - 1) {
                                ll_org_type.addView(createOrgTypeView(j, mOrgType[j], mOrgType[j + 1]));
                                j += 2;
                        } else {
                                ll_org_type.addView(createOrgTypeView(j, mOrgType[j], ""));
                        }
                }
        }

        private View createOrgTypeView(int id, String left, String right) {
                LayoutInflater infalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = infalter.inflate(R.layout.item_org_type, null);
                final ViewHolder holder = new ViewHolder();
                holder.checkBox_left = (CheckBox) view.findViewById(R.id.checkBox_left);
                holder.checkBox_right = (CheckBox) view.findViewById(R.id.checkBox_right);

                holder.checkBox_left.setId(id);
                holder.checkBox_right.setId(++id);

                if (left.length() > 0) {
                        holder.checkBox_left.setText(left);
                } else {
                        holder.checkBox_left.setVisibility(View.GONE);
                }
                if (right.length() > 0) {
                        holder.checkBox_right.setText(right);
                } else {
                        holder.checkBox_right.setVisibility(View.GONE);
                }

                holder.checkBox_left.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // TODO Auto-generated method stub
                                Log.i(TAG, "CheckBox Left Status : " + isChecked + ", Id : " + holder.checkBox_left.getId());
                                mOrgTypeStatus[holder.checkBox_left.getId()] = isChecked;
                        }
                });

                holder.checkBox_right.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // TODO Auto-generated method stub
                                Log.i(TAG, "CheckBox Right Status : " + isChecked + ", Id : " + holder.checkBox_right.getId());
                                mOrgTypeStatus[holder.checkBox_right.getId()] = isChecked;
                        }
                });

                return view;
        }

        private class ViewHolder {
                CheckBox checkBox_left;
                CheckBox checkBox_right;
        }

        class RegisterUserAsyncTask extends AsyncTask<String, Integer, String> {
                @Override
                protected String doInBackground(String... param) {

                        String result = UsersApiService.RegisterUser();

                        Log.i(TAG, "Result : " + result);

                        return result;
                }

                @Override
                protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (result != null) {
                                int checkReg = result.indexOf("<html");
                                String mReg = "";
                                String status = "";

                                if (checkReg >= 0) {
                                        mReg = "Register Info : server has been sleep." + "\n\n";
                                } else {
                                        mReg = "Register Info : " + result + "\n\n";
                                }
                                tv_info.setText(mReg);

                                try {
                                        JSONObject mJObj = new JSONObject(result);
                                        status = mJObj.getString("status");

                                        if (status.equals("success")) {
                                                UserData.setUserData(mContext, result);
                                                RegisterSuccess();
                                        } else {
                                                if (status.equals("fail")) {
                                                        FunctionUtil.AlertDialogCheck(mContext, "", mJObj.getString("message")).show();
                                                }
                                        }
                                } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                        }
                        psDialog.dismiss();
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                }

                /** 執行Async Task前 **/
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                        psDialog = ProgressDialog.show(mContext, "", "註冊中，請稍候...");
                }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);
                switch (requestCode) {
                case SOURCE_CAMERA:
                        if (resultCode == Activity.RESULT_OK) {
                                Bundle extras = data.getExtras();
                                Bitmap bitmap = (Bitmap) extras.get("data");
                                if (bitmap != null) {
                                        img_avatar.setImageBitmap(bitmap);
                                        saveImage(bitmap);
                                }
                        }
                        break;
                case SOURCE_PHOTO:
                        if (resultCode == Activity.RESULT_OK) {
                                try {
                                        Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(data.getData()));
                                        if (bitmap != null) {
                                                img_avatar.setImageBitmap(bitmap);
                                                saveImage(bitmap);
                                        }
                                } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                }
                        }
                        break;
                case CROP_PHOTO:
                        break;
                default:
                        break;
                }
        }

        private void saveImage(Bitmap bitmap) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                String path = mContext.getExternalCacheDir() + "/image.jpg";
                UserData.image = path;
                Log.d(TAG, "path = " + path);

                try {
                        File f = new File(path);
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        fo.close();
                } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, e.toString());
                }
        }

        /** Preferences **/
        /** End of Preferences **/

        /** GotoActivity **/
        /** Register Success and then go back to the MainActivity **/
        private void RegisterSuccess() {
                Intent intent = getActivity().getIntent();
                getActivity().setResult(REGISTER_SUCCESS_CODE, intent); // 回傳RESULT_OK
                getActivity().finish();
        }

        /** End of GotoActivity **/

        /** Activity Bundle **/
        /** End of Activity Bundle **/

        @Override
        public void onResume() {
                super.onResume();
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
