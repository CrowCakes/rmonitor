package com.example.rmonitor.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

public class ReportGenerator {
	private ObjectConstructor constructor = new ObjectConstructor();
	
	public byte[] generateReportParts(ConnectionManager manager) {
		manager.connect();
		List<Parts> data = constructor.constructParts(manager);
		manager.disconnect();
		
		//function final output stored here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		//create the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		//create styles
	    HSSFFont defaultFont= workbook.createFont();
	    defaultFont.setFontHeightInPoints((short)10);
	    defaultFont.setFontName("Arial");
	    defaultFont.setColor(IndexedColors.BLACK.getIndex());
	    defaultFont.setBold(false);
	    defaultFont.setItalic(false);
	    
	    HSSFFont font= workbook.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setBold(true);
	    font.setItalic(false);
	    
	    CellStyle columnStyle=workbook.createCellStyle();
	    columnStyle.setAlignment(HorizontalAlignment.CENTER);
        columnStyle.setBorderLeft(BorderStyle.THIN);
        columnStyle.setBorderRight(BorderStyle.THIN);
        //columnStyle.setBorderTop(BorderStyle.MEDIUM);
        columnStyle.setBorderBottom(BorderStyle.MEDIUM);
        columnStyle.setFont(font);
	    
	    CellStyle cellStyle=workbook.createCellStyle();
	    cellStyle.setAlignment(HorizontalAlignment.CENTER);
	    cellStyle.setBorderLeft(BorderStyle.THIN);
	    cellStyle.setBorderRight(BorderStyle.THIN);
	    cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setFont(defaultFont);
        cellStyle.setWrapText(true);
        
		//use these to refer to rows and cells in iterations
		HSSFRow row = null;
	    HSSFCell cell = null;
	    
	    HSSFSheet sheet = workbook.createSheet();
	    workbook.setSheetName(0, "Parts");
	    
	    //column headers
	    row = sheet.createRow(0);
	    cell = row.createCell(0);
	    cell.setCellValue("ID#");
	    
	    cell = row.createCell(1);
	    cell.setCellValue("Name");   
	    
	    cell = row.createCell(2);
	    cell.setCellValue("Type");
	    
	    cell = row.createCell(3);
	    cell.setCellValue("Status");
	    
	    for (int i = 0; i<4; i++) {
    		row.getCell(i).setCellStyle(columnStyle);
    	}
	    sheet.createFreezePane(0, 1);
	    //end column headers//
	    
	    //the data rows
	    for (int i = 0; i < data.size(); i++) {
	    	Parts dataRow = data.get(i);
	    	
	    	row = sheet.createRow(i+1);
	    	
	    	cell = row.createCell(0);
	    	cell.setCellValue(dataRow.getPartID());
	    	
	    	cell = row.createCell(1);
	    	cell.setCellValue(dataRow.getName());
	    	
	    	cell = row.createCell(2);
	    	cell.setCellValue(dataRow.getPartType());
	    	
	    	cell = row.createCell(3);
	    	cell.setCellValue(dataRow.getStatus());
	    	
	    	if (i > 0) {
	    		cellStyle.setBorderTop(BorderStyle.THIN);
	    	}
	        for (int j = 0; j<4; j++) {
	        	row.getCell(j).setCellStyle(cellStyle);
	        }
	    }
	    
	    //resize the columns
	    for (int k = 0; k < 4; k ++) {
	    	sheet.setColumnWidth(k, ((int)(20 * 1.14388)) * 256);
	    }
	    
	    try {
		    workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
		    try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}

	public byte[] generateReportAccessories(ConnectionManager manager) {
		manager.connect();
		List<Accessory> data = constructor.constructAccessories(manager);
		manager.disconnect();
		
		//function final output stored here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		//create the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		//create styles
	    HSSFFont defaultFont= workbook.createFont();
	    defaultFont.setFontHeightInPoints((short)10);
	    defaultFont.setFontName("Arial");
	    defaultFont.setColor(IndexedColors.BLACK.getIndex());
	    defaultFont.setBold(false);
	    defaultFont.setItalic(false);
	    
	    HSSFFont font= workbook.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setBold(true);
	    font.setItalic(false);
	    
	    CellStyle columnStyle=workbook.createCellStyle();
	    columnStyle.setAlignment(HorizontalAlignment.CENTER);
        columnStyle.setBorderLeft(BorderStyle.THIN);
        columnStyle.setBorderRight(BorderStyle.THIN);
        //columnStyle.setBorderTop(BorderStyle.MEDIUM);
        columnStyle.setBorderBottom(BorderStyle.MEDIUM);
        columnStyle.setFont(font);
	    
	    CellStyle cellStyle=workbook.createCellStyle();
	    cellStyle.setAlignment(HorizontalAlignment.CENTER);
	    cellStyle.setBorderLeft(BorderStyle.THIN);
	    cellStyle.setBorderRight(BorderStyle.THIN);
	    cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setFont(defaultFont);
        cellStyle.setWrapText(true);
        
		//use these to refer to rows and cells in iterations
		HSSFRow row = null;
	    HSSFCell cell = null;
	    
	    HSSFSheet sheet = workbook.createSheet();
	    workbook.setSheetName(0, "Accessories");
	    
	    //column headers
	    row = sheet.createRow(0);
	    cell = row.createCell(0);
	    cell.setCellValue("Rental Asset#");
	    
	    cell = row.createCell(1);
	    cell.setCellValue("Name");   
	    
	    cell = row.createCell(2);
	    cell.setCellValue("Type");
	    
	    cell = row.createCell(3);
	    cell.setCellValue("Status");
	    
	    cell = row.createCell(4);
	    cell.setCellValue("Remarks");
	    
	    for (int i = 0; i<5; i++) {
    		row.getCell(i).setCellStyle(columnStyle);
    	}
	    sheet.createFreezePane(0, 1);
	    //end column headers//
	    
	    //the data rows
	    for (int i = 0; i < data.size(); i++) {
	    	Accessory dataRow = data.get(i);
	    	
	    	row = sheet.createRow(i+1);
	    	
	    	cell = row.createCell(0);
	    	cell.setCellValue(dataRow.getRentalNumber());
	    	
	    	cell = row.createCell(1);
	    	cell.setCellValue(dataRow.getName());
	    	
	    	cell = row.createCell(2);
	    	cell.setCellValue(dataRow.getAccessoryType());
	    	
	    	cell = row.createCell(3);
	    	cell.setCellValue(dataRow.getStatus());
	    	
	    	cell = row.createCell(4);
	    	cell.setCellValue(dataRow.getRemarks());
	    	
	    	if (i > 0) {
	    		cellStyle.setBorderTop(BorderStyle.THIN);
	    	}
	        for (int j = 0; j<5; j++) {
	        	row.getCell(j).setCellStyle(cellStyle);
	        }
	    }
	    
	    //resize the columns
	    for (int k = 0; k < 4; k ++) {
	    	sheet.setColumnWidth(k, ((int)(20 * 1.14388)) * 256);
	    }
	    
	    try {
		    workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
		    try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	public byte[] generateReportSmallAccessories(ConnectionManager manager) {
		manager.connect();
		List<SmallAccessory> data = constructor.constructSmallAccessories(manager);
		manager.disconnect();
		
		//function final output stored here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		//create the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		//create styles
	    HSSFFont defaultFont= workbook.createFont();
	    defaultFont.setFontHeightInPoints((short)10);
	    defaultFont.setFontName("Arial");
	    defaultFont.setColor(IndexedColors.BLACK.getIndex());
	    defaultFont.setBold(false);
	    defaultFont.setItalic(false);
	    
	    HSSFFont font= workbook.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setBold(true);
	    font.setItalic(false);
	    
	    CellStyle columnStyle=workbook.createCellStyle();
	    columnStyle.setAlignment(HorizontalAlignment.CENTER);
        columnStyle.setBorderLeft(BorderStyle.THIN);
        columnStyle.setBorderRight(BorderStyle.THIN);
        //columnStyle.setBorderTop(BorderStyle.MEDIUM);
        columnStyle.setBorderBottom(BorderStyle.MEDIUM);
        columnStyle.setFont(font);
	    
	    CellStyle cellStyle=workbook.createCellStyle();
	    cellStyle.setAlignment(HorizontalAlignment.CENTER);
	    cellStyle.setBorderLeft(BorderStyle.THIN);
	    cellStyle.setBorderRight(BorderStyle.THIN);
	    cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setFont(defaultFont);
        cellStyle.setWrapText(true);
        
		//use these to refer to rows and cells in iterations
		HSSFRow row = null;
	    HSSFCell cell = null;
	    
	    HSSFSheet sheet = workbook.createSheet();
	    workbook.setSheetName(0, "Bulk Accessories");
	    
	    //column headers
	    row = sheet.createRow(0);
	    cell = row.createCell(0);
	    cell.setCellValue("Name");
	    
	    cell = row.createCell(1);
	    cell.setCellValue("Type");   
	    
	    cell = row.createCell(2);
	    cell.setCellValue("Total Qty");
	    
	    cell = row.createCell(3);
	    cell.setCellValue("Unavailable Qty");

	    cell = row.createCell(4);
	    cell.setCellValue("Remaining Qty");
	    
	    for (int i = 0; i<5; i++) {
    		row.getCell(i).setCellStyle(columnStyle);
    	}
	    sheet.createFreezePane(0, 1);
	    //end column headers//
	    
	    //the data rows
	    for (int i = 0; i < data.size(); i++) {
	    	SmallAccessory dataRow = data.get(i);
	    	
	    	row = sheet.createRow(i+1);
	    	
	    	cell = row.createCell(0);
	    	cell.setCellValue(dataRow.getName());
	    	
	    	cell = row.createCell(1);
	    	cell.setCellValue(dataRow.getAccessoryType());
	    	
	    	cell = row.createCell(2);
	    	cell.setCellValue(dataRow.getQuantity());

	    	cell = row.createCell(3);
	    	cell.setCellValue(dataRow.getQuantityMinus());

	    	cell = row.createCell(4);
	    	cell.setCellValue(dataRow.getQuantity() - dataRow.getQuantityMinus());
	    	
	    	if (i > 0) {
	    		cellStyle.setBorderTop(BorderStyle.THIN);
	    	}
	        for (int j = 0; j<5; j++) {
	        	row.getCell(j).setCellStyle(cellStyle);
	        }
	    }
	    
	    //resize the columns
	    for (int k = 0; k < 5; k ++) {
	    	sheet.setColumnWidth(k, ((int)(20 * 1.14388)) * 256);
	    }
	    
	    try {
		    workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
		    try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}

	public byte[] generateReportComputers(ConnectionManager manager) {
		manager.connect();
		List<Computer> data = constructor.constructComputers(manager);
		manager.disconnect();
		
		//function final output stored here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		//create the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		//create styles
	    HSSFFont defaultFont= workbook.createFont();
	    defaultFont.setFontHeightInPoints((short)10);
	    defaultFont.setFontName("Arial");
	    defaultFont.setColor(IndexedColors.BLACK.getIndex());
	    defaultFont.setBold(false);
	    defaultFont.setItalic(false);
	    
	    HSSFFont font= workbook.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setColor(IndexedColors.BLACK.getIndex());
	    font.setBold(true);
	    font.setItalic(false);
	    
	    CellStyle columnStyle=workbook.createCellStyle();
	    columnStyle.setAlignment(HorizontalAlignment.CENTER);
        columnStyle.setBorderLeft(BorderStyle.THIN);
        columnStyle.setBorderRight(BorderStyle.THIN);
        //columnStyle.setBorderTop(BorderStyle.MEDIUM);
        columnStyle.setBorderBottom(BorderStyle.MEDIUM);
        columnStyle.setFont(font);
	    
	    CellStyle cellStyle=workbook.createCellStyle();
	    cellStyle.setAlignment(HorizontalAlignment.CENTER);
	    cellStyle.setBorderLeft(BorderStyle.THIN);
	    cellStyle.setBorderRight(BorderStyle.THIN);
	    cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setFont(defaultFont);
        cellStyle.setWrapText(true);
        
		//use these to refer to rows and cells in iterations
		HSSFRow row = null;
	    HSSFCell cell = null;
	    
	    HSSFSheet sheet = workbook.createSheet();
	    workbook.setSheetName(0, "Computers");
	    
	    //column headers
	    row = sheet.createRow(0);
	    cell = row.createCell(0);
	    cell.setCellValue("Rental Asset#");
	    
	    cell = row.createCell(1);
	    cell.setCellValue("CPU");   
	    
	    cell = row.createCell(2);
	    cell.setCellValue("Type");
	    
	    cell = row.createCell(3);
	    cell.setCellValue("OS");

	    cell = row.createCell(4);
	    cell.setCellValue("Purchase Date");
	    
	    cell = row.createCell(5);
	    cell.setCellValue("Is Upgraded?");
	        
	    cell = row.createCell(6);
	    cell.setCellValue("# of Releases");
	    
	    cell = row.createCell(7);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(8);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(9);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(10);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(11);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(12);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(13);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(14);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(15);
	    cell.setCellValue("Part");
	    
	    cell = row.createCell(16);
	    cell.setCellValue("Status");
	    
	    cell = row.createCell(17);
	    cell.setCellValue("Description");
	    
	    for (int i = 0; i<18; i++) {
    		row.getCell(i).setCellStyle(columnStyle);
    	}
	    sheet.createFreezePane(0, 1);
	    //end column headers//
	    
	    //the data rows
	    for (int i = 0; i < data.size(); i++) {
	    	Computer dataRow = data.get(i);
	    	
	    	row = sheet.createRow(i+1);
	    	
	    	cell = row.createCell(0);
	    	cell.setCellValue(dataRow.getRentalNumber());
	    	
	    	cell = row.createCell(1);
	    	cell.setCellValue(dataRow.getCpu());
	    	
	    	cell = row.createCell(2);
	    	cell.setCellValue(dataRow.getPcType());

	    	cell = row.createCell(3);
	    	cell.setCellValue(dataRow.getOs());

	    	cell = row.createCell(4);
	    	cell.setCellValue(dataRow.getPurchaseDate().toString());
	    	
	    	cell = row.createCell(5);
	    	cell.setCellValue(dataRow.getIsUpgraded().toString());
	    	
	    	cell = row.createCell(6);
	    	manager.connect();
	    	cell.setCellValue(constructor.constructRentalUnitHistory(manager, dataRow.getRentalNumber()).size());
	    	manager.disconnect();
	    	
	    	manager.connect();
	    	Parts temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(0)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(7);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(1)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(8);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(2)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(9);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(3)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(10);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(4)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(11);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(5)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(12);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(6)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(13);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(7)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(14);
	    	cell.setCellValue(temp.getStringRepresentation());
	    	
	    	manager.connect();
	    	temp = constructor.fetchParts(manager, dataRow.getPartIDs().get(8)).get(0);
	    	manager.disconnect();
	    	
	    	cell = row.createCell(15);
	    	cell.setCellValue(temp.getStringRepresentation());

	    	cell = row.createCell(16);
	    	cell.setCellValue(dataRow.getStatus());

	    	cell = row.createCell(17);
	    	cell.setCellValue(dataRow.getDescription());
	    	
	    	if (i > 0) {
	    		cellStyle.setBorderTop(BorderStyle.THIN);
	    	}
	        for (int j = 0; j<18; j++) {
	        	row.getCell(j).setCellStyle(cellStyle);
	        }
	    }
	    
	    //resize the columns
	    for (int k = 0; k < 18; k ++) {
	    	sheet.setColumnWidth(k, ((int)(15 * 1.14388)) * 256);
	    }
	    
	    try {
		    workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
		    try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
}
