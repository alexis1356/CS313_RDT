package src;

public class Receiver extends TransportLayer{
    private byte[] data;

    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data) {

    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //        TODO
        extract(pkt, data);
        deliver_data(data);
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
