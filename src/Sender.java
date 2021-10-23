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

    public Sender(String name, NetworkSimulator simulator)
    {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
        seqnum = 0;
        waitsFor = 0;
        waiting = false;
    }

    @Override
    public void rdt_send(byte[] data) {
        //put it in a queue
        //save the data - losing packets TODO: ask if it is needed
        //check somewhere if the queue is empty to send remaining packets in rdt_receive

        //takes an array of byte data and turns this into TransportLayerPacket
        //which is sent to receiver
        //how to stop sending
        if(!waiting) {
            //send the 1st
            byte sum = checksum(data);
            System.out.println("Sender: checksum " + sum);
            this.packet = new TransportLayerPacket(seqnum, data, sum);
            simulator.sendToNetworkLayer(this, packet);
            System.out.println("Sender: initial sending " + Arrays.toString(packet.getData()));
            simulator.startTimer(this, 100);
            waiting = true;
        } else {
            allData.add(data);
        }
    }

    private byte checksum(byte[] data) {
        //calculate checksum
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum ^=0xFFFFFFFF;
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
        if (waitsFor != pkt.getAcknum() || isCorrupted(pkt.getData(), pkt.getChecksum())) {
            //do nothing
            System.out.println("A");
        }
        if (waitsFor == pkt.getAcknum() && !isCorrupted(pkt.getData(), pkt.getChecksum())) {
            simulator.stopTimer(this);
            seqnum = switchNum(seqnum);
            waitsFor = switchNum(waitsFor);
            System.out.println("Sender: " + Arrays.toString(pkt.getData()));
            System.out.println("Sender: stop timer and switch seqnum to " + seqnum + " in rdt_receive.");
            waiting = false;
            if(!allData.isEmpty()) {
                rdt_send(allData.poll());
            }
            System.out.println("______________________________");
        }
    }

    private boolean isCorrupted(byte[] data, byte checksum){
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        System.out.println("Sender checksum: " + sum);
        sum += checksum;
        if(sum == 0xFFFFFFFF){
            System.out.println("Sender: total sum not corrupted " + sum);
            return false;
        }
        else
            System.out.println("Sender: total sum corrupted " + sum);
        return true;
    }

    @Override
    public void timerInterrupt() {
        simulator.sendToNetworkLayer(this, packet);
        System.out.println("Sender: timeout, resend " + packet);
        simulator.startTimer(this, 100);
    }
}
