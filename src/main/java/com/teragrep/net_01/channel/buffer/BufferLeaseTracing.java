/*
 * Java Zero Copy Networking Library net_01
 * Copyright (C) 2024 Suomen Kanuuna Oy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *
 * Names of the licensors and authors may not be used for publicity purposes.
 *
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
 */
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
