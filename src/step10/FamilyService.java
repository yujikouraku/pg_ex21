package step10;

import java.util.ArrayList;

public class FamilyService implements Service{
	private boolean familyserviceflag = false;
	// 加入サービス情報
	private ArrayList<String> family_tel_num_array = new ArrayList<String>();

	public boolean isJoined() {
		return familyserviceflag;
	}

	@Override
	public void checkService(Record record) {
		if (record.getServiceCode().equals("C1")) {
			familyserviceflag = true;
			this.familyTelNumSet(record);
		}
	}

	private void familyTelNumSet(Record record) {
		family_tel_num_array.add(record.getServiceOption());
	}

	public boolean isFamilyTelNumber(String dst_tel_num) {
		if (!this.isJoined()) return false;
		return family_tel_num_array.contains(dst_tel_num);
	}

	@Override
	public int calcUnitPrice(Record record, int unit_price) {
		String dst_tel_num = record.getCallNumber();
		if (this.isFamilyTelNumber(dst_tel_num) == true) {
			unit_price = unit_price / 2;
		}

		return unit_price;
	}

	@Override
	public int calcBasicCharge(int basiccharge) {
		if (this.isJoined() == true) {
			basiccharge += 100;
		}
		return basiccharge;
	}

	@Override
	public void clear() {
		familyserviceflag = false;
	}

}
