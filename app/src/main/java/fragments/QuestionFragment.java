package fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ptpmcn.cong.mastertoeiclc.R;

import java.io.File;

/**
 * Created by cong on 9/22/2015.
 */
public class QuestionFragment extends Fragment {
    private ImageView imageView;
    private static QuestionFragment instance;

    public static QuestionFragment getInstance(int i){
        //if(instance == null)
        instance = new QuestionFragment();
        return instance;
    }
    private QuestionFragment(){};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.iv_question);
        return rootView;
    }
    public void setImageView(String path){
        File file = new File(path);
        if (imageView == null) {
            imageView = (ImageView) getActivity().findViewById(R.id.iv_question);
        }
        if (file.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        }
    }
}
