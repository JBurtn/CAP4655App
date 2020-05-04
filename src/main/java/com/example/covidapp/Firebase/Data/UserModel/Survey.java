package com.example.covidapp.Firebase.Data.UserModel;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Survey implements Serializable{
    //Queue
    public ArrayList<SurveyQuestion> survey = new ArrayList<>();
    public Survey(){
    }
    public Survey(ArrayList<SurveyQuestion> survey){
        this.survey = survey;
    }

    public ArrayList<SurveyQuestion> getSurvey() {
        return survey;
    }

    public void setSurvey(ArrayList<SurveyQuestion> survey) {
        this.survey = survey;
    }

    public void addQuestion(int Resid, String result){
        survey.add(new SurveyQuestion(Resid, result));
    }
    @Override
    public String toString(){
        String result = "";
        for(SurveyQuestion question: survey)
            result = result.concat(question.toString() + "/n");
        return result;
    }
    private void writeObject(@NotNull ObjectOutputStream io) throws IOException {
        io.writeObject(survey);
    }
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        try{
            survey = (ArrayList<SurveyQuestion>) in.readObject();
        }
        catch (ClassCastException e){
            Log.d("Class", e.getMessage());
        }
    }
}
