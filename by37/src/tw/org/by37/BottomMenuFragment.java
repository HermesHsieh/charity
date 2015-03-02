package tw.org.by37;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BottomMenuFragment extends Fragment {

        private ImageButton img_btn_1, img_btn_2, img_btn_3;
        private RelativeLayout rl_btn_1, rl_btn_2, rl_btn_3;
        private ImageView border_line;

        private LinearLayout ll_bottommenu;

        private boolean bg_transparent = false;

        private boolean isView = false;

        private int btn_1_src = -1;
        private int btn_2_src = -1;
        private int btn_3_src = -1;

        private int btn_counter = 3;

        public BottomMenuFragment() {
                super();
        }

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
                img_btn_1 = (ImageButton) view.findViewById(R.id.img_btn_1);
                img_btn_2 = (ImageButton) view.findViewById(R.id.img_btn_2);
                img_btn_3 = (ImageButton) view.findViewById(R.id.img_btn_3);

                rl_btn_1 = (RelativeLayout) view.findViewById(R.id.rl_btn_1);
                rl_btn_2 = (RelativeLayout) view.findViewById(R.id.rl_btn_2);
                rl_btn_3 = (RelativeLayout) view.findViewById(R.id.rl_btn_3);

                ll_bottommenu = (LinearLayout) view.findViewById(R.id.ll_bottommenu);

                switch (btn_counter) {
                case 1:
                        setBtn2GONE();
                case 2:
                        setBtn3GONE();
                case 3:
                        break;
                default:
                        break;
                }

                if (btn_1_src != -1)
                        setBtn1Src(btn_1_src);
                if (btn_2_src != -1)
                        setBtn2Src(btn_2_src);
                if (btn_3_src != -1)
                        setBtn3Src(btn_3_src);

                if (bg_transparent) {
                        if (isView) {
                        } else {
                        }
                }
        }

        /** 設定Button數量(1~3) **/
        public void setButtonCounter(int counter) {
                this.btn_counter = counter;
        }

        /** 設定Button的圖示(Btn1,Btn2,Btn3) **/
        public void setButtonResource(int btn1, int btn2, int btn3) {
                this.btn_1_src = btn1;
                this.btn_2_src = btn2;
                this.btn_3_src = btn3;
        }

        /** 設定透明背景是否開啟 **/
        public void setBackgroundTransparent(boolean status) {
                this.bg_transparent = status;
        }

        /** 設定是否於View使用,而使用不同高度背景模糊 **/
        public void setViewStatus(boolean status) {
                this.isView = status;
        }

        /** 設定按鈕的BackgroundResource **/
        public void setBackgroundStyle(int style) {
                img_btn_1.setBackgroundResource(style);
                img_btn_2.setBackgroundResource(style);
                img_btn_3.setBackgroundResource(style);
        }

        /** get Button1 object **/
        public ImageButton getBtn1() {
                return img_btn_1;
        }

        /** get Button2 object **/
        public ImageButton getBtn2() {
                return img_btn_2;
        }

        /** get Button3 object **/
        public ImageButton getBtn3() {
                return img_btn_3;
        }

        /** set Button1 Source **/
        public void setBtn1Src(int src) {
                img_btn_1.setImageResource(src);
        }

        /** set Button2 Source **/
        public void setBtn2Src(int src) {
                img_btn_2.setImageResource(src);
        }

        /** set Button3 Source **/
        public void setBtn3Src(int src) {
                img_btn_3.setImageResource(src);
        }

        public void setBtn1Visible() {
                rl_btn_1.setVisibility(View.VISIBLE);
        }

        public void setBtn2Visible() {
                rl_btn_2.setVisibility(View.VISIBLE);
        }

        public void setBtn3Visible() {
                rl_btn_3.setVisibility(View.VISIBLE);
        }

        public void setBtn1GONE() {
                rl_btn_1.setVisibility(View.GONE);
        }

        public void setBtn2GONE() {
                rl_btn_2.setVisibility(View.GONE);
        }

        public void setBtn3GONE() {
                rl_btn_3.setVisibility(View.GONE);
        }

        public void setBorderLineVisible() {
                border_line.setVisibility(View.VISIBLE);
        }

        public void setBorderLineGONE() {
                border_line.setVisibility(View.GONE);
        }
}
