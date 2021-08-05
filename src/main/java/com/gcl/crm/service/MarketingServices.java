package com.gcl.crm.service;

import com.gcl.crm.dto.SelectItem;
import com.gcl.crm.dto.SourceEvaluationDto;
import com.gcl.crm.entity.*;
import com.gcl.crm.enums.Status;
import com.gcl.crm.form.*;
import com.gcl.crm.repository.*;
import com.gcl.crm.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MarketingServices {

    @Autowired
    MarketingRepository marketingRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PotentialRepository2 potentialRepository2;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    MarketingGroupRepository marketingGroupRepository;

    @Autowired
    CustomerDistributionRepository customerDistributionRepository;

    @Autowired
    EmployeeService employeeService;

    public void initScreen() {
    }

    public CustomerStatusReportForm getCustomerStatusReport(CustomerStatusReportForm customerStatusReportForm) {
        int sumLv1 = 0;
        int sumLv2 = 0;
        int sumLv3 = 0;
        int sumLv4 = 0;
        int sumLv5 = 0;
        int sumLv6 = 0;
        int sumLv7 = 0;
        int sumTotal = 0;
        int sumRegisteredAcc = 0;
        int sumTopUp = 0;
        int sumLot = 0;
        String[] dateRange = customerStatusReportForm.getDateRange().split("-");

        List<CustomerStatusForm> customerStatusList = marketingRepository.getCustomerStatusList(dateRange[0].trim(), dateRange[1].trim());
        for (CustomerStatusForm cs : customerStatusList) {
            sumLv1 += Integer.parseInt(cs.getLevel_1());
            sumLv2 += Integer.parseInt(cs.getLevel_2());
            sumLv3 += Integer.parseInt(cs.getLevel_3());
            sumLv4 += Integer.parseInt(cs.getLevel_4());
            sumLv5 += Integer.parseInt(cs.getLevel_5());
            sumLv6 += Integer.parseInt(cs.getLevel_6());
            sumLv7 += Integer.parseInt(cs.getLevel_7());
            sumTotal += Integer.parseInt(cs.getTotal());
        }

        customerStatusReportForm.setSumLevel1(sumLv1);
        customerStatusReportForm.setSumLevel2(sumLv2);
        customerStatusReportForm.setSumLevel3(sumLv3);
        customerStatusReportForm.setSumLevel4(sumLv4);
        customerStatusReportForm.setSumLevel5(sumLv5);
        customerStatusReportForm.setSumLevel6(sumLv6);
        customerStatusReportForm.setSumLevel7(sumLv7);
        customerStatusReportForm.setSumLevelTotal(sumTotal);

        List<CustomerStatusEvaluationForm> customerStatusEvaluationList = marketingRepository
                                                                    .getCustomerStatusEvaluationList(dateRange[0].trim(), dateRange[1].trim());

        for(CustomerStatusEvaluationForm cs : customerStatusEvaluationList) {
            sumRegisteredAcc += cs.getNum_of_registered_account();
            sumTopUp += cs.getNum_of_top_up_account();
            sumLot += cs.getNum_of_lot();
        }
        customerStatusReportForm.setSumRegisteredAccount(sumRegisteredAcc);
        customerStatusReportForm.setSumTopUp(sumTopUp);
        customerStatusReportForm.setSumLot(sumLot);

        customerStatusReportForm.setCustomerStatusFormList(customerStatusList);
        customerStatusReportForm.setCustomerStatusEvaluationFormList(customerStatusEvaluationList);
        return customerStatusReportForm;
    }

    public Boolean validateDate(Date fromDate, Date toDate) {
        return Boolean.TRUE;
    }

    public List<User> getListUser(){
        return userRepository.findAllByEnabled(true);
    }

    public ComboboxForm initComboboxData() {
        ComboboxForm comboboxForm = new ComboboxForm();
        List<SelectItem> sourceList = sourceRepository.getAll()
                .stream()
                .map(source -> new SelectItem(
                        source.getSourceId().toString(), source.getSourceName()))
                .collect(Collectors.toList());

        List<SelectItem> brokerNameList = employeeService.getAllWorkingEmployees()
                .stream()
                .map(employee -> new SelectItem(employee.getId().toString(), employee.getName()))
                .collect(Collectors.toList());

        comboboxForm.setListBrokerName(brokerNameList);
        comboboxForm.setListSource(sourceList);
        return comboboxForm;
    }

    @Transactional
    public List<TMP_KPI_EMPLOYEE> getKPIEmployeeReport(String fromDate, String toDate) {
        return marketingRepository.getKPIEmployeeReport(fromDate, toDate);
    }

    @Transactional
    public void distributeCustomerData(CustomerDistributionForm customerDistributionForm, User user) {
        try{
            List<Long> mktIdList = customerDistributionForm.getMktIdList();
            List<Long> potentialIDList = customerDistributionForm.getPotentialIdList();
            Date systemDate = WebUtils.getSystemDate();
            int i = 0;
            for (Long potentialId : potentialIDList) {
                CustomerDistribution customerDistribution = new CustomerDistribution();
                customerDistribution.setPotential(potentialRepository2.getReferenceById(potentialId));
                customerDistribution.setMarketingGroup(marketingGroupRepository.findByIdAndStatus(mktIdList.get(i++), Status.ACTIVE));
                customerDistribution.setAdd_date(systemDate);
                customerDistribution.setDate_distribution(systemDate);
                customerDistribution.setCustomer(null);
                customerDistribution.setUpd_date(systemDate);
                customerDistribution.setAdd_user(user.getEmployee().getId());
                customerDistribution.setUpd_user(user.getEmployee().getId());
                customerDistributionRepository.insertDataCustomer(customerDistribution);
                if (i == mktIdList.size()) i = 0;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public List<SourceEvaluationDto> getSourceEvaluation(SourceEvaluationForm sourceEvaluationForm) {
        String[] dateRange = sourceEvaluationForm.getDateRange().split("-");
        return potentialRepository2.getSourceEvaluation(dateRange[0].trim(), dateRange[1].trim());
    }

}
