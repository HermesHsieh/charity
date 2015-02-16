package tw.org.by37.util;

import android.content.Context;
import android.widget.Toast;

public class FunctionUtil {

        public static void showToastMsg(Context context, String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
}
