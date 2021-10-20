package src;

public class Sender extends TransportLayer{
    private TransportLayerPacket packet;
    private boolean received;

    public Sender(String name, NetworkSimulator simulator)
    {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data) {
        //takes an array of byte data and turns this into TransportLayerPacket
        //which is sent to receiver
        //calculate checksum
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum ^=0xFFFFFFFF;
        TransportLayerPacket packet = new TransportLayerPacket(0, 0, data, sum);
        simulator.sendToNetworkLayer(this, packet);

    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        byte data[] = pkt.getData();
        //System.out.println(Integer.toBinaryString((sum & 0xFF) + 0x100).substring(1));
        //System.out.println(Integer.toBinaryString((pkt.getChecksum() & 0xFF) + 0x100).substring(1));
        simulator.sendToApplicationLayer(this, data);
        //received = true;
    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
