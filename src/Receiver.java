package src;

public class Receiver extends TransportLayer{
    private byte[] data;
    private Sender sender;
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
            System.out.println("Receiver: Send to application layer");
        }

        //received = true;
//        TransportLayerPacket packet = new TransportLayerPacket(0,0,data, (byte) 0);
//        simulator.sendToNetworkLayer(sender, packet); // senderTL not instantiated

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
            TransportLayerPacket sndpkt = new TransportLayerPacket(pkt.getAcknum(), pkt.getSeqnum(), pkt.getData(), pkt.getChecksum());
            simulator.sendToNetworkLayer(this, sndpkt);
            waitsFor = switchNum(waitsFor);
            System.out.println("Send back");
            System.out.println("Receiver: rdt_receive case not corrupted and matching " + waitsFor);
        }
        else if (isCorrupted(pkt.getData(), pkt.getChecksum()) ||
                pkt.getSeqnum() != waitsFor) {
            TransportLayerPacket sndpkt = new TransportLayerPacket(pkt.getAcknum(), pkt.getSeqnum(), pkt.getData(), pkt.getChecksum());
            simulator.sendToNetworkLayer(this, sndpkt);
            System.out.println("Send back");
            System.out.println("Receiver: rdt_receive case corrupted or not matching " + waitsFor);
        }

//        if (isCorrupted(pkt.getData(), pkt.getChecksum())) {
//            // corrupted
//            if (pkt.getSeqnum() == 0) {
//                TransportLayerPacket sndpkt = new TransportLayerPacket(0, 0, pkt.getData());
//                udtSend(sndpkt);
//            } else if (pkt.getSeqnum() == 1) {
//                TransportLayerPacket sndpkt = new TransportLayerPacket(1, 0, pkt.getData());
//                udtSend(sndpkt);
//            }
//        } else {
//            // not corrupted
//            if (seqnum == pkt.getSeqnum()) {
//                extract(pkt, pkt.getData());
//                deliverData(pkt.getData());
//                TransportLayerPacket sndpkt = new TransportLayerPacket(pkt.getSeqnum(), pkt.getAcknum(), pkt.getData());
//                udtSend(sndpkt);
//                if (seqnum == 0)
//                    seqnum = 1;
//                else
//                    seqnum = 0;
//            }

//            if (pkt.getSeqnum() == 0) {
//                TransportLayerPacket sndpkt = new TransportLayerPacket(0, 1, pkt.getData());
//                udtSend(sndpkt);
//            } else if (pkt.getSeqnum() == 1) {
//                TransportLayerPacket sndpkt = new TransportLayerPacket(1, 1, pkt.getData());
//                udtSend(sndpkt);
//            }
    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
