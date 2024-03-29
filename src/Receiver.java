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
        System.out.println("Receiver: init");
        expectedSeqnum = 0;
    }

    @Override
    public void rdt_send(byte[] data) {
        System.out.println("Receiver: SEND TO APPLICATION LAYER " + Arrays.toString(data));
        simulator.sendToApplicationLayer(this, data);
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
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //assign local packet to the arriving packet
        if (!isCorrupted(pkt.getData(), pkt.getChecksum()) && pkt.getSeqnum() == expectedSeqnum) {
            rdt_send(pkt.getData()); //we sent the packet ot the application layer
            this.packet = pkt;
            sndpkt = new TransportLayerPacket(expectedSeqnum, expectedSeqnum, packet.getData());
            simulator.sendToNetworkLayer(this, sndpkt);
            expectedSeqnum++;
        } else if (isCorrupted(pkt.getData(), pkt.getChecksum()) || pkt.getSeqnum() != expectedSeqnum) {
            sndpkt = new TransportLayerPacket(pkt.getSeqnum(), expectedSeqnum - 1, pkt.getData());
            simulator.sendToNetworkLayer(this, sndpkt);
            System.out.println("Receiver: Send back " + Arrays.toString(pkt.getData()));
            System.out.println("Receiver: rdt_receive case corrupted or not matching ackNum " + (expectedSeqnum - 1));
        }
    }


    @Override
    public void timerInterrupt() {}
}