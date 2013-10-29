package transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.jws.WebMethod;

import org.quartz.jobs.ee.jms.SendTopicMessageJob;

import connection.Connection;
import connection.ConnectionException;
import connection.ConnectionPool;

import pesitLog.PeSITLog;
import transfer.Transfer.EnumTransferState;

import fpdu.*;
import fpdu.FPDU.EnumFPDU;

/**
 * @brief The WS for the file transfer
 */
public class TransferWS {

	// Maximaum size for one article in bytes
	// TODO déterminer la MTU
	public static int articleMaximumSize = 1000;
	
	public TransferWS () {
		super();
	}
	
	public OutputTransferWS startTransfer(String idFile, String remoteBank) {
		Boolean respBool = false;
		ArrayList<FPDU> listFpduResponse = new ArrayList<FPDU>();
		int transferState = -1;
		// TODO
		// Retrieve remote id
		// Appel persistence
		// Recupération des id de connection
		// Récupération de la connexion
		// Création de l'objet transfer
		OutputTransferWS output = new OutputTransferWS(listFpduResponse, 
				respBool, transferState);
		return output;	
	}
	
	/**
	 * @brief Creation of one enumType fpdu (without payload)
	 * @param c
	 * @param localBank
	 * @param remoteBank
	 * @param enumType
	 * @return
	 */
	public FPDU createFPDU (Connection c, String localBank, String remoteBank, 
			EnumFPDU enumType) {
		FPDUParameter parameter = new FPDUParameter(localBank, remoteBank);
		Date date = new Date();
		parameter.setDate(date);
		parameter.setLocalConnectionId(c.getIdConnection());
		parameter.setRemoteConnectionId(c.getRemoteId());
		FPDU fpduResponse = new FPDU(enumType, parameter);
		return fpduResponse;
	}
	
	/**
	 * @brief	Create all fpdu (DTFDA, DTFMA, DTFFA) for a file transfer
	 * 			TODO : See how to inform the interface of the transfer's state
	 * 					using flows 
	 * 			NB : No payload for DTFFA
	 * @param c
	 * @return
	 * @throws TransferException 
	 * @throws WrongFPDUException 
	 */
	public ArrayList<FPDU> sendDTFFPDU(Connection c, String remoteBank) 
			throws TransferException, WrongFPDUException {
		ArrayList<FPDU> listFPDU = new ArrayList<FPDU>();
		FPDU fpduResponse;

		// Test if the connecction is valide
		if (c == null) {
			System.out.println("sendDTFFPDU Error: Null connection");
			// Raise exception
			throw new TransferException("sendDTFFPDU Error: Null connection");
		} 
		// Retrieve the current transfer associated to the connection
		Transfer transfer = c.getOutTransfer();		
		
		System.out.println("Start creation of all FPDU.DTF*");
		if (transfer.getState() != 
			EnumTransferState.transfered) {
			System.out.println("sendDTFFPDU Error: Not in the right " + 
					"to start actual transfer ");
			throw new WrongFPDUException("sendDTFFPDU Error: Not in the right" + 
					" to start sending DTF fpdu");
		}	
		// Changing of the state of the transfer
		//transfer.setState(EnumTransferState.read_file);
		
		// Create the DTF fpdu
		fpduResponse = createFPDU (c, ConnectionPool.getLocalIdentity(), 
				remoteBank, EnumFPDU.DTF);
		listFPDU.add(fpduResponse);
		
		// If a file segmentation is required
		if (transfer.getFileSize() > articleMaximumSize) {
			// Segmentation of the files into several "articles"
			int nbFullArticle = transfer.getFileSize() / articleMaximumSize;
			
			//Create DTFDA fpdu
			fpduResponse = createFPDU (c, ConnectionPool.getLocalIdentity(), 
					remoteBank, EnumFPDU.DTFDA);
			// No payload: only for the DTFMA
			listFPDU.add(fpduResponse);
			
			// Create all DTFMA fpdu
			int id;
			for (id = 0; id < nbFullArticle; id++) {
				fpduResponse = createFPDU(c, ConnectionPool.getLocalIdentity(), 
						remoteBank, EnumFPDU.DTFMA);
				FPDUParameter parameter = fpduResponse.getParameter();
				parameter.setDataSize(articleMaximumSize);
				// TODO : parameter.setPayload
				listFPDU.add(fpduResponse);
			}
			// Creation of the last DTFMA fpdu
			if (transfer.getFileSize() % 2 != 0) {
				fpduResponse = createFPDU (c, ConnectionPool.getLocalIdentity(), 
						remoteBank, EnumFPDU.DTFMA);
				FPDUParameter parameter = fpduResponse.getParameter();
				parameter.setDataSize(transfer.getFileSize() -
						nbFullArticle * articleMaximumSize);
				listFPDU.add(fpduResponse);
			}
			// Create one DTFFA fpdu
			fpduResponse = createFPDU (c, ConnectionPool.getLocalIdentity(), 
					remoteBank, EnumFPDU.DTFFA);
			// no payload for this one
			listFPDU.add(fpduResponse);
		}
		//Create DTF fpdu
		fpduResponse = createFPDU (c, "Local", remoteBank, EnumFPDU.DTFEND);
		listFPDU.add(fpduResponse);
		
		return listFPDU;
	}
	
