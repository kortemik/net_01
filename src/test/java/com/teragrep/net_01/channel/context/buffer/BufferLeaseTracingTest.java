package com.teragrep.net_01.channel.context.buffer;

import com.teragrep.net_01.channel.buffer.BufferLease;
import com.teragrep.net_01.channel.buffer.BufferLeasePool;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BufferLeaseTracingTest {

    @Test
    public void testTracing() {
        BufferLeasePool bufferLeasePool = new BufferLeasePool();

        List<BufferLease> leases = bufferLeasePool.take(1);

        BufferLease lease = leases.get(0);

        lease.addRef(); // add a trace
        lease.removeRef(); // add a trace
        lease.removeRef(); // terminate

        //System.out.println(lease);
    }

}
