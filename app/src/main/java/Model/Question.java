package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Question.java - A simple model class for instantiate instance for each question
 * @author Nguyen Trung Cong on 9/22/2015.
 * @version 1.0
 */
public class Question implements Parcelable {
    private int id;
    private String audio;
    private String question;
    private String answer;
    private String transcript;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.audio);
        dest.writeString(this.question);
        dest.writeString(this.answer);
        dest.writeString(this.transcript);
    }

    public Question() {
    }

    protected Question(Parcel in) {
        this.id = in.readInt();
        this.audio = in.readString();
        this.question = in.readString();
        this.answer = in.readString();
        this.transcript = in.readString();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
