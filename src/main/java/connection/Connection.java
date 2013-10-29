package connection;

import transfer.Transfer;

/**
 * 
 * @brief A Connection object is need for each local or remote file transfer. It
 *        indicates the state of a connection as well as the different bank
 *        concerned.
 * 
 */
public class Connection {
    /**
     * Attributes
     */
    // Name of the local bank
    private String localIdentity;
    // Name of the remote bank
    private String remoteIdentity;
    // State of the connection (enum type)
    private EnumState state;
    // Attributes concerning persistence
    private int idConnection;
    private int remoteId;
    // Transfer we have loaned out, if the connection state id "connected"
    // NB : one transfer at a time
    private Transfer outTransfer = new Transfer();

    /**
     * Enum type of different states for connection
     */
    public enum EnumState {
        idle, waitfor_acconnect, connection_refused, connected, file_selected, file_opened, transfer_inprogress, waitfor_relconf
    }

    /**
     * @brief First Constructor
     * @param localIdentity
     * @param remoteIdentity
     */
    public Connection(String localIdentity, String remoteIdentity) {
        super();
        this.localIdentity = localIdentity;
        this.remoteIdentity = remoteIdentity;
        this.state = EnumState.idle;
        this.outTransfer = null;
        System.out.println("Connection open, State : idle, Remote entity : " + remoteIdentity);
    }

    /**
     * @brief Second Constructor
     */
    public Connection() {
        super();
        this.state = EnumState.idle;
        this.outTransfer = null;
    }

    public void setLocalIdentity(String localIdentity) {
        this.localIdentity = localIdentity;
    }

    public String getLocalIdentity() {
        return localIdentity;
    }

    public void setState(EnumState state) {
        this.state = state;
    }

    public EnumState getState() {
        return state;
    }

    public String getRemoteIdentity() {
        return remoteIdentity;
    }

    public void setRemoteIdentity(String remoteIdentity) {
        this.remoteIdentity = remoteIdentity;
    }

    public int getIdConnection() {
        return idConnection;
    }

    public void setIdConnection(int idConnection) {
        this.idConnection = idConnection;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public Transfer getOutTransfer() {
        return outTransfer;
    }

    public void setOutTransfer(Transfer outTransfer) {
        this.outTransfer = outTransfer;
    }

}
