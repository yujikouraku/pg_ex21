package step6;

import java.io.StringWriter;

public class InvoiceWriter {
	private StringWriter output;

	public InvoiceWriter(StringWriter output) {
		this.output.write("1 " + owner_tel_num + "\n");
		this.output.write("5 " + basic_charge + "\n");
		this.output.write("7 " + call_charge + "\n");
		this.output.write("9 " + "====================" + "\n");
	}

	public void write(Invoice invoice) {
		String owner_tel_num = invoice.getOwnerTelNumber();
		int basic_charge =  invoice.getBasicCharge();
		int call_charge = invoice.getCallCharge();
	}

	public String toString(){
		return output;
	}

	public void close() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
