package connection;

import java.util.Date;
import java.util.StringTokenizer;

import javax.jws.WebMethod;
import javax.jws.WebService;

import pesitLog.PeSITLog;

import connection.Connection.EnumState;
import fpdu.FPDU;
import fpdu.FPDUParameter;
import fpdu.WrongFPDUException;
import fpdu.FPDU.EnumFPDU;

/**
 * @brief	Web service handling FPDU for connection and release of connection,
 * 			from local or remote access.
 */
@WebService
public class ConnectionWS {
	/**
	 * Constructor
	 */
	public ConnectionWS() {
		super();		
	}
	
	/**
	 * @brief 	Parse a string from a HTTP message in order to call 
	 * 			the right method connect / disconnect
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	public OutputConnectionWS messageParser (String message) throws Exception {
		StringTokenizer st = new StringTokenizer(message, "_");  	
		OutputConnectionWS output = null;
    	if ( st.countTokens() != 0 ) {
	    	String token = (st.nextToken()).toLowerCase();
			if (token.equals("connect")) {
				// TODO : verify and change this to correspond with FPDU
				String shortName = st.nextToken().toLowerCase();
				output = this.connect(shortName);
			} else if (token.equals("release")) {
				String shortName = st.nextToken().toLowerCase();
				output = this.disconnect(shortName);
			}
		}
    	return output;
	}
	
	/**
	 * @brief Method for local disconnection
	 * @param remoteBank
	 * @return
	 * @throws Exception
	 */
	public OutputConnectionWS disconnect (String bankName) throws Exception {
		FPDU fpduResponse = null;
		Date date;
		FPDUParameter parameter;
		Connection connection;
		// The local bank wants to close the connection
		connection = ConnectionPool.getOutConnections()
			.get(bankName);
		System.out.println("debug remote bank name : " 
				+ bankName);
		
		if (connection == null) {
			System.out.println("CONNECTION WS Error: " +
					"Cannot release null connection");
			throw new ConnectionException("CONNECTION WS: Error release"); 
		}
		if (connection.getState() != EnumState.connected) {
			System.out.println("CONNECTION WS Error: " +
					"Cannot release connection (connection " +
					"was not open in the first place)");
			// Raise exception
			throw new WrongFPDUException("CONNECTION WS Error: " +
					"Cannot release connection (connection " +
					"was not open in the first place)");
		}
		connection.setState(EnumState.waitfor_relconf);
		// Create FPDU relconf
		parameter = new FPDUParameter(ConnectionPool.getLocalIdentity(), 
				bankName);
		date = new Date();
		parameter.setDate(date);
		parameter.setLocalConnectionId(connection.getIdConnection());
		parameter.setRemoteConnectionId(connection.getRemoteId());
		fpduResponse = new FPDU(EnumFPDU.RELEASE, parameter);
		
		// If there is a message, we log it
		if (fpduResponse != null) {
			PeSITLog.logLocal(fpduResponse);
		}
		OutputConnectionWS output = new OutputConnectionWS(fpduResponse, true);
		return output;
	}
	
	/**
	 * @brief Method for local connection 
	 * @param bank
	 * @return
	 * @throws Exception
	 */
	public OutputConnectionWS connect (String bank) throws Exception {
		FPDU fpdu = null;
		Connection connection;
		System.out.println("REMOTE BANK: " + bank);
		
		System.out.println("CONNECTION WS: " +
				"Local bank asked for connection");
		connection = ConnectionPool.getConnection(bank);
		connection.setState(EnumState.waitfor_acconnect);
		Date date = new Date();
		// Send the CONNECT FPDU
		FPDUParameter parameter = new FPDUParameter("Local", bank);
		// Set date
		parameter.setDate(date);
		// Set local identifier
		parameter.setLocalConnectionId(connection.getIdConnection());
		fpdu = new FPDU(EnumFPDU.CONNECT, parameter);
		System.out.print("CONNECTION WS: Print FPDU -> ");
		System.out.println(fpdu);	
		// If there is a message, we log it
		if (fpdu != null) {
			PeSITLog.logLocal(fpdu);
		}
		OutputConnectionWS output = new OutputConnectionWS(fpdu, true);
		return output;
	}

