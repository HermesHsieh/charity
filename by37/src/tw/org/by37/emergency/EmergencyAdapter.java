package tw.org.by37.emergency;

import java.util.ArrayList;

import tw.org.by37.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EmergencyAdapter extends BaseAdapter {
	
	private static final String TAG = EmergencyAdapter.class.getName();
	private Context mContext;
	private ArrayList<EmergencyData> mEmergencyDataList;
	
	public EmergencyAdapter(Context context,ArrayList<EmergencyData> list){
		mContext = context;
		mEmergencyDataList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mEmergencyDataList.size();
	}

	@Override
	public EmergencyData getItem(int arg0) {
		// TODO Auto-generated method stub
		return mEmergencyDataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mViewHolder;
		if(arg1==null){
			mViewHolder= new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.item_emergency,null);
			mViewHolder.title = (TextView)arg1.findViewById(R.id.item_emergency_title_tv);
			mViewHolder.group = (TextView)arg1.findViewById(R.id.item_emergency_group_tv);
			arg1.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder) arg1.getTag();
		}
		
		EmergencyData mEmergencyData = mEmergencyDataList.get(arg0);
		mViewHolder.title.setText(mEmergencyData.getTitle());
		mViewHolder.group.setText(mEmergencyData.getGroup());
		return arg1;
	}
	
	static class ViewHolder{
		TextView title;
		TextView group;
	}

}
