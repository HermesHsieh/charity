package tw.org.by37.emergency;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.org.by37.R;
import tw.org.by37.config.SysConfig;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EmergencyFragment extends Fragment {
	
	private static final String TAG = EmergencyFragment.class.getName();
	
	/**廣播必備參數**/
    private IntentFilter            mGetEmergencyIntentFilter ;            
	private MainBroadcastReceiver   mMainBroadcastReceiverr   ;
	
	/**Emergency Fragment**/
	private EmergencyFragment mEmergencyFragment;
	private LinearLayout mEmergencyLinearLayout ;
	private TextView mEmergencyCountTextView ;
	private ListView mEmergencyListView ;
	private EmergencyAdapter mEmergencyAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_emergency, container, false);
		setView(v);
		init();
		return v;
	}
	
	 @Override
     public void onResume() {
             super.onResume();
             getActivity().registerReceiver(mMainBroadcastReceiverr, mGetEmergencyIntentFilter);
     }
	 
	 @Override
     public void onPause() {
             super.onPause();
             getActivity().unregisterReceiver(mMainBroadcastReceiverr);
     }
	
	 public void setView(View view) {
     	
     	mEmergencyLinearLayout = (LinearLayout)view.findViewById(R.id.main_emergency_linearlayout);
     	mEmergencyCountTextView = (TextView)view.findViewById(R.id.main_emergency_count_tv);
     	mEmergencyListView = (ListView)view.findViewById(R.id.main_emergency_listview);
     }
	
	/**初始參數**/
    private void init(){
    	mGetEmergencyIntentFilter = new IntentFilter(SysConfig.GET_EMERGENCY_BROADCAST);
    	mMainBroadcastReceiverr = new MainBroadcastReceiver();
    	
    	new GetEmergencyTask(getActivity()).execute();
    }
    
    /**設定收到廣播後的動作**/
    public class MainBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(SysConfig.GET_EMERGENCY_BROADCAST)){
				String strEmergency = intent.getExtras().getString(SysConfig.GET_EMERGENCY_BROADCAST);
				getEmergencyData(strEmergency);
			}
		}
	}
    
    /**解析Emergency字串**/
    private void getEmergencyData(String str){
    	ArrayList<EmergencyData> mEmergencyDataList = new ArrayList<EmergencyData>();
    	try {
			JSONArray strArray = new JSONArray(str);
			if(strArray.length()!=0){
				for(int i =0 ;i<strArray.length();i++){
					EmergencyData mEmergencyData = new EmergencyData();
					JSONObject emergecyData = strArray.getJSONObject(i);
					String id    = emergecyData.getString("id");
					String title = emergecyData.getString("title");
					String group = emergecyData.getString("group");
					
					mEmergencyData.setId(id);
					mEmergencyData.setTitle(title);
					mEmergencyData.setGroup(group);
					mEmergencyDataList.add(mEmergencyData);
					Log.d(TAG, "id:"+id);
					Log.d(TAG, "title:"+title);
					Log.d(TAG, "group:"+group);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	showEmergency(mEmergencyDataList);
   }
    
   private void showEmergency(ArrayList<EmergencyData> list){
	   
	   mEmergencyAdapter = new EmergencyAdapter(getActivity(), list);
	   
	   if(list.size()==0){
		   mEmergencyLinearLayout.setVisibility(View.GONE);
	   }else{
		   mEmergencyLinearLayout.setVisibility(View.VISIBLE);
		   mEmergencyCountTextView.setText(""+list.size());
		   mEmergencyListView.setAdapter(mEmergencyAdapter);
		   mEmergencyListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				EmergencyData mEmergencyData =mEmergencyAdapter.getItem(arg2);
				Intent i = new Intent();
				i.setClass(getActivity(), EmergencyActivity.class);
				i.putExtra("ID", mEmergencyData.getId());
				getActivity().startActivity(i);
			}
		});
	   }
	   
   }
}
	
