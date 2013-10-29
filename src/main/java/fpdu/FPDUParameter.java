package fpdu;

import java.util.Date;

public class FPDUParameter {

	// Name of the sender of the FPDU
	private String sender;
	// Name of the receiver of the FPDU
	private String receiver;
	// Identifier of the file to be transfered
	private String fileIdentifier;
	// Identifier of the transfer
	private String transferIdentifier;
	// Boolean to determine whether we use synchronisation or not
	private boolean resynchronisation;
	// Point from where restart the transfer
	private int restartPoint;
	// Number to identify a synchronisation point
	private int numberSynchroPoint;
	// Size of the file to be transfered
	private int dataSize;
	// Diagnostic of an encountered error
	private String diagnostic;
	// Date of the server
	private Date date;
	// Access key for the sender and receiver to identify themselves (password)
	private String accessControl;
	// Priority of the transfer
	private int priority;
	// Maximum size of data that can be transfered at one time
	private int maxSizeOfDataEntity;
	// Local connection identifier
	private int localConnectionId = 0;
	// Remote connection identifier
	private int remoteConnectionId = 0;
	// Id of the transfered article
	private int articleId;
	// Byte array
	private byte[] byteArray = null;
	// ID byteArray
	private int idByteArray = -1;;
	
	/*
	 * Missing parameters:
	 * 	data -> data of a portion of file to be sent
	 */

	public FPDUParameter () {}
	
	public FPDUParameter(String sender, String receiver) {
		super();
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public String getSender() {
		return this.sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return this.receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public boolean isResynchronisation() {
		return resynchronisation;
	}
	public void setResynchronisation(boolean resynchronisation) {
		this.resynchronisation = resynchronisation;
	}
	public int getDataSize() {
		return dataSize;
	}
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}
	public String getDiagnostic() {
		return diagnostic;
	}
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getFileIdentifier() {
		return fileIdentifier;
	}
	public void setFileIdentifier(String fileIdentifier) {
		this.fileIdentifier = fileIdentifier;
	}
	public String getTransferIdentifier() {
		return transferIdentifier;
	}
	public void setTransferIdentifier(String transferIdentifier) {
		this.transferIdentifier = transferIdentifier;
	}
	public int getRestartPoint() {
		return restartPoint;
	}
	public void setRestartPoint(int restartPoint) {
		this.restartPoint = restartPoint;
	}
	public int getNumberSynchroPoint() {
		return numberSynchroPoint;
	}
	public void setNumberSynchroPoint(int numberSynchroPoint) {
		this.numberSynchroPoint = numberSynchroPoint;
	}
	public String getAccessControl() {
		return accessControl;
	}
	public void setAccessControl(String accessControl) {
		this.accessControl = accessControl;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getMaxSizeOfDataEntity() {
		return maxSizeOfDataEntity;
	}
	public void setMaxSizeOfDataEntity(int maxSizeOfDataEntity) {
		this.maxSizeOfDataEntity = maxSizeOfDataEntity;
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

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public int getIdByteArray() {
		return idByteArray;
	}

	public void setIdByteArray(int idByteArray) {
		this.idByteArray = idByteArray;
	}

}
