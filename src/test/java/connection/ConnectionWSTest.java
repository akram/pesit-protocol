package connection;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import connection.Connection.EnumState;
import fpdu.FPDU;
import fpdu.FPDUParameter;
import fpdu.WrongFPDUException;
import fpdu.FPDU.EnumFPDU;

/**
 * 
 * @brief	Test
 * 				@test	Test the http message parser
 * 				@result	parsing of the "" string should return null.
 * 				
 * 				@test	Test disconnection
 * 				@result	*	close the connection with a bank with which there is
 * 							no connection should raise ConnectionException
 * 						*	close the connection with a bank with which there is
 * 							a connection should trigger the creation of a
 * 							release fpdu and the connection's state should be
 * 							"waitfor_relconf".
 * 						*	close the connection with a bank with which 
 * 							connection is not in the "connected" state
 * 							should raiseWrongFPDUException
 * 				
 * 				@test	Test connection
 * 				@result	one connection at a time should be open by the local
 * 						user and it should trigger the creation of a CONNECT 
 * 						packet.
 * 				
 * 				@test	Test fpdu reception.
 * 				@result	* 	reception of CONNECT fpdu from an unknown remote 
 * 							bank should trigger creation of ACONNECT fpdu.
 * 						* 	reception of CONNECT fpdu from a remote bank which 
 * 							has already ask for a connection should raise
 * 							ConnectionException.
 *  					* 	reception of ACONNECT fpdu from a remote bank as the
 *  						local user did not ask for a connection should raise
 *  						a WrongFPDUException.
 *  					* 	reception of ACONNECT fpdu from a remote bank as the
 *  						local user did not ask for a connection should raise
 *  						a WrongFPDUException.
 *  					* 	reception of RCONNECT, RELEASE or RELCONF fpdu from 
 *  						a remote bank as the local user did not ask for a 
 *  						connection or is not connected to the remote bank 
 *  						should raise a WrongFPDUException.
 *
 */
public class ConnectionWSTest {

	@Test
	public void testMessageParser() {
		ConnectionWS cws = new ConnectionWS();
		OutputConnectionWS ocws;
		try {
			ocws = cws.messageParser("");
			assertNull(ocws);
		} catch (Exception e) {
			fail("testMessageParser : No exception " +
			"should have been raised");
		}		
	}

	@Test
	public void testDisconnect() {
		ConnectionWS cws = new ConnectionWS();
		// Case 1
		System.out.println("testDisconnect : Case 1/3");
		OutputConnectionWS ocws;
		try {
			ocws = cws.disconnect("Remote");
			assertNull(ocws);
			fail("testDisconnect : ConnectionException " +
			"should have been raised");
		} catch (ConnectionException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Case 2
		System.out.println("testDisconnect : Case 2/3");
		Connection c = new Connection("Local", "Remote");
		c.setIdConnection(1);
		c.setRemoteId(2);
		c.setState(EnumState.connected);
		ConnectionPool.getOutConnections().put("Remote", c);
		try {
			ocws = cws.disconnect("Remote");
			assertEquals(EnumState.waitfor_relconf, c.getState());
			assertEquals(EnumFPDU.RELEASE, ocws.getFpdu().getType());					
		} catch (ConnectionException e) {
		} catch (Exception e) {
			fail("testDisconnect : No exception " +
			"should have been raised");
		}
		
		// Case 3
		System.out.println("testDisconnect : Case 3/3");
		c.setState(EnumState.idle);
		try {
			ocws = cws.disconnect("Remote");
			assertNull(ocws);
			fail("testDisconnect : WrongFPDUException " +
			"should have been raised");
		} catch (WrongFPDUException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConnect() {
		ConnectionWS cws = new ConnectionWS();
		// Case 1
		System.out.println("testConnect : Case 1/1");
		OutputConnectionWS ocws;
		try {
			ocws = cws.connect("Remote");
			assertEquals(EnumFPDU.CONNECT, ocws.getFpdu().getType());	
			assertEquals("Remote", ocws.getFpdu().getParameter().getReceiver());
			assertEquals("Local", ocws.getFpdu().getParameter().getSender());
		} catch (Exception e) {
			fail("testConnect : No exception " +
			"should have been raised");
		}
	}
	
	@Test
	public void testFPDUReception() {
		ConnectionWS cws = new ConnectionWS();
		FPDUParameter parameter = new FPDUParameter("Bob", "Local");
		parameter.setLocalConnectionId(1);
		FPDU fpdu = new FPDU(EnumFPDU.CONNECT, parameter);
		
		// Case 1
		System.out.println("testFPDUReception : Case 1/6");
		OutputConnectionWS ocws;
		try {
			ocws = cws.FPDUReception(fpdu);
			assertEquals(EnumFPDU.ACONNECT, ocws.getFpdu().getType());	
			assertEquals("Bob", ocws.getFpdu().getParameter().getReceiver());
			assertEquals("Local", ocws.getFpdu().getParameter().getSender());
		} catch (Exception e) {
			fail("testFPDUReception : No exception " +
			"should have been raised");
		}
		
		// Case 2
		System.out.println("testFPDUReception : Case 2/6");
		try {
			ocws = cws.FPDUReception(fpdu);
			fail("testFPDUReception : ConnectionException " +
			"should have been raised");
		} catch (ConnectionException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Case 3
		System.out.println("testFPDUReception : Case 3/6");
		parameter = new FPDUParameter("Alice", "Local");
		fpdu = new FPDU(EnumFPDU.ACONNECT, parameter);
		try {
			ocws = cws.FPDUReception(fpdu);
			fail("testFPDUReception : WrongFPDUException " +
			"should have been raised");
		} catch (WrongFPDUException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Case 4
		System.out.println("testFPDUReception : Case 4/6");
		try {
			fpdu.setType(EnumFPDU.RCONNECT);
			cws.FPDUReception(fpdu);
			fail("testFPDUReception : WrongFPDUException " +
			"should have been raised");
		} catch (WrongFPDUException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Case 5
		System.out.println("testFPDUReception : Case 5/6");
		try {
			fpdu.setType(EnumFPDU.RELCONF);
			cws.FPDUReception(fpdu);
			fail("testFPDUReception : WrongFPDUException " +
			"should have been raised");
		} catch (WrongFPDUException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Case 6
		System.out.println("testFPDUReception : Case 6/6");
		try {
			fpdu.setType(EnumFPDU.RELEASE);
			cws.FPDUReception(fpdu);
			fail("testFPDUReception : WrongFPDUException " +
			"should have been raised");
		} catch (WrongFPDUException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
