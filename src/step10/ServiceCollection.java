package step10;

public class ServiceCollection implements Service {
	private int fs_count = 0;

	// Serviceインスタンス生成
	Service[] services = {new DayService(), new FamilyService()};

	/*
	// DayServiceインスタンス生成
	DayService dayService = new DayService();

	// FamilyServiceインスタンス生成
	FamilyService familyService = new FamilyService();
	*/

	@Override
	public void clear() {
		for (int i = 0; i < services.length; i++) {
			services[i].clear();
		}
		fs_count = 0;
	}

	@Override
	public void checkService(Record record) {
		for (int i = 0; i < services.length; i++) {
			services[i].checkService(record);
		}
	}

	@Override
	public int calcUnitPrice(Record record, int unitPrice) {
		for (int i = 0; i < services.length; i++) {
			unitPrice = services[i].calcUnitPrice(record, unitPrice);
		}
		return unitPrice;
	}

	@Override
	public int calcBasicCharge(int basicCharge) {
		basicCharge = services[0].calcBasicCharge(basicCharge);
		if (fs_count == 0) {
			basicCharge = services[1].calcBasicCharge(basicCharge);
			fs_count += 1;
		}
		return basicCharge;
	}

}
