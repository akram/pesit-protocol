package connection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import connection.Connection.EnumState;


/**
 * 
 * @brief	Test
 * 				@test	Creation of a Connection object 
 * 				@result	the connection should not be null, its state
 * 						should be "idle" and its attributes "LocalIdentity"
 * 						and "remoteIdentity" should be the same as the ones
 * 						in the constructor's parameters.
 *
 */
public class ConnectionTest {
	
	@Test
	public void testConnection() {
		String localIdentity = "Local";
		String remoteIdentity = "Remote";
		Connection c = new Connection(localIdentity, remoteIdentity);
		System.out.println("testConnection : Case 1/1");
		assertEquals(EnumState.idle, c.getState());
		assertEquals("Local", c.getLocalIdentity());
		assertEquals("Remote", c.getRemoteIdentity());
	}
}
