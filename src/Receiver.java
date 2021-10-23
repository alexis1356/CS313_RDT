package src;

import java.util.Arrays;

public class Receiver extends TransportLayer{
    private TransportLayerPacket packet;
    private int waitsFor;

    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
        waitsFor = 0;
    }

    @Override
    public void rdt_send(byte[] data) {
        if (packet.getSeqnum() == waitsFor) {
            simulator.sendToApplicationLayer(this, data);
            System.out.println("Receiver: Send to application layer " + Arrays.toString(data));
        }
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

    private int switchNum(int num) {
        if (num == 0)
            return 1;
        else
            return 0;
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //        TODO
        this.packet = pkt;
        if (!isCorrupted(pkt.getData(), pkt.getChecksum()) &&
        pkt.getSeqnum() == waitsFor) {
            rdt_send(pkt.getData());
//            TransportLayerPacket sndpkt = new TransportLayerPacket(pkt.getAcknum(), pkt.getSeqnum(), pkt.getData(), pkt.getChecksum());
//            simulator.sendToNetworkLayer(this, sndpkt);
            waitsFor = switchNum(waitsFor);
//            System.out.println("Send back " + Arrays.toString(pkt.getData()));
//            System.out.println("Receiver: rdt_receive case not corrupted and matching " + waitsFor);
        }
//        else if (isCorrupted(pkt.getData(), pkt.getChecksum()) ||
//                pkt.getSeqnum() != waitsFor) {
//
//        }
        TransportLayerPacket sndpkt = new TransportLayerPacket(pkt.getAcknum(), pkt.getSeqnum(), pkt.getData(), pkt.getChecksum());
        simulator.sendToNetworkLayer(this, sndpkt);
        System.out.println("Send back " + Arrays.toString(pkt.getData()));
        System.out.println("Receiver: rdt_receive case corrupted or not matching " + waitsFor);
    }

    @Override
    public void timerInterrupt() {}
}
