/*
 * Copyright 2024, AutoMQ CO.,LTD.
 *
 * Use of this software is governed by the Business Source License
 * included in the file BSL.md
 *
 * As of the Change Date specified in that file, in accordance with
 * the Business Source License, use of this software will be governed
 * by the Apache License, Version 2.0
 */
package com.automq.stream;

import io.netty.buffer.ByteBuf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByteBufSeqAllocTest {

    @Test
    public void testAlloc() {
        ByteBufSeqAlloc alloc = new ByteBufSeqAlloc(0, 8);

        ByteBuf buf1 = alloc.byteBuffer(12);
        buf1.writeLong(1);
        buf1.writeInt(2);

        ByteBuf buf2 = alloc.byteBuffer(20);
        buf2.writeLong(3);
        buf2.writeInt(4);
        buf2.writeLong(5);

        ByteBuf buf3 = alloc.byteBuffer(ByteBufSeqAlloc.HUGE_BUF_SIZE - 12 - 20 - 4);

        ByteBuf buf4 = alloc.byteBuffer(16);
        buf4.writeLong(6);
        buf4.writeLong(7);

        assertEquals(1, buf1.readLong());
        assertEquals(2, buf1.readInt());
        assertEquals(3, buf2.readLong());
        assertEquals(4, buf2.readInt());
        assertEquals(5, buf2.readLong());
        assertEquals(6, buf4.readLong());
        assertEquals(7, buf4.readLong());

        buf1.release();
        buf2.release();
        buf3.release();
        buf4.release();

        alloc.byteBuffer(ByteBufSeqAlloc.HUGE_BUF_SIZE - 12).release();
        alloc.byteBuffer(12).release();

    }
}
