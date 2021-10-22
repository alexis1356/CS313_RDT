package src;

public class Sender extends TransportLayer{
    private TransportLayerPacket packet;
    //private boolean isAck = true;
    private int[] states = {1,2}; //lists the states of sender as ints so
    // state 1 - means we are waiting from a call from above - can send packets
    // state 2 - means we are waiting for an ACk or NAK - can't send packets
    private int currentState = 1; //int of what state we are currently on
    private int nextData = 0; //index of the next data to be sent
    private int nextFreeIndex = 0;
    private byte[][] dataQueue = new byte[10][]; //list of data that needs to be sent;

    public Sender(String name, NetworkSimulator simulator)
    {
        super(name, simulator);
    }

    @Override
    public void init() {
        currentState = 1;
        //        TODO
    }

    @Override
    public void rdt_send(byte[] data){
        switch (currentState){
            case 1:
                byte sum = 0;
                for (int i = 0; i < data.length; i++) {
                    sum += data[i];
                }
                sum ^= 0xFFFFFFFF;
                packet = new TransportLayerPacket(0, 0, data, sum);
                simulator.sendToNetworkLayer(this, packet);
                //System.out.println(Integer.toBinaryString((corruptedSum & 0xFF) + 0x100).substring(1));
                System.out.println("Sent: " + Integer.toBinaryString((sum & 0xFF) + 0x100).substring(1));
                currentState =2;
            case 2:
                 //nextData = data;
                    dataQueue[nextFreeIndex] = data;
                    nextFreeIndex++;
        }

    }



    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        if (pkt.getAcknum() == 0) {
            System.out.println("Received NAK");
            simulator.sendToNetworkLayer(this, packet);
            currentState=2;
        }
        else {
            System.out.println("Received ACK");
            currentState=1;
            if (pkt.getData() != dataQueue[nextData]) {
                System.out.println("Next data: " + nextData +" Next Free Index: "+ nextFreeIndex);
                rdt_send(dataQueue[nextData]);
                nextData++;
            }
        }
        //simulator.sendToApplicationLayer(this, data);

    }

    @Override
    public void timerInterrupt() {
        //        TODO
    }
}
