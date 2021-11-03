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
        System.out.println("Waiting for: " + waitsFor);
        if (!isCorrupted(pkt.getData(), pkt.getChecksum()) &&
                pkt.getSeqnum() == waitsFor) {
            //assign local packet to the arriving packet
            this.packet = pkt;
            TransportLayerPacket sndpkt = new TransportLayerPacket(packet.getSeqnum(), waitsFor, packet.getData());
            simulator.sendToNetworkLayer(this, sndpkt);
            rdt_send(pkt.getData()); //we sent the packet to the application layer
            waitsFor = switchNum(waitsFor); //we wait for the next packet with seq number - 1
        }
        else if(isCorrupted(pkt.getData(),pkt.getChecksum()) || pkt.getSeqnum() != waitsFor){
            TransportLayerPacket sndpkt = new TransportLayerPacket(pkt.getSeqnum(), switchNum(waitsFor), pkt.getData());
            simulator.sendToNetworkLayer(this, sndpkt);
            System.out.println("Send back " + Arrays.toString(pkt.getData()));
            System.out.println("Receiver: rdt_receive case corrupted or not matching " + waitsFor);
        }
    }

    @Override
    public void timerInterrupt() {}
}
