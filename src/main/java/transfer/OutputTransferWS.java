package transfer;

import java.util.ArrayList;

import fpdu.FPDU;

/**
 * Class allowing the communication with the "inter server" entity in order to
 * provide it with : 
 * 		a FPDU list object to send or
 * 		an exception to handle or
 * 		information concerning the fact that nothing needs to be done for now.
 */
public class OutputTransferWS {
	/**
	 * Attributes
	 */
	// FPDU object to be sent by the inter-server
	private ArrayList<FPDU> listFpdu;
	// Indicate if connectionWS needs to send a fpdu
	// Indeed, this.fpdu can be null not because of an error but only because
	// the WS does not need to send anything
	private boolean handleFPDU;
	// State of a transfer from 0 if started -1 if not yet started
	int transferState;
	
	/**
	 * @briefConstructor
	 * @param fpdu
	 * @param handleFPDU
	 */
	public OutputTransferWS(ArrayList<FPDU> listFpdu, boolean handleFPDU, int state) {
		super();
		this.listFpdu = listFpdu;
		this.handleFPDU = handleFPDU;
		this.transferState = state;
	}
	
	public boolean isHandleFPDU() {
		return handleFPDU;
	}
	
	public void setHandleFPDU(boolean handleFPDU) {
		this.handleFPDU = handleFPDU;
	}

	public int getTransferState() {
		return transferState;
	}

	public void setTransferState(int transferState) {
		this.transferState = transferState;
	}

	public ArrayList<FPDU> getListFpdu() {
		return listFpdu;
	}

	public void setListFpdu(ArrayList<FPDU> listFpdu) {
		this.listFpdu = listFpdu;
	}
}
