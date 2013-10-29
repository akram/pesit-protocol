package connection;

import connection.Connection.EnumState;

public class ConnectionFactory {
	
	public Connection createConnection(){
		Connection c = new Connection();
		return c;
	}

	/**
	 * @throws ConnectionException 
	 * @brief	Check that a returned Connection is valid
 	 *			and reset the state to idle
	 */
    public boolean validateConnection(Connection o) throws ConnectionException {
    	boolean b = false;
    	if (o != null) {
	    	if (!(o instanceof Connection)) {
	            b = false;
	        } else { 
		        Connection c = (Connection) o;
		        c.setState(EnumState.idle); 
		        b = true;
	        }
    	} else {
    		throw new ConnectionException("Connection factory : Null " +
    				"connection to be validated");
    	}
    	return b;
    }
}
