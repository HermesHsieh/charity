package tw.org.by37.test;

import tw.org.by37.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageViewFragment extends Fragment {

        private final static String TAG = "ImageViewFragment";

        private Context mContext;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();

                View view = inflater.inflate(R.layout.fragment_test_get, container, false);

                getActivity().setTitle(R.string.title_test_imageView);

                return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.i(TAG, "Request Code : " + requestCode);
                Log.i(TAG, "Result Code : " + resultCode);
        }

        @Override
        public void onResume() {
                super.onResume();
        }

        @Override
        public void onStart() {
                super.onStart();
        }

        @Override
        public void onStop() {
                super.onStop();
        }

        @Override
        public void onPause() {
                super.onPause();
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
        }

}
