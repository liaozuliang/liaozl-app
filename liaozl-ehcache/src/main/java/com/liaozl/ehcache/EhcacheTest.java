package com.liaozl.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheTest {

	private static final String TABLE_NAME = "table1";

	private static final String ROW1 = "row1";
	private static final String ROW2 = "row2";

	private final static int LOOP_COUNT = 100 * 10000;

	private Cache table;

	enum ColumnEnum {
		ID, NAME, ADDRESS
	}

	private void init() {
		CacheManager cm = CacheManager.create();
		cm.addCache(TABLE_NAME);
		table = cm.getCache(TABLE_NAME);

		// =======row1========
		cm.addCache(ROW1);
		Cache row1 = cm.getCache(ROW1);

		row1.put(new Element(ColumnEnum.ID, "row1_id"));
		row1.put(new Element(ColumnEnum.NAME, "row1_name"));
		row1.put(new Element(ColumnEnum.ADDRESS, "row1_address"));

		table.put(new Element(ROW1, row1));

		// =======row2========
		cm.addCache(ROW2);
		Cache row2 = cm.getCache(ROW2);

		row2.put(new Element(ColumnEnum.ID, "row2_id"));
		row2.put(new Element(ColumnEnum.NAME, "row2_name"));
		row2.put(new Element(ColumnEnum.ADDRESS, "row2_address"));

		table.put(new Element(ROW2, row2));
	}

	private void query(int index, String row_key, ColumnEnum column_key) {
		if (table.isKeyInCache(row_key)) {
			Cache row = (Cache) table.get(row_key).getObjectValue();
			if (row.isKeyInCache(column_key)) {
				String columnData = (String) row.get(column_key).getObjectValue();
				System.out.println(index + ", " + Thread.currentThread().getName() + ", 找到行列[" + row_key + "][" + column_key + "]数据：" + columnData);
			} else {
				System.err.println(index + ", " + Thread.currentThread().getName() + ", 未找到列" + column_key);
			}
		} else {
			System.err.println(index + ", " + Thread.currentThread().getName() + ", 未找到数据行" + row_key);
		}
	}

	private void loopQuery(String row_key, ColumnEnum column_key) {
		for (int i = 0; i < LOOP_COUNT; i++) {
			query(i, row_key, column_key);
		}
	}

	private void test() {
		init();

		long startTime = System.currentTimeMillis();

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				loopQuery(EhcacheTest.ROW1, ColumnEnum.ID);
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				loopQuery(EhcacheTest.ROW2, ColumnEnum.NAME);
			}
		});
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				loopQuery(EhcacheTest.ROW1, ColumnEnum.ID);
			}
		});
		Thread t4 = new Thread(new Runnable() {
			@Override
			public void run() {
				loopQuery(EhcacheTest.ROW1, ColumnEnum.ADDRESS);
			}
		});
		Thread t5 = new Thread(new Runnable() {
			@Override
			public void run() {
				loopQuery(EhcacheTest.ROW1, ColumnEnum.NAME);
			}
		});
		Thread t6 = new Thread(new Runnable() {
			@Override
			public void run() {
				loopQuery(EhcacheTest.ROW2, ColumnEnum.ID);
			}
		});

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();

		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();

			long takeTime = System.currentTimeMillis() - startTime;
			System.out.println("takeTime:" + takeTime + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EhcacheTest t = new EhcacheTest();
		t.test();
	}
}
