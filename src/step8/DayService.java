package step8;

public class DayService {
	private boolean dayserviceflag = false; // インスタンス作成直後は加入フラグはfalse
	private boolean daytimeflag = false;

	//
	public boolean isJoined() {
		return dayserviceflag;
	}

	public void checkService(Record record) {
		if (record.getServiceCode().equals("E1")) {
			dayserviceflag = true;
		}
	}

	public void clear() {
		dayserviceflag = false;
		daytimeflag = false;
	}

	public boolean isServiceTime(int i) {

		if (dayserviceflag == true) {
			if (8 <= i && i < 18) {
				daytimeflag = true;
			} else {
				daytimeflag = false;
			}
		} else {
			daytimeflag = false;
		}
		return daytimeflag;
	}

	public int calcUnitPrice(Record record, int i) {
		int unitprice = i; // 通話料金の単価
		int starthour = record.getStartHour(); // 通話開始時間取得
		// 昼トク割引対象の時間の場合は、単価を5円引き
		if (this.isServiceTime(starthour) == true) {
			unitprice -= 5;
		}
		return unitprice;
	}

	public int calcBasicCharge(int i) {
		int basiccharge = i; // 基本料金
		// 昼トク割引加入者の場合は基本料金200円増し
		if (this.isJoined() == true) {
			basiccharge += 200;
		}
		return basiccharge;
	}

}
