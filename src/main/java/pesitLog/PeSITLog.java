package pesitLog;

import java.util.Date;

import fpdu.FPDU;
import fpdu.FPDUParameter;


/**
 * This class is the log part of PeSIT Service. It aims at keeping track of
 * every action made (FPDU sent or received)
 */
public class PeSITLog {
	
	/**
	 * Log a local action 
	 * (sending or reception of a FPDU) in the persistence layer
	 * @param fpdu The FPDU to be logged
	 * @return The message to be logged
	 */
	public static String logLocal (FPDU fpdu) {
		// Item to be logged
		String logItem;
		// Date of the evenement
		Date date = new Date();
		// Parameters of the FPDU
		FPDUParameter parameters;
		
		if (fpdu != null && fpdu.getType() != null) {
			parameters = fpdu.getParameter();
			
			logItem = date + " Sent: " + fpdu.toString();
			
			if(parameters.getReceiver() != null) {
				logItem += " to " + parameters.getReceiver() + ". ";
			}
			
			switch(fpdu.getType()) {
			case CONNECT :
				logItem += "We ask " + parameters.getReceiver() + 
					" for a connection. Waiting for ACONNECT.";
				break;
			case ACONNECT :
				logItem += "Connection accepted. We are now connected to " +
					parameters.getReceiver();
				break;
			case RCONNECT :
				logItem += "Connection to " + parameters.getReceiver() + 
					" refused.";
				break;
			case RELEASE :
				logItem += "We will close the connection with " + 
					parameters.getReceiver() + ". Waiting for RELCONF.";
				break;
			case RELCONF :
				logItem += "We are now disconnected.";
				break;
			case ABORT :
				logItem += "We abort the connection";
				break;
			case SELECT :
				logItem += "We select a file from " + parameters.getReceiver() +
					". Waiting for ACKSELECT.";
				break;
			case ACKSELECT :
				logItem += "Acknowledgement of remote selection.";
				break;
			case DESELECT :
				logItem += "We deselect a file from " +
					parameters.getReceiver() + ". Waiting for ACKDESELECT.";
				break;
			case ACKDESELECT :
				logItem += "Acknowledgement of remote deselection.";
				break;
			case ORF :
				logItem += "We ask " + parameters.getReceiver() + 
					" for a file opening. Waiting for ACKORF.";
				break;
			case ACKORF :
				logItem += "Acknowledgement of a remote file opening request.";
				break;
			case CRF :
				logItem += "We ask " + parameters.getReceiver() + 
				" for a file closing. Waiting for ACKCRF.";
			break;
			case ACKCRF :
				logItem += "Acknowledgement of a remote file closing request.";
				break;
			case READ :
				logItem += "We ask " + parameters.getReceiver() + 
				" for a file reading. Waiting for ACKREAD.";
				break;
			case ACKREAD :
				logItem += "Acknowledgement of a remote file reading request.";
				break;
			case TRANSEND :
				logItem += "We tell " + parameters.getReceiver() + 
				" that the transfer is finished. Waiting for ACKTRANSEND.";
				break;
			case ACKTRANSEND :
				logItem += "Acknowledgement of a remote transfer end.";
				break;
			case DTF :
				logItem += "Sending data to " + parameters.getReceiver() + ".";
				break;
			case DTFDA :
				logItem += "Sending data to " + parameters.getReceiver() + 
					". Beginning of an article";
				break;
			case DTFMA :
				logItem += "Sending data to " + parameters.getReceiver() + 
					". Middle of an article.";
				break;
			case DTFFA :
				logItem += "Sending data to " + parameters.getReceiver() + 
					". End of an article";
				break;
			default :
				// We do nothing	
				break;
			}
			
			/*
			 * logItem has to be stored in persistence 
			 */
			System.out.println("\n---PeSIT Log---\n" + logItem + "\n");
		} else {
			logItem = "Error null FPDU";
			System.out.println("\n---PeSIT Log---\n" + logItem + "\n");
		}
		return logItem;
	}
	
	/**
	 * Log a remote action 
	 * (sending or reception of a FPDU) in the persistence layer
	 * @param fpdu The FPDU to be logged
	 * @return The message to be logged
	 */
	public static String logRemote (FPDU fpdu) {
		// Item to be logged
		String logItem;
		// Date of the evenement
		Date date = new Date();
		// Parameters of the FPDU
		FPDUParameter parameters;
		
		if (fpdu != null) {
			parameters = fpdu.getParameter();
			
			logItem = date + " Received: " + fpdu.toString();
			
			if(parameters.getSender() != null) {
				logItem += " from " + parameters.getSender() + ". ";
			}
			
			switch(fpdu.getType()) {
			case CONNECT :
				logItem += parameters.getSender() + " wants to connect.";
				break;
			case ACONNECT :
				logItem += "Connection accepted. We are now connected to " +
					parameters.getSender() + ".";
				break;
			case RCONNECT :
				logItem += "Connection refused.";
				break;
			case RELEASE :
				logItem += parameters.getSender() + 
					" wants to disconnect.";
				break;
			case RELCONF :
				logItem += "We are now disconnected.";
				break;
			case ABORT :
				logItem += parameters.getSender() + " aborted the connection";
				break;
			case SELECT :
				logItem += parameters.getSender() + " selected a file " +
					"from us.";
				break;
			case ACKSELECT :
				logItem += parameters.getSender() + 
					" acknowledged our file selection.";
				break;
			case DESELECT :
				logItem += parameters.getSender() + 
					" deselcted a file.";
				break;
			case ACKDESELECT :
				logItem += parameters.getSender() + 
					" acknowledged our file deselection.";
				break;
			case ORF :
				logItem += "File opening request from " + 
					parameters.getSender();
				break;
			case ACKORF :
				logItem += parameters.getSender() + 
					" acknowledged our file opening request.";
				break;
			case CRF :
				logItem += "File closing request from " + 
					parameters.getSender();
			break;
			case ACKCRF :
				logItem += parameters.getSender() + 
					" acknowledged our file closing request.";
				break;
			case READ :
				logItem += "File reading request from " + 
					parameters.getSender() + ".";
				break;
			case ACKREAD :
				logItem += parameters.getSender() + 
					" acknowledged our file reading request.";
				break;
			case TRANSEND :
				logItem += "Transfer from " + parameters.getSender() + 
				" finished.";
				break;
			case ACKTRANSEND :
				logItem += parameters.getSender() + 
					" acknowledged our TRANSEND.";
				break;
			case DTF :
				logItem +="Receiving data from " + parameters.getSender() + ".";
				break;
			case DTFDA :
				logItem += "Receiving data from " + parameters.getSender() + 
					". Beginning of an article";
				break;
			case DTFMA :
				logItem += "Receiving data from " + parameters.getSender() + 
					". Middle of an article.";
				break;
			case DTFFA :
				logItem += "Receiving data from " + parameters.getSender() + 
					". End of an article";
				break;
			default :
				// We do nothing	
				break;
			}
			
			/*
			 * logItem has to be stored in persistence 
			 */
			System.out.println("\n---PeSIT Log---\n" + logItem + "\n");
		} else {
			logItem = "Error null FPDU";
			System.out.println("\n---PeSIT Log---\n" + logItem + "\n");
		}
		return logItem;
	}
}
