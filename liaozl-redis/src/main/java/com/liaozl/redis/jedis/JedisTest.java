package com.liaozl.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JedisTest {

	private static final String IP = "192.168.18.66";
	private static final int READ_PORT = 8379;
	private static final int WRITE_PORT = 8379;

	private static final String ROW_KEY_TEMP = "RedisRowKey_%s_%s_%s";

	private static final String DEFAULT_DATABASE = "database1";
	private static final String DEFAULT_TABLE = "table1";
	private static final int MAX_ROW = 5 * 10000;

	enum ColumnEnum {
		ID, NAME, AGE, EMAIL, ADDRESS
	}

	public static void testAdd() {
		long startTime = System.currentTimeMillis();

		Jedis jedis = JedisFactory.getInstance().getJedis(IP, WRITE_PORT);

		String row_key = null;
		String pkId = null;
		HashMap<String, String> row_data_map = null;

		for (int i = 1; i <= MAX_ROW; i++) {
			pkId = String.valueOf(i);
			row_key = String.format(ROW_KEY_TEMP, DEFAULT_DATABASE, DEFAULT_TABLE, pkId);

			row_data_map = new HashMap<String, String>();
			row_data_map.put(ColumnEnum.ID.toString(), "id" + i);
			row_data_map.put(ColumnEnum.NAME.toString(), "name" + i);
			row_data_map.put(ColumnEnum.AGE.toString(), "age" + i);
			row_data_map.put(ColumnEnum.EMAIL.toString(), "email" + i);
			row_data_map.put(ColumnEnum.ADDRESS.toString(), "address" + i);

			jedis.hmset(row_key, row_data_map);
		}

		long taketime = System.currentTimeMillis() - startTime;
		long min = taketime / 1000 / 60;
		long ms = taketime / MAX_ROW;
		System.out.println("testAdd dataCount:" + MAX_ROW + ", takeTime:" + taketime + "ms,  " + min + "分钟，" + ms + " 毫秒/条");
	}
	

	public static void testQuery() {
		long startTime = System.currentTimeMillis();

		Jedis jedis = JedisFactory.getInstance().getJedis(IP, READ_PORT);

		String row_key = null;
		String pkId = null;
		Map<String, String> rowData = null;

		for (int i = 1; i <= MAX_ROW; i++) {
			pkId = String.valueOf(i);
			row_key = String.format(ROW_KEY_TEMP, DEFAULT_DATABASE, DEFAULT_TABLE, pkId);

			rowData = jedis.hgetAll(row_key);
		}

		long taketime = System.currentTimeMillis() - startTime;
		long min = taketime / 1000 / 60;
		long ms = taketime / MAX_ROW;
		System.out.println("testQuery dataCount:" + MAX_ROW + ", takeTime:" + taketime + "ms,  " + min + "分钟，" + ms + " 毫秒/条");
	}

	public static void testTransaction() {
		Jedis jedis = JedisFactory.getInstance().getJedis("192.168.18.66", 8379);
		long start = System.currentTimeMillis();

		Transaction tx = jedis.multi();

		for (int i = 0; i < 10; i++) {
			tx.set("testTransaction" + i, "t" + i);
		}

		List<Object> results = tx.exec();

		long end = System.currentTimeMillis();

		System.out.println("Transaction SET: " + ((end - start) / 1000.0) + " seconds");

		jedis.disconnect();
	}

	public static void main(String[] args) {
		for(int i=0;i<5;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					JedisTest.testAdd();
				}
			}).start();
		}
		
		for(int i=0;i<5;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					JedisTest.testQuery();
				}
			}).start();
		}
	}
}
