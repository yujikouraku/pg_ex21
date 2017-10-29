package step4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import step5.Record;

public class InvoiceCalcProgram {
	private static int call_charge_temp;

	public static void main(String[] args) {
		// 通話記録ファイルを読み込み
		try {
			// 通話記録ファイルの配置先パスを指定
			String pass1 = "src/resource/record.log";

			// 通話記録ファイルを開く
			File file1 = new File(pass1);

			// 通話記録ファイルから1列ずつ文字列を読み込み
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
			// RecordReader インスタンス生成
			RecordReader recordReader = new RecordReader(bufferedReader);

			// 契約者情報
			String src_tel_num = null;

			// 加入サービス情報
			ArrayList<String> family_tel_num_array = new ArrayList<String>();
			// 請求金額
			// 基本料金：1000円/月
			int base_charge = 1000;
			// 通話料金（合計）
			int call_charge_total = 0;

			for (step4.Record record = recordReader.read(); record != null; record = recordReader.read()) {
				// 読み込んだ文字列の一桁目の文字を取得
				char init_char = record.getRecordCode();
				// 一桁目の文字で処理を分岐
				// 1桁目が1の場合は契約者の電話番号を取得
				switch (init_char) {
				case '1':
					// src_tel_num = list[1];
					src_tel_num = record.getOwnerTelNumber();
					break;
				// 1桁目が2の場合は加入サービス情報を取得
				case '2':
					base_charge = calcBaseCharge(record.toString(), base_charge);
					family_tel_num_array.add(record.getServiceOption());
					break;
				case '5':
					// call_date = list[1];
					call_charge_total += calcCallCharge(record.toString(), base_charge, family_tel_num_array);
					break;
				// 1桁目が9の場合は請求ファイルに契約者の請求金額を出力
				case '9':
					// 請求ファイルに契約者情報を書き込み
					addInvoiceFile(src_tel_num, base_charge, call_charge_total);

					// 各変数を初期化
					base_charge = 1000;
					call_charge_total = 0;
					break;
			}
			}
			// 最後にファイルを閉じてリソースを開放する
			recordReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int calcCallCharge(String line, int base_charge, ArrayList<String> family_tel_num_array) {

		Record record = new Record(line);
		int call_time = record.getStartHour();
		int call_duration = record.getCallMinutes();
		String dst_tel_num = record.getCallNumber();
		int call_charge = 20;
		call_charge_temp = 0;
		// 通話料金を計算
		// 家族割引と昼特割引両方に加入している場合
		if (base_charge == 1300) {
			if (8 <= call_time && call_time < 18) {
				call_charge = call_charge - 5;
			}
			if (family_tel_num_array.contains(dst_tel_num)) {
				call_charge = call_charge / 2;
			}
			call_charge_temp = call_charge * call_duration;
		}
		// 昼特割引のみに加入している場合
		else if (base_charge == 1200) {
			if (8 <= call_time && call_time < 18) {
				call_charge = call_charge - 5;
			}
			call_charge_temp = call_charge * call_duration;
		}
		// 家族割引のみに加入している場合
		else if (base_charge == 1100) {
			if (family_tel_num_array.contains(dst_tel_num)) {
				call_charge = call_charge / 2;
			}
			call_charge_temp = call_charge * call_duration;
		}
		// 家族割引と昼特割引両方に加入していない場合
		else if (base_charge == 1000) {
			call_charge_temp = call_charge * call_duration;
		}
		return call_charge_temp;
	}

	private static void addInvoiceFile(String src_tel_num, int base_charge, int call_charge_total) {
		// 請求ファイルの配置先パスを指定
		String pass2 = "src/resource/invoice.dat";

		// 請求ファイルを開く
		File file2 = new File(pass2);

		PrintWriter bufferedWriter;
		try {

			bufferedWriter = new PrintWriter(new BufferedWriter(new FileWriter(file2)));

			// 請求ファイルに契約者情報を書き込み
			bufferedWriter.println("1 " + src_tel_num);
			System.out.println("1 " + src_tel_num);

			// 請求ファイルに基本料金を書き込み
			bufferedWriter.println("5 " + base_charge);
			System.out.println("5 " + base_charge);

			// 請求ファイルに通話料金を書き込み
			bufferedWriter.println("7 " + call_charge_total);
			System.out.println("7 " + call_charge_total);

			// 請求ファイルに区切り文字を書き込み
			bufferedWriter.println("9 " + "====================");
			System.out.println("9 " + "====================");

			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int calcBaseCharge(String line, int base_charge) {
		String[] list = line.split(" ");
		if (list[1].charAt(0) == 'C') {
			if (base_charge == 1000) {
				base_charge += 100;
			}
		}
		// 昼トク割引
		if (list[1].charAt(0) == 'E') {
			base_charge += 200;
		}
		return base_charge;
	}

	public static String getOwnerTelNumber(String line) {
		String[] list = line.split(" ");
		return list[1];
	}
}