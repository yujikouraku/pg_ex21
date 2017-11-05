package step10;

import java.io.StringWriter;

public class InvoiceCalcProgram {

	public static void main(String[] args) {
		String owner_tel_num = null;
		int basic_charge = 1000;
		int call_charge = 0;

		// 通話記録ファイルを読み込み
		try {
			// RecordReader インスタンス生成
			RecordReader recordreader = new RecordReader();


			// 通話料金（合計）
			Invoice invoice = new Invoice();// Invoiceインスタンス生成
			ServiceCollection service = new ServiceCollection();// Serviceインスタンス生成

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
					// 基本料金を計算
					basic_charge = invoice.getBasicCharge();
					service.checkService(record);
					basic_charge = service.calcBasicCharge(basic_charge);
					invoice.setBasicCharge(basic_charge);
					break;
				case '5':
					int unit_price = 20; // 基本の通話単価
					// 通話単価を算出
					unit_price = service.calcUnitPrice(record, unit_price);
					// 通話料金計算
					call_charge = calcCallCharge(record, unit_price);
					invoice.addCallCharge(call_charge);
					break;
				// 1桁目が9の場合は請求ファイルに契約者の請求金額を出力
				case '9':
					// 請求ファイルに契約者情報を書き込み
					InvoiceWriter writer = new InvoiceWriter(new StringWriter());
					writer.write(invoice);
					writer.close();
					// 当該インスタンス初期化
					invoice.clear();
					service.clear();
					// 次のインスタンス生成
					invoice = new Invoice();// Invoiceインスタンス生成
					service = new ServiceCollection();// Serviceインスタンス生成
					break;
				}
			}
			// 最後にファイルを閉じてリソースを開放する
			recordreader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int calcCallCharge(Record record, int unit_price) {
		int call_duration = record.getCallMinutes();
		int call_charge = 0;
		call_charge = unit_price * call_duration;
		return call_charge;
	}}