package tw.org.by37.emergency;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import tw.org.by37.config.SysConfig;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GetEmergencyContentTask extends AsyncTask<String, Void, String> {
	private static final String TAG = GetEmergencyContentTask.class.getName();
	private Context mContext;
	
	private static String uri = SysConfig.emergencyAPI;
	
	public GetEmergencyContentTask(Context context){
		this.mContext = context;
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		
		Log.d(TAG, "Start");
		String strResult = null;
		String id = arg0[0];
        // 透過HTTP連線取得回應
        try {
                // for port 80 requests!
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(uri+id);
                Log.d(TAG, "URI = "+uri+id);
                HttpResponse response = httpclient.execute(httpget);

                /* 取出回應字串 */
                strResult = EntityUtils.toString(response.getEntity(), "UTF-8");
//                Log.d(TAG, "strResult = "+strResult);
        } catch (Exception e) {
                e.printStackTrace();
        }
        
		return strResult;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Intent i = new Intent();
		i.putExtra(SysConfig.GET_EMERGENCY_CONTENT_BROADCAST, result);
		i.setAction(SysConfig.GET_EMERGENCY_CONTENT_BROADCAST);
		mContext.sendBroadcast(i);
		
		Log.d(TAG, result);
		
	}

}
