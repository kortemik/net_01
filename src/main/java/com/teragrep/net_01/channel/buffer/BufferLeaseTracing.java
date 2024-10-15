package com.teragrep.net_01.channel.buffer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class BufferLeaseTracing implements BufferLease {

    private final BufferLease bufferLease;
    private final SortedMap<Long, Exception> traceMap;

    public BufferLeaseTracing(BufferLease bufferLease) {
        this(bufferLease, new TreeMap<>());
    }

    public BufferLeaseTracing(BufferLease bufferLease, SortedMap<Long, Exception> traceMap) {
        this.bufferLease = bufferLease;
        this.traceMap = traceMap;
    }

    @Override
    public long id() {
        addTrace(new Exception());
        return bufferLease.id();
    }

    @Override
    public long refs() {
        addTrace(new Exception());
        return bufferLease.refs();
    }

    @Override
    public ByteBuffer buffer() {
        addTrace(new Exception());
        return bufferLease.buffer();
    }

    @Override
    public void addRef() throws IllegalStateException {
        addTrace(new Exception());
        bufferLease.addRef();
    }

    @Override
    public void removeRef() throws IllegalStateException {
        addTrace(new Exception());
        bufferLease.removeRef();
    }

    @Override
    public boolean isTerminated() {
        addTrace(new Exception());
        return bufferLease.isTerminated();
    }

    @Override
    public boolean isStub() {
        addTrace(new Exception());
        return bufferLease.isStub();
    }

    private void addTrace(Exception e) {
        traceMap.put(System.nanoTime(), e);
    }

    @Override
    public String toString() {
        SortedMap<Long, String> printMap = new TreeMap<>();
        for (Map.Entry<Long, Exception> entry : traceMap.entrySet()) {
            if (entry.getValue().getStackTrace().length > 1) {
                StackTraceElement ste0 = entry.getValue().getStackTrace()[0];
                StackTraceElement ste1 = entry.getValue().getStackTrace()[1];

                printMap.put(entry.getKey(), "\n" + ste0 + "\n" + ste1);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, String> entry : printMap.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
        }
        return sb.toString();
    }
}
