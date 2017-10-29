package step6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class InvoiceCalcProgram {

	public static void main(String[] args) {
		// 通話記録ファイルを読み込み
		try {
			// 通話記録ファイルの配置先パスを指定
			String pass1 = "src/resource/record.log";

			// 通話記録ファイルを開く
			File file1 = new File(pass1);

			// 通話記録ファイルから1列ずつ文字列を読み込み
			BufferedReader reader = new BufferedReader(new FileReader(file1));

			// RecordReader インスタンス生成
			RecordReader recordReader = new RecordReader(reader);

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


			for (Record record = recordReader.read(); record != null; record = recordReader.read()) {
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
					basic_charge = calcBaseCharge(record, basic_charge);
					family_tel_num_array.add(record.getServiceOption());
					invoice.setBasicCharge(basic_charge);
					break;
				case '5':
					call_charge = calcCallCharge(record, basic_charge, family_tel_num_array);
					invoice.addCallCharge(call_charge);
					break;
				// 1桁目が9の場合は請求ファイルに契約者の請求金額を出力
				case '9':
					// 請求ファイルに契約者情報を書き込み
					addInvoiceFile(invoice);

					// 各変数を初期化
					invoice.clear();

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
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int calcCallCharge(Record record, int basic_charge, ArrayList<String> family_tel_num_array) {
		int call_time = record.getStartHour();
		int call_duration = record.getCallMinutes();
		String dst_tel_num = record.getCallNumber();
		int call_charge_unit = 20;
		int call_charge = 0;
		// 通話料金を計算
		// 家族割引と昼特割引両方に加入している場合
		if (basic_charge == 1300) {
			if (8 <= call_time && call_time < 18) {
				call_charge_unit = call_charge_unit - 5;
			}
			if (family_tel_num_array.contains(dst_tel_num)) {
				call_charge_unit = call_charge_unit / 2;
			}
			call_charge = call_charge_unit * call_duration;
		}
		// 昼特割引のみに加入している場合
		else if (basic_charge == 1200) {
			if (8 <= call_time && call_time < 18) {
				call_charge_unit = call_charge_unit - 5;
			}
			call_charge = call_charge_unit * call_duration;
		}
		// 家族割引のみに加入している場合
		else if (basic_charge == 1100) {
			if (family_tel_num_array.contains(dst_tel_num)) {
				call_charge_unit = call_charge_unit / 2;
			}
			call_charge = call_charge_unit * call_duration;
		}
		// 家族割引と昼特割引両方に加入していない場合
		else if (basic_charge == 1000) {
			call_charge = call_charge_unit * call_duration;
		}
		return call_charge;
	}

	private static void addInvoiceFile(Invoice invoice) {
		// 請求ファイルの配置先パスを指定
		String pass2 = "src/resource/invoice.dat";

		// 請求ファイルを開く
		File file2 = new File(pass2);

		PrintWriter bufferedWriter;
		try {

			bufferedWriter = new PrintWriter(new BufferedWriter(new FileWriter(file2)));

			String owner_tel_num = invoice.getOwnerTelNumber();
			int basic_charge = invoice.getBasicCharge();
			int call_charge = invoice.getCallCharge();

			// 請求ファイルに契約者情報を書き込み
			bufferedWriter.println("1 " + owner_tel_num);
			System.out.println("1 " + owner_tel_num);

			// 請求ファイルに基本料金を書き込み
			bufferedWriter.println("5 " + basic_charge);
			System.out.println("5 " + basic_charge);

			// 請求ファイルに通話料金を書き込み
			bufferedWriter.println("7 " + call_charge);
			System.out.println("7 " + call_charge);

			// 請求ファイルに区切り文字を書き込み
			bufferedWriter.println("9 " + "====================");
			System.out.println("9 " + "====================");

			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int calcBaseCharge(Record record, int basic_charge) {
		String service_code = record.getServiceCode();
		if (service_code.equals("C1")) {
			if (basic_charge == 1000) {
				basic_charge += 100;
			}
		}
		// 昼トク割引
		if (service_code.equals("E1")) {
			basic_charge += 200;
		}
		return basic_charge;
	}

	public static String getOwnerTelNumber(String line) {
		String[] list = line.split(" ");
		return list[1];
	}
}