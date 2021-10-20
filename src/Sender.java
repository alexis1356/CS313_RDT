package src;

public class Sender extends TransportLayer{
    private TransportLayerPacket packet;
    private boolean isAck = true;

    public Sender(String name, NetworkSimulator simulator)
    {
        super(name, simulator);
    }

    @Override
    public void init() {
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data){
        while (!isAck) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum ^= 0xFFFFFFFF;
        packet = new TransportLayerPacket(0, 0, data, sum);
        simulator.sendToNetworkLayer(this, packet);
        //System.out.println(Integer.toBinaryString((corruptedSum & 0xFF) + 0x100).substring(1));
        System.out.println("Sent: " + Integer.toBinaryString((sum & 0xFF) + 0x100).substring(1));
        isAck = false;
    }



    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        if (pkt.getAcknum() == 0) {
            System.out.println("Received NAK");
            //TODO data is corrupted
            simulator.sendToNetworkLayer(this, packet);
        }
        else {
            System.out.println("Received ACK");
            isAck = true;
        }
        //simulator.sendToApplicationLayer(this, data);

    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
