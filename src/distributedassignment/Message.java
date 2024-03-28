/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distributedassignment;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private String sender;
    private int senderID;
    private int receiver;

    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public Message(String message) {
        this.message = message;
        this.sender = null;
        this.receiver = 0;
    }

    public Message(String message, String sender, int senderID, int receiver) {
        this.message = message;
        this.sender = sender;
        this.senderID = senderID;
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return "[ " + this.sender + " ]: ";
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

}
