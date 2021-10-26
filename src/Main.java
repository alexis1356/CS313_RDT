package src;

public class Main {

        public static void main(String[] args) {
            NetworkSimulator sim = new NetworkSimulator(5, 0, 0, 10.0, true, 1);

            Receiver receiver = new Receiver("receiver", sim);
            Sender sender = new Sender("sender", sim);
            // TODO: Set the sender   (sim.setSender)
            sim.setSender(sender);

            // TODO: Set the receiver (sim.setReceiver)
            sim.setReceiver(receiver);

            sim.runSimulation();
        }


    }
