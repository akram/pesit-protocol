package pesitLog;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Ignore;
import org.junit.Test;

import fpdu.FPDU;
import fpdu.FPDUParameter;
import fpdu.FPDU.EnumFPDU;

public class TestLog {

	@Test
	/**
	 * Test of logLocal with the case of a correct CONNECT FPDU
	 * Expected return:
	 * *Date* Sent: FPDU CONNECT   to Bob.
	 * 		We ask Bob for a connection. Waiting for ACONNECT.
	 */
	public void testLogLocalNominalConnect() {
		FPDUParameter param = new FPDUParameter("Alice", "Bob");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.CONNECT);
		
		String result = PeSITLog.logLocal(fpdu);
		assertThat(result, containsString("Sent: " +
				"FPDU CONNECT   to Bob. We ask Bob for a connection. "
				+ "Waiting for ACONNECT."));
	}
	
	@Test
	/**
	 * Test of logLocal with the case of a correct RELEASE FPDU
	 * Expected return:
	 * *Date* Sent: FPDU RELEASE   to Bob.
	 * 		We will close the connection with Bob. Waiting for RELCONF.
	 */
	public void testLogLocalNominalRelease() {
		FPDUParameter param = new FPDUParameter("Alice", "Bob");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.RELEASE);
		
		String result = PeSITLog.logLocal(fpdu);
		assertThat(result, containsString("Sent: " +
				"FPDU RELEASE   to Bob. " + 
				"We will close the connection with Bob. Waiting for RELCONF."));
	}
	
	@Test
	/**
	 * Test of logLocal with the case of a correct RELCONF FPDU
	 * Expected return:
	 * *Date* Sent: FPDU RELCONF   to Bob. We are now disconnected.
	 */
	public void testLogLocalNominalRelconf() {
		FPDUParameter param = new FPDUParameter("Alice", "Bob");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.RELCONF);
		
		String result = PeSITLog.logLocal(fpdu);
		assertThat(result, containsString("Sent: " +
				"FPDU RELCONF   to Bob. We are now disconnected."));
	}
	
	@Test
	/**
	 * Test of logLocal with the case of a correct DTF FPDU
	 * Expected return:
	 * *Date* Sent: FPDU DTF   to Bob. Sending data to Bob.
	 */
	public void testLogLocalNominalDTF() {
		FPDUParameter param = new FPDUParameter("Alice", "Bob");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.DTF);
		
		String result = PeSITLog.logLocal(fpdu);
		assertThat(result, containsString("Sent: " +
				"FPDU DTF   to Bob. Sending data to Bob."));
	}
	
	@Test
	/**
	 * Test of logLocal with a null FPDU
	 * Expected return: Error null FPDU
	 */
	public void testLogLocalNullFPDU() {
		FPDU fpdu = null;
		
		String result = PeSITLog.logLocal(fpdu);
		assertEquals("failure - should be equal", "Error null FPDU", result);
	}
	
	@Test
	@Ignore
	/**
	 * Test of logLocal with a FPDU that has null type.
	 * (this should never happen)
	 * Expected return: Error null FPDU
	 * This test doesn't pass because when we create a FPDU without telling
	 * its type, the type is DTF (0).
	 */
	public void testLogLocalNullType() {
		FPDUParameter param = new FPDUParameter("Alice", "Bob");

		FPDU fpdu = new FPDU();
		fpdu.setParameter(param);
		
		System.out.println("\n\n\n!!!!! " + fpdu.getType() + " !!!!!\n\n\n");
		
		String result = PeSITLog.logLocal(fpdu);
		assertEquals("failure - should be equal", "Error null FPDU", result);
	}
	
	@Test
	@Ignore
	/**
	 * Test of logLocal with a FPDU that has null parameters.
	 * (this should never happen)
	 * No specific return is expected has this case is not handled
	 */
	public void testLogLocalNullParameters() {
		FPDU fpdu = new FPDU();
		
		fpdu.setType(EnumFPDU.CONNECT);
		
		String result = PeSITLog.logLocal(fpdu);
		assertThat(result, containsString("Sent: " + 
				"FPDU CONNECT  . We ask null for a connection."));
		//assertEquals("failure - should be equal", "Error null FPDU", result);
	}

	@Test
	/**
	 * Test of logRemote with a correct CONNECT FPDU
	 * Expected return:
	 * *Date* Received: FPDU CONNECT   from Bob. Bob wants to connect.
	 */
	public void testLogRemoteNominalConnect() {
		FPDUParameter param = new FPDUParameter("Bob", "Alice");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.CONNECT);
		
		String result = PeSITLog.logRemote(fpdu);
		assertThat(result, containsString("Received: " +
				"FPDU CONNECT   from Bob. Bob wants to connect."));
	}
	
	@Test
	/**
	 * Test of logRemote with a correct ACONNECT FPDU
	 * Expected return:
	 * *Date* Received: FPDU ACONNECT   from Bob.
	 * 		Connection accepted. We are now connected to Bob.
	 */
	public void testLogRemoteNominalAConnect() {
		FPDUParameter param = new FPDUParameter("Bob", "Alice");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.ACONNECT);
		
		String result = PeSITLog.logRemote(fpdu);
		assertThat(result, containsString("Received: " +
				"FPDU ACONNECT   from Bob. Connection accepted. " +
				"We are now connected to Bob."));
	}
	
	@Test
	/**
	 * Test of logRemote with a correct RCONNECT FPDU
	 * Expected return:
	 * *Date* Received: FPDU RCONNECT   from Bob. Connection refused.
	 */
	public void testLogRemoteNominalRConnect() {
		FPDUParameter param = new FPDUParameter("Bob", "Alice");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.RCONNECT);
		
		String result = PeSITLog.logRemote(fpdu);
		assertThat(result, containsString("Received: " +
				"FPDU RCONNECT   from Bob. Connection refused."));
	}
	
	@Test
	/**
	 * Test of logRemote with a correct READ FPDU
	 * Expected return:
	 * *Date* Received: FPDU READ   from Bob. File reading request from Bob.
	 */
	public void testLogRemoteNominalRead() {
		FPDUParameter param = new FPDUParameter("Bob", "Alice");

		FPDU fpdu = new FPDU(param);
		fpdu.setType(EnumFPDU.READ);
		
		String result = PeSITLog.logRemote(fpdu);
		assertThat(result, containsString("Received: " +
				"FPDU READ   from Bob. File reading request from Bob."));
	}
	
	@Test
	/**
	 * Test of logRemote with a null FPDU
	 * Expected return: Error null FPDU
	 */
	public void testLogRemoteNullFPDU() {
		FPDU fpdu = null;
		
		String result = PeSITLog.logRemote(fpdu);
		assertEquals("failure - should be equal", "Error null FPDU", result);
	}

}
