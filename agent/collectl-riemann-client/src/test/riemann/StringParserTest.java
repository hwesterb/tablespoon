package riemann;

import static org.junit.Assert.*;

import org.junit.Test;

import events.EventLayout;
import events.EventType;
import events.Format;

public class StringParserTest {

	@Test
	public void findTypeTest() {
		EventType et = StringParser.findType("DSK");
		assertEquals(EventType.DSK, et);
	}

	@Test
	public void oneMatchTest() {
		EventLayout[] els = StringParser.handleHeaders("#UTC xxx xxx");
		assertEquals(2, els.length);
		assertEquals(null, els[0].getName());
	}

	@Test
	public void threeMatchTest() {
		EventLayout[] els = StringParser.handleHeaders("#UTC [CPU]UserTot");
		assertEquals(1, els.length);
		assertEquals("User", els[0].getName());
		assertEquals(EventType.CPU, els[0].getType());
	}
	
	
	@Test
	public void handleHeadersTest() {
		EventLayout[] els = StringParser.handleHeaders("#UTC [CPU]User% [CPU]Nice% [CPU]Sys% [CPU]Wait% [CPU]Irq% [CPU]Soft% [CPU]Steal% [CPU]Idle% [CPU]Totl% [CPU]Intrpt/sec [CPU]Ctx/sec [CPU]Proc/sec [CPU]ProcQue [CPU]ProcRun [CPU]L-Avg1 [CPU]L-Avg5 [CPU]L-Avg15 [CPU]RunTot [CPU]BlkTot [NET]RxPktTot [NET]TxPktTot [NET]RxKBTot [NET]TxKBTot [NET]RxCmpTot [NET]RxMltTot [NET]TxCmpTot [NET]RxErrsTot [NET]TxErrsTot [DSK]ReadTot [DSK]WriteTot [DSK]OpsTot [DSK]ReadKBTot [DSK]WriteKBTot [DSK]KbTot [DSK]ReadMrgTot [DSK]WriteMrgTot [DSK]MrgTot");
		assertEquals(EventType.CPU, els[9].getType());
		assertEquals("Intrpt", els[9].getName());
		assertEquals(Format.PERSEC, els[9].getFormat());
	}

}
