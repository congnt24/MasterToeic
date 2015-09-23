package fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cong.audiocong.AudioCong;
import com.ptpmcn.cong.mastertoeiclc.R;

import java.io.File;

/**
 * Created by cong on 9/22/2015.
 */
public class QuestionFragment extends Fragment {
    private ImageView imageView;
    private android.widget.TextView tvq1;
    private RadioButton radio1A;
    private RadioButton radio1B;
    private RadioButton radio1C;
    private RadioButton radio1D;
    private RadioGroup groupradio1;
    private android.widget.TextView tvq2;
    private RadioButton radio2A;
    private RadioButton radio2B;
    private RadioButton radio2C;
    private RadioButton radio2D;
    private RadioGroup groupradio2;
    private android.widget.TextView tvq3;
    private RadioButton radio3A;
    private RadioButton radio3B;
    private RadioButton radio3C;
    private RadioButton radio3D;
    private RadioGroup groupradio3;
    public static QuestionFragment instance;
    private LinearLayout chooselayout;
    private LinearLayout fragment3question;

    /**
     * Create new instance of Fragment class for call in PagerAdapter
     * @param part:     define part of test
     * @param question: define a path of a image or question
     * @return QuestionFragment
     */
    public static QuestionFragment newInstance(int part, String question) {
        //if(instance == null)
        instance = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt("part", part);
        args.putString("question", question);
        instance.setArguments(args);
        return instance;
    }

    /**
     * Singleton Partern to get only one object of this fragment and change value of components inside this fragment
     *
     * @return QuestionFragment
     */
    public static QuestionFragment getInstance() {
        if (instance == null)
            instance = new QuestionFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.fragment_layout);
        ViewGroup.LayoutParams matchParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        String question = getArguments().getString("question", null);
        switch (getArguments().getInt("part")) {
            case 1:
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(matchParams);
                layout.addView(imageView);
                if (question != null) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(question));
                }
                break;
            default:
                LinearLayout subLayout = (LinearLayout) inflater.inflate(R.layout.fragment_3questions, container, false);
                this.groupradio3 = (RadioGroup) subLayout.findViewById(R.id.group_radio3);
                this.radio3D = (RadioButton) subLayout.findViewById(R.id.radio3D);
                this.radio3C = (RadioButton) subLayout.findViewById(R.id.radio3C);
                this.radio3B = (RadioButton) subLayout.findViewById(R.id.radio3B);
                this.radio3A = (RadioButton) subLayout.findViewById(R.id.radio3A);
                this.tvq3 = (TextView) subLayout.findViewById(R.id.tv_q3);
                this.groupradio2 = (RadioGroup) subLayout.findViewById(R.id.group_radio2);
                this.radio2D = (RadioButton) subLayout.findViewById(R.id.radio2D);
                this.radio2C = (RadioButton) subLayout.findViewById(R.id.radio2C);
                this.radio2B = (RadioButton) subLayout.findViewById(R.id.radio2B);
                this.radio2A = (RadioButton) subLayout.findViewById(R.id.radio2A);
                this.tvq2 = (TextView) subLayout.findViewById(R.id.tv_q2);
                this.groupradio1 = (RadioGroup) subLayout.findViewById(R.id.group_radio1);
                this.radio1D = (RadioButton) subLayout.findViewById(R.id.radio1D);
                this.radio1C = (RadioButton) subLayout.findViewById(R.id.radio1C);
                this.radio1B = (RadioButton) subLayout.findViewById(R.id.radio1B);
                this.radio1A = (RadioButton) subLayout.findViewById(R.id.radio1A);
                this.tvq1 = (TextView) subLayout.findViewById(R.id.tv_q1);
                layout.addView(subLayout, matchParams);
                setQuestion(question.replaceAll("(?m)^\\s", "").split("\\n"));
                break;
        }
        return rootView;
    }

    public void setImageView(String path){
        File file = new File(path);
        if (file.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        }
    }
    public void setQuestion(String[] questions){
        try {
            tvq1.setText(questions[0]);
            radio1A.setText(questions[1]);
            radio1B.setText(questions[2]);
            radio1C.setText(questions[3]);
            radio1D.setText(questions[4]);
            tvq2.setText(questions[5]);
            radio2A.setText(questions[6]);
            radio2B.setText(questions[7]);
            radio2C.setText(questions[8]);
            radio2D.setText(questions[9]);
            tvq3.setText(questions[10]);
            radio3A.setText(questions[11]);
            radio3B.setText(questions[12]);
            radio3C.setText(questions[13]);
            radio3D.setText(questions[14]);
        }catch (Exception e){
            Log.e("Exception", "QuestionFragment");
        }
    }
}
