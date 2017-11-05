package step10;

public class ServiceCollection implements Service {
	private int fs_count = 0;

	// DayServiceインスタンス生成
	DayService dayService = new DayService();

	// FamilyServiceインスタンス生成
	FamilyService familyService = new FamilyService();

	@Override
	public void clear() {
		dayService.clear();
		familyService.clear();
		fs_count = 0;
	}

	@Override
	public void checkService(Record record) {
		dayService.checkService(record);
		familyService.checkService(record);
	}

	@Override
	public int calcUnitPrice(Record record, int unitPrice) {
		unitPrice = dayService.calcUnitPrice(record, unitPrice);
		unitPrice = familyService.calcUnitPrice(record, unitPrice);
		return unitPrice;
	}

	@Override
	public int calcBasicCharge(int basicCharge) {
		basicCharge = dayService.calcBasicCharge(basicCharge);
		if (fs_count == 0) {
			basicCharge = familyService.calcBasicCharge(basicCharge);
			fs_count += 1;
		}
		return basicCharge;
	}

}
