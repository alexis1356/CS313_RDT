package src;

public class Sender extends TransportLayer {
    private TransportLayerPacket packet;
    private int seqnum;

    public Sender(String name, NetworkSimulator simulator)
    {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
        seqnum = 0;
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
        System.out.println("checksum sender: " + sum);
        this.packet = new TransportLayerPacket(seqnum, data,  sum);
        simulator.sendToNetworkLayer(this, packet);
        // start a timer
        simulator.startTimer(this, 10);
    }

    private int switchNum(int num) {
        if (num == 0)
            return 1;
        else
            return 0;
    }


    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        if (pkt.getAcknum() != seqnum) {

        }
        if (pkt.getAcknum() == seqnum) {
            simulator.stopTimer(this);
            seqnum = switchNum(seqnum);
//            rdt_send(pkt.getData());
        }

//        byte data[] = pkt.getData();
//        if (pkt.getAcknum() == 0) {
//            //NAK
//            System.out.println("Resend NAK " + pkt.getChecksum());
//            simulator.sendToNetworkLayer(this, pkt);
//            // start a timer
//            simulator.startTimer(this, 100);
//
////            simulator.sendToApplicationLayer(this, data);
//        } else {
//            //ACK
//            System.out.println("ACK");
//            simulator.stopTimer(this);
//            if (pkt.getSeqnum() == 0) {
//                seqnum = 1;
//                System.out.println("Seq num switch to 1");
//            } else {
//                seqnum = 0;
//                System.out.println("Seq num switch to 0");
//            }
//            //stop timer
//        }
//
//        if (seqnum == pkt.getAcknum()) {
//            if (seqnum == 0)
//                seqnum = 1;
//            else
//                seqnum = 0;
//        }
    }

    @Override
    public void timerInterrupt() {
        //        TODO - retransmit
        simulator.sendToNetworkLayer(this, packet);
        System.out.println("resend");
        // start a timer
        simulator.startTimer(this, 10);
    }
}
