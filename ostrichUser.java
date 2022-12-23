import java.io.*;
import java.util.*;

class master{
    public static void master(){
        boolean login = false;
        user a;
        Scanner sc= new Scanner(System.in);
        while(!login){
            System.out.println("Enter your Username :");
            String name = sc.nextLine();
            a = new user(name);

            if(a.getStatus())
                login = true;
            
            System.out.println("Login Success");
        
            while(true){
                System.out.println("1.Send \t 2.refresh");
                int k = sc.nextInt();
                switch(k){
                    case 3:
                        System.out.println(a.address);
                        break;
                    case 1:
                        //send a message!
                        System.out.println("Enter destination address :");
                        int add2 = sc.nextInt();
                        System.out.println("Enter The message :");
                        sc.nextLine();
                        String mess = sc.nextLine();
                        a.send(add2,mess);
                    case 2:
                        //recieve message!
                        try{
                            File recieveBox = new File(name+".recieve");
                            Scanner comms = new Scanner(recieveBox);
                            while(comms.hasNext()){
                                String Recieved = comms.next();
                                System.out.println(Recieved);
                            }
                        }
                        catch(Exception e){
                            //none
                        }
                        break;
                }
            }
        }
        

    }

    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int a = 1;
        while(a==1){
            System.out.println("1.login \t 2.exit");
            int k = sc.nextInt();
            switch(k){
            case 1:
                master();
                System.out.println("You've logged out");
                break;
            case 2:
                break;
            }
        }
    }
}

class buffer{
    private boolean status = false;
    private boolean userExist = true;
    private String name;
    public boolean configure(String Name){
        this.name = Name;
        try{
            File CU = new File("control.txt");
            if(!CU.exists()){
                System.out.println("Couldn't connect to OStrich");
                return false;
            }

            CU = new File(Name + ".config");
            if(CU.exists()){
                System.out.println("User already exist !");
                return false;
            }

            PrintWriter writer = new PrintWriter("control.txt");
            writer.print("b"+Name+"\n");
            writer.close();
            return true;
        }
        catch(Exception e){
            System.out.println("Error while connecting to Ostrich!");
        }
        return false;
    }

    public int getAddress(){
        try{
            File user = new File(name+".config");
            Scanner comms = new Scanner(user);
            while(comms.hasNext()){
                String address = comms.next();
                return Integer.parseInt(address);
            }
        }
        catch(Exception e){
            System.out.println("Error while getting the address!");
        }
        return 0;
    }

    public String formatInt(int a){
        String fin = Integer.toString(a);
        while(fin.length()<4){
                fin = '0'+fin;
        }
        return fin;
    }

    public int sendMess(int address1,int address2){
        try{
            PrintWriter writer = new PrintWriter("control.txt");
            writer.print("a"+formatInt(address1)+formatInt(address2)+"\n");
            writer.close();
            return 1;
        }
        catch(Exception e){
            System.out.println("Error while sending the message!");
        }
        return 0;
    }
}

class user{
    private boolean status = false;
    public int address = 1;
    buffer s = new buffer();
    private String Name;

    user(String name){
        if(!s.configure(name)){
            status = false;
        }
        else{
            Name = name;
            address = s.getAddress();
            status = true;
        }
    }

    public boolean getStatus(){
        return status;
    }

    public int send(int add2,String mess){
        //prepare send box;
        try{
            PrintWriter writer = new PrintWriter(Name+".send");
            writer.print(mess+"\n");
            writer.close();
            s.sendMess(address,add2);     
            return 1;
        }
        catch(Exception e){
            System.out.println("Error while preparing send message!");
        }
        return 0;
    }
}

