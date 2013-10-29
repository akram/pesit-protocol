package connection;

import static org.junit.Assert.*;

import org.junit.Test;

import connection.Connection.EnumState;

/**
 * 
 * @brief Test
 * @test Creation of a Connection object without parameters
 * @result the connection should not be null
 * @test Validation of a Connection object
 * @result * the validation of a regular connection should put the state of the
 *         connection to "idle". * the validation of a null connection should
 *         raise a ConnectionException.
 * 
 */
public class ConnectionFactoryTest {

    ConnectionFactory cf = new ConnectionFactory();

    @Test
    public void testCreateConnection() {
        Connection c = cf.createConnection();
        System.out.println("testConnectionFactory : Case 1/1");
        assertNotNull(c);
    }

    @Test
    public void testValidateConnection() throws ConnectionException {
        Connection c = cf.createConnection();
        // Case 1
        System.out.println("testConnectionFactory : Case 1/2");
        assertTrue(cf.validateConnection(c));
        assertEquals(EnumState.idle, c.getState());

        // Case 2
        System.out.println("testConnectionFactory : Case 2/2");
        c = null;
        try {
            cf.validateConnection(c);
            fail("testConnectionFactory : NullPointerException " + "should have been raised");
        } catch (ConnectionException e) {
        }
    }

}
