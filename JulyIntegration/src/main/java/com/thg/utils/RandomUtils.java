package com.thg.utils;

import java.util.Random;

/**
 * Created by tanhuigen
 * Date 2022-09-17
 * Description
 */
public class RandomUtils {


    private static final Random random = new Random();
    private static final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1);


    public static long generateNewId() {
        return idGenerator.nextId();
    }

    public static int nextInt() {
        return Math.abs(random.nextInt());
    }

    public static int nextInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }


    public static long nextLong() {
        return Math.abs(random.nextLong());
    }

    public static long nextLong(long minLong, long maxLong) {
        if (maxLong - minLong < Integer.MAX_VALUE - 1) {
            return random.nextInt((int) (maxLong - minLong + 1)) + minLong;
        }
        return (long) (Math.random() * (maxLong - minLong)) + minLong;
    }

    public static String nextString(int minLength, int maxLength) {
        int length = random.nextInt() % (maxLength - minLength + 1) + minLength;
        return nextString(length);
    }

    public static String nextString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append((char) result);
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append((char) result);
                    break;
                case 2:
                    sb.append(new Random().nextInt(10));
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

}
