package se.kth.tablespoon.agent.general;

import se.kth.tablespoon.agent.general.CollectlStringParser;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.kth.tablespoon.agent.metrics.MetricLayout;
import se.kth.tablespoon.agent.events.ResourceType;
import se.kth.tablespoon.agent.metrics.MetricFormat;

public class CollectlStringParserTest {

	@Test
	public void findTypeTest() {
		ResourceType et = CollectlStringParser.findType("DSK");
		assertEquals(ResourceType.DSK, et);
	}

	@Test
	public void oneMatchTest() {
		MetricLayout[] els = CollectlStringParser.handleHeaders("#UTC xxx xxx");
		assertEquals(2, els.length);
		assertEquals(null, els[0].getName());
	}

	@Test
	public void threeMatchTest() {
		MetricLayout[] els = CollectlStringParser.handleHeaders("#UTC [CPU]UserTot");
		assertEquals(1, els.length);
		assertEquals("User", els[0].getName());
		assertEquals(ResourceType.CPU, els[0].getSource());
	}
	
	
	@Test
	public void handleHeadersTest() {
		MetricLayout[] els = CollectlStringParser.handleHeaders("#UTC [CPU]User% [CPU]Nice% [CPU]Sys% [CPU]Wait% [CPU]Irq% [CPU]Soft% [CPU]Steal% [CPU]Idle% [CPU]Totl% [CPU]Intrpt/sec [CPU]Ctx/sec [CPU]Proc/sec [CPU]ProcQue [CPU]ProcRun [CPU]L-Avg1 [CPU]L-Avg5 [CPU]L-Avg15 [CPU]RunTot [CPU]BlkTot [NET]RxPktTot [NET]TxPktTot [NET]RxKBTot [NET]TxKBTot [NET]RxCmpTot [NET]RxMltTot [NET]TxCmpTot [NET]RxErrsTot [NET]TxErrsTot [DSK]ReadTot [DSK]WriteTot [DSK]OpsTot [DSK]ReadKBTot [DSK]WriteKBTot [DSK]KbTot [DSK]ReadMrgTot [DSK]WriteMrgTot [DSK]MrgTot");
		assertEquals(ResourceType.CPU, els[9].getSource());
		assertEquals("Intrpt", els[9].getName());
		assertEquals(MetricFormat.PERSEC, els[9].getFormat());
	}

}
