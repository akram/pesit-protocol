package transfer;

/**
 * 
 * @brief 	Class intended to represent information concerning the transfer of a 
 * 			file. 
 * 			A transfer correspond to the willing of a user to "read" a file.
 * 			It is associated with a connection (it an attribute of a Connection
 * 			object).
 * 			It can have different states depending on the selection, opening, 
 * 			creation or actual download of the file.
 *
 */
public class Transfer {
	
	/**
	 * Attributes
	 */
	private EnumTransferState state;
	private int transferId;
	private int localConnectionId;
	private int remoteConnectionId;
	// Size of the file to be transfered in bytes
	// TODO : initialize this variable while doing the selection of the file !
	private int fileSize;
	
	/**
	 * Enum type of different states of the transfer
	 */
	public enum EnumTransferState {
		connected,
		wait_for_selection,
		file_selected,
		transfered, 
		wait_for_free_file,
		wait_for_file_creation,
		wait_for_file_opening,
		wait_for_file_closing,
		data_transfer_idle,
		wait_for_launch_reading,
		wait_for_end_reading,
		wait_for_ack_read,
		end_reading,
		read_file
	}
	
	/**
	 * @brief Constructor
	 * @param transferId
	 * @param localConnectionId
	 * @param remoteConnectionId
	 */
	public Transfer(int transferId, int localConnectionId,
			int remoteConnectionId) {
		super();
		this.transferId = transferId;
		this.localConnectionId = localConnectionId;
		this.remoteConnectionId = remoteConnectionId;
		this.state = EnumTransferState.connected;
	}
	public Transfer() {
		super();
	}

	public EnumTransferState getState() {
		return state;
	}

	public void setState(EnumTransferState state) {
		this.state = state;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public int getLocalConnectionId() {
		return localConnectionId;
	}

	public void setLocalConnectionId(int localConnectionId) {
		this.localConnectionId = localConnectionId;
	}

	public int getRemoteConnectionId() {
		return remoteConnectionId;
	}

	public void setRemoteConnectionId(int remoteConnectionId) {
		this.remoteConnectionId = remoteConnectionId;
	}
	
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}


}
