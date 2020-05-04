package com.example.covidapp.Firebase.Data.UserModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UserModel implements Serializable{


    private String first_name;
    private String last_name;
    private String date_of_birth;
    private String address;
    private Survey serve;


    public UserModel(){}

    public UserModel(String first_name, String last_name, String date_of_birth, String address, Survey serve){

        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.serve = serve;
    }
    @NotNull
    public String toString(){

        String s = first_name + " " + last_name + "\n" +
                address + "\n" + date_of_birth + "\n";
        if(serve != null)
            return s + serve.toString();
        return s;
    }
    public boolean checkUserdata(){
        //perform standard Regex
        boolean result = false;
        String[] data = {first_name,last_name,address};
        for(String d: data){
            if(d == null || d.equals(""))
                return false;
            else
                result = d.matches("^[a-zA-Z0-9]+$");
        }
//        result = date_of_birth.matches("^(0[1-9]|1[0-2])/([0-2][0-9])|(3[0-1])/[0-9]{2,4}$");
        return result;
    }
    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        first_name = aInputStream.readUTF();
        last_name = aInputStream.readUTF();
        date_of_birth = aInputStream.readUTF();
        address = aInputStream.readUTF();

        serve = (Survey) aInputStream.readObject();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.writeUTF(first_name);
        aOutputStream.writeUTF(last_name);
        aOutputStream.writeUTF(date_of_birth);
        aOutputStream.writeUTF(address);
        aOutputStream.writeObject(serve);
    }
    public String getFirst_name() {
        return first_name;
    }

    public String getAddress() {
        return address;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getLast_name() {
        return last_name;
    }

    public Survey getSurvey() {
        return serve;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setServe(Survey serve) {
        this.serve = serve;
    }
}
