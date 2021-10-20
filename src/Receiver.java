package src;
public class Receiver extends TransportLayer{
    private byte[] data;
    private Sender sender;

    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    public void setSender(Sender sender){
        this.sender = sender;
    }
    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data) {
      //don't need this for rdt 2.0
    }

    public boolean isCorrupt(byte[] data, byte checksum){
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum += checksum;
        if(sum == 0xFFFFFFFF){
            return false;
        }
        else
            return true;
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        byte data[] = pkt.getData();
        byte checksum = pkt.getChecksum();
        System.out.println("Received: " + Integer.toBinaryString((checksum & 0xFF) + 0x100).substring(1));
        if (isCorrupt(data,checksum)){ //packet data is corrupt
            TransportLayerPacket nak = new TransportLayerPacket(0,0, data);
            simulator.sendToNetworkLayer(this, nak);
        }
        else {
            simulator.sendToApplicationLayer(this, data);
            TransportLayerPacket ack = new TransportLayerPacket(0, 1, data);
            simulator.sendToNetworkLayer(this, ack);
        }
    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
