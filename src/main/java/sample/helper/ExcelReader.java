package sample.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExcelReader {

	FileInputStream fi;
	FileOutputStream fo;

	HSSFSheet s;
	HSSFWorkbook w;
	HSSFWorkbook wo;
/**
 * Load file path 
 * @param file
 * @throws IOException
 */
	public void testDataFile(String file) throws IOException {
		fi = new FileInputStream(file);

		w = new HSSFWorkbook(fi);
	}

	/**
	 * Returns data present in specified sheet on teh basis of row no and column no
	 * @param sheetname
	 * @param row
	 * @param column
	 * @return
	 * @throws IOException
	 */
	public String readFromColumn(String sheetname, int row, int column)
			throws IOException // To
	
	{

		s = w.getSheet(sheetname);
		String data = null;
		HSSFRow r = s.getRow(row);
		if (r.getCell(column).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			r.getCell(column).setCellType(Cell.CELL_TYPE_STRING);
			data = r.getCell(column).getStringCellValue();
			data = data.trim();

		} else if (r.getCell(column).getCellType() == XSSFCell.CELL_TYPE_STRING) {
			data = r.getCell(column).getStringCellValue();
			data = data.trim();
			System.out.println(data);
		}

		return data;
	}



	/**
	 * Returns complete list of data from a column name
	 * @param sheetname
	 * @param columnname
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> dataProviderByRow(String sheetname,
			String columnname) throws IOException {
		String str = null;
		int column = 0;
		s = w.getSheet(sheetname);
		ArrayList<String> data = new ArrayList<String>();
		HSSFRow r = s.getRow(0);
		for (int i = 0; i < r.getLastCellNum(); i++) {
			try {
				if (r.getCell(i).getStringCellValue().equals(columnname)) {
					column = i;
					break;
				}
			}

			catch (Exception e) {
				System.out.println(e.getMessage() + "   --NO such sheet exist");
			}
		}
		for (int i = 1; i < s.getPhysicalNumberOfRows(); i++) {
			HSSFRow row = s.getRow(i);

			try {

				if (row.getCell(column).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					row.getCell(column).setCellType(Cell.CELL_TYPE_STRING);
					str = row.getCell(column).getStringCellValue();
					str = str.trim();
					data.add(str);

				} else if (row.getCell(column).getCellType() == XSSFCell.CELL_TYPE_STRING) {
					str = row.getCell(column).getStringCellValue();
					str = str.trim();
					data.add(str);
				}

			}

			catch (NullPointerException e) {

			}

		}
		return data;
	}

	/**
	 * Returns list of element on a specific row no
	 * @param j
	 * @param sheet
	 * @return
	 */
	public ArrayList<String> data(int j, String sheet) {
		s = w.getSheet(sheet);
		ArrayList<String> value = new ArrayList<String>();
		HSSFRow row = s.getRow(j);
		for (int i = 1; i < row.getLastCellNum(); i++) {
			try {

				String str = row.getCell(i).getStringCellValue();
				str = str.trim();
				if (!(str.equals(""))) {
					value.add(i + "%" + str);
				}

			} catch (NullPointerException e) {

			}

		}
		return value;
	}

	
	public void closeFile() throws IOException // Closes the opened excel file.
	{
		fi.close();

	}
}
