package com.ghvirtualaccount.cmmn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

public class XlasParser2Test {
	@Test
	public void parse() {
		int mb = 1024*1024;
		Runtime runtime = Runtime.getRuntime();
		System.out.println("Used Memory:"+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		long start = System.currentTimeMillis();
		String filepath = "C:\\Users\\FINGER\\Downloads\\test500.xlsx";
		//XlsxEventParser example = new XlsxEventParser();
		List<List<Object>> listOfList = null;
		try {
			//listOfList = example.parse(new FileInputStream(filepath), 2, 50);
			listOfList = XlsxUserParser.parse(new FileInputStream(filepath), 2, 50);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
//		System.out.println("test:" + listOfList);
		System.out.println(listOfList.size());
		System.out.println("Used Memory:"+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		long end = System.currentTimeMillis();
		System.out.println( "User실행 시간 : " + ( end - start )/1000.0 );
	}

	@Test
	public void parse2() {
		int mb = 1024*1024;
		Runtime runtime = Runtime.getRuntime();
		System.out.println("Used Memory:"+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		long start = System.currentTimeMillis();
		String filepath = "C:\\Users\\FINGER\\Downloads\\test500.xlsx";
		//XlsxEventParser example = new XlsxEventParser();
		List<List<Object>> listOfList = null;
		try {
			//listOfList = example.parse(new FileInputStream(filepath), 2, 50);
			listOfList = XlsxEventParser.parse(new FileInputStream(filepath), 2, 50);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
//		System.out.println("test:" + listOfList);
		System.out.println(listOfList.size());
		System.out.println("Used Memory:"+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		long end = System.currentTimeMillis();
		System.out.println( "Event실행 시간 : " + ( end - start )/1000.0 );
		
		System.out.println(listOfList.get(0));
	}
	
}
