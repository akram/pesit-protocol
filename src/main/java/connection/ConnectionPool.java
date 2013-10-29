package connection;

import java.util.Iterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @brief	A connection pool must be instantiate for each running ConnectionWS
 * 			entity. A pool provides the WS with Connection objects and
 * 			"recycle" them.
 *
 */
public class ConnectionPool {
	
	
	/**
	 * Attributes
	 */
	private static ConnectionFactory factory = new ConnectionFactory();
    private static int curConnections = 0;
    // Name of the local bank
    private static String localIdentity = "Local";
    // Number max of connections
	public static final int maxConnections = 1000;
    // Connection we have loaned out
    private static HashMap<String, Connection> outConnections = 
    	new HashMap<String, Connection>(maxConnections);
	// Connection we currently have waiting
    private static List<Connection> inConnections = 
    	new LinkedList<Connection>( );
    
    /**
     * @brief	Retrieve a resource from the pool
     * @return	connection or null when a local user 
     * 			is already using  the service
     * @throws ConnectionException 
     * @throws Exception
     */
    public static Connection getConnection(String bank) 
    		throws ConnectionException {
    	Connection c = null;
    	// Check if the bank is already using the service
    	if (outConnections.get(bank) != null) {
    		return outConnections.get(bank);
    	}
        // Check if a connection is already created
        if (!inConnections.isEmpty( )) {
            c = inConnections.remove(0);
            // Reset of the connection
            if (!factory.validateConnection(c)) {
                c = (Connection) factory.createConnection();
                c.setLocalIdentity(localIdentity);
                // TODO : find a way to reset idConnection
            }
            c.setRemoteIdentity(bank);
            outConnections.put(bank, c);
        } else if (curConnections < maxConnections) {

        	System.out.println("after outCi");
        	// Create a new resource if we haven't
        	// reached the limit yet    
        	curConnections++;
            c = (Connection) factory.createConnection();
            c.setLocalIdentity(localIdentity);
            c.setRemoteIdentity(bank);
            c.setIdConnection(curConnections);
            outConnections.put(bank, c);
        }
        
        // If no resources are available, return null for the moment
        return c;
    }
    
    /**
     * @brief	Retrieve a resource from the pool from the idConnection
     * @return	connection or null when a local user 
     * 			is already using  the service
     * @throws Exception
     */
    public static Connection getConnection(int idConnection) {
    	Connection c = null;
    	Iterator<Entry<String, Connection>> it = outConnections.entrySet().iterator();
    	while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Connection currentConnection = (Connection) pairs.getValue();
			if (currentConnection.getIdConnection() == idConnection) {
				c = currentConnection;
				break;
			}
    	}
        return c;
    }
    
    
    /**
     * @brief	Return a resource to the pool
     * @param o
     */
    public static void returnConnection(String bank, Connection c) {
        if (outConnections.remove(bank) == null)
            throw new IllegalStateException("Returned item not in pool");
        // Recycle connection
        inConnections.add(c);
    }

	public static void setLocalIdentity(String localIdentity) {
		ConnectionPool.localIdentity = localIdentity;
	}

	public static String getLocalIdentity() {
		return localIdentity;
	}
	
	public static HashMap<String, Connection> getOutConnections() {
		return outConnections;
	}

	public static void setOutConnections(HashMap<String, Connection> outConnections) {
		ConnectionPool.outConnections = outConnections;
	}

	public static int getCurConnections() {
		return curConnections;
	}

	public static void setCurConnections(int curConnections) {
		ConnectionPool.curConnections = curConnections;
	}
	
}
