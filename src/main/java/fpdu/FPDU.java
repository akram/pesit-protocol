package fpdu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FPDU implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6697885502393774520L;

    public static enum EnumFPDU {
        // Connection and disconnection
        CONNECT, ACONNECT, RCONNECT, RELEASE, RELCONF, ABORT,
        // (de)selection and opening/closing files
        SELECT, ACKSELECT, DESELECT, ACKDESELECT, ORF, ACKORF, CRF, ACKCRF,
        // File transfer
        READ, ACKREAD, TRANSEND, ACKTRANSEND, DTF, DTFDA, DTFMA, DTFFA, DTFEND
    }

    private static Map<EnumFPDU, Byte> enumFPDUToByteFPDU;
    private static Map<Byte, EnumFPDU> byteFPDUToenumFPDU;
    private static Map<EnumFPDU, String> enumFPDUToString;
    static {
        // Init HashMaps
        enumFPDUToByteFPDU = new HashMap<EnumFPDU, Byte>();
        byteFPDUToenumFPDU = new HashMap<Byte, EnumFPDU>();
        enumFPDUToString = new HashMap<FPDU.EnumFPDU, String>();

        // Connection
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.CONNECT, Byte.parseByte("20"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ACONNECT, Byte.parseByte("21"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.RCONNECT, Byte.parseByte("22"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.RELEASE, Byte.parseByte("23"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.RELCONF, Byte.parseByte("24"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ABORT, Byte.parseByte("25"));
        byteFPDUToenumFPDU.put(Byte.parseByte("20"), FPDU.EnumFPDU.CONNECT);
        byteFPDUToenumFPDU.put(Byte.parseByte("21"), FPDU.EnumFPDU.ACONNECT);
        byteFPDUToenumFPDU.put(Byte.parseByte("22"), FPDU.EnumFPDU.RCONNECT);
        byteFPDUToenumFPDU.put(Byte.parseByte("23"), FPDU.EnumFPDU.RELEASE);
        byteFPDUToenumFPDU.put(Byte.parseByte("24"), FPDU.EnumFPDU.RELCONF);
        byteFPDUToenumFPDU.put(Byte.parseByte("25"), FPDU.EnumFPDU.ABORT);

        // Selection
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.SELECT, Byte.parseByte("12"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ACKSELECT, Byte.parseByte("31"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.DESELECT, Byte.parseByte("13"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ACKDESELECT, Byte.parseByte("32"));
        byteFPDUToenumFPDU.put(Byte.parseByte("12"), FPDU.EnumFPDU.SELECT);
        byteFPDUToenumFPDU.put(Byte.parseByte("32"), FPDU.EnumFPDU.ACKSELECT);
        byteFPDUToenumFPDU.put(Byte.parseByte("13"), FPDU.EnumFPDU.DESELECT);
        byteFPDUToenumFPDU.put(Byte.parseByte("32"), FPDU.EnumFPDU.ACKDESELECT);

        // Opening
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ORF, Byte.parseByte("14"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ACKORF, Byte.parseByte("33"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.CRF, Byte.parseByte("15"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ACKCRF, Byte.parseByte("34"));
        byteFPDUToenumFPDU.put(Byte.parseByte("14"), FPDU.EnumFPDU.ORF);
        byteFPDUToenumFPDU.put(Byte.parseByte("33"), FPDU.EnumFPDU.ACKORF);
        byteFPDUToenumFPDU.put(Byte.parseByte("15"), FPDU.EnumFPDU.CRF);
        byteFPDUToenumFPDU.put(Byte.parseByte("34"), FPDU.EnumFPDU.ACKCRF);

        // Transfer
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.READ, Byte.parseByte("01"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ACKREAD, Byte.parseByte("35"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.TRANSEND, Byte.parseByte("08"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.ACKTRANSEND, Byte.parseByte("37"));
        enumFPDUToByteFPDU.put(FPDU.EnumFPDU.DTFEND, Byte.parseByte("04"));
        byteFPDUToenumFPDU.put(Byte.parseByte("04"), FPDU.EnumFPDU.DTFEND);
        byteFPDUToenumFPDU.put(Byte.parseByte("01"), FPDU.EnumFPDU.READ);
        byteFPDUToenumFPDU.put(Byte.parseByte("35"), FPDU.EnumFPDU.ACKREAD);
        byteFPDUToenumFPDU.put(Byte.parseByte("08"), FPDU.EnumFPDU.TRANSEND);
        byteFPDUToenumFPDU.put(Byte.parseByte("37"), FPDU.EnumFPDU.ACKTRANSEND);

        // EnumFPDU to String
        enumFPDUToString.put(FPDU.EnumFPDU.CONNECT, "CONNECT");
        enumFPDUToString.put(FPDU.EnumFPDU.ACONNECT, "ACONNECT");
        enumFPDUToString.put(FPDU.EnumFPDU.RCONNECT, "RCONNECT");
        enumFPDUToString.put(FPDU.EnumFPDU.RELEASE, "RELEASE");
        enumFPDUToString.put(FPDU.EnumFPDU.RELCONF, "RELCONF");
        enumFPDUToString.put(FPDU.EnumFPDU.ABORT, "ABORT");

        enumFPDUToString.put(FPDU.EnumFPDU.SELECT, "SELECT");
        enumFPDUToString.put(FPDU.EnumFPDU.ACKSELECT, "ACKSELECT");
        enumFPDUToString.put(FPDU.EnumFPDU.DESELECT, "DESELECT");
        enumFPDUToString.put(FPDU.EnumFPDU.ACKDESELECT, "ACKDESELECT");
        enumFPDUToString.put(FPDU.EnumFPDU.ORF, "ORF");
        enumFPDUToString.put(FPDU.EnumFPDU.ACKORF, "ACKORF");
        enumFPDUToString.put(FPDU.EnumFPDU.CRF, "CRF");
        enumFPDUToString.put(FPDU.EnumFPDU.ACKCRF, "ACKCRF");

        enumFPDUToString.put(FPDU.EnumFPDU.READ, "READ");
        enumFPDUToString.put(FPDU.EnumFPDU.ACKREAD, "ACKREAD");
        enumFPDUToString.put(FPDU.EnumFPDU.TRANSEND, "TRANSEND");
        enumFPDUToString.put(FPDU.EnumFPDU.ACKTRANSEND, "ACKTRANSEND");
        enumFPDUToString.put(FPDU.EnumFPDU.DTF, "DTF");
        enumFPDUToString.put(FPDU.EnumFPDU.DTFFA, "DTFFA");
        enumFPDUToString.put(FPDU.EnumFPDU.DTFDA, "DTFDA");
        enumFPDUToString.put(FPDU.EnumFPDU.DTFMA, "DTFMA");
        enumFPDUToString.put(FPDU.EnumFPDU.DTFEND, "DTFEND");

    }

    // HEADER
    // TODO : messageSize, idDest, octet6
    // Use for message size on octet 1 and 2
    private char messageSize;
    // Octet 3 for protocolPhase
    private byte protocolPhase;
    // Octet 4 for messageType
    private byte messageType;
    // Octet 5 for idDest
    private byte octet5;
    // Octet 6 : 0 by default. Modify when necessary
    private byte octet6;

    // PAYLOAD
    private Object payload;

    public FPDU() {
    }

    // TODO : remove because useless now
    public FPDU(String temp) {
    }

    public FPDU(String temp, FPDUParameter parameter) {
        this.setParameter(parameter);
    }

    public FPDU(EnumFPDU type, FPDUParameter parameter) {
        messageSize = 0;
        protocolPhase = 0;
        messageType = (byte) (Integer.parseInt("FF", 16) & 0xff);
        octet5 = 0;
        octet6 = 0;
        payload = null;
        this.setType(type);
        this.setParameter(parameter);
    }

    public FPDU(FPDUParameter parameter) {
        messageSize = 0;
        protocolPhase = 0;
        messageType = (byte) (Integer.parseInt("FF", 16) & 0xff);
        octet5 = 0;
        octet6 = 0;
        payload = null;
        this.setParameter(parameter);
    }

    public EnumFPDU getType() {
        return FPDU.byteFPDUToenumFPDU.get(this.messageType);
    }

    public void setType(EnumFPDU type) {
        this.messageType = FPDU.enumFPDUToByteFPDU.get(type);
        // also set the protocolPhase field
    }

    private void setProtocolPhase(Byte byteValue) {
        if (byteValue >= (byte) (Integer.parseInt("20", 16) & 0xff) && byteValue <= (byte) (Integer.parseInt("25", 16) & 0xff)) {
            this.protocolPhase = (byte) (Integer.parseInt("40", 16) & 0xff);
        } else if (byteValue == (byte) (Integer.parseInt("00", 16) & 0xff)
                || (byteValue >= (byte) (Integer.parseInt("40", 16) & 0xff) && byteValue <= (byte) (Integer.parseInt("42", 16) & 0xff))) {
            this.protocolPhase = (byte) (Integer.parseInt("00", 16) & 0xff);
        } else {
            this.protocolPhase = (byte) (Integer.parseInt("CO", 16) & 0xff);
        }
    }

    private Byte getProtocolPhase() {
        return this.protocolPhase;
    }

    // TODO
    public void setParameter(FPDUParameter parameter) {
        // TODO if getReceiver then ask for persistence if entry already exists
        // if entry already exists then set idDest
        // else if FPDU.CONNECT then create new entry in database and get idDest
        // and id (with octet6)
        // else raise error

        // Set idDest field

        /*
         * 
         * Les paramétres obligatoires dont l'absence dans une FPDU est une
         * erreur de protocole. - Les paramétres optionnels pour lesquels une
         * valeur implicite est définie. Cette valeur est alors soulignée dans
         * la description du paramétre (é 4.7.3). Si le paramétre est omis,
         * il sera considéré comme ayant la valeur implicite. - Les
         * paramétres optionnels sans valeur implicite, qui peuvent étre
         * présents ou non dans la FPDU.
         */
        PI tempPi = null;
        ArrayList<PIBase> listPI = new ArrayList<PIBase>();

        // // If CRC
        // if (parameter.getCRC()) {
        // tempPi = new PI(Byte.parseByte("1"));
        // tempPi.setValue(Byte.parseByte("1"));
        // listPI.add(tempPi);
        // }
        // else {
        // tempPi = new PI(Byte.parseByte("1"));
        // tempPi.setValue(Byte.parseByte("0"));
        // listPI.add(tempPi);
        // }

        // If diagnostic
        if (parameter.getDiagnostic() != null) {
            tempPi = new PI((byte) (Integer.parseInt("2", 16) & 0xff));
            Byte[] value = new Byte[3];
            // TODO : create HashMap for errors and code and populate value
            value[0] = (byte) (Integer.parseInt("00", 16) & 0xff);
            value[1] = (byte) (Integer.parseInt("00", 16) & 0xff);
            value[2] = (byte) (Integer.parseInt("00", 16) & 0xff);
            tempPi.setValue(value);
            listPI.add(tempPi);
        } else {
            tempPi = new PI((byte) (Integer.parseInt("2", 16) & 0xff));
            Byte[] value = new Byte[3];
            // No error code
            value[0] = (byte) (Integer.parseInt("00", 16) & 0xff);
            value[1] = (byte) (Integer.parseInt("00", 16) & 0xff);
            value[2] = (byte) (Integer.parseInt("00", 16) & 0xff);
            tempPi.setValue(value);
            listPI.add(tempPi);

        }

        // If sender
        if (parameter.getSender() != null) {
            String temp = parameter.getSender();
            tempPi = new PI((byte) (Integer.parseInt("3", 16) & 0xff));
            Character[] value = new Character[temp.length()];
            for (int i = 0; i < temp.length(); i++) {
                value[i] = new Character(temp.charAt(i));
            }
            tempPi.setValue(value);
            listPI.add(tempPi);
        }

        // If receiver
        if (parameter.getReceiver() != null) {
            String temp = parameter.getReceiver();
            tempPi = new PI((byte) (Integer.parseInt("4", 16) & 0xff));
            Character[] value = new Character[temp.length()];
            for (int i = 0; i < temp.length(); i++) {
                value[i] = new Character(temp.charAt(i));
            }
            tempPi.setValue(value);
            listPI.add(tempPi);
        }

        // If accessControl
        if (parameter.getAccessControl() != null) {
            String temp = parameter.getAccessControl();
            tempPi = new PI((byte) (Integer.parseInt("5", 16) & 0xff));
            char[] value = null;
            // TODO : write in documentation the separator
            if (temp.contains("|")) {
                // TODO : check that temp is size of 16
                if (temp.length() == 9) {
                    value = temp.replaceAll("|", "").toCharArray();
                } else {
                    // raise error
                }
            } else {
                // check size
                if (temp.length() == 4) {
                    value = temp.toCharArray();
                } else {
                    // raise error
                }
            }
            tempPi.setValue(value);
            listPI.add(tempPi);
        }

        if (parameter.getFileIdentifier() != null) {

        }

        if (parameter.getTransferIdentifier() != null) {

        }

        // IF idByteArray : choose byte to 28 arbitrary
        if (parameter.getIdByteArray() >= 0) {
            tempPi = new PI((byte) (Integer.parseInt("28", 16) & 0xff));
            tempPi.setValue(parameter.getIdByteArray());
        }

        // If byteArray : choose byte to 16 arbitrary
        if (parameter.getByteArray() != null) {
            tempPi = new PI((byte) (Integer.parseInt("16", 16) & 0xff));
            tempPi.setValue(parameter.getByteArray());
        }

        // Resynchronisation
        // if (parameter.isResynchronisation() != null) {
        //
        // }

        if (parameter.getRestartPoint() >= 0) {

        }

        if (parameter.getNumberSynchroPoint() >= 0) {

        }

        if (parameter.getDataSize() >= 0) {

        }

        if (parameter.getDate() != null) {

        }

        // If priority
        if (parameter.getPriority() >= 0) {

        }

        if (parameter.getMaxSizeOfDataEntity() >= 0) {

        }

        // If localIdentifier
        if (parameter.getLocalConnectionId() > 0) {
            this.octet6 = Byte.valueOf(Integer.toString(parameter.getLocalConnectionId()));
        }

        // If remoteIdentifier
        if (parameter.getRemoteConnectionId() > 0) {
            this.octet5 = Byte.valueOf(Integer.toString(parameter.getRemoteConnectionId()));
        }

        this.payload = listPI;
        // TODO : add PGI possibility
        // if (listPI.size() > 1) {
        // this.payload = new PGI(listPI);
        // }
        // else if (listPI.size() == 1) {
        // this.payload = listPI.get(0);
        // }
        // else {
        // // raise Error;
        // }

        // TODO : setFPDU size
        System.out.print("FPDU.setParameter : FPDU has been created with parameters -> ");
        System.out.println(listPI);
    }

    /**
     * @brief Transform a Character array into a String
     * @param array
     *            Array of Character
     * @return String corresponding to the array
     */
    private String characterArrayToString(Character[] array) {
        String s = "";
        for (Character c : array) {
            s += c.charValue();
        }
        return s;
    }

    // TODO
    public FPDUParameter getParameter() {
        // Build a FPDUParameter object with the payload
        FPDUParameter response = new FPDUParameter();
        // TODO : add PGI possibility
        if (this.payload instanceof ArrayList) {
            for (PIBase tempPi : (ArrayList<PIBase>) this.payload) {
                if (tempPi instanceof PI) {
                    if (((PI) tempPi).getId() == (byte) (Integer.parseInt("3", 16) & 0xff)) {
                        response.setSender(characterArrayToString((Character[]) ((PI) tempPi).getValue()));
                    } else if (((PI) tempPi).getId() == (byte) (Integer.parseInt("4", 16) & 0xff)) {
                        response.setReceiver(characterArrayToString((Character[]) ((PI) tempPi).getValue()));
                    } else if (((PI) tempPi).getId() == (byte) (Integer.parseInt("16", 16) & 0xff)) {
                        response.setByteArray((byte[]) ((PI) tempPi).getValue());
                    } else if (((PI) tempPi).getId() == (byte) (Integer.parseInt("28", 16) & 0xff)) {
                        response.setIdByteArray(((Integer) ((PI) tempPi).getValue()).intValue());
                    }
                }
            }
        }

        // Action on octet 5 and 6
        // CONNECT
        if (this.messageType == FPDU.enumFPDUToByteFPDU.get(FPDU.EnumFPDU.CONNECT)) {
            response.setLocalConnectionId(this.octet5);
        } else if (this.messageType == FPDU.enumFPDUToByteFPDU.get(FPDU.EnumFPDU.ACONNECT)) {
            response.setLocalConnectionId(this.octet5);
            response.setRemoteConnectionId(this.octet6);
        } else if (this.messageType == FPDU.enumFPDUToByteFPDU.get(FPDU.EnumFPDU.RCONNECT)) {
            response.setRemoteConnectionId(this.octet6);
        }

        return response;
    }

    public char getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(char messageSize) {
        this.messageSize = messageSize;
    }

    public String toString() {
        return "FPDU " + enumFPDUToString.get(getType()) + "  ";
    }

    public static void main(String[] args) {
        // System.out.println(FPDU.enumFPDUToByteFPDU.get(FPDU.EnumFPDU.CONNECT));
        FPDUParameter par = new FPDUParameter("OPA", "PMU");
        FPDU connect = new FPDU(FPDU.EnumFPDU.CONNECT, par);

        System.out.println(connect.getParameter().getSender());
        System.out.println(connect);
    }

}
