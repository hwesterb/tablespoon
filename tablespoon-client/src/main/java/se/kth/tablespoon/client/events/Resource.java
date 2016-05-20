package se.kth.tablespoon.client.events;

/*
0 = [CPU]User%
1 = [CPU]Nice%
2 = [CPU]Sys%
3 = [CPU]Wait%
4 = [CPU]Irq%
5 = [CPU]Soft%
6 = [CPU]Steal%
7 = [CPU]Idle%
8 = [CPU]Totl%
9 = [CPU]Intrpt/sec
10 = [CPU]Ctx/sec
11 = [CPU]Proc/sec
12 = [CPU]ProcQue
13 = [CPU]ProcRun
14 = [CPU]L-Avg1
15 = [CPU]L-Avg5
16 = [CPU]L-Avg15
17 = [CPU]RunTot
18 = [CPU]BlkTot
19 = [MEM]Tot
20 = [MEM]Used
21 = [MEM]Free
22 = [MEM]Shared
23 = [MEM]Buf
24 = [MEM]Cached
25 = [MEM]Slab
26 = [MEM]Map
27 = [MEM]Anon
28 = [MEM]Commit
29 = [MEM]Locked
30 = [MEM]SwapTot
31 = [MEM]SwapUsed
32 = [MEM]SwapFree
33 = [MEM]SwapIn
34 = [MEM]SwapOut
35 = [MEM]Dirty
36 = [MEM]Clean
37 = [MEM]Laundry
38 = [MEM]Inactive
39 = [MEM]PageIn
40 = [MEM]PageOut
41 = [MEM]PageFaults
42 = [MEM]PageMajFaults
43 = [MEM]HugeTotal
44 = [MEM]HugeFree
45 = [MEM]HugeRsvd
46 = [MEM]SUnreclaim
47 = [NET]RxPktTot
48 = [NET]TxPktTot
49 = [NET]RxKBTot
50 = [NET]TxKBTot
51 = [NET]RxCmpTot
52 = [NET]RxMltTot
53 = [NET]TxCmpTot
54 = [NET]RxErrsTot
55 = [NET]TxErrsTot
56 = [DSK]ReadTot
57 = [DSK]WriteTot
58 = [DSK]OpsTot
59 = [DSK]ReadKBTot
60 = [DSK]WriteKBTot
61 = [DSK]KbTot
62 = [DSK]ReadMrgTot
63 = [DSK]WriteMrgTot
64 = [DSK]MrgTot
65 = [MEM]MemoryUsed%
*/
public class Resource {
  
  private int collectIndex;
  private ResourceType resourceType;
  private boolean collectIndexPriority;
  
  public void setCollectIndex(int collectIndex) {
    this.collectIndex = collectIndex;
    collectIndexPriority = true;
  }
  
  public void setResourceType(ResourceType resourceType) {
    this.resourceType = resourceType;
    collectIndexPriority = false;
  }
  
  public int getCollectIndex() {
    return collectIndex;
  }
  
  public ResourceType getResourceType() {
    return resourceType;
  }
  
  public boolean isCollectIndexPriority() {
    return collectIndexPriority;
  }
  
  
}
