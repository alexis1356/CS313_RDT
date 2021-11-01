package src;

public class TransportLayerPacket {

    // Maybe remove these
    // You may need extra fields
    private int seqnum;
    private int acknum;

    byte[] data;
    byte checksum;

    // You may need extra methods

    public TransportLayerPacket(int sequm, int acknum, byte[] data){
        setSeqnum(sequm);
        setAcknum(acknum);
        setData(data);
        setChecksum(checksum(data));
    }

    public TransportLayerPacket(TransportLayerPacket pkt) {
        this.seqnum = pkt.seqnum;
        this.acknum = pkt.acknum;
        this.data = pkt.data.clone();
        this.checksum = checksum(data);
    }



    public void setSeqnum(int seqnum) {
        this.seqnum = seqnum;
    }

    public void setAcknum(int acknum) {
        this.acknum = acknum;
    }

    public void setData(byte[] data) {this.data=data; }

    public void setChecksum(byte checksum) {this.checksum=checksum; }

    public byte[] getData() {
        return data;
    }

    public int getAcknum() {
        return acknum;
    }

    public int getSeqnum() {
        return seqnum;
    }

    public byte getChecksum() {
        return checksum;   }

    private byte checksum(byte[] data) {
        //calculate checksum
        byte sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        sum ^= 0xFFFFFFFF;
        return sum;
    }
}
