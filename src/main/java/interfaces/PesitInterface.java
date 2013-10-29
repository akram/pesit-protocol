package interfaces;

import fpdu.FPDU;

public interface PesitInterface {

	boolean connect(String bankName);

	boolean disconnect(String bankName);

	void FPDUReception(FPDU fpdu);        

	void getFile(String fileName);

	int getStatus(String fileName);
	
}