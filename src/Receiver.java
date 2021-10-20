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
        simulator.sendToApplicationLayer(this, data);
        //received = true;
        TransportLayerPacket packet = new TransportLayerPacket(0,0,data);
        simulator.sendToNetworkLayer(sender, packet); // senderTL not instantiated

    }

    public boolean isCorrupted(byte[] data, byte checksum) {
        //System.out.println(Integer.toBinaryString((data & 0xFF) + 0x100).substring(1));
        System.out.println(Integer.toBinaryString((checksum & 0xFF) + 0x100).substring(1));
        //TODO check if packet is all 1's
        return true;
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //        TODO
        byte data[] = pkt.getData();
        byte checksum = pkt.getChecksum();
        isCorrupted(data, checksum);
        simulator.sendToApplicationLayer(this, data); // receiverTL not instatinated

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
