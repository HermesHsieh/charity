package tw.org.by37.supplieshelp;

import android.content.Context;
import android.os.AsyncTask;

public class GetSuppliesTotalTask extends AsyncTask<String, Void, String> {

        private static final String TAG = GetSuppliesTotalTask.class.getName();

        private Context mContext;

        public GetSuppliesTotalTask(Context context) {
                this.mContext = context;
        }

        @Override
        protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
        }

        /** 執行Async Task前 **/
        @Override
        protected void onPreExecute() {
                super.onPreExecute();
        }

}
