package com.mycompany.app.json;

import com.google.gson.Gson;
/**
 * Created by admin on 19.06.17.
 */
public class Res {

    Response response;

    public Res(int status, int seq, String[] data) {

        this.response = new Response(status, seq, data);


    }

    public int getStatus() {


        return response.status;
    }

    public int getSeq() {

        return response.seq;
    }

    public String[] getData() {

        return response.data;
    }


    public String toJson() {
        Gson gson = new Gson();
        String res = gson.toJson(this);


        return res;
    }


    public static Res fromJson(String string) {
        Gson gson = new Gson();
        Res res = gson.fromJson(string, Res.class);
        return res;

    }


    private class Response {
        private int status;
        private int seq;
        private String[] data;


        public Response(int status, int seq, String[] data) {

            this.status = status;
            this.seq = seq;
            this.data = data;


        }



    }
}

