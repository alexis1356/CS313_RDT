package src;

public class Sender extends TransportLayer{
    private TransportLayerPacket packet;
    private boolean received;

    public Sender(String name, NetworkSimulator simulator)
    {
        super(name, simulator);
        received = false;
    }

    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data) {
        //takes an array of byte data and turns this into TransportLayerPacket
        //which is sent to receiver
        TransportLayerPacket packet = new TransportLayerPacket(0, 0, data);
        simulator.sendToNetworkLayer(this, packet);

        //calculate checksum
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        //int number_of_bits =
        //        (int)(Math.floor(Math.log(sum) / Math.log(2))) + 1;
        System.out.println("Old " + sum);
        //sum ^= ((1 << number_of_bits) - 1);
        sum = (byte) ((byte) ~sum & 0xFFFFFFFF);
        sum = (byte) ((byte) sum +1);
        System.out.println(sum);
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        byte data[] = pkt.getData();
        simulator.sendToApplicationLayer(this, data);
        received = true;

    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
