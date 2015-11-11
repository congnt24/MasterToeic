package fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ptpmcn.cong.dictionary.Dictionary;
import com.ptpmcn.cong.mastertoeiclc.IMenuHandler;
import com.ptpmcn.cong.mastertoeiclc.Part3Activity;
import com.ptpmcn.cong.mastertoeiclc.R;
import com.ptpmcn.cong.mastertoeiclc.SelectableTextView;

import org.w3c.dom.Text;

import java.io.File;


import apv.congnt24.customviews.AnswerView;

/**
 * Created by cong on 9/22/2015.
 */
public class QuestionFragment extends Fragment implements IMenuHandler {
    private static final int QUESTIONNUMBER = 30;
    private ImageView imageView;
    private SelectableTextView tvq1;
    private SelectableTextView radioes[][] = new SelectableTextView[3][4];
    private AnswerView answerViews[] = new AnswerView[3];
    private SelectableTextView tvq2;
    private SelectableTextView tvq3;
    public static QuestionFragment instance;
    private View rootView;
    public String[] listResult = new String[QUESTIONNUMBER];
    private int answerAmount=4;
    private int part;
    private TextView sen_count;

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
                sen_count = (TextView) subLayout.findViewById(R.id.sen_count);
                answerViews[0] = (AnswerView) subLayout.findViewById(R.id.answer_view1);
                answerViews[1] = (AnswerView) subLayout.findViewById(R.id.answer_view2);
                answerViews[2] = (AnswerView) subLayout.findViewById(R.id.answer_view3);
                this.radioes[2][3] = (SelectableTextView) subLayout.findViewById(R.id.radio3D);
                this.radioes[2][2] = (SelectableTextView) subLayout.findViewById(R.id.radio3C);
                this.radioes[2][1] = (SelectableTextView) subLayout.findViewById(R.id.radio3B);
                this.radioes[2][0] = (SelectableTextView) subLayout.findViewById(R.id.radio3A);
                this.tvq3 = (SelectableTextView) subLayout.findViewById(R.id.tv_q3);
                this.radioes[1][3] = (SelectableTextView) subLayout.findViewById(R.id.radio2D);
                this.radioes[1][2] = (SelectableTextView) subLayout.findViewById(R.id.radio2C);
                this.radioes[1][1] = (SelectableTextView) subLayout.findViewById(R.id.radio2B);
                this.radioes[1][0] = (SelectableTextView) subLayout.findViewById(R.id.radio2A);
                this.tvq2 = (SelectableTextView) subLayout.findViewById(R.id.tv_q2);
                this.radioes[0][3] = (SelectableTextView) subLayout.findViewById(R.id.radio1D);
                this.radioes[0][2] = (SelectableTextView) subLayout.findViewById(R.id.radio1C);
                this.radioes[0][1] = (SelectableTextView) subLayout.findViewById(R.id.radio1B);
                this.radioes[0][0] = (SelectableTextView) subLayout.findViewById(R.id.radio1A);
                this.tvq1 = (SelectableTextView) subLayout.findViewById(R.id.tv_q1);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        radioes[i][j].init(this);
                    }
                }
                tvq1.init(this);
                tvq2.init(this);
                tvq3.init(this);

                //If PART 2: remove D answer
                if (part==2){
                    answerAmount = 3;
                    radioes[0][3].setVisibility(View.GONE);
                    radioes[1][3].setVisibility(View.GONE);
                    radioes[2][3].setVisibility(View.GONE);
                    for (int i = 0; i < 3; i++) {
                        answerViews[i].resize(3);
                    }
                }
                layout.addView(subLayout, matchParams);
                initQuestion(question.replaceAll("(?m)^\\s", "").split("\\n"), Part3Activity.getInstance().count);
                //If test, we will able to check answer
                if (!Part3Activity.getInstance().isReviewMode) {//Is test mode
                    answerViews[0].setOnAnswerChange(new AnswerView.OnAnswerChange() {
                        @Override
                        public void onAnswerChange(AnswerView view, int index) {
                            listResult[Part3Activity.getInstance().count*3] = String.valueOf((char) (((int) 'A') + index));
                        }
                    });
                    answerViews[1].setOnAnswerChange(new AnswerView.OnAnswerChange() {
                        @Override
                        public void onAnswerChange(AnswerView view, int index) {
                            listResult[Part3Activity.getInstance().count*3+1] = String.valueOf((char) (((int) 'A') + index));
                        }
                    });
                    answerViews[2].setOnAnswerChange(new AnswerView.OnAnswerChange() {
                        @Override
                        public void onAnswerChange(AnswerView view, int index) {
                            listResult[Part3Activity.getInstance().count*3+2] = String.valueOf((char) (((int) 'A') + index));
                        }
                    });
                }else{//review mode
                    listResult = Part3Activity.getInstance().listResult;
                    autoCheckRadioButton(Part3Activity.getInstance().count);
                }

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
    public void initQuestion(String[] questions, int count){
        sen_count.setText("Câu "+(count+1)+":");
        try {
            for (int j = 0; j < answerAmount; j++) {
                radioes[0][j].setText((char)(65+j)+". "+questions[j+1]);
                radioes[1][j].setText((char)(65+j)+". "+questions[j+6]);
                radioes[2][j].setText((char) (65 + j) + ". " + questions[j + 11]);
            }
            tvq1.setText(questions[0]);
            tvq2.setText(questions[5]);
            tvq3.setText(questions[10]);
            clearAllAnswer();
            if (Part3Activity.getInstance().isReviewMode){
                autoCheckRadioButton(count);
            }
        }catch (Exception e){
            Log.e("Exception", "QuestionFragment");
        }
    }
    public void clearGroupCheck(){
        try {
            clearAllAnswer();
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
        for (int j = 0; j < 3; j++) {
            answerViews[j].disableAll();
            int correct = (int) Part3Activity.getInstance().list.get(i).getAnswer().toUpperCase().charAt(j) - 65;
            if (listResult[i * 3 + j] != null) {
                int usercheck = listResult[i * 3 + j]==null?-1:(int) listResult[i * 3 + j].charAt(0) - 65;// 012
                if (usercheck!=-1)
                    answerViews[j].setActiveIndex(usercheck);
            }
            answerViews[j].setTrueAnswer(correct);
        }
    }
    public void clearAllAnswer(){
        answerViews[0].clearAll();
        answerViews[1].clearAll();
        answerViews[2].clearAll();
    }

    @Override
    public void onCompleteSelectTextView(String str) {
        Dictionary.getInstance().showDialogAndAddToHistory(getActivity(), str);
    }
}
