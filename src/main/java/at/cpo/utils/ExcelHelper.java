/*
 * MIT License
 *
 * Copyright (c) 2018 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package at.cpo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

// TODO: Auto-generated Javadoc
/**
 * The Class ExcelHelper.
 */
public class ExcelHelper {
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager.getLogger(ExcelHelper.class.getSimpleName());
    
    /** The data. */
    private LinkedList<Object[]> data = null;

	/**
     * Instantiates a new excel helper.
     *
     * @param file the file
     * @param sheetName the sheet name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public ExcelHelper(File file, String sheetName) throws IOException {
        this.data = this.doloadFromSpreadsheet(file.getPath(), sheetName, true);
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List<Object[]> getData() {
        return this.data;
    }

    /**
     * Doload from spreadsheet.
     *
     * @param pFilePath the file path
     * @param pSheetName the sheet name
     * @param skipFirstRow the skip first row
     * @return the linked list
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private LinkedList<Object[]> doloadFromSpreadsheet(String pFilePath, String pSheetName, boolean skipFirstRow) throws IOException {
        LinkedList<Object[]> rows = null;
        HSSFWorkbook workbook = null;
        InputStream testdata = new FileInputStream(pFilePath);
        Throwable err = null;

        try {
            workbook = new HSSFWorkbook(testdata);
            if (!(new File(pFilePath)).exists()) {
            	workbook.close();
                throw new IOException("Die Datei '" + pFilePath + "' konnte nicht gefunden werden");
            }
        } catch (Throwable e1) {
            err = e1;
            throw e1;
        } finally {
            if (testdata != null) {
                if (err != null) {
                    try {
                        testdata.close();
                    } catch (Throwable e2) {
                        err.addSuppressed(e2);
                    }
                } else {
                    testdata.close();
                }
            }

        }

        this.data = new LinkedList<Object[]>();
        String logMsg = "Benutze folgende Testdaten:\n" + (new File(pFilePath)).getCanonicalPath() + " / Sheet: " + pSheetName;
        LOGGER.info(logMsg);
        HSSFSheet sheet = workbook.getSheet(pSheetName);
        int numberOfColumns = this.countNonEmptyColumns(sheet);
        rows = new LinkedList<Object[]>();
        List<Object> rowData = new ArrayList<Object>();
        boolean isFirstRow = skipFirstRow;
        Iterator<?> sheetIt = sheet.iterator();

        while(sheetIt.hasNext()) {
            Row row = (Row)sheetIt.next();
            if (this.isEmpty(row)) {
                break;
            }

            if (isFirstRow) {
                isFirstRow = false;
            } else {
                rowData.clear();

                for(int column = 0; column < numberOfColumns; ++column) {
                    Cell cell = row.getCell(column);
                    rowData.add(this.stringValueFromCell(workbook, cell));
                }

                rows.add(rowData.toArray());
            }
        }

        return rows;
    }

    /**
     * Gets the sheet name.
     *
     * @param clazz the clazz
     * @return the sheet name
     */
    public static String getSheetName(Class<?> clazz) {
        String sheetName = clazz.getSimpleName();
        String[] sheetNameArr = sheetName.split("_");
        if (sheetNameArr.length > 0) {
            sheetName = sheetNameArr[0];
        }

        return sheetName;
    }

    /**
     * Checks if is empty.
     *
     * @param row the row
     * @return true, if is empty
     */
    private boolean isEmpty(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell == null || firstCell.getCellTypeEnum().equals(CellType.BLANK);
    }

    /**
     * Count non empty columns.
     *
     * @param sheet the sheet
     * @return the int
     */
    private int countNonEmptyColumns(HSSFSheet sheet) {
        Row firstRow = sheet.getRow(0);
        return this.firstEmptyCellPosition(firstRow);
    }

    /**
     * First empty cell position.
     *
     * @param cells the cells
     * @return the int
     */
    private int firstEmptyCellPosition(Row cells) {
        int columnCount = 0;

        for(Iterator<?> it = cells.iterator(); it.hasNext(); ++columnCount) {
            Cell cell = (Cell)it.next();
            if (cell.getCellTypeEnum().equals(CellType.BLANK)) {
                break;
            }
        }

        return columnCount;
    }

    /**
     * String value from cell.
     *
     * @param workbook the workbook
     * @param cell the cell
     * @return the string
     */
    private String stringValueFromCell(HSSFWorkbook workbook, Cell cell) {
        String cellValue = null;
        if (cell == null) {
            return "";
        } else {
            if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                cellValue = "" + this.getNumericCellValue(cell);
            } else if (cell.getCellTypeEnum().equals(CellType.STRING)) {
                cellValue = cell.getRichStringCellValue().getString();
            } else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
                cellValue = "" + this.evaluateCellFormula(workbook, cell);
            } else if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
                cellValue = Boolean.toString(cell.getBooleanCellValue());
            }

            return cellValue;
        }
    }

    /**
     * Gets the numeric cell value.
     *
     * @param cell the cell
     * @return the numeric cell value
     */
    private Object getNumericCellValue(Cell cell) {
        Object cellValue;
        if (DateUtil.isCellDateFormatted(cell)) {
            cellValue = new Date(cell.getDateCellValue().getTime());
        } else {
            cellValue = cell.getNumericCellValue();
        }

        return cellValue;
    }

    /**
     * Evaluate cell formula.
     *
     * @param workbook the workbook
     * @param cell the cell
     * @return the object
     */
    private Object evaluateCellFormula(HSSFWorkbook workbook, Cell cell) {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        Object result = null;
        if (cellValue.getCellTypeEnum().equals(CellType.NUMERIC)) {
            result = cellValue.getNumberValue();
        } else if (cellValue.getCellTypeEnum().equals(CellType.STRING)) {
            result = cellValue.getStringValue();
        } else if (cellValue.getCellTypeEnum().equals(CellType.BOOLEAN)) {
            result = cellValue.getBooleanValue();
        }

        return result;
    }
}
