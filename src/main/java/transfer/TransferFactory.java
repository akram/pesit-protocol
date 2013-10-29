package transfer;

import transfer.Transfer.EnumTransferState;;

public class TransferFactory {
	
	public Transfer createTransfer(){
		Transfer t = new Transfer();
		return t;
	}

	/**
	 * @brief	Check that a returned Transfer is valid
 	 *			and reset the state to idle
	 */
    public boolean validateTransfer(Transfer t) {
    	boolean b = false;
    	if (t != null) {
	    	if (!(t instanceof Transfer)) {
	            b = false;
	        } else { 
		        Transfer c = (Transfer) t;
		        c.setState(EnumTransferState.connected); 
		        b = true;
	        }
    	} else {
    		throw new NullPointerException("Transfer factory : Null " +
    				"transfer to be validated");
    	}
    	return b;
    }
}
