package com.mycompany.app.client;

import com.mycompany.app.json.Req;
import com.mycompany.app.json.Res;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * Hello world!
 */
public class EmailClient {

    private static Integer port = 8060;
    private static String ip = "";
    final static CommandFactory cf = CommandFactory.init();
    static Random random = new Random();
    static Integer ranNumb = random.nextInt(1000) + 1;
    static BufferedReader in;
    static PrintWriter out;


    public static void main(String[] args) {

        ip = "localhost";
        try {

            Socket client = new Socket(ip, port);

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            //ip = args[0];
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }

            System.out.println("Simple Email EmailClient");
            cf.listCommands();
            new Thread(() -> {
                getInputFromConsole();
            }).start();

            new Thread(() -> {
                getOutputFromServer();
            }).start();



        } catch (IOException e) {
            System.out.println("Connection lost");
            e.printStackTrace();

        }
    }


    private static void getOutputFromServer() {
        Res res;
        while (true) {
            try {


                res = Res.fromJson(in.readLine());
                System.out.println();
                System.out.println(String.join(" ", res.getData()));
                System.out.print("$>");
            } catch (IOException e) {
                System.out.println("Connection lost to the server");
                e.printStackTrace();
            }

        }

    }

    private static void getInputFromConsole()  {

            Scanner eingabe = new Scanner(System.in);
            while (true) {
                System.out.print("$>");
                String a = eingabe.nextLine().toLowerCase().trim();
                if (a.startsWith("hilfe")) {
                    cf.listCommands();
                } else {
                    Req req = cf.executeCommand(ranNumb, a);
                    if (req != null) {
                        String reqToJson = req.toJson();
                        ranNumb++;
                        out.println(reqToJson);

                    }

                }


            }

    }
}
