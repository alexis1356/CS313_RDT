package src;

public class Sender extends TransportLayer{
    private TransportLayerPacket packet;
    private Receiver receiver;

    public Sender(String name, NetworkSimulator simulator)
    {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
    }

    public void setReceiver(Receiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void rdt_send(byte[] data) {
        //takes an array of byte data and turns this into TransportLayerPacket
        //which is sent to receiver
        TransportLayerPacket packet = new TransportLayerPacket(0,0,data);
        simulator.sendToNetworkLayer(receiver, packet);
        System.out.println("Sent! in senderTL");

//        packet = make_pkt(data);
//        udt_send(packet);
    }

    public TransportLayerPacket make_pkt(byte[] data) {
        //        TODO
        return new TransportLayerPacket(packet);
    }

    public void udt_send(TransportLayerPacket packet) {
        //        TODO
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        byte data[] = pkt.getData();
        System.out.println("Received! in senderTL");
        //sim.sendToApplicationLayer(senderTL, data); // receiverTL not instatinated

    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
