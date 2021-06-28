package com.gcl.crm.utils;

import com.gcl.crm.entity.Potential;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {

    public static final int COLUMN_INDEX_PHONE = 1;
    public static final int COLUMN_INDEX_DATE = 2;
    public static final int COLUMN_INDEX_NAME = 3;
    public static final int COLUMN_INDEX_EMAIL = 4;
    public static final int COLUMN_INDEX_SOURCE = 5;
    public static final int COLUMN_INDEX_ADDRESS = 6;
    public static final int COLUMN_INDEX_MKT_ID = 7;
    public static final int COLUMN_INDEX_MKT_TEAM = 9;
    public static final int COLUMN_INDEX_SALE = 10;
    public static final int COLUMN_INDEX_COURSE = 11;
    public static final int COLUMN_INDEX_INITIAL_STATE = 12;
    public static final int COLUMN_INDEX_FIRST_CARE = 13;
    public static final int COLUMN_INDEX_SECOND_CARE = 15;
    public static final int COLUMN_INDEX_THIRD_CARE = 17;
    public static final int COLUMN_INDEX_TRADING_ACCOUNT = 19;
    public static final int COLUMN_INDEX_MARGIN_ACCOUNT = 21;
    public static final int COLUMN_INDEX_NOTE = 23;

    private Workbook getWorkbook(InputStream stream, String filename) throws IOException {
        Workbook workbook = null;
        if (filename.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(stream);
        } else if (filename.endsWith("xls")) {
            workbook = new HSSFWorkbook(stream);
        }
        return workbook;
    }

    public List<Potential> getPotentialData(InputStream stream, String filename) throws IOException, IllegalStateException {
        List<Potential> potentialData = new ArrayList<>();
        Workbook workbook = this.getWorkbook(stream, filename);
        if (workbook == null) throw new IllegalStateException("Tập tin đã chọn không đúng định dạng");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < 3) {
                continue;
            }
            if (row.getLastCellNum() != 24){
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            Potential item = new Potential();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Object cellValue = this.getCellValue(cell);
                if (cellValue == null) {
                    continue;
                }
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_INDEX_PHONE:
                        if (cellValue instanceof String) {
                            item.setPhoneNumber(cellValue.toString().trim().replace(" ", ""));
                        } else if (cellValue instanceof Double) {
                            Double phone = Double.valueOf(cellValue.toString());
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            numberFormat.setMaximumFractionDigits(20);
                            numberFormat.setGroupingUsed(false);
                            if (phone != null) {
                                item.setPhoneNumber(numberFormat.format(phone));
                            }
                        }
                        break;
                    case COLUMN_INDEX_DATE:
                        Date date;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        if (cellValue instanceof Double) {
                            date = DateUtil.getJavaDate((Double) cellValue);
                        } else {
                            try {
                                date = simpleDateFormat.parse((String) cellValue);
                            } catch (ParseException e) {
                                item.setDate(null);
                                break;
                            }
                        }
                        item.setDate(simpleDateFormat.format(date));
                        break;
                    case COLUMN_INDEX_NAME:
                        String name = cellValue.toString();
                        item.setName(name);
                        break;
                    case COLUMN_INDEX_EMAIL:
                        String email = cellValue.toString();
                        item.setEmail(email);
                        break;
                    case COLUMN_INDEX_SOURCE:
                        String source = cellValue.toString();
                        item.setSourceName(source);
                        break;
                    case COLUMN_INDEX_ADDRESS:
                        String address = cellValue.toString();
                        item.setAddress(address);
                        break;
                    case COLUMN_INDEX_MKT_ID:
                        String mktId = cellValue.toString();
                        item.setMktId(mktId);
                        break;
                    case COLUMN_INDEX_MKT_TEAM:
                        String mktTeam = cellValue.toString();
                        item.setMktTeam(mktTeam);
                        break;
                    case COLUMN_INDEX_SALE:
                        String sale = cellValue.toString();
                        item.setSale(sale);
                        break;
                    case COLUMN_INDEX_COURSE:
                        String course = cellValue.toString();
                        item.setCourse(course);
                        break;
                    case COLUMN_INDEX_INITIAL_STATE:
                        String initialState = cellValue.toString();
                        item.setInitialState(initialState);
                        break;
                    case COLUMN_INDEX_FIRST_CARE:
                        String firstCare = cellValue.toString();
                        item.setFirstCare(firstCare);
                        break;
                    case COLUMN_INDEX_SECOND_CARE:
                        String secondCare = cellValue.toString();
                        item.setSecondCare(secondCare);
                        break;
                    case COLUMN_INDEX_THIRD_CARE:
                        String thirdCare = cellValue.toString();
                        item.setThirdCare(thirdCare);
                        break;
                    case COLUMN_INDEX_TRADING_ACCOUNT:
                        String tradingAccount = cellValue.toString();
                        item.setTradingAccount(tradingAccount);
                        break;
                    case COLUMN_INDEX_MARGIN_ACCOUNT:
                        String marginAccount = cellValue.toString();
                        item.setMarginAccount(marginAccount);
                        break;
                    case COLUMN_INDEX_NOTE:
                        String note = cellValue.toString();
                        item.setNote(note);
                        break;
                    default:
                        break;
                }
            }
            if (item.getEmail() != null || item.getPhoneNumber() != null) {
                if (!potentialData.contains(item)){
                    potentialData.add(item);
                }
            }
        }
        return potentialData;
    }

    private Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
            default:
                break;
        }
        if (cellValue != null && cellValue.toString().equals("0.0")){
            return null;
        }
        return cellValue;
    }
}
