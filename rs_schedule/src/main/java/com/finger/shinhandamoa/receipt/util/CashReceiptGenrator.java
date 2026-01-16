package com.finger.shinhandamoa.receipt.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.finger.shinhandamoa.data.CashReceiptFactory;
import com.finger.shinhandamoa.data.model.CashReceiptMaster;

/**
 * KIS 현금영수증 전문파일을 DB로 import
 * @author jhjeong@finger.co.kr
 * @modified 2018. 10. 24.
 */
public class CashReceiptGenrator {
	
	/**
	 * 전문을 라인별로 읽어서 FW_CASH_RECEIPT_MASTER 테이블로 insert 한다.
	 * @param messages
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 25.
	 */
	private void parseMessage(List<String> messages) {
		
		CashReceiptManager crmanager = new CashReceiptManager();
		int parseCount = 0;
		int row = 0;
		long curTime = System.currentTimeMillis();
		for (String message : messages) {
			row++;
			if (row == 1) continue;
			if (row == messages.size()) break;
			CashReceiptMaster record = CashReceiptFactory.parse(message);
			parseCount += crmanager.insertCashReceiptDataSelective(record);
		}
		long finishedTime = System.currentTimeMillis() - curTime;
		crmanager.commitAfterClose();
		
		System.out.println(String.format("CashReceiptData : FW_CASH_RECEIPT_MASTER %d rows Inserted (%dsec)", parseCount, Math.round(finishedTime/1000)));
	}
	
	/**
	 * 입력 받은 인자가 directory 인경우 하위 서칭후 파일변환
	 * 파일인 경우 라인별로 읽어들인다.
	 * @param argument
	 * @throws Exception
	 * @author jhjeong@finger.co.kr
	 * @modified 2018. 10. 25.
	 */
	private void run(String argument) throws Exception {
		
		if (argument == null || argument == "") {
			System.out.println("Argument is empty.");
			System.exit(0);
		}
		
		File target = new File(argument);
		if (!target.exists()) {
			System.out.println("Directory/File is not exists.");
			System.exit(0);
		}
		
		
		if (target.isDirectory()) {
			for(final File f : target.listFiles()) {
				if (f.exists() == false) continue;
				
				if (f.isDirectory()) {
					this.scanDirectories(f);
				} else {
					this.readFile(f);
				}
			}
			
		} else {
			this.readFile(target);
		}
	}
	
	private void scanDirectories(File directory) {
		try {
			
			for(final File f : directory.listFiles()) {
				if (f.exists() == false) continue;
				
				if (f.isDirectory()) {
					this.scanDirectories(f);
				} else {
					this.readFile(f);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void readFile(File file) {
		try {
			final List<String> messages = FileUtils.readLines(file, Charset.forName("KSC5601"));
			this.parseMessage(messages);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		try {
			CashReceiptGenrator crg = new CashReceiptGenrator();
			
			if (args == null) {
				System.out.println("Not enough input arguments : target directory or file name");
				System.out.println("input arguments : directory or file name.  ex> src/test/resources");
				
				Scanner scan = new Scanner(System.in);
				String input = scan.nextLine();
				crg.run(input);
			} else {
				if (args.length == 0) {
					System.out.println("Not enough input arguments : target directory or file name");
					System.out.println("input arguments : directory or file name.  ex> src/test/resources");
					
					Scanner scan = new Scanner(System.in);
					String input = scan.nextLine();
					crg.run(input);
				} else {
					crg.run(args[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
