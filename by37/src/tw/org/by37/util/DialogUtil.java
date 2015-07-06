package tw.org.by37.util;

import tw.org.by37.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

public class DialogUtil {

        private final static String TAG = DialogUtil.class.getName();

        private Context mContext;

        private Intent intent;

        private boolean isIntent = false;

        public DialogUtil(Context context) {
                this.mContext = context;
        }

        public void setIntent(Intent intent) {
                this.intent = intent;
                this.isIntent = true;
        }

        public void NotificationDialog(String title, String msg) {
                Log.i(TAG, "NotificationDialog");
                // 產生一個Builder物件
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // 設定Dialog的標題
                builder.setTitle(title);
                // 設定Dialog的內容
                builder.setMessage(msg);
                // 設定Positive按鈕資料
                builder.setPositiveButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Positive+");
                        }
                });

                builder.setNegativeButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                Log.d(TAG, "Negative+");
                                if (isIntent) {
                                        mContext.startActivity(intent);
                                        intent = null;
                                }
                        }
                });
                builder.show();
        }
}
