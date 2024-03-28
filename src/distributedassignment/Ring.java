package distributedassignment;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ring {

    public LinkedList<Node> ring;

    public Ring() {
        this.ring = new LinkedList<>();
    }

    public LinkedList<Node> getRing() {
        return ring;
    }

    public void setRing(LinkedList<Node> ring) {
        this.ring = ring;
    }

    public void addNode(Node n) {
        this.ring.add(n);
        this.updateRing();

    }

    public void addNode(Node n, int position) throws IOException {
        if (position == this.ring.size()) {
            this.addNode(n);
        } else {
            System.out.println("bruh");
            this.ring.set(position, n);
        }
        this.updateRing();
    }

    public void removeNode(Node n) {
        this.ring.remove(n);
        this.updateRing();

    }

    public void updateRing() {
        System.out.println("Updating Ring");
        for (int i = 0; i < this.ring.size(); i++) {

            System.out.println(this.ring.get(i).getClass().getName());
            this.ring.get(i).setSuccessor(this.ring.get((i + 1) % this.ring.size()));

            System.out.println("Successor " + this.ring.get(i).getSuccessor().getName());
        }

        this.establishThreads();
    }

    public void establishThreads() {
        for (Node node : this.ring) {
            try {
                node.updateThreads();
            } catch (IOException ex) {
                System.out.println("Failed Establishing Stuff");
            }
            System.out.println("Establishing stuff");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ring.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread nodeThread = new Thread(node);
            nodeThread.start();

        }
    }
}
