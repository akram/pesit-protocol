package connection;

import fpdu.FPDU;

/**
 * Class allowing the communication with the "inter server" entity in order to
 * provide it with 
 * 		a FPDU object to send or
 * 		an exception to handle or 
 * 		information concerning the fact that nothing needs to be done for now.
 */
public class OutputConnectionWS {
	/**
	 * Attributes
	 */
	// FPDU object to be sent by the inter-server
	private FPDU fpdu;
	// Indicate if connectionWS needs to send a fpdu
	// Indeed, this.fpdu can be null not because of an error but only because
	// the WS does not need to send anything
	private boolean handleFPDU;

	/**
	 * @briefConstructor
	 * @param fpdu
	 * @param handleFPDU
	 */
	public OutputConnectionWS(FPDU fpdu, boolean handleFPDU) {
		super();
		this.fpdu = fpdu;
		this.handleFPDU = handleFPDU;
	}
	
	public FPDU getFpdu() {
		return fpdu;
	}
	
	public void setFpdu(FPDU fpdu) {
		this.fpdu = fpdu;
	}
	
	public boolean isHandleFPDU() {
		return handleFPDU;
	}
	
	public void setHandleFPDU(boolean handleFPDU) {
		this.handleFPDU = handleFPDU;
	}
}
