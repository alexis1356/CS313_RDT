package src;

public class Sender extends TransportLayer {
    private TransportLayerPacket packet;
    private int seqnum;
    private int waitsFor;
    private boolean waiting;

    public Sender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
        seqnum = 0;
        waitsFor = 0;
        waiting = false;
    }

    /**
     * Takes an array of bytes and packs it into TransportLayerPacket
     * which is sent to receiver via network layer
     *
     * @param data to be sent to receiver
     */
    @Override
    public void rdt_send(byte[] data) {
        //how to stop sending
        if (!waiting) {
//        if (waitsFor == seqnum) {
            byte sum = checksum(data);
            System.out.println("Sender: checksum " + sum);
            this.packet = new TransportLayerPacket(seqnum, data, sum);
            simulator.sendToNetworkLayer(this, packet);
            System.out.println("Sender: initial sending");
            simulator.startTimer(this, 100);
            waiting = true;
        }

//        simulator.sendToNetworkLayer(this, packet);
        // start a timer

    }

    /**
     * Calculates checksum
     *
     * @param data the data to be sent
     * @return data array's sum with its bits flipped
     */
    private byte checksum(byte[] data) {
        //calculate checksum
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum ^= 0xFFFFFFFF;
        return sum;
    }

    /**
     * Switches number between 1 and 0
     *
     * @param num current value
     * @return opposite value
     */
    private int switchNum(int num) {
        if (num == 0)
            return 1;
        else
            return 0;
    }

    /**
     * Receives packet from receiver and performs different
     * actions depending on retrieved acknowledgement number
     *
     * @param pkt the src.TransportLayerPacket received via unreliable transport
     */
    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        if (waitsFor != pkt.getAcknum() || isCorrupted(pkt.getData(), pkt.getChecksum())) {
            //do nothing
            System.out.println("A");
        }
        if (waitsFor == pkt.getAcknum() && !isCorrupted(pkt.getData(), pkt.getChecksum())) {
            simulator.stopTimer(this);
            seqnum = switchNum(seqnum);
            waitsFor = switchNum(waitsFor);
            System.out.println("Sender: stop timer and switch seqnum to " + seqnum + " in rdt_receive.");
            waiting = false;
            System.out.println("______________________________");
        }
        if (pkt.getAcknum() != seqnum) {
//            simulator.sendToNetworkLayer(this, packet);
//            System.out.println("Sender: resend when not matching");
        }
        if (pkt.getAcknum() == seqnum) {
//            simulator.stopTimer(this);
//            seqnum = switchNum(seqnum);
//            System.out.println("Sender: stop timer and switch seqnum to " + seqnum + " in rdt_receive.");
//            System.out.println("______________________________");

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

    /**
     * Identifies if received data is corrupted
     *
     * @param data     received data array
     * @param checksum received checksum
     * @return true if data is corrupted, otherwise false
     */
    private boolean isCorrupted(byte[] data, byte checksum) {
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        System.out.println("Sender checksum: " + sum);
        sum += checksum;
        if (sum == 0xFFFFFFFF) {
            System.out.println("Sender: total sum not corrupted " + sum);
            return false;
        } else
            System.out.println("Sender: total sum corrupted " + sum);
        return true;
    }

    /**
     * Timer interrupts sending process
     */
    @Override
    public void timerInterrupt() {
        //        TODO - retransmit
        simulator.sendToNetworkLayer(this, packet);
        System.out.println("Sender: timeout, resend");
        // start a timer
        simulator.startTimer(this, 100);
    }
}
