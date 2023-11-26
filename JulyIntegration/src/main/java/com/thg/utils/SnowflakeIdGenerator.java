package com.thg.utils;

import lombok.Data;

/**
 * Created by tanhuigen
 * Date 2022-09-17
 * Description
 */

/*
 *  @timestamp    : 41bits
 *  @dataCenterId : 6bits
 *  @workId       : 8bits
 *  @nonce        : 6bits
 *  @snowflake Id = timestamp | dataCenterId | workId | nonce
 *
 */
@Data
public class SnowflakeIdGenerator {

    private long workId;
    private long dataCenterId;

    private long nonce = 0;

    private long lastTimestamp = -1L;

    /*  2020-01-01 */
    private final long twepoch = 1577808000L;

    private final int timestampBits = 41;
    private final int dataCenterBits = 6;
    private final int workIdBits = 8;
    private final int nonceBits = 6;

    private final int timestampLeftShift = dataCenterBits+workIdBits+nonceBits;
    private final int dataCenterLeftShift = workIdBits+nonceBits;
    private final int workIdLeftShift = nonceBits;

    private final long nonceMask = -1L ^ (-1L << nonceBits);

    public SnowflakeIdGenerator(int workId, long dataCenterId) {
        this.workId = workId;
        this.dataCenterId = dataCenterId;
    }

    public synchronized Long nextId(){
        long timestamp = timeGen();
        if(timestamp < lastTimestamp){
            throw new RuntimeException(
                    String.format("Snowflake timestamp moved backwards. Refusing at lastTimestamp : %d and current timestamp %d",lastTimestamp,timestamp)
            );
        }

        if(timestamp == lastTimestamp){
            nonce = (nonce + 1) & nonceMask;
            if(nonce == 0){
                //阻塞到下一秒
                timestamp = tilNextMillis(timestamp);
            }
        }else{
            nonce = 0L;
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift)
             | (dataCenterId << dataCenterLeftShift)
             | (workId << workIdLeftShift)
             | nonce;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
