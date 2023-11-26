# 这是第一个文档

```java
        String json = "[{\"mockId\":\"newDAO\",\"methodParams\":{\"hello\":\"hello1\",\"world\":[],\"map2\":{\"1\":\"tanhuigen\",\"2\":\"lizehao\"}}}]";
        ObjectMapper objectMapper = generaterObjectMapper(null);
        Class<MockData> aClass = MockData.class;
        List<MockData> mockData1 = objectMapper.readValue(json,
            new TypeReference<List<MockData>>() {
            });
        System.out.println("mockData1 = " + mockData1);

        JedisPool jedisPool = new JedisPool();
        Jedis jedis = jedisPool.getResource();
```



记录一下
