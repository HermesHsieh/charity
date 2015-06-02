package tw.org.by37;

import static tw.org.by37.config.RequestCode.REGISTER_SUCCESS_CODE;
import tw.org.by37.organization.NearOrganizationFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class NearOrganizationActivity extends BackActivity {

        private final static String TAG = "NearOrganizationActivity";

        private Context mContext;

        private NearOrganizationFragment mNearOrganizationFragment;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                mContext = this;
                setContentView(R.layout.activity_near_organization);

                switchNearOrganizationFragment();
        }

        /**
         * switchNearOrganizationFragment 介面
         */
        public void switchNearOrganizationFragment() {
                Log.i(TAG, "switchNearOrganizationFragment");
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.main_content);

                FragmentTransaction ft = manager.beginTransaction();

                if (mNearOrganizationFragment == null)
                        mNearOrganizationFragment = new NearOrganizationFragment();

                mNearOrganizationFragment.setIndexView(false);

                if (fragment == null) {
                        ft.add(R.id.main_content, mNearOrganizationFragment);
                } else {
                        ft.replace(R.id.main_content, mNearOrganizationFragment);
                }
                ft.commit();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                Log.v(TAG, "Request Code : " + requestCode);
                Log.v(TAG, "Result Code : " + resultCode);

        }

        @Override
        public void onResume() {
                super.onResume();
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
