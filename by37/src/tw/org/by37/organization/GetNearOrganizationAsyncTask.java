package tw.org.by37.organization;

import java.util.ArrayList;
import java.util.List;

import tw.org.by37.config.SysConfig;
import tw.org.by37.data.SelectingData;
import tw.org.by37.service.OrganizationApiService;
import tw.org.by37.util.FunctionUtil;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 獲取鄰近機構資訊, 傳入參數 param[0] = lat, param[1] = lng
 * 
 * @author Tellasy
 * 
 */
public class GetNearOrganizationAsyncTask extends AsyncTask<String, Void, String> {

        private final static String TAG = "GetNearOrganizationAsyncTask";

        private Context mContext;

        public GetNearOrganizationAsyncTask(Context context) {
                this.mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
                Log.d(TAG, "doInBackground");

                Log.d(TAG, "Lat : " + param[0]);
                Log.d(TAG, "Lng : " + param[1]);

                /** 傳入參數 param[0] = lat, param[1] = lng **/
                String result = OrganizationApiService.getNearOrganization(param[0], param[1]);
                Log.d(TAG, "Result : " + result);

                return result;
        }

        @Override
        protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d(TAG, "onPostExecute");
                if (result != null) {
                        /** 判別伺服器是否正常work狀態 **/
                        if (!FunctionUtil.isSleepServer(result)) {
                                Gson gson = new Gson();
                                ArrayList<OrganizationData> mData = gson.fromJson(result, new TypeToken<List<OrganizationData>>() {
                                }.getType());

                                SelectingData.setNearOrganizationDataList(mData);

                                Log.d(TAG, "OrganizationData List Size : " + mData.size());

                                if (mData != null) {
                                        Intent i = new Intent();
                                        i.putExtra(SysConfig.GET_NEAR_ORGANIZATION_CONTENT_BROADCAST, true);
                                        i.setAction(SysConfig.GET_NEAR_ORGANIZATION_CONTENT_BROADCAST);
                                        mContext.sendBroadcast(i);
                                }

                        } else {
                                Log.d(TAG, "Server is sleep new.");
                        }
                } else {
                        Log.e(TAG, "Result == null");
                }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
        }

        /** 執行Async Task前 **/
        @Override
        protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "onPreExecute");
        }
}