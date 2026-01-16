package com.ghvirtualaccount.cmmn;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class XlsxEventParser {

	public static List<List<Object>> parse(InputStream inputStream, int startRow, int columnCount) throws Exception {
		OPCPackage pkg = OPCPackage.open(inputStream);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst, startRow, columnCount);

		// To look up the Sheet Name / Sheet Order / rID,
		// you need to process the core Workbook stream.
		// Normally it's of the form rId# or rSheet#
		// 첫번째 시트
		InputStream sheet2 = r.getSheet("rId1");
		InputSource sheetSource = new InputSource(sheet2);

		parser.parse(sheetSource);
		sheet2.close();

		return ((SheetHandler) parser.getContentHandler()).getList();
	}

	private static XMLReader fetchSheetParser(SharedStringsTable sst, int startRow, int columnCount) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new SheetHandler(sst, startRow, columnCount);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private static class SheetHandler extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;

		private List<List<Object>> itemList = new ArrayList<>();
		private int startRow;
		private int columnCount;

		private int rowNo = 0;
		private int columnNo = 0;
		private boolean end = false;

		private List<Object> currentData;

		private SheetHandler(SharedStringsTable sst, int startRow, int columnCount) {
			this.sst = sst;
			this.startRow = startRow;
			this.columnCount = columnCount;
		}

		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

			if (name.equals("row")) {
				rowNo++;
				currentData = new ArrayList<>();
			}

			// c => cell
			if (name.equals("c")) {
				columnNo++;
				// Print the cell reference
				// System.out.print(attributes.getValue("r") + " - ");
				// Figure out if the value is an index in the SST
				String cellType = attributes.getValue("t");
				if (cellType != null && cellType.equals("s")) {
					if (columnNo > columnCount) {
						nextIsString = false;
					}else {
					nextIsString = true;
					}
				} else {
					nextIsString = false;
				}
			}
			// Clear contents cache
			lastContents = "";
		}

		public void endElement(String uri, String localName, String name) throws SAXException {

			if (end) {
				return;
			}

			// 로우 일경우
			if (name.equals("row")) {
				columnNo = 0;

				if (startRow >= rowNo) {
					return;
				}

				// 빈값이나 첫번째 값이 ""일경우 리턴
				if (!isEmpty(currentData)) {
					itemList.add(currentData);
				} else {
					end = true;
				}
				currentData = new ArrayList<>();
			}

			if (name.equals("c")) {

				// System.out.println("R" + rowNo + ":C" + columnNo + ">"+ lastContents + "<");
				currentData.add(lastContents);
			}

			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}

			// v => contents of a cell
			// Output after we've seen the string contents
			if (name.equals("v")) {
				// System.out.println("end: "+ lastContents);
			}
		}

		private boolean isEmpty(List<Object> currentData) {
			for (int i = 0; i < currentData.size(); i++) {
				Object obj = currentData.get(i);
				if (!obj.equals("") && obj != null) {
					return false;
				}
			}
			return true;

		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
			// System.out.println("lastContents: " + lastContents);
		}

		public List<List<Object>> getList() {
			return this.itemList;
		}
	}//sheethandler
}