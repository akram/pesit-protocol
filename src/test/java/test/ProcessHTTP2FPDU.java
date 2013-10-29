package test;

import java.util.Date;
import java.util.StringTokenizer;

import fpdu.FPDU;
import fpdu.FPDUParameter;
import fpdu.FPDU.EnumFPDU;


public class ProcessHTTP2FPDU {

	public FPDU process(String message) {
		FPDU fpdu = null;
		System.out.println(message);
		Date date = new Date();
		String type;
		String from = "Bob";
		String to = "Alice";
		
		StringTokenizer tokenizer;
		String token;
		
		FPDUParameter parameter;
		
		if (!message.equals("/favicon.ico")) {
			tokenizer = new StringTokenizer(message, "/?& =");
			// If there is at least one token
			if (tokenizer.hasMoreTokens()) {
				// The first token is the type of FPDU
				type = tokenizer.nextToken();
				
				// While there are token, we get the parameters
				while (tokenizer.hasMoreTokens()) {
					token = tokenizer.nextToken();
					if (token.equals("type")) {
						if(tokenizer.hasMoreTokens()) {
							type = tokenizer.nextToken();
						} else {
							// No value for parameter
							System.out.println("Error in the HTTP request, " +
									"no value for type");
						}
					} else if(token.equals("from")) {
						if(tokenizer.hasMoreTokens()) {
							from = tokenizer.nextToken();
						} else {
							// No value for parameter
							System.out.println("Error in the HTTP request, " +
									"no value for from");
						}
					} else if(token.equals("to")) {
						if(tokenizer.hasMoreTokens()) {
							to = tokenizer.nextToken();
						} else {
							// No value for parameter
							System.out.println("Error in the HTTP request, " +
									"no value for to");
						}
					}
				}
				parameter = new FPDUParameter(from, to);
				parameter.setDate(date);
				fpdu = new FPDU(getRightType(type), parameter);
			}
		} else {
			fpdu = new FPDU("Ceci n'est pas un FPDU");
		}
		return fpdu;
	}
	
	/**
	 * Method to get the EnumFPDU type from a String
	 * @param sType The wanted type as a String
	 * @return The corresponding EnumPFDU
	 */
	public EnumFPDU getRightType(String sType) {
		EnumFPDU rightType = null;
		sType = sType.toUpperCase();
		if (sType.equals("CONNECT")) {
			rightType = EnumFPDU.CONNECT;
		} else if (sType.equals("ACONNECT")) {
			rightType = EnumFPDU.ACONNECT;
		} else if (sType.equals("RCONNECT")) {
			rightType = EnumFPDU.RCONNECT;
		} else if (sType.equals("ORF")) {
			rightType = EnumFPDU.ORF;
		} else if (sType.equals("ACKORF")) {
			rightType = EnumFPDU.ACKORF;
		} else if (sType.equals("SELECT")) {
			rightType = EnumFPDU.SELECT;
		} else if (sType.equals("ACKSELECT")) {
			rightType = EnumFPDU.ACKSELECT;
		} else if (sType.equals("RELCONF")) {
			rightType = EnumFPDU.RELCONF;
		} else if (sType.equals("RELEASE")) {
			rightType = EnumFPDU.RELEASE;
		} else if (sType.equals("ABORT")) {
			rightType = EnumFPDU.ABORT;
		} else if (sType.equals("TRANSEND")) {
			rightType = EnumFPDU.TRANSEND;
		} else if (sType.equals("ABORT")) {
			rightType = EnumFPDU.ABORT;
		} else if (sType.equals("DTF")) {
			rightType = EnumFPDU.DTF;
		} else if (sType.equals("DTFDA")) {
			rightType = EnumFPDU.DTFDA;
		} else if (sType.equals("DTFMA")) {
			rightType = EnumFPDU.DTFMA;
		} else if (sType.equals("DTFFA")) {
			rightType = EnumFPDU.DTFFA;
		} else if (sType.equals("DESELECT")) {
			rightType = EnumFPDU.DESELECT;
		} else if (sType.equals("CRF")) {
			rightType = EnumFPDU.CRF;
		} else if (sType.equals("ACKCRF")) {
			rightType = EnumFPDU.ACKCRF;
		} else if (sType.equals("ACKTRANSEND")) {
			rightType = EnumFPDU.ACKTRANSEND;
		} else if (sType.equals("READ")) {
			rightType = EnumFPDU.READ;
		} else if (sType.equals("ACKREAD")) {
			rightType = EnumFPDU.ACKREAD;
		}
		
		return rightType;
	}
}
