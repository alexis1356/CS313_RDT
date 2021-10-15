package src;

public class Sender extends TransportLayer{
    private TransportLayerPacket packet;

    public Sender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data) {
        packet = make_pkt(data);
        udt_send(packet);
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

    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
