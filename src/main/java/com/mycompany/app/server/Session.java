package com.mycompany.app.server;

import com.mycompany.app.client.EmailClient;
import com.mycompany.app.json.Req;
import com.mycompany.app.json.Res;
import com.sun.security.ntlm.Server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by admin on 20.06.17.
 */
public class Session {

    public void setReq(Req req) {
        this.req = req;
    }

    private Req req;
    private User user;
    private EmailServer emailServer;
    Socket socket;
    private static HashMap<String, Socket> usernameList = new HashMap<>();
    private static ArrayList<String> notizen = new ArrayList<>();
    public static int messageLifetime = 10 * 60;


    public Session(Socket socket){
       this.socket = socket;
       req = null;
    }




    private boolean loggedIn() {

        return this.user != null;
    }


    public Res giveResponse() {

        String cmd = req.getCmd();
        int seq = req.getSeq();
        String[] params = req.getParams();
        System.out.println(loggedIn());
        if (!(loggedIn())) {

            if (cmd.startsWith("login")) {
                if (usernameList.size() < EmailServer.MAX_CLIENTS) {
                    final String username = req.getParams()[0];
                    if (!userExist(username)) {


                        return loggedsuccess(username);
                    } else {
                        return sendUsernameAlreadyTaken(username);
                    }


                } else {

                    return tooManyConnections();
                }
            } else {

                return notAuthorized();
            }
        }
        else {
            switch (cmd) {
                case "login":
                    return loginAlreadyDone();
                case "logout":
                    return logout();
                case "who":
                    return sendWho();
                case "time":
                    return sendTime();
                case "ls":
                    return sendLs(params);
                case "chat":
                    return sendChat(params);
                case "notify":
                    return sendNotify(params);
                case "note":
                    return sendNote(params);
                case "notes":
                    return giveNotes();
                default:
                    return notImplemented();
            }

        }

    }

    private Res giveNotes() {
        if (notizen.size() == 0){

            return new Res(400, req.getSeq(), "Es gibt keine aktuelle Notizen".split(" "));
        }
        String alle = String.join("\n", notizen);


        return new Res(200, req.getSeq(), alle.split(" "));

    }

    private Res sendNote(String[] params) {

        List<String> notizenAsString = Arrays.asList(params);

        String note = String.join(" ", notizenAsString);
        notizen.add(note);


        new java.util.Timer().schedule(new java.util.TimerTask() {
                                           @Override
                                           public void run() {
                                               notizen.remove(note);
                                           }
                                       },
                messageLifetime*20
        );
        return new Res(200, req.getSeq(), ("Eine Notiz wurde erfolgreich für alle Benutzer hinterlegt").split(" "));

    }

    private Res sendNotify(String[] params) {

        try {
            Collection<Socket> values = usernameList.values();
            for (Socket socket : values) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Res messageTo = new Res(201, req.getSeq(), params);

                out.println(messageTo.toJson());

            }

        }
        catch(IOException e){
            return new Res(400, req.getSeq(), ("Notify wurde nicht versendet").split(" "));

        }
        return new Res(201, req.getSeq(), ("Notify wurde erfolgreich an allen eingeloggten Nutzer versendet").split(" "));

    }

    private Res sendChat(String[] params) {
        String user = params[0];
        StringBuilder message = new StringBuilder();
        for(int i =1; i<params.length; i++){
            message.append(params[i] + " ");


        }



        if (usernameList.get(user)==null){

            return new Res(400, req.getSeq(), "This user do not exist on our server".split(" "));
        }
        try {

                PrintWriter out = new PrintWriter(usernameList.get(user).getOutputStream(), true);
                Res messageTo = new Res(201, req.getSeq(), (this.user.getName() + " : " + message.toString()).split(" "));

        out.println(messageTo.toJson());



        }
        catch(IOException e){
            return new Res(400, req.getSeq(), ("Message: " + message +" wurde an nicht " + user+ " versendet").split(" "));

        }

        return new Res(200, req.getSeq(), ("Message: " + message +" wurde an " + user+ " versendet").split(" "));
    }

    private Res sendWho() {

        return new Res(200, req.getSeq(), usernameList.keySet().toArray(new String[usernameList.size()]));
    }

    private Res loggedsuccess(String username) {
        this.user = new User(username);
        usernameList.put(username,socket);
      return new Res(200, req.getSeq(), "Welcome to our server. You are now logged in".split(" "));

    }

    private Res loginAlreadyDone() {
  return new Res(400, req.getSeq(), "You are already logged in, you dummy ;-)".split(" " ));

    }

    private Res sendUsernameAlreadyTaken(String username) {

        return new Res(400, req.getSeq(), (username + " already taken, please take another one").split(" "));
    }

    private Res tooManyConnections() {

        return new Res(503, req.getSeq(), ("Max clients of " + EmailServer.MAX_CLIENTS + " reached. PLease wait until someone will disconnect from the server. Thx").split(" "));
    }

    private Res notAuthorized() {

        return new Res(401, req.getSeq(), "you are not authorized, please first login!".split(" "));
    }

    private Res notImplemented() {

        return new Res(501, req.getSeq(), "not implemented".split(" "));
    }

    private Res logout() {

        Thread.currentThread().interrupt();
        usernameList.remove(this.user.getName());
        Res res = new Res(204, req.getSeq(), (user.getName() + " has been logout").split(" "));
        this.user = null;
        EmailServer.getThreadLis().remove(this);
        System.out.println("User logged out");
        return res;

    }

    private Res sendTime(){
        Date date = new Date();
        return new Res(200, req.getSeq(), date.toString().split(" "));



}




    private Res sendLs(String[] params) {
         File file = new File(params[0]);
            if (file.exists()) {
                if (file.isDirectory()) {
                    String[] ls = file.list();
                    List<String> lsArray = Arrays.asList(ls);
                    String allFiles = String.join("\n", lsArray);

                    return new Res(200, req.getSeq(), allFiles.split(" "));
                } else {
                    return new Res(404 ,req.getSeq(), "is not a directory".split(" "));
                }
            }
                return new Res(404, req.getSeq(), "directory not found".split(" "));

        }



private boolean userExist(String username){
return usernameList.containsKey(username);

}


}



