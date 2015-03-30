package tw.org.by37.fragment.productsell;

import java.util.ArrayList;

import tw.org.by37.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainProductSellFragment extends Fragment {
	
	private static final String TAG = MainProductSellFragment.class.getName();

	private HorizontalScrollView mTabScrollView;;
	private String[] tabTitles = {"推薦","慈善專區","促銷品","一元起標","全部商品"};
	private ArrayList<TextView> mTextViewAry;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_main_productsell, container,false);
		setView(v);
		init();
		initTabBar();
		return v;
	}
	
	private void setView(View v){
		mTabScrollView = (HorizontalScrollView)v.findViewById(R.id.productsell_tab_scrollview);
		
		
	}
	
	private void init(){
		mTextViewAry = new ArrayList<TextView>();
	}
	
	private void initTabBar(){
		LinearLayout mTabLinerLayout = new LinearLayout(getActivity());
		for(int i =0;i<tabTitles.length;i++){
			String title = tabTitles[i];
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			p.width=getScreenWidth()/3;
			p.height=130;
			TextView tv = new TextView(getActivity());
			tv.setText(title);
			tv.setTextSize(18);
			tv.setGravity(Gravity.CENTER);
			tv.setLayoutParams(p);
			tv.setTag(title);
			mTextViewAry.add(tv);
			mTabLinerLayout.addView(tv);
			if(i==0){
				tv.setBackgroundColor(Color.YELLOW);
			}
			tv.setOnClickListener(new TextView.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(TextView tv : mTextViewAry){
						String tag = (String) tv.getTag();
						if(tag.equals(v.getTag())){
							tv.setBackgroundColor(Color.YELLOW);
						}else{
							tv.setBackgroundColor(Color.GRAY);
						}
					}
				}
			});
		}
		mTabScrollView.addView(mTabLinerLayout);
	}
	
	private int getScreenWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
	}

}