	/**
	 * @brief	Handle a pesit FPDU which has been received
	 * @param fpdu
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	public OutputConnectionWS FPDUReception (FPDU fpdu) 
		throws ConnectionException, WrongFPDUException {
		Connection connection;
		FPDU fpduResponse = null;
		FPDUParameter parameter;
		String remoteBank = fpdu.getParameter().getSender();
		Date date;
		Boolean respBool = false;
		
		// First we log the received FPDU
		PeSITLog.logRemote(fpdu);
		
		switch (fpdu.getType()) {
			case CONNECT:
				System.out.println(remoteBank +
						" asked for connection");
				connection = ConnectionPool.getConnection(
						remoteBank);
				// If a bank is already using the service
				if (connection == null) {
					System.out.println("CONNECTION WS Error: " 
							+ ConnectionPool.getLocalIdentity() +
							" already asked for a connection");
					// Raise exception
					throw new ConnectionException("CONNECTION WS " +
							"Error: " 
							+ ConnectionPool.getLocalIdentity() +
							" already asked for a connection");
				} else if (connection.getState() != EnumState.idle) {
					System.out.println("Error: " 
							+ remoteBank +
							" cannot ask for connection as a connection" +
							" already exists");
					throw new ConnectionException("CONNECTION WS " +
							"Error: " 
							+ remoteBank +
							" cannot ask for connection as a connection" +
							" already exists");
				}
				connection.setState(EnumState.connected);
				// Set connection ids
				connection.setRemoteId(
						fpdu.getParameter().getLocalConnectionId());
				// If the new connection is open, send aconnect
				parameter = new FPDUParameter("Local",
						fpdu.getParameter().getSender());
				date = new Date();
				parameter.setDate(date);
				parameter.setLocalConnectionId(connection.getIdConnection());
				parameter.setRemoteConnectionId(connection.getRemoteId());
				fpduResponse = new FPDU(EnumFPDU.ACONNECT, parameter);
				respBool = true;
				break;
			case ACONNECT:
				connection = ConnectionPool.getOutConnections()
					.get(remoteBank);
				if (connection == null || 
					connection.getState() != EnumState.waitfor_acconnect) {		
					System.out.println("CONNECTION WS Error: " +
							"Cannot ack connect  ( " +
							"did not send connect in the first place)");
					// Raise exception
					throw new WrongFPDUException("CONNECTION WS Error: " +
							"Cannot ack connect  ( " +
							"did not receive connect in the first place)");
				}
				connection.setState(EnumState.connected);
				// A transfer is now ready to start
				break;
			case RCONNECT:
				connection = ConnectionPool.getOutConnections()
					.get(remoteBank);
				if (connection == null ||
					connection.getState() != EnumState.waitfor_acconnect) {		
					System.out.println("CONNECTION WS Error: " +
							"Cannot refuse connect  ( " +
							"did not receive connect in the first place)");
					// Raise exception
					throw new WrongFPDUException("CONNECTION WS Error: " +
							"Cannot refuse connect  ( " +
							"did not receive connect in the first place)");
				}
				connection.setState(EnumState.connection_refused);
				// A transfer is now ready to start
				break;
			case RELEASE:
				// Here the remote bank wants to close the connection
				
				// We have to check if the remote bank is the actual seeker
				// of the connection , otherwise it cannot ask for a release
				
				connection = ConnectionPool.getOutConnections()
					.get(remoteBank);
				if (connection == null ||
					connection.getState() != EnumState.connected) {
					System.out.println("CONNECTION WS Error: " +
							"Cannot release connection (connection " +
							"was not open in the first place)");
					// Raise exception
					throw new WrongFPDUException("CONNECTION WS Error: " +
							"Cannot release connection (connection " +
							"was not open in the first place)");
					// TODO: handle lost of release packets
				}
				connection.setState(EnumState.idle);
				// Release connection
				ConnectionPool.returnConnection(remoteBank, connection);

				// Create FPDU relconf
				parameter = new FPDUParameter("Local", remoteBank);
				date = new Date();
				parameter.setDate(date);
				parameter.setLocalConnectionId(connection.getIdConnection());
				parameter.setRemoteConnectionId(connection.getRemoteId());
				fpduResponse = new FPDU(EnumFPDU.RELCONF, parameter);
				respBool = true;
				break;
			case RELCONF:
				// The connection is closed
				connection = ConnectionPool.getOutConnections()
					.get(remoteBank);
				if (connection == null || connection.getState() != 
					EnumState.waitfor_relconf) {
					System.out.println("CONNECTION WS Error: " +
							"Received relconf but was not waiting for" +
							" one");
					// Raise exception
					throw new WrongFPDUException("CONNECTION WS Error: " +
							"Received relconf but was not waiting for one");
				}
				connection.setState(EnumState.idle);
				ConnectionPool.returnConnection(remoteBank, connection);
				break;
			default:
				break;
		}
		
		// If there is a response, we log it
		if (fpduResponse != null) {
			PeSITLog.logLocal(fpduResponse);
		}
		OutputConnectionWS output = new OutputConnectionWS(fpduResponse, 
				respBool);
		return output;
	}	
}
