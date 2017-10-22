package ex21;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceCalcProgram {

	public static void main(String[] args) {
		// 通話記録ファイルを読み込み
		try {
			// 通話記録ファイルの配置先パスを指定
			String pass1 = "src/resource/record.log";

			// 通話記録ファイルを開く
			File file1 = new File(pass1);

			// 請求ファイルの配置先パスを指定
			String pass2 = "src/resource/invoice.dat";

			// 請求ファイルを
			File file2 = new File(pass2);


			// 通話記録ファイルから1列ずつ文字列を読み込み
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
			PrintWriter bufferedWriter = new PrintWriter(new BufferedWriter(new FileWriter(file2)));

			// 変数定義
			String line;

			int count1 = 0;

			// 契約者情報
			String src_tel_num = null;

			// 通話記録
			//String call_date;

			SimpleDateFormat sdFormat = new SimpleDateFormat("hh:mm");
			Date start_time = sdFormat.parse("08:00");
			Date end_time = sdFormat.parse("17:59");
			Date call_time;


			int call_duration;
			String dst_tel_num;

			// 加入サービス情報
			String family_tel_num1 = null;
			String family_tel_num2 = null;
			// 請求金額
			// 基本料金：1000円/月
			int base_charge = 1000;
			// 通話料金：20円/分
			int call_charge = 20;
			// 通話料金（合計）
			int call_charge_total = 0;


			// 通話記録ファイルのすべての行を読み込むまで繰り返し実行
			while((line = bufferedReader.readLine()) != null) {

				// 文字列を配列に分割
				String[] list = line.split(" ");

				// 読み込んだ文字列の一桁目の文字を取得
				char init_char = line.charAt(0);

				// 一桁目の文字で処理を分岐
				// 1桁目が1の場合は契約者の電話番号を取得
				switch(init_char) {
				case '1':
					src_tel_num = list[1];
					break;
				// 1桁目が2の場合は加入サービス情報を取得
				case '2':
					// 家族割の場合
					if(list[1].charAt(0) == 'C') {
						if (count1 == 0 ) {
							base_charge += 100;
							family_tel_num1 = list[2];
							count1 += 1;
						}else if(count1 == 1) {
							family_tel_num2 = list[2];
						}
					}
					// 昼トク割引
					if(list[1].charAt(0) == 'E') {
						base_charge += 200;
					}
					break;

				case '5':
					//call_date = list[1];
					call_time = sdFormat.parse(list[2]);
					call_duration = Integer.parseInt(list[3]);
					dst_tel_num = list[4];
					// 通話料金を計算
					// 家族割引と昼特割引両方に加入している場合
					if(base_charge == 1300) {
						if(start_time.compareTo(call_time) < 0 && 0 < end_time.compareTo(call_time)){
							call_charge_total += (call_charge - 5) * call_duration;
						}
						if(dst_tel_num == family_tel_num1){
							call_charge_total += call_charge / 2 * call_duration;
						}
						if(dst_tel_num == family_tel_num2){
							call_charge_total += call_charge / 2 * call_duration;
						}
					}
					// 昼特割引のみに加入している場合
					else if(base_charge == 1200){
						if(start_time.compareTo(call_time) < 0 && 0 < end_time.compareTo(call_time)){
							call_charge_total += (call_charge - 5) * call_duration;
						}
					}
					// 家族割引のみに加入している場合
					else if(base_charge == 1100){
						System.out.println(dst_tel_num);
						System.out.println(family_tel_num1);
						System.out.println(dst_tel_num == family_tel_num1);
						if(dst_tel_num == family_tel_num1){
							call_charge_total += call_charge / 2 * call_duration;
						}
						if(dst_tel_num == family_tel_num2){
							call_charge_total += call_charge / 2 * call_duration;
						}
					}
					// 家族割引と昼特割引両方に加入していない場合
					else if(base_charge == 1000){
						call_charge_total += call_charge * call_duration;
					}
					break;
				// 1桁目が9の場合は請求ファイルに契約者の請求金額を出力
				case '9':
					// 請求ファイルに契約者情報を書き込み
					System.out.println("1 " + src_tel_num);
					bufferedWriter.println("1 " + src_tel_num);

					// 請求ファイルに基本料金を書き込み
					System.out.println("5 " + base_charge);
					bufferedWriter.println("5 " + base_charge);

					// 請求ファイルに通話料金を書き込み
					System.out.println("7 " + call_charge_total);
					bufferedWriter.println("7 " + call_charge_total);

					// 請求ファイルに区切り文字を書き込み
					System.out.println("9 " + "====================");
					bufferedWriter.println("9 " + "====================");

					//各変数を初期化

					count1 = 0;
					base_charge = 1000;
					call_charge_total = 0;
					break;
				}
			}
			// 最後にファイルを閉じてリソースを開放する
			bufferedReader.close();
			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
