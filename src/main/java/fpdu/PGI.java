package fpdu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PGI extends PIBase implements Serializable{

	//idPGI list
	private static HashMap<Byte, ArrayList<Byte>> idPGIList;
	static {
		idPGIList = new HashMap<Byte, ArrayList<Byte>>();
		//populate idPGIList
		Byte tempByte = Byte.parseByte("9"); // Identificateur fichier
		idPGIList.put(tempByte, new ArrayList<Byte>());
		idPGIList.get(tempByte).add(Byte.parseByte("3"));
		idPGIList.get(tempByte).add(Byte.parseByte("4"));
		idPGIList.get(tempByte).add(Byte.parseByte("11"));
		idPGIList.get(tempByte).add(Byte.parseByte("12"));
		tempByte = Byte.parseByte("30");  // Attributes logiques
		idPGIList.put(tempByte, new ArrayList<Byte>());
		idPGIList.get(tempByte).add(Byte.parseByte("31"));
		idPGIList.get(tempByte).add(Byte.parseByte("32"));
		idPGIList.get(tempByte).add(Byte.parseByte("33"));
		idPGIList.get(tempByte).add(Byte.parseByte("34"));
		idPGIList.get(tempByte).add(Byte.parseByte("36"));
		idPGIList.get(tempByte).add(Byte.parseByte("37"));
		idPGIList.get(tempByte).add(Byte.parseByte("38"));
		idPGIList.get(tempByte).add(Byte.parseByte("39"));
		tempByte = Byte.parseByte("40");  // Attributes physiques
		idPGIList.put(tempByte, new ArrayList<Byte>());
		idPGIList.get(tempByte).add(Byte.parseByte("41"));
		idPGIList.get(tempByte).add(Byte.parseByte("42"));
		tempByte = Byte.parseByte("50");  // Attributes historiques
		idPGIList.put(tempByte, new ArrayList<Byte>());
		idPGIList.get(tempByte).add(Byte.parseByte("51"));
		idPGIList.get(tempByte).add(Byte.parseByte("52"));
	}
	
	private ArrayList<PI> value;
	
	public PGI(byte id) {
		this.setId(id);
		this.value = new ArrayList<PI>();
	}
	
	public PGI(ArrayList<PI> listPi) {
		// Check keys on listPi to set Id and maybe raise Error
		if (listPi.size() > 0) {
			Byte byteCreate = null;
			// Set byteCreate regarding keys in listPI
			PI tempPI = listPi.get(0);
			if (PGI.idPGIList.get(Byte.parseByte("9")).contains(Byte.valueOf(tempPI.id))) {
				byteCreate = Byte.parseByte("9");
			}
			else if (PGI.idPGIList.get(Byte.parseByte("30")).contains(Byte.valueOf(tempPI.id))) {
				byteCreate = Byte.parseByte("30");
			}
			else if (PGI.idPGIList.get(Byte.parseByte("40")).contains(Byte.valueOf(tempPI.id))) {
				byteCreate = Byte.parseByte("40");
			}
			else if (PGI.idPGIList.get(Byte.parseByte("50")).contains(Byte.valueOf(tempPI.id))) {
				byteCreate = Byte.parseByte("50");
			}
			else {
				// Raise error
			}
			this.setId(byteCreate);
			this.value = listPi;	
		}
		// Raise error
	}
	
	@Override
	public void setId(byte id) {
		// set id field
		this.id = id;	
	}
	
	public void createPIList() {
		// get PI id List for this id
		ArrayList<Byte> PIIdList = PGI.idPGIList.get(this.id);
		for (Byte currentPIId : PIIdList) {
			// Add the PIId to the value
			this.value.add(new PI(currentPIId));
			// TODO : where set the value for this ID
		}	
	}
	
	
}
