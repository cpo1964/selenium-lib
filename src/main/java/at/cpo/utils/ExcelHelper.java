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
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class ExcelHelper {
    private static final Logger LOGGER = Logger.getLogger(ExcelHelper.class.getSimpleName());
    private LinkedList<Object[]> data = null;

    public ExcelHelper(File file, String sheetName) throws IOException {
        this.data = this.doloadFromSpreadsheet(file.getPath(), sheetName, true);
    }

    public List<Object[]> getData() {
        return this.data;
    }

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

    public static String getSheetName(Class<?> clazz) {
        String sheetName = clazz.getSimpleName();
        String[] sheetNameArr = sheetName.split("_");
        if (sheetNameArr.length > 0) {
            sheetName = sheetNameArr[0];
        }

        return sheetName;
    }

    private boolean isEmpty(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell == null || firstCell.getCellTypeEnum().equals(CellType.BLANK);
    }

    private int countNonEmptyColumns(HSSFSheet sheet) {
        Row firstRow = sheet.getRow(0);
        return this.firstEmptyCellPosition(firstRow);
    }

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

    private Object getNumericCellValue(Cell cell) {
        Object cellValue;
        if (DateUtil.isCellDateFormatted(cell)) {
            cellValue = new Date(cell.getDateCellValue().getTime());
        } else {
            cellValue = cell.getNumericCellValue();
        }

        return cellValue;
    }

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
