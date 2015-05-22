package tw.org.by37.member;

import static tw.org.by37.config.RequestCode.LOGOUT_SUCCESS_CODE;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tw.org.by37.R;
import tw.org.by37.data.UserData;
import tw.org.by37.util.FunctionUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberFragment extends Fragment {

        private final static String TAG = "MemberFragment";

        private Context mContext;

        private ProgressDialog psDialog;

        private Button btn_logout;

        private ImageView img_avatar;

        private TextView tv_info;
        private TextView tv_trade;
        private TextView tv_money;

        private Button btn_trade;
        private Button btn_money;
        private Button btn_edit_product;
        private Button btn_my_market;
        private Button btn_like_history;
        private Button btn_edit_member;
        private Button btn_edit_favorite_org;
        private Button btn_edit_position;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_member, container, false);

                findView(view);

                loadUserAvatar();

                loadUserData();

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                btn_logout = (Button) view.findViewById(R.id.btn_logout);
                btn_trade = (Button) view.findViewById(R.id.btn_trade);
                btn_money = (Button) view.findViewById(R.id.btn_money);
                btn_edit_product = (Button) view.findViewById(R.id.btn_edit_product);
                btn_my_market = (Button) view.findViewById(R.id.btn_my_market);
                btn_like_history = (Button) view.findViewById(R.id.btn_like_history);
                btn_edit_member = (Button) view.findViewById(R.id.btn_edit_member);
                btn_edit_favorite_org = (Button) view.findViewById(R.id.btn_edit_favorite_org);
                btn_edit_position = (Button) view.findViewById(R.id.btn_edit_position);

                btn_logout.setOnClickListener(mButtonListener);
                btn_trade.setOnClickListener(mButtonListener);
                btn_money.setOnClickListener(mButtonListener);
                btn_edit_product.setOnClickListener(mButtonListener);
                btn_my_market.setOnClickListener(mButtonListener);
                btn_like_history.setOnClickListener(mButtonListener);
                btn_edit_member.setOnClickListener(mButtonListener);
                btn_edit_favorite_org.setOnClickListener(mButtonListener);
                btn_edit_position.setOnClickListener(mButtonListener);

                img_avatar = (ImageView) view.findViewById(R.id.img_avatar);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_personal);
                if (img_avatar != null && bitmap != null) {
                        img_avatar.setImageBitmap(bitmap);
                }

                tv_info = (TextView) view.findViewById(R.id.tv_info);
                tv_trade = (TextView) view.findViewById(R.id.tv_trade);
                tv_money = (TextView) view.findViewById(R.id.tv_money);

        }

        private OnClickListener mButtonListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                        switch (v.getId()) {
                        case R.id.btn_logout:
                                UserData.clearUserData(mContext);
                                LogoutSuccess();
                                break;
                        case R.id.btn_trade:
                                break;
                        case R.id.btn_money:
                                break;
                        case R.id.btn_edit_product:
                                break;
                        case R.id.btn_my_market:
                                break;
                        case R.id.btn_like_history:
                                break;
                        case R.id.btn_edit_member:
                                break;
                        case R.id.btn_edit_favorite_org:
                                break;
                        case R.id.btn_edit_position:
                                break;
                        default:
                                break;
                        }
                }
        };

        private void loadUserAvatar() {
                Log.d(TAG, "User Image : " + UserData.image);
                if (UserData.image.length() > 0) {
                        new AsyncTask<String, Void, Bitmap>() {
                                @Override
                                protected Bitmap doInBackground(String... params) {
                                        String url = params[0];
                                        return getBitmapFromURL(url);
                                }

                                @Override
                                protected void onPostExecute(Bitmap result) {
                                        super.onPostExecute(result);
                                        psDialog.dismiss();
                                        if (result != null) {
                                                img_avatar.setImageBitmap(result);
                                        }
                                }

                                @Override
                                protected void onPreExecute() {
                                        super.onPreExecute();
                                        psDialog = ProgressDialog.show(mContext, "", "讀取中，請稍候...");
                                }
                        }.execute(UserData.image);
                }
        }

        private void loadUserData() {
                String data = UserData.showUserData();
                if (tv_info != null)
                        tv_info.setText(data);
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

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);
        }

        /** Preferences **/
        /** End of Preferences **/

        /** GotoActivity **/
        private void LogoutSuccess() {
                Intent intent = getActivity().getIntent();
                getActivity().setResult(LOGOUT_SUCCESS_CODE, intent); // 回傳RESULT_OK
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