	/**
	 * @brief	Handle a pesit FPDU which has been received
	 * @param fpdu
	 * @return
	 * @throws ConnectionException 
	 * @throws Exception
	 */
	@WebMethod
	public OutputTransferWS FPDUReception (FPDU fpdu) 
			throws TransferException, WrongFPDUException, ConnectionException {
		int transferState = -1; // i.e. not started yet
		Boolean respBool = false;
		
		ArrayList<FPDU> listFpduResponse = null;
		FPDUParameter parameter = null;
		String remoteBank = fpdu.getParameter().getSender();
		Connection connection = ConnectionPool.getConnection(
				remoteBank);
		if (connection == null) {
			System.out.println("TRANSFER WS Error: No connection for " + 
					remoteBank);
			// Raise exception
			throw new TransferException("TRANSFER WS Error: " 
					+ "No connection for " + remoteBank);
		} 
		Transfer transfer = connection.getOutTransfer();
		// First we log the received FPDU
		PeSITLog.logRemote(fpdu);
		
		switch (fpdu.getType()) {
			case READ:
				System.out.println("Received FPDU.READ from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.data_transfer_idle) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.READ from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.READ from " + remoteBank);
				}	
				// Changing of the state of the transfer
				transfer.setState(EnumTransferState.transfered);
				// Create FPDU ack(read)
				FPDU fpduResponse = createFPDU (connection, 
						ConnectionPool.getLocalIdentity(), remoteBank, 
						EnumFPDU.ACKREAD);
				listFpduResponse = new ArrayList<FPDU>();
				listFpduResponse.add(fpduResponse);	
				// create all data FPDU
				listFpduResponse.addAll(sendDTFFPDU(connection, remoteBank));
				respBool = true;
				break;
				
			case DTFEND:
				System.out.println("Received FPDU.DTFEND from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.transfered) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.DTFEND from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.DTFEND from " + remoteBank);
				}	

				// create TRANSEND FPDU
				fpduResponse = createFPDU (connection, 
						ConnectionPool.getLocalIdentity(), remoteBank, 
						EnumFPDU.TRANSEND);
				listFpduResponse = new ArrayList<FPDU>();
				listFpduResponse.add(fpduResponse);	
				// set state to transfer_idle
				transfer.setState(EnumTransferState.data_transfer_idle);
				respBool = true;
				break;
				
