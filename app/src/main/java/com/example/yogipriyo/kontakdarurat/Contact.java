package com.example.yogipriyo.kontakdarurat;

/**
 * Created by yogipriyo on 24/10/15.
 */
public class Contact {
    //private variables
    int _id;
    String _name;
    String _noid;
    String _bpjs;
    String _mother;
    String _father;
    String _office;
    String _other;

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(int id, String name, String noid, String bpjs,String mother, String father, String office, String other){
        this._id = id;
        this._name = name;
        this._noid = noid;
        this._bpjs = bpjs;
        this._mother = mother;
        this._father = father;
        this._office = office;
        this._other = other;
    }

    // constructor
    public Contact(String name, String noid, String bpjs, String mother, String father, String office, String other){
        this._name = name;
        this._noid = noid;
        this._bpjs = bpjs;
        this._mother = mother;
        this._father = father;
        this._office = office;
        this._other = other;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting identity number
    public String getNoId(){
        return this._noid;
    }

    // setting identity number
    public void setNoId(String noid){
        this._noid = noid;
    }

    // getting bjps
    public String getBpjs(){
        return this._bpjs;
    }

    // setting name
    public void setBpjs(String bpjs){
        this._bpjs = bpjs;
    }

    // getting mother
    public String getMother(){
        return this._mother;
    }

    // setting mother
    public void setMother(String mother){
        this._mother = mother;
    }

    // getting father
    public String getFather(){
        return this._father;
    }

    // setting father
    public void setFather(String father){
        this._father = father;
    }

    // getting office
    public String getOffice(){
        return this._office;
    }

    // setting office
    public void setOffice(String office){
        this._office = office;
    }

    // getting other
    public String getOther(){
        return this._other;
    }

    // setting other
    public void setOther(String other){
        this._other = other;
    }
}
