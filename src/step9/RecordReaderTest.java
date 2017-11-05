package step9;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

public class RecordReaderTest {

	@Test
	public void RecordReaderTest1() throws Exception{

		RecordReader recordreader = new RecordReader(new StringReader(""));
		assertNull(recordreader.read());
		recordreader = new RecordReader(new StringReader("a"));
		assertNotNull(recordreader.read());
		recordreader = new RecordReader(new StringReader("ab"));
		assertEquals("ab",recordreader.read().toString());


	}

}
