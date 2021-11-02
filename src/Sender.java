package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Sender extends TransportLayer {
//    private TransportLayerPacket packet[] = new TransportLayerPacket[3];
    private ArrayList<TransportLayerPacket> packet = new ArrayList<>();
    private int base;
    private int nextSeqnum;
    private final int n = 2; // window size
    private Queue<byte[]> allData;

    public Sender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        base = 0;
        nextSeqnum = 0;
        allData = new LinkedList<>();
        System.out.println("Sender: init");
    }

    @Override
    public void rdt_send(byte[] data) {
        //save the data - losing packets TODO: ask if it is needed
        //send the 1st or next packet
        System.out.println("Next Seqnum: "+ nextSeqnum + " Base: " + base);
        if (nextSeqnum < base + n) {
            //calculates checksum
            byte sum = checksum(data);
            System.out.println("Sender: checksum " + sum);
            //creates the packet and assigns for 1st package a seq number 0
            packet.add(nextSeqnum, new TransportLayerPacket(nextSeqnum,nextSeqnum, data));
            System.out.println("Sender: initial sending " + Arrays.toString(packet.get(nextSeqnum).getData()));
            //sending the packet to the network layer
            simulator.sendToNetworkLayer(this, packet.get(nextSeqnum));
//            printArray(packet);
            System.out.println("Sender: " + nextSeqnum);
            //start timer
            if (base == nextSeqnum) {
                simulator.startTimer(this, 100);
            }
            nextSeqnum++;
        } else{
//            simulator.sendToApplicationLayer(this, data);
            allData.add(data);
            System.out.println("Sender: allData " + allData);
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

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //getAckNum should be the one that the receiver was waiting for and acknowledges it
        if (!isCorrupted(pkt.getChecksum(), pkt)) {
            base = pkt.getAcknum() + 1;
            System.out.println("Base updating: " + base);
            simulator.stopTimer(this);
            if (base==nextSeqnum) {
//                simulator.stopTimer(this);
            }
            else {
//                simulator.stopTimer(this);
                simulator.startTimer(this, 100);
            }
            System.out.println("Sender: " + Arrays.toString(pkt.getData()));
            System.out.println("______________________________");
            if (!allData.isEmpty()) {
                System.out.println("Sender: poll ");
              rdt_send(allData.poll());
             }
        }
    }

    private boolean isCorrupted(byte receivedChecksum, TransportLayerPacket pkt) {
        if(base > n) {
            return true;
        }
        if (receivedChecksum == packet.get(base).getChecksum() || pkt.getAcknum() >= 0) {
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
            simulator.sendToNetworkLayer(this, packet.get(i));
            System.out.println("Sender: timeout, resend " + packet.get(i));
        }
    }
}
