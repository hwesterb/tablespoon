package se.kth.tablespoon.client.events;
  
/* collectl V4.0.4
    0 - [CPU]User%
    1 - [CPU]Nice%
    2 - [CPU]Sys%
    3 - [CPU]Wait%
    4 - [CPU]Irq%
    5 - [CPU]Soft%
    6 - [CPU]Steal%
    7 - [CPU]Idle%
    8 - [CPU]Totl%
    9 - [CPU]Guest%
    10 - [CPU]GuestN%
    11 - [CPU]Intrpt/sec
    12 - [CPU]Ctx/sec
    13 - [CPU]Proc/sec
    14 - [CPU]ProcQue
    15 - [CPU]ProcRun
    16 - [CPU]L-Avg1
    17 - [CPU]L-Avg5
    18 - [CPU]L-Avg15
    19 - [CPU]RunTot
    20 - [CPU]BlkTot
    21 - [MEM]Tot
    22 - [MEM]Used
    23 - [MEM]Free
    24 - [MEM]Shared
    25 - [MEM]Buf
    26 - [MEM]Cached
    27 - [MEM]Slab
    28 - [MEM]Map
    29 - [MEM]Anon
    30 - [MEM]AnonH
    31 - [MEM]Commit
    32 - [MEM]Locked
    33 - [MEM]SwapTot
    34 - [MEM]SwapUsed
    35 - [MEM]SwapFree
    36 - [MEM]SwapIn
    37 - [MEM]SwapOut
    38 - [MEM]Dirty
    39 - [MEM]Clean
    40 - [MEM]Laundry
    41 - [MEM]Inactive
    42 - [MEM]PageIn
    43 - [MEM]PageOut
    44 - [MEM]PageFaults
    45 - [MEM]PageMajFaults
    46 - [MEM]HugeTotal
    47 - [MEM]HugeFree
    48 - [MEM]HugeRsvd
    49 - [MEM]SUnreclaim
    50 - [NET]RxPktTot
    51 - [NET]TxPktTot
    52 - [NET]RxKBTot
    53 - [NET]TxKBTot
    54 - [NET]RxCmpTot
    55 - [NET]RxMltTot
    56 - [NET]TxCmpTot
    57 - [NET]RxErrsTot
    58 - [NET]TxErrsTot
    59 - [DSK]ReadTot
    60 - [DSK]WriteTot
    61 - [DSK]OpsTot
    62 - [DSK]ReadKBTot
    63 - [DSK]WriteKBTot
    64 - [DSK]KbTot
    65 - [DSK]ReadMrgTot
    66 - [DSK]WriteMrgTot
    67 - [DSK]MrgTot
    68 - [MEM]MemoryUsed%
  */



public class Resource {
  
  private int collectIndex;
  private ResourceType resourceType;
  private boolean collectIndexPriority;
  
  
  public Resource(int collectIndex) {
    this.collectIndex = collectIndex;
    collectIndexPriority = true;
  }
  
  public Resource(ResourceType resourceType) {
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
