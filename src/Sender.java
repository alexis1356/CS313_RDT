package src;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Sender extends TransportLayer {
    private TransportLayerPacket packet[] = new TransportLayerPacket[5];
    private int seqnum =0;
    private int base =0;
    private int nextSeqnum=0;
    private final int n = 3; // window size
    private Queue<byte[]> allData = new LinkedList<>();

    public Sender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        seqnum = 0;

    }

    @Override
    public void rdt_send(byte[] data) {
        //save the data - losing packets TODO: ask if it is needed
        //send the 1st or next packet
        System.out.println("Next Seqnum: "+ nextSeqnum + " Base: " + base);
        if (nextSeqnum<base+n) {
            //calculates checksum
            byte sum = checksum(data);
            System.out.println("Sender: checksum " + sum);
            //creates the packet and assigns for 1st package a seq number 0
            packet[nextSeqnum] = new TransportLayerPacket(nextSeqnum,nextSeqnum, data, sum);
            //sending the packet to the network layer
            printArray(packet);
            System.out.println("Sender: initial sending " + Arrays.toString(packet[nextSeqnum].getData()));
            System.out.println("Sender: " + nextSeqnum);
            //start timer
            if (base == nextSeqnum) {
                simulator.startTimer(this, 100);
            }
            nextSeqnum++;
        } else{
            simulator.sendToApplicationLayer(this, data);
        }
    }

    private void printArray(TransportLayerPacket[] pkt){
        for (int i=0; i<pkt.length; i++){
            System.out.println(i + " : " + pkt[i]);
        }
    }

    private byte checksum(byte[] data) {
        //calculate checksum
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum ^= 0xFFFFFFFF;
        return sum;
    }

    private int switchNum(int num) {
        if (num == 0)
            return 1;
        else
            return 0;
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //getAckNum should be the one that the receiver was waiting for and acknowledges it
     if (!isCorrupted(pkt.getChecksum())) {
            base = pkt.getAcknum()+1;
         System.out.println("Base updating: " + base);
            if (base==nextSeqnum) {
                simulator.stopTimer(this);
            }
            else {
                simulator.startTimer(this, 100);
            }
            System.out.println("Sender: " + Arrays.toString(pkt.getData()));
            System.out.println("Sender: stop timer and switch seqnum to " + seqnum + " in rdt_receive.");
            System.out.println("______________________________");
            //if (!allData.isEmpty()) {
              //  rdt_send(allData.poll());
           // }
        }
    }

    private boolean isCorrupted(byte receivedChecksum) {
        if (receivedChecksum == packet[base].getChecksum()) {
            System.out.println("Sender: checksum not corrupted ");
            return false;
        } else {
            System.out.println("Sender: checksum corrupted ");
            return true;
        }
    }



    @Override
    public void timerInterrupt() {
        simulator.startTimer(this, 100);
        for (int i = base; i < nextSeqnum; i++){
            simulator.sendToNetworkLayer(this, packet[i]);
            System.out.println("Sender: timeout, resend " + packet[i]);
        }
    }
}
