package fpdu;

import java.io.Serializable;

public abstract class PIBase  implements Serializable{

	//identificateur de parametre
	// paragraphe 4.7.3, page 151
	protected byte id;
	
	//longueur du champ du parametre sur 3 octets
	// 1 octet = byte 0 a 254
	// 3 octets = ? 255 a 65535 , little endian, premier octet 1111 1111
	// 0 rejeté par le protocole
	protected byte length0;
	protected byte length1;
	protected byte length2;
	
	public abstract void setId(byte id);
	
}
