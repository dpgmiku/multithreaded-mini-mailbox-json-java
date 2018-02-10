package com.mycompany.app.client;

import com.mycompany.app.json.Req;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


//robert dziuba, skala,  springer


/**
 * Created by admin on 20.06.17.
 */
public final class CommandFactory {
private final HashMap<String, Command> commands;
private final HashMap<String, String> describes;


private CommandFactory() {
   commands = new HashMap<String, Command>();
   describes = new HashMap<>();
}

public void addCommand(final String name, final Command command, final String describe){
    commands.put(name,command);
    describes.put(name,describe);

}

public Req executeCommand(int rndSequence, String input){
    String first = input.split(" ")[0];

if (commands.containsKey(first)){
return commands.get(first).apply(rndSequence, input);
}
else {

System.out.println("Was bedeutet " + input + "? Schreibe hilfe, um alle gültige Commands zu bekommen");
}
return null;
}

public void listCommands(){
    ArrayList<String> namesofCommands = new ArrayList<>(commands.keySet());
    for(int var = 0; var<namesofCommands.size(); var++){
        String name = namesofCommands.get(var);
        System.out.println(name + " - " + describes.get(name));

    }

}


/*Factory pattern*/

public static CommandFactory init(){

   final CommandFactory cf = new CommandFactory();
   cf.addCommand("login", (int rndSequence, String input) -> {
       String[] array = input.split(" ");
       if (array.length <=1){
           System.out.println("Parameter <username> fehlt");
           return null;
       }
       return new Req(rndSequence, input.split(" ")[0], new String[] {input.split(" ")[1]});


   }, " einloggen des Benutzers mit dem Namen <username>");
   cf.addCommand("logout", (int rndSequence, String input) -> {
       return new Req(rndSequence, input.split(" ")[0], new String[]{});


   }, "Beenden und abmelden");
   cf.addCommand("who", (int rndSequence, String input) ->{
       return new Req(rndSequence, input, new String[]{});


   }, "Liste mit verbundenen Benutzern");
   cf.addCommand("time", (int rndSequence, String input)-> {
       return new Req(rndSequence, input.split(" ")[0], new String[]{});


   }, "aktuelle Zeit");
   cf.addCommand("ls", (int rndSequence, String input)->{
       String[] array = input.split(" ");
       if (array.length <=1){
         System.out.println("Parameter <Pfad> fehlt");
return null;
       }
       return new Req(rndSequence, input.split(" ")[0], new String[] {input.split(" ")[1]});


   }, "Dateiliste von <Pfad>");
   cf.addCommand("chat", (int rndSequence, String input)->{
       String[] array = input.split(" ");
       int len = array.length;
       switch (len){
           case 1: {
               System.out.println("Parameter <message> und <username> fehlt");
               return null;
           }
               case 2: {
                   System.out.println("Parameter <message> fehlt");
                   return null;
               }


           }

       String[] params = Arrays.copyOfRange(array, 1, len);
       return new Req(rndSequence, input.split(" ")[0], params);


   }, "sendet <message> an < username >");
   cf.addCommand("notify", (int rndSequence, String input)->{
       String[] params = input.split(" ");
       if (params.length <=1){
         System.out.println("<message> parameter fehlt");
         return null;
       }
       String[] message = Arrays.copyOfRange(params, 1, params.length);

       return new Req(rndSequence, input.split(" ")[0], message);


   }, "benachrichtigt alle eingeloggten Benutzer.");
   cf.addCommand("note", (int rndSequence, String input)->{
       String[] params = input.split(" ");
       if (params.length <=1){
           System.out.println("<text> parameter fehlt");
           return null;
       }
       params = Arrays.copyOfRange(params, 1, params.length);

       return new Req(rndSequence, input.split(" ")[0], params);


   }, "hinterlegt eine Notiz <text> für alle Benutzer.");
   cf.addCommand("notes", (int rndSequence, String input)->{

       return new Req(rndSequence, input.split(" ")[0], new String[]{});


   }, "alle Notizen werden angezeigt.");

return cf;
}


}
