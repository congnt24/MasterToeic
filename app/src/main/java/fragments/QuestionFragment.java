package fragments;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.example.cong.audiocong.AudioCong;
import com.ptpmcn.cong.mastertoeiclc.MainActivity;
import com.ptpmcn.cong.mastertoeiclc.Part3Activity;
import com.ptpmcn.cong.mastertoeiclc.R;

import java.io.File;

/**
 * Created by cong on 9/22/2015.
 */
public class QuestionFragment extends Fragment {
    private static final int QUESTIONNUMBER = 30;
    private ImageView imageView;
    private android.widget.TextView tvq1;
    private RadioButton radioes[][] = new RadioButton[3][4];
    private RadioGroup groupradio[] = new RadioGroup[3];
    private android.widget.TextView tvq2;
    private android.widget.TextView tvq3;
    public static QuestionFragment instance;
    private View rootView;
    public String[] listResult = new String[QUESTIONNUMBER];
    private int answerAmount=4;
    private int part;

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
        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.fragment_layout);
        ViewGroup.LayoutParams matchParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        String question = getArguments().getString("question", null);
        part = getArguments().getInt("part");
        switch (part) {
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
                this.groupradio[2] = (RadioGroup) subLayout.findViewById(R.id.group_radio3);
                this.radioes[2][3] = (RadioButton) subLayout.findViewById(R.id.radio3D);
                this.radioes[2][2] = (RadioButton) subLayout.findViewById(R.id.radio3C);
                this.radioes[2][1] = (RadioButton) subLayout.findViewById(R.id.radio3B);
                this.radioes[2][0] = (RadioButton) subLayout.findViewById(R.id.radio3A);
                this.tvq3 = (TextView) subLayout.findViewById(R.id.tv_q3);
                this.groupradio[1] = (RadioGroup) subLayout.findViewById(R.id.group_radio2);
                this.radioes[1][3] = (RadioButton) subLayout.findViewById(R.id.radio2D);
                this.radioes[1][2] = (RadioButton) subLayout.findViewById(R.id.radio2C);
                this.radioes[1][1] = (RadioButton) subLayout.findViewById(R.id.radio2B);
                this.radioes[1][0] = (RadioButton) subLayout.findViewById(R.id.radio2A);
                this.tvq2 = (TextView) subLayout.findViewById(R.id.tv_q2);
                this.groupradio[0] = (RadioGroup) subLayout.findViewById(R.id.group_radio1);
                this.radioes[0][3] = (RadioButton) subLayout.findViewById(R.id.radio1D);
                this.radioes[0][2] = (RadioButton) subLayout.findViewById(R.id.radio1C);
                this.radioes[0][1] = (RadioButton) subLayout.findViewById(R.id.radio1B);
                this.radioes[0][0] = (RadioButton) subLayout.findViewById(R.id.radio1A);
                this.tvq1 = (TextView) subLayout.findViewById(R.id.tv_q1);
                //If PART 2: remove D answer
                if (part==2){
                    answerAmount = 3;
                    radioes[0][3].setVisibility(View.GONE);
                    radioes[1][3].setVisibility(View.GONE);
                    radioes[2][3].setVisibility(View.GONE);
                }
                layout.addView(subLayout, matchParams);
                Log.d("InitQuestion", "Q: "+question+ "- COunt: "+ Part3Activity.getInstance().count);
                initQuestion(question.replaceAll("(?m)^\\s", "").split("\\n"), Part3Activity.getInstance().count);
                //If test, we will able to check answer
                if (!Part3Activity.getInstance().isReviewMode) {//Is test mode
                    groupradio[0].setOnCheckedChangeListener(onChecked);
                    groupradio[1].setOnCheckedChangeListener(onChecked2);
                    groupradio[2].setOnCheckedChangeListener(onChecked3);
                }else{
                    listResult = Part3Activity.getInstance().listResult;
                    autoCheckRadioButton(Part3Activity.getInstance().count);
                }
                break;
        }
        return rootView;
    }

    private RadioGroup.OnCheckedChangeListener onChecked = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton btn= (RadioButton) rootView.findViewById(checkedId);
            try{
                if (btn.getText()!=null)
                    listResult[Part3Activity.getInstance().count*3] = String.valueOf(btn.getText().charAt(0));
                Log.d("Part3: ", "Part3: "+String.valueOf(btn.getText().charAt(0)));
            }catch (Exception e){

            }
        }
    };
    private RadioGroup.OnCheckedChangeListener onChecked2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton btn= (RadioButton) rootView.findViewById(checkedId);
            try{
                if (btn.getText()!=null)
                    listResult[Part3Activity.getInstance().count*3+1] = String.valueOf(btn.getText().charAt(0));
            }catch (Exception e){

            }
        }
    };
    private RadioGroup.OnCheckedChangeListener onChecked3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton btn= (RadioButton) rootView.findViewById(checkedId);
            try{
                if (btn.getText()!=null)
                    listResult[Part3Activity.getInstance().count*3+2] = String.valueOf(btn.getText().charAt(0));
            }catch (Exception e){

            }
        }
    };

    public void setImageView(String path){
        File file = new File(path);
        if (file.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        }
    }
    public void initQuestion(String[] questions, int count){
        try {
            for (int j = 0; j < answerAmount; j++) {
                radioes[0][j].setText((char)(65+j)+". "+questions[j+1]);
                radioes[1][j].setText((char)(65+j)+". "+questions[j+6]);
                radioes[2][j].setText((char) (65 + j) + ". " + questions[j + 11]);
            }
            tvq1.setText(questions[0]);
            tvq2.setText(questions[5]);
            tvq3.setText(questions[10]);
            groupradio[0].clearCheck();
            groupradio[1].clearCheck();
            groupradio[2].clearCheck();
            if (Part3Activity.getInstance().isReviewMode){
                autoCheckRadioButton(count);
            }
        }catch (Exception e){
            Log.e("Exception", "QuestionFragment");
        }
    }
    public void clearGroupCheck(){
        try {
            groupradio[0].clearCheck();
            groupradio[1].clearCheck();
            groupradio[2].clearCheck();
        }catch (Exception e){
            Log.d("Exeption", QuestionFragment.class.getName());
        }
    }
    /**
     * After finish testting, user can't review all the test they just taking. In this review mode,
     * this method will be used to check the corrects answer and the answer what user choosed
     * @param i: the count = id of question
     */
    public void autoCheckRadioButton(int i){
        removeAllButtonDrawable();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_action_check_circle);
        drawable.setColorFilter(new LightingColorFilter(Color.GREEN, Color.GREEN));
        for (int j = 0; j < 3; j++) {
            int correct = (int) Part3Activity.getInstance().list.get(i).getAnswer().toUpperCase().charAt(j) - 65;
            if (listResult[i * 3 + j] != null) {
                int usercheck = (int) listResult[i * 3 + j].charAt(0) - 65;// 012
                if (correct != usercheck) {
                    radioes[j][usercheck].setChecked(true);
                    radioes[j][usercheck].setTextColor(Color.RED);
                }
            }
            radioes[j][correct].setButtonDrawable(drawable);
            radioes[j][correct].setTextColor(Color.GREEN);
        }
    }
    /**
     * Set all radio Button to default drawable
     */
    public void removeAllButtonDrawable(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < answerAmount; j++) {
                radioes[i][j].setButtonDrawable(R.drawable.default_radio_button);
                radioes[i][j].setClickable(false);
                radioes[i][j].setTextColor(Color.BLACK);
            }
        }
    }

}
