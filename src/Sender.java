package src;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

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
//        CRC32 crc = new CRC32();
//        crc.update(data, 0, data.length);
//        Long checksum = crc.getValue();
        System.out.println("checksum sender: " + sum);
        TransportLayerPacket packet = new TransportLayerPacket(0, 0, data,  sum);
        simulator.sendToNetworkLayer(this, packet);

    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        byte data[] = pkt.getData();
        if (pkt.getAcknum() == 0) {
            //NAK
            System.out.println("Resend " + pkt.getChecksum());
            simulator.sendToNetworkLayer(this, pkt);
//            simulator.sendToApplicationLayer(this, data);
        } else {
            //ACK
            System.out.println("ACK");

        }
        //System.out.println(Integer.toBinaryString((sum & 0xFF) + 0x100).substring(1));
        //System.out.println(Integer.toBinaryString((pkt.getChecksum() & 0xFF) + 0x100).substring(1));

    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
