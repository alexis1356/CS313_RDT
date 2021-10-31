package src;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Sender extends TransportLayer {
    private TransportLayerPacket packet;
    private int seqnum;
    private int waitsFor;
    private boolean waiting;
    private Queue<byte[]> allData = new LinkedList<>();

    public Sender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        seqnum = 0;
        waitsFor = 0;
        waiting = false;
    }

    @Override
    public void rdt_send(byte[] data) {
        //save the data - losing packets TODO: ask if it is needed
        //send the 1st or next packet
        if (!waiting) {
            //calculates checksum
            byte sum = checksum(data);
            System.out.println("Sender: checksum " + sum);
            //creates the packet and assigns for 1st package a seq number 0
            this.packet = new TransportLayerPacket(seqnum, data, sum);
            //sending the packet to the network layer
            simulator.sendToNetworkLayer(this, packet);
            System.out.println("Sender: initial sending " + Arrays.toString(packet.getData()));
            //start timer
            simulator.startTimer(this, 100);
            waiting = true;
        } else {
            //if it is waiting it adds the new data to a queue that will be sent later
            allData.add(data);
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
        if (waitsFor == pkt.getAcknum() && !isCorrupted(pkt.getChecksum()) && inOrderData(pkt.getData())) {
            simulator.stopTimer(this);
            seqnum = switchNum(seqnum);
            waitsFor = switchNum(waitsFor);
            System.out.println("Sender: stop timer and switch seqnum to " + seqnum + " in rdt_receive.");
            waiting = false;
            System.out.println("______________________________");
            if (!allData.isEmpty()) {
                rdt_send(allData.poll());
            }
        }
    }

    private boolean inOrderData(byte[] recievedData){
        byte[] orginalData = packet.getData();
        if (recievedData.length != orginalData.length){
            return false;
        }
        for (int i=0; i<recievedData.length; i++){
            if (recievedData[i] != orginalData[i]){
                return false;
            }
        }
        return true;
    }

    private boolean isCorrupted(byte receivedChecksum) {
        if (receivedChecksum == packet.getChecksum()) {
            System.out.println("Sender: checksum not corrupted ");
            return false;
        } else {
            System.out.println("Sender: checksum corrupted ");
            return true;
        }
    }

    @Override
    public void timerInterrupt() {
        simulator.sendToNetworkLayer(this, packet);
        System.out.println("Sender: timeout, resend " + packet);
        simulator.startTimer(this, 100);
    }
}
