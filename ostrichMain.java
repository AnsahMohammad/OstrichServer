import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
class test{
    public static void main(String[] args){
        System.out.println("Hello world");
        Ostrich serv = new Ostrich();
        while(true){
            serv.process();
        }
    }
}

class Ostrich{
    private boolean controlFlag = false;
    private boolean proceed = true;
    UserMan a = new UserMan();
    Ostrich(){
        System.out.println("Ostrich is Running !");
    }

    public void initprocess(){
        //intitializing the control file
        try{
            File CU = new File("control.txt");
            if(CU.exists()){
                controlFlag = true;
            }
            else{
                System.out.println("control not found, creating control ..");
                try{
                    Formatter f = new Formatter("control.txt");
                    CU = new File("control.txt");
                    controlFlag = true;
                }
                catch(Exception e){
                    System.out.println("control creation failed");
                }
            }
        }
        catch(Exception e){
            System.out.println("An error occured opening the control file");
        }
    }

    public void process(){
        initprocess();
        if(proceed){
            //iterating the control to find commands;
            try{
                File CU = new File("control.txt");
                Scanner comms = new Scanner(CU);
                while(comms.hasNext()){
                    String command = comms.next();
                    if(command.charAt(0) == 'b'){
                        System.out.println("Register user");
                        int id = a.create(command.substring(1,command.length()));
                        a.configure(id);
                        System.out.println("User created !");
                    }
                    else if(command.charAt(0) == 'a'){
                        int ID1 = Integer.parseInt(command.substring(1,5));
                        int ID2 = Integer.parseInt(command.substring(5,command.length()));
                        a.send(ID1,ID2);
                    }
                    else{
                        //ignore;
                    }
                }
                //if there's no more string!
                PrintWriter writer = new PrintWriter("control.txt");
                writer.print("");
                writer.close();
            }
            catch(Exception e){
                System.out.println("Error while processing!");
            }
        }
    }
}

class UserMan{
    private int headCount = 0;
    HashMap<Integer, String> dataBase = new HashMap<Integer, String>();
    public int create(String PATH){
        dataBase.put(headCount, PATH);
        return headCount++;
    }
    public void display(int c){
        System.out.println(dataBase.get(c));
    }

    public void configure(int ID){
        String Path = dataBase.get(ID);
        try{
            String configPath = Path+".config";
            Formatter f = new Formatter(configPath);
            f.format("%d",ID);
            f.close();
            System.out.println("User configured");
        }
        catch(Exception e){
            System.out.println("User configuration failed");
        }
    }

    public void send(int ID1, int ID2){
        //to send message from ID1 to ID2
        try{
            String path1 = dataBase.get(ID1);
            String recievePath = path1+".send";
            File file = new File(recievePath);
            //gathering the message
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");
            String message = sc.next();
            send1(ID2,message);
        }
        catch(Exception e){
            System.out.println("No text found to send!");
        }
    }

    public void send1(int ID,String message){
        try{
            String path = dataBase.get(ID);
            String sendPath = path+".recieve";
            File file = new File(sendPath);
            FileWriter fr = new FileWriter(file, true);
            fr.write(message);
            fr.close();
            System.out.printf("Message successfully sent to %d\n",ID);
        }
        catch(Exception e){
            System.out.printf("Message could not be sent to %d\n",ID);
        }
    }

}