package src;

public class Main {

    public static void main(String[] args) {
        NetworkSimulator sim = new NetworkSimulator(10, 0.0, 0.0, 10.0, false, 1);
        TransportLayer receiverTL;
        TransportLayer senderTL;
        receiverTL = new TransportLayer("receiverTL", sim) {

            @Override
            public void init() {

            }

            @Override
            public void rdt_send(byte[] data) {
                TransportLayerPacket packet = new TransportLayerPacket(0,0,data);
                //sim.sendToNetworkLayer(senderTL, packet); // senderTL not instantiated
                System.out.println("Sent! in receiverTL");
            }

            @Override
            public void rdt_receive(TransportLayerPacket pkt) {
                byte data[] = pkt.getData();
                System.out.println("Received! in receiverTL");
                //sim.sendToApplicationLayer(receiverTL, data); // receiverTL not instatinated
            }

            @Override
            public void timerInterrupt() {

            }
        };

        senderTL = new TransportLayer("senderTL", sim) {
            @Override
            public void init() {

            }

            @Override
            public void rdt_send(byte[] data) {
                //takes an array of byte data and turns this into TransportLayerPacket
                //which is sent to receiver
                TransportLayerPacket packet = new TransportLayerPacket(0,0,data);
                sim.sendToNetworkLayer(receiverTL, packet);
                System.out.println("Sent! in senderTL");

            }

            @Override
            public void rdt_receive(TransportLayerPacket pkt) {
                byte data[] = pkt.getData();
                System.out.println("Received! in senderTL");
                //sim.sendToApplicationLayer(senderTL, data); // receiverTL not instatinated
            }

            @Override
            public void timerInterrupt() {

            }
        };
        sim.setSender(senderTL);

        sim.setReceiver(receiverTL);

        sim.runSimulation();
    }

}
