package com.mycompany.app.json;

import com.google.gson.Gson;

/**
 * Created by admin on 19.06.17.
 */



public class Req {


    Request req;
    public Req(int seq, String cmd, String[] params ) {
        this.req = new Request(seq, cmd, params);

    }


    public String toJson(){
        Gson gson = new Gson();
        String res = gson.toJson(this);


        return res;

    }

        public static Req fromJson(String string){
            Gson gson = new Gson();
            Req res = gson.fromJson(string, Req.class);
            return res;


    }
    public int getSeq(){

        return req.seq;

    }

    public String getCmd(){

        return req.cmd;

    }

    public String[] getParams(){
        return req.params;


    }

private  class Request {

    private int seq;
    private String cmd;
    private String[] params;


public Request(int seq, String cmd, String[] params){
    this.seq = seq;
    this.cmd = cmd;
    this.params = params;


}









}
}