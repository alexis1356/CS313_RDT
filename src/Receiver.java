package src;

public class Receiver extends TransportLayer{
    private byte[] data;
    private Sender sender;

    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    public void setSender(Sender sender){
        this.sender = sender;
    }
    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data) {
        TransportLayerPacket packet = new TransportLayerPacket(0,0,data);
        simulator.sendToNetworkLayer(sender, packet); // senderTL not instantiated

    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //        TODO
        byte data[] = pkt.getData();
        simulator.sendToApplicationLayer(sender, data); // receiverTL not instatinated

//        extract(pkt, data);
//        deliver_data(data);
    }

    public void extract(TransportLayerPacket pkt, byte[] data) {
        //        TODO
    }

    public void deliver_data(byte[] data) {
        //        TODO
    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
