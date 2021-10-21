package src;

public class Receiver extends TransportLayer{
    private byte[] data;
    private Sender sender;

    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data) {
        simulator.sendToApplicationLayer(this, data);
        //received = true;
        TransportLayerPacket packet = new TransportLayerPacket(0,0,data, (byte) 0);
        simulator.sendToNetworkLayer(sender, packet); // senderTL not instantiated

    }

    private void deliverData(byte[] data) {
        simulator.sendToApplicationLayer(this, data);
    }

    private void extract (TransportLayerPacket pkt, byte[] data) {
        this.data = pkt.getData();
        // TODO: remove the data from the packet ??
//        pkt.setData(new byte[0]);
    }

    private void udtSend (TransportLayerPacket pkt) {
        this.simulator.sendToNetworkLayer(this, pkt);
    }



    private boolean isCorrupted(byte[] data, byte checksum){
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
//        CRC32 crc = new CRC32();
//        crc.update(data, 0, data.length);
//        Long sum = crc.getValue();
        System.out.println("checksum receiver: " + sum);
        sum += checksum;
        if(sum == 0xFFFFFFFF){
            System.out.println("total sum not corrupted " + sum);
            return false;
        }
        else
            System.out.println("total sum corrupted" + sum);
            return true;
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //        TODO
        if (isCorrupted(pkt.getData(), pkt.getChecksum())) {
            // corrupted
            if (pkt.getSeqnum() == 0) {
                TransportLayerPacket sndpkt = new TransportLayerPacket(0, 0, pkt.getData());
                udtSend(sndpkt);
            } else if (pkt.getSeqnum() == 1) {
                TransportLayerPacket sndpkt = new TransportLayerPacket(1, 0, pkt.getData());
                udtSend(sndpkt);
            }
        } else {
            // not corrupted
            if (pkt.getSeqnum() == 0) {
                extract(pkt, pkt.getData());
                deliverData(pkt.getData());
                TransportLayerPacket sndpkt = new TransportLayerPacket(0, 1, pkt.getData());
                udtSend(sndpkt);
            }
                extract(pkt, pkt.getData());
                deliverData(pkt.getData());
                TransportLayerPacket sndpkt = new TransportLayerPacket(1, 1, pkt.getData());
                udtSend(sndpkt);
            }
    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
