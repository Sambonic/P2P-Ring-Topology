package Classes;

import GUI.CoordinatorControl;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Coordinator extends Node implements Serializable {

    private Ring ringNetwork;
    private Queue<Node> joining;
    private CoordinatorControl cn;

    public Coordinator() {
        this.joining = null;
        this.ringNetwork = null;
        this.cn = null;
    }

    public Coordinator(int hostID, String name) throws IOException {
        super(hostID, name);
        this.joining = new LinkedList<>();
        this.ringNetwork = new Ring();
        this.cn = null;
    }

    public Ring getRingNetwork() {
        return ringNetwork;
    }

    public void setRingNetwork(Ring ringNetwork) {
        this.ringNetwork = ringNetwork;
    }

    public Queue<Node> getJoining() {
        return joining;
    }

    public void setJoining(Queue<Node> joining) {
        this.joining = joining;
    }

    public CoordinatorControl getCn() {
        return cn;
    }

    public void setCn(CoordinatorControl cn) {
        this.cn = cn;
    }

    public void addJoin(Node n) {
        this.joining.add(n);
        this.refresh();
    }

    public void addToNetwork(Node n) {
        this.ringNetwork.addNode(n);
        this.joining.remove(n);
        this.refresh();
    }

    public void leaveJoin(Node n) {
        this.joining.remove(n);
        this.refresh();

    }

    public void leaveNetwork(Node n) {
        this.ringNetwork.removeNode(n);
        if (this.cn != null) {
            this.cn.nodeLeft(n);
            this.refresh();
        }

    }

    public void refresh() {
        if (this.cn != null) {
            this.cn.refreshCount();
            this.cn.refreshLists();
            this.refreshNodeList();
        }
    }

    public void refreshNodeList() {
        for (Node n : this.ringNetwork.getRing()) {
            if (n.getNg() != null) {
                n.getNg().refreshNodeList();
            }
        }
    }
}