			case ACKREAD:
				System.out.println("Received FPDU.READ from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.wait_for_ack_read) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.READ from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.READ from " + remoteBank);
				}	
				// Changing of the state of the transfer
				transfer.setState(EnumTransferState.transfered);
				// * The interface is notify thanks to the "transferState" 
				// parameter in OutputTransferWS that the transfer starts
				transferState = 0;
				// * Now waiting for the file
				break;
				
			case TRANSEND:
				System.out.println("Received FPDU.TRANSEND from " + remoteBank);
				if (transfer.getState() != EnumTransferState.transfered) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.SELECT from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.SELECT from " + remoteBank);
				}
				transfer.setState(EnumTransferState.data_transfer_idle);
				fpduResponse = createFPDU (connection,
						ConnectionPool.getLocalIdentity(), remoteBank, 
						EnumFPDU.CRF);
				transfer.setState(EnumTransferState.wait_for_file_closing);
				listFpduResponse = new ArrayList<FPDU>();
				listFpduResponse.add(fpduResponse);	
				
				break;
				
			case SELECT:
				System.out.println("Received FPDU.SELECT from " + remoteBank);
				if (transfer.getState() != EnumTransferState.connected) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.SELECT from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.SELECT from " + remoteBank);
				}
					// Changing of the state of the transfer
					transfer.setState(EnumTransferState.file_selected);
					// Create FPDU ack(read)
					fpduResponse = createFPDU (connection, 
							ConnectionPool.getLocalIdentity(), remoteBank, 
							EnumFPDU.ACKSELECT);
					listFpduResponse = new ArrayList<FPDU>();
					listFpduResponse.add(fpduResponse);		
					//listFpduResponse.addAll(sendDTFFPDU(connection, remoteBank));
					respBool = true;
				break;
				
			case ACKSELECT:
				System.out.println("Received FPDU.ACKSELECT from "+ remoteBank);
				if (transfer.getState() 
						!= EnumTransferState.wait_for_selection) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.SELECT from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.SELECT from " + remoteBank);
				}	
				// Changing of the state of the transfer
				transfer.setState(EnumTransferState.file_selected);
				// * The interface is notify thanks to the "transferState" 
				// parameter in OutputTransferWS that the transfer starts
				transferState = 0;
				fpduResponse = createFPDU (connection, 
						ConnectionPool.getLocalIdentity(), remoteBank, 
						EnumFPDU.ORF);
				listFpduResponse = new ArrayList<FPDU>();
				transfer.setState(EnumTransferState.wait_for_file_opening);
				//listFpduResponse.add(fpduResponse);		
				// * Now waiting for the file
				break;	
			
			case ORF:
				System.out.println("Received FPDU.ORF from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.file_selected) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.ORF from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.ORF from " + remoteBank);
				}	
					// Changing of the state of the transfer
					transfer.setState(EnumTransferState.data_transfer_idle);
					// Create FPDU ack(read)
					fpduResponse = createFPDU (connection,
							ConnectionPool.getLocalIdentity(), remoteBank, 
							EnumFPDU.ACKORF);
					listFpduResponse = new ArrayList<FPDU>();
					listFpduResponse.add(fpduResponse);		
					//listFpduResponse.addAll(sendDTFFPDU(connection, remoteBank));
					respBool = true;
				break;

			case ACKORF:
				System.out.println("Received FPDU.ACKORF from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.wait_for_file_opening) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.ORF from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.ORF from " + remoteBank);
				}	
				// Changing of the state of the transfer
				
				//It seems we are lack of the etatg of opening a file
				transfer.setState(EnumTransferState.data_transfer_idle);
				
				fpduResponse = createFPDU (connection, 
						ConnectionPool.getLocalIdentity(), remoteBank, 
						EnumFPDU.READ);
				listFpduResponse = new ArrayList<FPDU>();
				listFpduResponse.add(fpduResponse);		
				transfer.setState(EnumTransferState.wait_for_ack_read);
				// parameter in OutputTransferWS that the transfer starts
				transferState = 0;
				// * Now waiting for the file
				break;
				
			case DESELECT:
				System.out.println("Received FPDU.DESELECT from " + remoteBank);
				if (transfer.getState() != 
					//It seems we have forgotten the state of deselect
					EnumTransferState.file_selected) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.DESELECT from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.DESELECT from " + remoteBank);
				}
					// Changing of the state of the transfer
					// The state EnumTransferState.wait_for_free_file means deselect a file
					transfer.setState(EnumTransferState.wait_for_free_file);
					// Create FPDU ack(SELECT)
					fpduResponse = createFPDU (connection, 
							ConnectionPool.getLocalIdentity(), remoteBank, 
							EnumFPDU.ACKDESELECT);
					listFpduResponse = new ArrayList<FPDU>();
					listFpduResponse.add(fpduResponse);		
					//listFpduResponse.addAll(sendDTFFPDU(connection, remoteBank));
					respBool = true;
				break;
				
			case ACKDESELECT:
				System.out.println("Received FPDU.ACKDESELECT from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.wait_for_free_file) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.DESELECT from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.DESELECT from " + remoteBank);
				}	
				
				// Changing of the state of the transfer
				
				// It seems the state of deselect is required EnumTransferState.file_deselected
				transfer.setState(EnumTransferState.connected);
				// parameter in OutputTransferWS that the transfer starts
				transferState = 0;
				// * Now waiting for the file
				break;
				
			case CRF:
				System.out.println("Received FPDU.CRF from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.data_transfer_idle) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.CRF from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.CRF from " + remoteBank);
				}	
				// Changing of the state of the transfer
				transfer.setState(EnumTransferState.file_selected);
				// Create FPDU ack(read)
				fpduResponse = createFPDU (connection, ConnectionPool.getLocalIdentity(), remoteBank, 
						EnumFPDU.ACKCRF);
				listFpduResponse = new ArrayList<FPDU>();
				listFpduResponse.add(fpduResponse);		
				//listFpduResponse.addAll(sendDTFFPDU(connection, remoteBank));
				respBool = true;
				break;
				
			case ACKCRF:
				System.out.println("Received FPDU.ACKCRF from " + remoteBank);
				if (transfer.getState() != 
					EnumTransferState.wait_for_file_closing) {
					System.out.println("TRANSFER WS Error: Not in the right " + 
							"to receive fpdu.CRF from " + remoteBank);
					// Raise exception
					throw new WrongFPDUException("TRANSFER WS Error: " +
							"Not in the right " + 
							"to receive fpdu.CRF from " + remoteBank);
				}	
				// Changing of the state of the transfer
				
				// The state of reading a file and closing a file are missing? 
				transfer.setState(EnumTransferState.wait_for_free_file);
				fpduResponse = createFPDU (connection, ConnectionPool.getLocalIdentity(), remoteBank, 
						EnumFPDU.DESELECT);
				listFpduResponse = new ArrayList<FPDU>();
				listFpduResponse.add(fpduResponse);	
				break;
				
			default:
				break;
		}
		
		// If there is a response, we log it
		if (listFpduResponse != null) {
			for (FPDU f : listFpduResponse) {
				PeSITLog.logLocal(f);
			}
		}
		OutputTransferWS output = new OutputTransferWS(listFpduResponse, 
				respBool, transferState);
		return output;
	}
}
