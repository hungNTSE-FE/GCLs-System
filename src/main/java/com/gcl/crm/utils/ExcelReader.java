package com.gcl.crm.utils;

import com.gcl.crm.config.AppConst;
import com.gcl.crm.dto.CustomerDTO;
import com.gcl.crm.entity.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

    private Workbook getWorkbook(InputStream stream, String filename) throws IOException, NotOfficeXmlFileException {
        Workbook workbook = null;
        if (filename.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(stream);
        } else if (filename.endsWith("xls")) {
            workbook = new HSSFWorkbook(stream);
        }
        return workbook;
    }

    /**
     * Read old customer data from excel file
     * @param stream
     * @param filename
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */
    public List<CustomerDTO> getCustomerData(InputStream stream, String filename) throws IOException, IllegalStateException {
        LOGGER.info("Start getCustomerData");
        List<CustomerDTO> customerData = new ArrayList<>();
        try {
            Workbook workbook = this.getWorkbook(stream, filename);
            if (workbook == null) {
                throw new IllegalStateException("Tập tịn đã chọn không đúng định dạng");
            }
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() < 1) {
                    continue;
                }
                if (row.getLastCellNum() != 21){
                    continue;
                }

                Iterator<Cell> cellIterator = row.cellIterator();
                CustomerDTO item = new CustomerDTO();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Object cellValue = this.getCellValue(cell);
                    if (cellValue == null) {
                        continue;
                    }
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case AppConst.COLUMN_NO:
                            long id = (long) Double.parseDouble(cellValue.toString());;
                            item.setId(id);
                            break;
                        case AppConst.COLUMN_BROKER_CODE:
                            String brokerCode = cellValue.toString();
                            item.setBrokerCode(brokerCode);
                            break;
                        case AppConst.COLUMN_BROKER_NAME:
                            String brokerName = cellValue.toString();
                            item.setBrokerName(brokerName);
                            break;
                        case AppConst.COLUMN_BROKER_PHONE:
                            String brokerPhone = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                brokerPhone = cellValue.toString();
                            }
                            item.setBrokerPhone(brokerPhone);
                            break;
                        case AppConst.COLUMN_BROKER_MAIL:
                            String brokerEmail = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                brokerEmail = cellValue.toString();
                            }
                            item.setBrokerEmail(brokerEmail);
                            break;
                        case AppConst.COLUMN_TRADING_ACCOUNT_CODE:
                            String tradingAccountCode = cellValue.toString();
                            item.setTradingAccountCode(tradingAccountCode);
                            break;
                        case AppConst.COLUMN_TRADING_ACCOUNT_NAME:
                            String tradingAccountName = cellValue.toString();
                            item.setTradingAccountName(tradingAccountName);
                            break;
                        case AppConst.COLUMN_PHONE_NUMBER:
                            String phoneNumber = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                phoneNumber = cellValue.toString();
                            }
                            item.setPhoneNumber(phoneNumber);
                            break;
                        case AppConst.COLUMN_EMAIL:
                            String email = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                email = cellValue.toString();
                            }
                            item.setEmail(email);
                            break;
                        case AppConst.COLUMN_ADDRESS:
                            String address = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                address = cellValue.toString();
                            }
                            item.setAddress(address);
                            break;
                        case AppConst.COLUMN_IDENTITY_NUMBER:
                            String identityNumber = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                identityNumber = cellValue.toString();
                            }
                            item.setIdentityNumber(identityNumber);
                            break;
                        case AppConst.COLUMN_BIRTHDATE:
                            String birthDate = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                birthDate = DateTimeUtil.convertCellValueToDate(cellValue, AppConst.FORMAT_DD_MM_YYYY_CROOSSIES);
                            }
                            item.setBirthDate(birthDate);
                            break;
                        case AppConst.COLUMN_ISSUE_DATE:
                            String issueDate = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                issueDate = DateTimeUtil.convertCellValueToDate(cellValue, AppConst.FORMAT_DD_MM_YYYY_CROOSSIES);
                            }
                            item.setIssueDate(issueDate);
                            break;
                        case AppConst.COLUMN_PLACE_OF_CREATION:
                            String issuePlace = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                issuePlace = cellValue.toString();
                            }
                            item.setIssuePlace(issuePlace);
                            break;
                        case AppConst.COLUMN_BANK_NAME:
                            String bankName = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                bankName = cellValue.toString();
                            }
                            item.setBankName(bankName);
                            break;
                        case AppConst.COLUMN_BANK_CODE:
                            String bankCode = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                bankCode = cellValue.toString();
                            }
                            item.setBankCode(bankCode);
                            break;
                        case AppConst.COLUMN_CONTRACT_STATUS:
                            String contractStatus = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                contractStatus = cellValue.toString();
                            }
                            item.setContractStatus(contractStatus);
                            break;
                        case AppConst.COLUMN_DESCRIPTION:
                            String description = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                description = cellValue.toString();
                            }
                            item.setDescription(description);
                            break;
                        case AppConst.COLUMN_STATUS:
                            String status = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                status = cellValue.toString();
                            }
                            item.setStatus(status);
                            break;
                        case AppConst.COLUMN_CREATE_DATE_CONTRACT:
                            String createDateContract = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                createDateContract = DateTimeUtil.convertCellValueToDate(cellValue, AppConst.TIME_FORMAT_DD_MM_YYYY);
                            }
                            item.setCreateDateContract(createDateContract);
                            break;
                        case AppConst.COLUMN_CONTRACT_ID:
                            String contractID = null;
                            if (!AppConst.NULL.equals(cellValue.toString())) {
                                contractID = cellValue.toString();
                            }
                            item.setContractID(contractID);
                            break;
                    }
                }
                if (!customerData.contains(item)){
                    customerData.add(item);
                }
            }
            LOGGER.info("End getCustomerData");
        } catch (Exception e) {
            LOGGER.error("Has exception when getCustomerData ", e);
        }
        return customerData;
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
                    case AppConst.COLUMN_INDEX_PHONE:
                        if (cellValue instanceof String) {
                            item.setPhoneNumber(cellValue.toString().trim().replace(" ", ""));
                        } else if (cellValue instanceof Double) {
                            Double phone = Double.valueOf(cellValue.toString());
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            numberFormat.setMaximumFractionDigits(20);
                            numberFormat.setGroupingUsed(false);
                            if (phone != null) {
                                item.setPhoneNumber((phone.toString().startsWith("0") || phone.toString().startsWith("+84"))
                                        ? numberFormat.format(phone)
                                        : "+84" + numberFormat.format(phone));
                            }
                        }
                        break;
                    case AppConst.COLUMN_INDEX_DATE:
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
                    case AppConst.COLUMN_INDEX_NAME:
                        String name = cellValue.toString();
                        item.setName(name);
                        break;
                    case AppConst.COLUMN_INDEX_EMAIL:
                        String email = cellValue.toString();
                        item.setEmail(email);
                        break;
                    case AppConst.COLUMN_INDEX_SOURCE:
                        String source = cellValue.toString();
                        item.setSourceName(source);
                        break;
                    case AppConst.COLUMN_INDEX_ADDRESS:
                        String address = cellValue.toString();
                        item.setAddress(address);
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
    public List<TransactionHistory> getTransactionData(InputStream stream, String filename) throws IOException, IllegalStateException {
        List<TransactionHistory> transactionData = new ArrayList<>();
        Workbook workbook = this.getWorkbook(stream, filename);
        if (workbook == null) throw new IllegalStateException("Tập tin đã chọn không đúng định dạng");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < 7) {
                continue;
            }
            if (row.getLastCellNum() != 28){
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            TransactionHistory item = new TransactionHistory();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Object cellValue = this.getCellValue(cell);
                if (cellValue == null) {
                    continue;
                }
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case AppConst.COLUMN_INDEX_INSERT_DATE:
                        Date date;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        if (cellValue instanceof Double) {
                            date = DateUtil.getJavaDate((Double) cellValue);
                        } else {
                            try {
                                date = simpleDateFormat.parse((String) cellValue);
                            } catch (ParseException e) {
                                item.setTransactionDate(null);
                                    break;
                            }
                        }
                        item.setTransactionDate(date);
                        break;

                    case AppConst.COLUMN_INDEX_ACCOUNT_NUMBER:
                        String account_number = cellValue.toString();
                        item.setTradingAccount(new TradingAccount(account_number));
                        break;
                    case AppConst.COLUMN_INDEX_ID:
                        String id = cellValue.toString();
                        if(Objects.nonNull(id) && !id.isEmpty()) {
                            item.setTransactionID(Long.parseLong(id));
                        }
                        break;
                    case AppConst.COLUMN_INDEX_TYPE:
                        String type = cellValue.toString();
                        item.setTransactionType(type);
                        break;
                    case AppConst.COLUMN_INDEX_LOT:
                        if(!cellValue.toString().isEmpty()){
                            String lot = cellValue.toString();
                            item.setLot((int) Math.round(Double.parseDouble(lot)));
                        }else{
                            return transactionData;
                        }
                        break;
                    case AppConst.COLUMN_INDEX_MONEY:
                        String money = cellValue.toString();
                        item.setMoney(money);
                        break;

                    case AppConst.COLUMN_INDEX_BROKER_CODE:
                        item.setBroker_code(cellValue.toString());
                        break;
                    default:
                        break;
                }
            }
            if(item.getTransactionID() != null){
                transactionData.add(item);
            }
        }

        System.out.println(transactionData.get(transactionData.size()-1).getTransactionID());
        return transactionData;
    }

    public List<TradingAccount> getTradingAccountsBalance(InputStream stream, String filename) throws IOException, IllegalStateException {
        List<TradingAccount> accountData = new ArrayList<>();
        Workbook workbook = this.getWorkbook(stream, filename);
        if (workbook == null) throw new IllegalStateException("Tập tin đã chọn không đúng định dạng");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < 1) {
                continue;
            }
            if (row.getLastCellNum() != 20){
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            TradingAccount item = new TradingAccount();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                Object cellValue = this.getCellValue(cell);
                if (cellValue == null) {
                    continue;
                }
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case AppConst.COLUMN_ACCOUNT_NUMBER:
                        String account_number = cellValue.toString();
                        item.setAccountNumber(account_number);
                        break;
                    case AppConst.COLUMN_BALANCE:
                        String account_balance = cellValue.toString();
                        item.setBalance(Double.parseDouble(account_balance));
                        break;
                    default:
                        break;
                }
            }
            if(item.getAccountNumber() != null){
                accountData.add(item);
            }
        }
        return accountData;
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
