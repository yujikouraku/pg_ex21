package step9;

public class Invoice {
	String owner_tel_num = null;
	int basic_charge = 0;
	int call_charge = 0;

	public String getOwnerTelNumber() {
		return owner_tel_num;
	}

	public int getBasicCharge() {
		return basic_charge;
	}

	public int getCallCharge() {
		return call_charge;
	}

	public void setOwnerTelNumber(String string) {
		owner_tel_num = string;
	}

	public void setBasicCharge(int i) {
		basic_charge = i;
	}

	public void addCallCharge(int i) {
		call_charge += i;
	}

	public void clear() {
		owner_tel_num = null;
		basic_charge = 0;
		call_charge = 0;
	}



}
