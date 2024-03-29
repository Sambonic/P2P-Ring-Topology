package Classes;

import GUI.NodeGUI;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node implements Runnable {

    private int hostID;
    private String name;
    private Node successor;
    private Socket cSocket;
    private Socket c2Socket;
    private ServerSocket sSocket;
    private ArrayList<String> messages = new ArrayList<>();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private NodeGUI ng;

    public Node() {
    }

    
    public Node(int hostID, String name) throws IOException {
        this.hostID = hostID;
        this.name = name;
        this.successor = null;
    }

    public int getHostID() {
        return hostID;
    }

    public void setHostID(int hostID) {
        this.hostID = hostID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        this.successor = successor;
    }

    public Socket getcSocket() {
        return cSocket;
    }

    public void setcSocket(Socket cSocket) throws IOException {
        if (this.successor != null) {
            this.cSocket = new Socket("localhost", this.successor.getHostID());
        }
    }

    public void setcSocket() throws IOException {
        if (this.successor != null) {
            this.cSocket = new Socket("localhost", this.successor.getHostID());
        }
    }

    public ServerSocket getsSocket() {
        return sSocket;
    }

    public void setsSocket(ServerSocket sSocket) throws IOException {
        this.sSocket = new ServerSocket(this.getHostID());
    }

    public final void setsSocket() throws IOException {
        this.sSocket = new ServerSocket(this.getHostID());
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public NodeGUI getNg() {
        return ng;
    }

    public void setNg(NodeGUI ng) {
        this.ng = ng;
    }

    public synchronized void sendMessage(Message message) throws IOException {
        if (this.cSocket == null || this.cSocket.isClosed()) {
            this.setcSocket();
        }
        //Client Side (To send message to Successor or specific port) 
        if (this.getSuccessor() != null) {
            this.out = new ObjectOutputStream(this.cSocket.getOutputStream());
            out.writeObject(message);
            System.out.println("[ " + this.getName() + " ] sent " + message.getMessage());

        } else {
            System.err.println("Error: Cannot send message without a successor node.");
        }
    }
    
    //Encrypt and Decrypt using target port number
    public String encryptMessage(String message, int targetPort) {
        int zeroes = Integer.toString(targetPort).length();
        int subtractor = Math.max(1, (int) Math.round(targetPort / Math.pow(10, zeroes)));
        String encryptedMsg = "";
        for (int i = 0; i < message.length(); i++) {
            encryptedMsg += (char) (message.charAt(i) - subtractor);

        }
        return encryptedMsg;
    }

    public void decryptMessage(Message msg) {
        int zeroes = Integer.toString(this.getHostID()).length();
        int subtractor = Math.max(1, (int) Math.round(this.getHostID() / Math.pow(10, zeroes)));
        String decryptedMsg = "";
        for (int i = 0; i < msg.getMessage().length(); i++) {
            decryptedMsg += (char) (msg.getMessage().charAt(i) + subtractor);

        }
        String output = msg.getSender() + " (Private) " + decryptedMsg;
        this.getMessages().add(output);
        this.ng.refreshMessages(output);
    }

    public void updateThreads() throws IOException {
        if (this.out != null) {
            this.out.close();
            System.out.println("Closing output stream");
        }
        if (this.cSocket != null) {
            this.setcSocket();
            System.out.println("Setting up New Client Socket");
        }
        if (this.in != null) {
            this.setsSocket();
            this.c2Socket.close();
            this.c2Socket = this.getsSocket().accept();
            System.out.println("Closing input stream");
        }
    }

    @Override
    public void run() {
        try {
            if (this.getsSocket() == null) {
                this.setsSocket();
            }
            System.out.println(this.getName() + " is lisenting");

            this.c2Socket = this.getsSocket().accept();

            while (true) {
                System.out.println("Connected");
                this.setIn(new ObjectInputStream(this.c2Socket.getInputStream()));
                Message msg = null;
                try {
                    msg = (Message) this.getIn().readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }

                if ((msg.getReceiver() != 0 && msg.getReceiver() == this.getHostID()) || (msg.getSenderID() != 0 && msg.getSenderID() == this.getHostID())) {

                    //Decrypt Message for both sender and reciever only
                    this.decryptMessage(msg);
                } else {
                    String output = msg.getSender() + msg.getMessage();
                    this.getMessages().add(output);
                    System.out.println(msg);
                    if (this.ng != null) {
                        this.ng.refreshMessages(output);
                    }
                }

            }

        } catch (IOException e) {
            System.out.println("Error while " + this.getName() +  " was listening");
        }
    }
}
