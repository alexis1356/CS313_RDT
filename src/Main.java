package src;

public class Main {

    public static void main(String[] args) {
        NetworkSimulator sim = new NetworkSimulator(10, 0.2, 0.0, 10.0, false, 1);

        Receiver receiver = new Receiver("receiver", sim);
        Sender sender = new Sender("sender", sim);

        sim.setSender(sender);

        sim.setReceiver(receiver);

        sim.runSimulation();
    }


}
