package com.mycompany.app.server;

import com.mycompany.app.json.Req;
import com.mycompany.app.json.Res;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by admin on 20.06.17.
 */
public class EmailServer  {
    final static int PORT_NUMBER = 8060;
    public final static int MAX_CLIENTS = 5;
    static Socket csocket;




    private  static ArrayList<ConnectionHandler> threadLis = new ArrayList<>();


    static BufferedReader in;
    static PrintWriter out;


    public Socket getCsocket() {
        return csocket;
    }

    public EmailServer(Socket csocket) {
        this.csocket = csocket;
    }


    public static void main(String args[]) throws Exception {
        ServerSocket ssock = new ServerSocket(PORT_NUMBER);
        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
            Session session = new Session(sock);
            ConnectionHandler handler = new ConnectionHandler(sock);
            threadLis.add(handler);
            new Thread(handler).start();
        }

    }



    public static ArrayList<ConnectionHandler> getThreadLis() {
        return threadLis;
    }


}
