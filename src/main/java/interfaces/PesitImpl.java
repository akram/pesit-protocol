package interfaces;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

//import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.params.HttpMethodParams;

import fpdu.FPDU;
import connection.Connection;
import connection.ConnectionPool;
import connection.ConnectionWS;
import connection.OutputConnectionWS;
import connection.Connection.EnumState;

@WebService(serviceName = "PesitWebservice", name = "PesitWebserviceName", targetNamespace = "http://localhost:8085/pesit")
public class PesitImpl implements PesitInterface {

    public static int timeOut = 5;

    private boolean sendFPDU(FPDU fpdu) {
        // ObjectFactory factory = new ObjectFactory();

        boolean result = false;
        // TODO : test function
        // URL wsdlURL = InterServerOutgoing.WSDL_LOCATION;
        // InterServerOutgoing ss = new InterServerOutgoing(wsdlURL,
        // InterServerOutgoingPortType_InterServerOutgoingPort_Client.SERVICE_NAME);
        // InterServerOutgoingPortType port = ss.getInterServerOutgoingPort();
        //
        // SendFPDU sendFPDU = new SendFPDU();
        // //interserverClient.FPDU newFPDU = factory.createFPDU();
        // //
        // //newFPDU.setParameter((interServerClient.FPDUParameter)fpdu.getParameter());
        // interserverClient.FPDUParameter newFPDUParamter = new
        // interserverClient.FPDUParameter();
        // //newFPDU.setType(factory.createFPDUType(interserverClient.EnumFPDU.ACONNECT));
        // // newFPDU.setParameter(fpdu.getParameter());
        // fpdu.FPDU newfpdu = new fpdu.FPDU();
        // newfpdu.setType(fpdu.FPDU.EnumFPDU.CONNECT);
        // sendFPDU.setFpdu(newfpdu);
        // System.out.println("newfpdu : " + newfpdu);
        // // sendFPDU.setRecipient(fpdu.getParameter().getReceiver());
        // sendFPDU.setRecipient(factory.createSendFPDURecipient(tfpdu.getParameter().getReceiver()));
        //
        //
        // //boolean result = port.sendFPDU(fpdu,
        // fpdu.getParameter().getReceiver());
        // try {
        // SendFPDUResponse response = port.sendFPDU(sendFPDU);
        // result = response.isReturn();
        // }
        // catch (javax.xml.ws.soap.SOAPFaultException e) {
        // System.out.println("SOAPe");
        // e.printStackTrace();
        // }
        // catch (Exception e) {
        // System.out.println("global");
        // }
        // pesit.outgoing.InterServerOutgoing temp = new
        // pesit.outgoing.InterServerOutgoing();
        // result = temp.sendFPDU(fpdu, fpdu.getParameter().getReceiver());
        System.out.println("Youhou");

        return result;
    }

    @WebMethod
    public boolean connect(String bankName) {
        // Verify if connection already exists
        Connection currentConnection = null;
        try {
            currentConnection = ConnectionPool.getConnection(bankName);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // Test the connection state : if already connected then return true
        if (currentConnection != null && currentConnection.getState() == EnumState.connected) {
            return true;
        } else if (currentConnection != null && currentConnection.getState() == EnumState.idle) {
            // if idle then ask for a connection
            ConnectionWS currentConnectionWS = new ConnectionWS();
            OutputConnectionWS outputToSend;
            try {
                outputToSend = currentConnectionWS.messageParser("connect_" + bankName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            FPDU fpduToSend = outputToSend.getFpdu();

            // Send FPDU to the interserver
            if (!sendFPDU(fpduToSend)) {
                // If sendFPDU fail then return false
                return false;
            }

        }

        // Get the connection and wait for connected state
        try {
            currentConnection = ConnectionPool.getConnection(bankName);
        } catch (Exception e1) {
            return false;
        }
        // If null connection return false
        if (currentConnection == null) {
            return false;
        }
        // Wait for connected state
        for (int i = 0; i < PesitImpl.timeOut; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Check state of currentConnection
            if (currentConnection.getState() == EnumState.connected) {
                return true;
            } else if (currentConnection.getState() == EnumState.connection_refused) {
                break;
            }
        }

        // If connection is still idle then the CONNECT message was lost
        // Otherwise it means that the connection is in a particular state for a
        // client and then
        // - if transfer_in_progress return true
        // - if wait_for_relconf then return false
        // TODO : more cases
        if (currentConnection.getState() == EnumState.idle) {
            // Remove connection from connection pool
            ConnectionPool.returnConnection(bankName, currentConnection);
            return false;
        } else if (currentConnection.getState() == EnumState.waitfor_relconf) {
            return false;
        }

        return false;
    }

    @WebMethod
    public boolean disconnect(String bankName) {
        // Check for connection state
        Connection currentConnection = null;
        try {
            currentConnection = ConnectionPool.getConnection(bankName);
        } catch (Exception e1) {
            return false;
        }

        // If idle then return true
        // If connection busy then return false
        if (currentConnection.getState() == Connection.EnumState.idle) {
            return true;
        } else if (currentConnection.getState() == Connection.EnumState.file_opened
                || currentConnection.getState() == Connection.EnumState.file_selected
                || currentConnection.getState() == Connection.EnumState.transfer_inprogress
                || currentConnection.getState() == Connection.EnumState.waitfor_acconnect
                || currentConnection.getState() == Connection.EnumState.waitfor_relconf) {
            return false;
        }
        // test on idle, waitfor_aconnect, transfer_inprogress, transfer_waiting
        ConnectionWS currentConnectionWS = new ConnectionWS();
        OutputConnectionWS outputToSend;
        try {
            outputToSend = currentConnectionWS.messageParser("release_" + bankName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception 1");
            return false;
        }

        FPDU fpduToSend = outputToSend.getFpdu();

        // Send FPDU to the interserver
        if (!sendFPDU(fpduToSend)) {
            // If sendFPDU fail then return false
            return false;
        }

        for (int i = 0; i < PesitImpl.timeOut; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Check state of currentConnection
            if (currentConnection.getState() == EnumState.idle) {
                return true;
            }
        }

        // Update connection state from connection pool
        try {
            ConnectionPool.getConnection(bankName).setState(EnumState.connected);
        } catch (Exception e) {
            System.out.println("exception 3");
            return false;
        }

        return false;
    }

    @WebMethod
    public void getFile(String fileName) {

        // TODO : Get bankName corresponding to the fileName from persistence
        String bankName = null;

        // Call connect
        boolean connectionResult = this.connect(bankName);
        // If connection refused then return a fault
        if (connectionResult == false) {
            // TODO : return false or raise exception
        }

        // Call to pesit get file

        // Call disconnect
        this.disconnect(bankName);
    }

    @WebMethod
    public int getStatus(String fileName) {
        // TODO
        return 0;
    }

    @WebMethod
    public void FPDUReception(FPDU fpdu) {
        ConnectionWS currentConnectionWS = new ConnectionWS();
        OutputConnectionWS outputToSend = null;
        try {
            outputToSend = currentConnectionWS.FPDUReception(fpdu);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (outputToSend == null) {
            // TODO : raise exception
        }

        // If fpdu need to be sent
        if (outputToSend.isHandleFPDU()) {
            FPDU fpduToSend = outputToSend.getFpdu();
            // Send FPDU to the interserver
            if (!sendFPDU(fpduToSend)) {
                // If sendFPDU fail then return false
                // : TODO : raise Exception or return false
            }
        }
    }

    public static void main(String[] args) {

    }

}