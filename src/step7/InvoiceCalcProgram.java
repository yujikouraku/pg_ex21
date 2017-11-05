package step7;

import java.io.StringWriter;
import java.util.ArrayList;

public class InvoiceCalcProgram {

	public static void main(String[] args) {
		// 通話記録ファイルを読み込み
		try {
			// RecordReader インスタンス生成
			RecordReader recordreader = new RecordReader();

			// 契約者情報
			String owner_tel_num = null;

			// 基本料金：1000円/月
			int basic_charge = 1000;

			// 通話料金（合計）
			int call_charge = 0;

			// 加入サービス情報
			ArrayList<String> family_tel_num_array = new ArrayList<String>();

			// Invoiceインスタンス生成
			Invoice invoice = new Invoice();

			// DayServiceインスタンス生成
			DayService dayService = new DayService();


			for (Record record = recordreader.read(); record != null; record = recordreader.read()) {
				// 読み込んだ文字列の一桁目の文字を取得
				char init_char = record.getRecordCode();
				// 一桁目の文字で処理を分岐
				switch (init_char) {
				// 1桁目が1の場合は契約者の電話番号を取得
				case '1':
					owner_tel_num = record.getOwnerTelNumber();
					invoice.setOwnerTelNumber(owner_tel_num);
					break;
					// 1桁目が2の場合は加入サービス情報を取得
				case '2':
					// 家族割引
					basic_charge = calcBaseCharge(record, basic_charge);
					family_tel_num_array.add(record.getServiceOption());

					// 昼トク割引
					dayService.checkService(record);
					basic_charge = dayService.calcBasicCharge(basic_charge);

					invoice.setBasicCharge(basic_charge);
					break;
				case '5':
					int unit_price = 20; //基本の通話単価

					// 昼トク割引対象
					unit_price = dayService.calcUnitPrice(record, unit_price);

					call_charge = calcCallCharge(record, basic_charge, family_tel_num_array,unit_price);
					invoice.addCallCharge(call_charge);
					break;
				// 1桁目が9の場合は請求ファイルに契約者の請求金額を出力
				case '9':
					// 請求ファイルに契約者情報を書き込み
					InvoiceWriter writer = new InvoiceWriter(new StringWriter());
					writer.write(invoice);
					writer.close();

					// 各インスタンスを初期化
					invoice.clear();
					dayService.clear();

					// Invoiceインスタンス生成
					invoice = new Invoice();

					// 契約者情報
					owner_tel_num = null;
					invoice.setOwnerTelNumber(owner_tel_num);

					// 基本料金：1000円/月
					basic_charge = 1000;
					invoice.setBasicCharge(basic_charge);

					// 通話料金（合計）
					call_charge = 0;
					invoice.addCallCharge(call_charge);
					break;
					}
				}
			// 最後にファイルを閉じてリソースを開放する

			recordreader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int calcCallCharge(Record record, int basic_charge, ArrayList<String> family_tel_num_array,int unit_price) {
		int call_duration = record.getCallMinutes();
		String dst_tel_num = record.getCallNumber();
		int call_charge = 0;
		// 通話料金を計算
		// 家族割引と昼特割引両方に加入している場合
		if (basic_charge == 1300) {
			if (family_tel_num_array.contains(dst_tel_num)) {
				unit_price = unit_price / 2;
			}
		}
		// 家族割引のみに加入している場合
		else if (basic_charge == 1100) {
			if (family_tel_num_array.contains(dst_tel_num)) {
				unit_price = unit_price / 2;
			}
		}
		call_charge = unit_price * call_duration;
		return call_charge;
	}

	private static int calcBaseCharge(Record record, int basic_charge) {
		String service_code = record.getServiceCode();
		if (service_code.equals("C1")) {
			if (basic_charge == 1000) {
				basic_charge += 100;
			}
		}
		return basic_charge;
	}

	public static String getOwnerTelNumber(String line) {
		String[] list = line.split(" ");
		return list[1];
	}
}