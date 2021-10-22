package src;

public class TransportLayerPacket {

    // Maybe remove these
    // You may need extra fields
    private int seqnum;
    private int acknum;

    byte[] data;
    byte checksum;

    // You may need extra methods

    public TransportLayerPacket(int sequm, int acknum, byte[] data, byte checksum){
        setSeqnum(sequm);
        setAcknum(acknum);
        setData(data);
        setChecksum(checksum);
    }

    public TransportLayerPacket(int sequm, byte[] data, byte checksum){
        setSeqnum(sequm);
        setData(data);
        setChecksum(checksum);
    }

    public TransportLayerPacket(int sequm, int acknum, byte[] data){
        setSeqnum(sequm);
        setAcknum(acknum);
        setData(data);
    }

    public TransportLayerPacket(TransportLayerPacket pkt) {
        this.seqnum = pkt.seqnum;
        this.acknum = pkt.acknum;
        this.data = pkt.data;
        this.checksum = pkt.checksum;
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
}
