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
    private static ArrayList<SurveyQuestion> survey = new ArrayList<>();
    public Survey(){
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
    private static class SurveyQuestion implements Serializable{
        String result;
        int Resid;

        public SurveyQuestion(){}

        SurveyQuestion(int Resid, String result){
            this.Resid = Resid;
            this.result = result;
        }

        public int getResid() {
            return Resid;
        }

        public String getResult() {
            return result;
        }

        public void setResid(int resid) {
            Resid = resid;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String toString(){
            return Resources.getSystem().getString(Resid) + "\t" + result;
        }
        private void writeObject(@NotNull ObjectOutputStream io) throws IOException {
            io.writeInt(Resid);
            io.writeUTF(result);
        }
        private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
            try{
                Resid = in.readInt();
                result = in.readUTF();
            }
            catch (ClassCastException e){
                Log.d("Class", e.getMessage());
            }
        }
    }
}
