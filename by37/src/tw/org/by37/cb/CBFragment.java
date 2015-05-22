package tw.org.by37.cb;

import tw.org.by37.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CBFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_charity_bazaar, container, false);
		FragmentTabHost tabHost = (FragmentTabHost)v.findViewById(android.R.id.tabhost);
		tabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), R.id.realtabcontent);
		//1
		tabHost.addTab(tabHost.newTabSpec("推薦")
			   				  .setIndicator("推薦"), 
   					  RecommendFragment.class, 
   					  null);
	    //2
		tabHost.addTab(tabHost.newTabSpec("慈善專區")
				   			  .setIndicator("慈善專區"), 
					  CharitableFragment.class, 
					  null);
	    //3
		tabHost.addTab(tabHost.newTabSpec("全部商品")
				   			  .setIndicator("全部商品"), 
					  AllProductFragment.class, 
				      null);
		return v;
	}
}
