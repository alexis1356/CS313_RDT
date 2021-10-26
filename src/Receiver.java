package src;

import java.util.Arrays;

public class Receiver extends TransportLayer{
    private TransportLayerPacket packet ;
    private TransportLayerPacket sndpkt;
    private int expectedSeqnum;

    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        expectedSeqnum=0;
        packet = new TransportLayerPacket (0,0 ,null, (byte)0);
    }

    @Override
    public void rdt_send(byte[] data) {
        if (packet.getSeqnum()  == expectedSeqnum) {
            simulator.sendToApplicationLayer(this, data);
            System.out.println("Receiver: Send to application layer " + Arrays.toString(data));
        }
        //expectedSeqnum =0;
        //packet = new TransportLayerPacket (0,packet.getAcknum(),packet.getData(), packet.getChecksum());
    }

    private boolean isCorrupted(byte[] data, byte checksum){
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        System.out.println("Receiver checksum: " + sum);
        sum += checksum;
        if(sum == 0xFFFFFFFF){
            System.out.println("Receiver: total sum not corrupted " + sum);
            return false;
        }
        else
            System.out.println("Receiver: total sum corrupted " + sum);
            return true;
    }//

    private int switchNum(int num) {
        if (num == 0)
            return 1;
        else
            return 0;
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //assign local packet to the arriving packet
            if (!isCorrupted(pkt.getData(), pkt.getChecksum()) && pkt.getSeqnum() == expectedSeqnum) {
                rdt_send(pkt.getData()); //we sent the packet ot the application layer
                this.packet = pkt;
                sndpkt = new TransportLayerPacket(expectedSeqnum, expectedSeqnum, packet.getData(), packet.getChecksum());
                simulator.sendToNetworkLayer(this, sndpkt);
                expectedSeqnum++;
            } else if (isCorrupted(pkt.getData(), pkt.getChecksum()) || pkt.getSeqnum() != expectedSeqnum) {
                sndpkt = new TransportLayerPacket(pkt.getSeqnum(), expectedSeqnum, pkt.getData(), pkt.getChecksum());
                simulator.sendToNetworkLayer(this, sndpkt);
                System.out.println("Send back " + Arrays.toString(pkt.getData()));
                System.out.println("Receiver: rdt_receive case corrupted or not matching " + expectedSeqnum);
            }
        }


    @Override
    public void timerInterrupt() {}
}
