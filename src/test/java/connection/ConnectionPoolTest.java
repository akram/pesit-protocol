package connection;

import static org.junit.Assert.*;

import org.junit.Test;

import connection.Connection.EnumState;

/**
 * 
 * @brief	Test
 * 				@test	Retrieve a connection from the ConnectionPool with a
 * 						bank name which has never ask for a connection
 * 				@result	the connection should be create for the remote bank
 * 						with the "idle" state.
 * 				@test	Retrieve a connection from the ConnectionPool with a
 * 						connection id already existing in the pool
 * 				@result	the connection should be retrieved successfully.
 * 				@test	Retrieve a connection from the ConnectionPool with a
 * 						connection id which is not in the pool.
 * 				@result	no connection should be retrieved (null).
 * 				
 * 				@test	Return of a Connection object which bank name is not
 * 						in the pool.
 * 				@result	IllegalStateException should be raised.
 * 				@test	Return of a Connection object which bank name is
 * 						in the pool.
 * 				@result	the connection should not be in the pool anymore and
 * 						asking for this connection in the pool should return 
 * 						null.
 *
 */
public class ConnectionPoolTest {

	@Test
	public void testGetConnection() throws ConnectionException {
		Connection c = ConnectionPool.getConnection("bankName");
		
		System.out.println("testGetConnection : Case 1/3");
		if (c != null) {
			assertEquals(EnumState.idle, c.getState());
			assertEquals("Local", c.getLocalIdentity());
			assertEquals("bankName",c.getRemoteIdentity());
			assertEquals(ConnectionPool.getCurConnections(), 
					c.getIdConnection());
		}
		
		System.out.println("testGetConnection : Case 2/3");
		c = ConnectionPool.getConnection(c.getIdConnection());
		assertNotNull(c);
		
		System.out.println("testGetConnection : Case 3/3");
		c = ConnectionPool.getConnection(2);
		assertNull(c);
	}
	
	@Test
	public void testReturnConnection() throws ConnectionException {
		Connection c = ConnectionPool.getConnection("bankName");
		
		System.out.println("testReturnConnection : Case 1/2");
		try {
			ConnectionPool.returnConnection("wrongBankName", c);
			fail("testReturnConnection : IllegalStateException " +
			"should have been raised");
		} catch (IllegalStateException e) {}
		
		System.out.println("testReturnConnection : Case 2/2");
		int idConnection = c.getIdConnection();
		ConnectionPool.returnConnection("bankName", c);
		c = ConnectionPool.getConnection(idConnection);
		assertNull(c);
	}

}
