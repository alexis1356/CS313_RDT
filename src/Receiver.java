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
        TransportLayerPacket packet = new TransportLayerPacket(0,0,data, (byte) 0);
        simulator.sendToNetworkLayer(sender, packet); // senderTL not instantiated

    }

    public boolean isCorrupted(byte[] data, byte checksum){
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum += checksum;
        if(sum == 0xFFFFFFFF){
            return false;
        }
        else
            return true;
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //        TODO
        byte data[] = pkt.getData();
        byte checksum = pkt.getChecksum();
        simulator.sendToApplicationLayer(this, data);

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
