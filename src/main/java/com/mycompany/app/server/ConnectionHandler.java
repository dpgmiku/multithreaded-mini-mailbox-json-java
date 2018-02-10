package com.mycompany.app.server;

import com.mycompany.app.json.Req;
import com.mycompany.app.json.Res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by admin on 04.07.17.
 */
public class ConnectionHandler implements Runnable {


    //EmailServer emailServer;
    Session session;
    Socket socket;
    public ConnectionHandler(Socket socket){
       // this.emailServer = emailServer;
        this.session = session;
        this.socket = socket;
        session = new Session(socket);

                //emailServer.getCsocket();


    }

    @Override
    public void run() {
        try {

            Res res = new Res(200, 99999,"Welcome to this server. Please log in".split(" "));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(res.toJson());
            while(true) {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                String incomingMessage = in.readLine();
                System.out.println(incomingMessage);
                Req req = Req.fromJson(incomingMessage);
                session.setReq(req);
                out.println(session.giveResponse().toJson());

            }


            // csocket.close();;


        } catch (IOException e) {
            System.out.println("Connection lost with the client");
            System.out.println(e);


        }
    }
}
