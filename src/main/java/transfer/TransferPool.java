package transfer;

import java.util.LinkedList;
import java.util.List;

import connection.Connection;
import connection.ConnectionException;
import connection.ConnectionPool;
import connection.Connection.EnumState;

/**
 * 
 * @brief	A transfer pool must be instantiate for each running TransferWS
 * 			entity. A pool provides the WS with Transfer objects and 
 * 			"recycle" them.
 *
 */
public class TransferPool {
	/**
	 * Attributes
	 */
	private static TransferFactory factory = new TransferFactory();
	// General counter for all of the transfers
    private static int curTransfers = 0;
	// Number maximum of transfer for 1 connection
	public static final int maxTransfers = 1000;
	// Transfer we currently have waiting
    private static List<Transfer> inTransfers = 
    	new LinkedList<Transfer>( );
    
    /**
     * @brief	Retrieve a resource from the pool
     * @return	transfer or null when a local user 
     * 			is already using  the service
     * @throws TransferException 
     * @throws ConnectionException 
     * @throws Exception
     */
    public static Transfer getTransfer(String bank) 
    	throws TransferException, ConnectionException {
    	Transfer t = null;
    	// Get the connection associated to the bank
    	Connection connection = ConnectionPool.getConnection(
				bank);
		if (connection == null) {
			System.out.println("TRANSFERPOOL Error: No connection for " + bank);
			// Raise exception
			throw new TransferException("TRANSFERPOOL Error: " 
					+ "No connection for " + bank);
		} else if (connection.getState() != EnumState.connected) {
			// No transfer can start if the connection is not in "connected" 
			// state
			System.out.println("TRANSFERPOOL Error: " +
					" Connection with "+ bank +" not in \"connected\" state");
			throw new TransferException("TRANSFERPOOL Error: " +
					" Connection with "+ bank +" not in \"connected\" state");
		}
    	// Get the transfer which file name equals the fileName parameter
		t = connection.getOutTransfer();
    	if ( t != null) {
    		return t;
    	}
        // Check if a transfer is already created
        if (!inTransfers.isEmpty( )) {
            t = inTransfers.remove(0);
            // Reset of the transfer
            if(!factory.validateTransfer(t)) {
                t = (Transfer) factory.createTransfer();
                // TODO : find a way to reset idTransfer
            }
            connection.setOutTransfer(t);
        } else if (curTransfers < maxTransfers) {
        	// Create a new resource if we haven't
        	// reached the limit yet    
        	curTransfers++;
            t = (Transfer) factory.createTransfer();
            t.setTransferId(curTransfers);
            // Add a new transfer to the transfer's map of the connection
            connection.setOutTransfer(t);
        }
        
        // If no resources are available, return null for the moment
        return t;
    }
    
    /**
     * @brief	Return a resource to the pool
     * @param o
     * @throws ConnectionException 
     */
    public static void returnTransfer(String bank, String fileName, 
    		Transfer t) throws ConnectionException {
    	// Get the connection associated to the bank
    	Connection connection = ConnectionPool.getConnection(
				bank);
        if (connection.getOutTransfer() == null)
            throw new IllegalStateException("Returned item not in pool");
        // Recycle transfer
        inTransfers.add(t);
    }

	public static int getCurTransfers() {
		return curTransfers;
	}

	public static void setCurTransfers(int curTransfers) {
		TransferPool.curTransfers = curTransfers;
	}
	
}
