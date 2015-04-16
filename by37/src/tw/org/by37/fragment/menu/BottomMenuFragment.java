package tw.org.by37.fragment.menu;

import tw.org.by37.R;
import tw.org.by37.R.id;
import tw.org.by37.R.layout;
import tw.org.by37.fragment.productsell.NewProductActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BottomMenuFragment extends Fragment {

        private final static String TAG = "BottomMenuFragment";

        private RelativeLayout rl_btn_1, rl_btn_2;
        private TextView tv_btn_1, tv_btn_2;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_bottommenu, container, false);

                findView(view);

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        public void findView(View view) {
                rl_btn_1 = (RelativeLayout) view.findViewById(R.id.rl_btn_1);
                rl_btn_2 = (RelativeLayout) view.findViewById(R.id.rl_btn_2);

                rl_btn_1.setOnClickListener(mButtonOnClick);
                rl_btn_2.setOnClickListener(mButtonOnClick);

                tv_btn_1 = (TextView) view.findViewById(R.id.tv_btn_1);
                tv_btn_2 = (TextView) view.findViewById(R.id.tv_btn_2);
        }

        private OnClickListener mButtonOnClick = new OnClickListener() {
                @Override
                public void onClick(View v) {
                        // TODO Auto-generated method stub
                        switch (v.getId()) {
                        case R.id.rl_btn_1:
                                Log.i(TAG, "Btn1 Click");
						Intent i = new Intent();
						i.setClass(getActivity(), NewProductActivity.class);
						getActivity().startActivity(i);
                                break;
                        case R.id.rl_btn_2:
                                Log.i(TAG, "Btn2 Click");
                                break;
                        }
                }
        };
}
