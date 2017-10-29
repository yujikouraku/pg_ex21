package step4;

import static org.junit.Assert.*;

import org.junit.Test;

public class Record_Test {

	@Test
	public void getRecordCodeTest1() {
		Record record = new Record("1 090-1234-0001");
		assertEquals('1', record.getRecordCode());
	}

	@Test
	public void getOwnerTelNumberTest1() {
		Record record = new Record("1 090-1234-0001");
		assertEquals("090-1234-0001", record.getOwnerTelNumber());
	}

	@Test
	public void getServiceCodeTest1() {
		Record record = new Record("2 C1 090-1234-0002");
		assertEquals("C1", record.getServiceCode());
	}

	@Test
	public void getServiceCodeTest2() {
		Record record = new Record("2 E1");
		assertEquals("E1", record.getServiceCode());
	}

	@Test
	public void getServiceOptionTest1() {
		Record record = new Record("2 C1 090-1234-0002");
		assertEquals("090-1234-0002", record.getServiceOption());
	}

	@Test
	public void getServiceOptionTest2() {
		Record record = new Record("2 E1");
		assertEquals(null, record.getServiceOption());
	}

	@Test
	public void getStartHourTest1() {
		Record record = new Record("5 2004/06/05 17:50 010 090-1234-9999");
		assertEquals(17, record.getStartHour());
	}

	@Test
	public void getCallMinutesTest1() {
		Record record = new Record("5 2004/06/05 17:50 010 090-1234-9999");
		assertEquals(10, record.getCallMinutes());
	}

	@Test
	public void getCallNumberTest1() {
		Record record = new Record("5 2004/06/05 17:50 010 090-1234-9999");
		assertEquals("090-1234-9999", record.getCallNumber());
	}

	@Test
	public void toStringTest1() {
		Record record = new Record("5 2004/06/05 17:50 010 090-1234-9999");
		assertEquals("5 2004/06/05 17:50 010 090-1234-9999", record.toString());
	}



}
