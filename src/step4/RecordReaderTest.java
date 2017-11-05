package step4;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

public class RecordReaderTest {

	@Test
	public void testRecordReader1() {

		try {
			RecordReader recordreader = new RecordReader();
		} catch (FileNotFoundException e) {
			fail("ファイルが存在しません");
			e.printStackTrace();
		}
	}

}
