package com.gcl.crm.service.impl;

import com.gcl.crm.config.AppConst;
import com.gcl.crm.dto.CustomerDTO;
import com.gcl.crm.dto.ErrorInFo;
import com.gcl.crm.dto.SelectItem;
import com.gcl.crm.entity.*;
import com.gcl.crm.enums.*;
import com.gcl.crm.form.ComboboxForm;
import com.gcl.crm.form.CustomerForm;
import com.gcl.crm.repository.*;
import com.gcl.crm.repository.custom.CustomRepository;
import com.gcl.crm.repository.custom.CustomerDistributionCustomRepository;
import com.gcl.crm.service.*;
import com.gcl.crm.utils.DateTimeUtil;
import com.gcl.crm.utils.ValidateUtil;
import com.gcl.crm.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    EmployeeService employeeService;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomRepository customRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PotentialRepository potentialRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    IdentificationRepository identificationRepository;

    @Autowired
    TradingAccountRepository tradingAccountRepository;

    @Autowired
    CustomerDistributionCustomRepository customerDistributionCustomRepository;

    @Autowired
    PotentialService potentialService;

    @Autowired
    CustomerProcessService customerProcessService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    MarketingGroupService marketingGroupService;

    @Autowired
    CustomerDistributionRepository customerDistributionRepository;

    @Autowired
    MarketingGroupRepository marketingGroupRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    PositionService positionService;

    @Override
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

    @Override
    public CustomerForm initForm(Long potentialId) {
        CustomerForm form = new CustomerForm();
        List<MarketingGroup> marketingGroupList = marketingGroupService.getAllMktByStatus();
        form.setMarketingGroupList(marketingGroupList);
        form.setComboboxForm(initComboboxData());
        if (Objects.nonNull(potentialId)) {
            Potential potential = potentialService.getPotentialById(potentialId);
            form.setCustomerName(potential.getName());
            form.setEmail(potential.getEmail());
            form.setPhoneNumber(potential.getPhoneNumber());
            form.setHdnSourceId(potential.getSource().getSourceId());
            form.setHdmPotentialId(potentialId);
        }
        return form;
    }

    @Transactional
    @Override
    public void registerCustomer(CustomerForm customerForm, User user, MultipartFile before,MultipartFile after) {
        try {
            Customer customer = convertToCustomerEntity(customerForm, user);
            // Set level 6 as default of customer when register customer successfully
            customer.setLevel(new Level(LevelEnum.LEVEL_6.getValue()));
            //Identification
            //kh-ng depar-docu
            customer.setNumber("none");
            customer.setContractNumber("none");
            BankAccount bankAccount = registerBanking(customerForm);
            bankAccount.setCustomer(customer);
            List<BankAccount> bankAccountList = new ArrayList<>();
             bankAccountList.add(bankAccount);
            customer.setIdentification(registerIdentification(customerForm));
            customer.setBankAccounts(bankAccountList);
            Customer newCustomer = customerRepository.register(customer);
            customerDistributionRepository.updateCusDisAfterRegistCus(customerForm.getHdmPotentialId(), newCustomer.getCustomerId());
            potentialService.updateLevelPotentialAfterRegistCus(customerForm.getHdmPotentialId());
            customerProcessService.saveAvatar(after,customer);
            customerProcessService.saveAvatar(before,customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean importCustomer(List<CustomerDTO> customers, User user) {
        LOGGER.info("Start importPotential");
        try {
            if (ValidateUtil.isNullOrEmpty(customers)) {
                return false;
            }
            for (CustomerDTO customer: customers) {
                Customer customerEntity = new Customer();
                Potential potentialEntity = new Potential();
                Identification identificationEntity = new Identification();
                MarketingGroup marketingGroup = new MarketingGroup();
                Employee employee = new Employee();
                CustomerDistribution customerDistribution = new CustomerDistribution();
                TradingAccount tradingAccount = new TradingAccount();
                Contract contract = new Contract();
                // Search for potential data in the system
                if (potentialRepository.findAllByPhoneNumberOrEmail(customer.getPhoneNumber(), customer.getEmail()).size() == 0) {
                    potentialEntity.setAddress(customer.getAddress());
                    potentialEntity.setEmail(customer.getEmail());
                    potentialEntity.setMaker(user.getEmployee().getId());
                    potentialEntity.setName(customer.getTradingAccountName());
                    potentialEntity.setPhoneNumber(customer.getPhoneNumber());
                    potentialEntity.setStatus(Status.ACTIVE);
                    potentialEntity.setLastModified(DateTimeUtil.getCurrentDate());
                    potentialRepository.save(potentialEntity);
                }
                // Search for customer data in the system
                if (customRepository.findAllByEmailOrPhoneNumber(customer.getEmail(), customer.getPhoneNumber()).size() == 0) {
                    customerEntity.setCustomerCode(customer.getTradingAccountCode());
                    customerEntity.setCustomerName(customer.getTradingAccountName());
                    customerEntity.setDescription(customer.getDescription());
                    customerEntity.setEmail(customer.getEmail());
                    customerEntity.setPhoneNumber(customer.getPhoneNumber());
                    customerEntity.setAddress(customer.getAddress());
                    customerEntity.setStatus(Status.ACTIVE);
                    if (!identificationRepository.findById(customer.getIdentityNumber()).isPresent()) {
                        identificationEntity.setIdentityNumber(customer.getIdentityNumber());
                        identificationEntity.setBirthDate(DateTimeUtil.convertStringToDate(customer.getBirthDate(), AppConst.FORMAT_DD_MM_YYYY_CROOSSIES));
                        identificationEntity.setIssueDate(DateTimeUtil.convertStringToDate(customer.getIssueDate(), AppConst.FORMAT_DD_MM_YYYY_CROOSSIES));
                        identificationEntity.setIssuePlace(customer.getIssuePlace());
                        identificationRepository.save(identificationEntity);
                    }
                    identificationEntity = identificationRepository.findByIdentityNumber(customer.getIdentityNumber());
                    customerEntity.setIdentification(identificationEntity);
                    customerEntity.setBankCode(customer.getBankCode());
                    customerEntity.setBankName(customer.getBankName());
                    customerEntity.setAddUser(user.getEmployee().getId());
                    customerEntity.setUpdUser(user.getEmployee().getId());
                    customerEntity.setUpdDate(DateTimeUtil.getCurrentDate());
                    customerEntity.setCreateDate(DateTimeUtil.getCurrentDate());
                    customRepository.save(customerEntity);
                }
                // Search for employee data in the system
                if (employeeRepository.findAllByPhoneOrCompanyEmail(customer.getBrokerPhone(), customer.getBrokerEmail()).size() == 0) {
                    Position position = positionRepository.findByName(AppConst.EMPLOYEE_ENUM);
                    Department department = departmentRepository.findByDepartmentName("business");
                    employee.setDepartment(department);
                    employee.setPosition(position);
                    employee.setPhone(customer.getBrokerPhone());
                    employee.setCompanyEmail(customer.getBrokerEmail());
                    employee.setName(customer.getBrokerName());
                    employee.setStatus(EmployeeStatus.WORKING);
                    employeeRepository.save(employee);
                }
                // Search for marketing group data in the system
                if (marketingGroupRepository.findAllByCodeOrName(customer.getBrokerCode(), customer.getBrokerName()).size() == 0) {
                    marketingGroup.setCode(customer.getBrokerCode());
                    marketingGroup.setName(customer.getBrokerName());
                    marketingGroup.setStatus(Status.ACTIVE);
                    marketingGroup.setMaker(user.getEmployee().getId());
                    marketingGroup.setCreateDate(DateTimeUtil.getCurrentDate());
                    marketingGroupRepository.save(marketingGroup);
                }
                //Get customer by email or phone.
                customerEntity = customRepository.findByEmailOrPhoneNumber(customer.getEmail(), customer.getPhoneNumber());
                //Get marketing group by code or name.
                marketingGroup = marketingGroupRepository.findByCodeOrName(customer.getBankCode(), customer.getBrokerName());
                if (employeeRepository.findAllByPhoneOrCompanyEmail(customer.getBrokerPhone(), customer.getBrokerEmail()).size() > 0) {
                    // Step1: Set employee to group MKT
                    employee.setMarketingGroup(marketingGroup);
                    // Step2: Update emp to group MKT
                    employeeRepository.save(employee);
                    // End Step2
                }
                if (customerDistributionCustomRepository.findAllByCustomerAndMarketingGroup(customerEntity, marketingGroup).size() == 0) {
                    customerDistribution.setCustomer(customerEntity);
                    customerDistribution.setPotential(potentialEntity);
                    customerDistribution.setMarketingGroup(marketingGroup);
                    customerDistribution.setAdd_date(DateTimeUtil.getCurrentDate());
                    customerDistribution.setDate_distribution(DateTimeUtil.getCurrentDate());
                    customerDistribution.setUpd_date(DateTimeUtil.getCurrentDate());
                    customerDistribution.setAdd_user(user.getEmployee().getId());
                    customerDistribution.setUpd_user(user.getEmployee().getId());
                    customerDistributionCustomRepository.save(customerDistribution);
                }

                //Insert trading account.
                if (tradingAccountRepository.findAllByAccountNumberAndBrokerCode(customer.getTradingAccountCode(), customer.getBrokerCode()).size() == 0) {
                    tradingAccount.setAccountNumber(customer.getTradingAccountCode());
                    tradingAccount.setAccountName(customer.getTradingAccountName());
                    tradingAccount.setBrokerCode(customer.getBrokerCode());
                    tradingAccount.setBrokerName(customer.getBrokerName());
                    tradingAccount.setCreateDate(DateTimeUtil.getCurrentDate());
                    tradingAccount.setStatus("Active");
                    tradingAccount.setCustomer(customerEntity);
                    tradingAccountRepository.save(tradingAccount);
                    //End trading account.
                }
                if (customRepository.findAllByEmailOrPhoneNumber(customer.getEmail(), customer.getPhoneNumber()).size() > 0) {
                    //Search trading account
                    tradingAccount = tradingAccountRepository.findByAccountNumber(customer.getTradingAccountCode());
                    //Update data trading account in customer
                    customerEntity.setTradingAccount(tradingAccount);
                    customRepository.save(customerEntity);
                }
                // Step3: Update contract
                if (contractRepository.findAllById(customer.getContractID()).size() == 0) {
                    contract.setStatus(customer.getContractStatus());
                    contract.setId(customer.getContractID());
                    contract.setCreateDate(DateTimeUtil.convertStringToDate(customer.getCreateDateContract(), AppConst.FORMAT_DD_MM_YYYY_CROOSSIES));
                    contract.setBroker_name(customer.getBrokerName());
                    contract.setBrokerCode(customer.getBrokerCode());
                    contract.setAccount_name(customer.getTradingAccountName());
                    contract.setNumber(customer.getTradingAccountCode());
                    contractRepository.save(contract);
                }
                //End step3
                if (customRepository.findAllByEmailOrPhoneNumber(customer.getEmail(), customer.getPhoneNumber()).size() > 0) {
                    contract = contractRepository.findContractById(customer.getContractID());
                    customerEntity.setContract(contract);
                    customRepository.save(customerEntity);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Has exception when importPotential ", e);
        }
        return true;
    }

    @Override
    public BankAccount registerBanking(CustomerForm customerForm) throws ParseException{
        BankAccount bankAccount = new BankAccount(customerForm.getBankNumber(),
                customerForm.getBankName(),
                customerForm.getOwnerBankingName());
        bankAccount.setBalance(0);;
        bankAccount.setCreateDate(WebUtils.getSystemDate());
        bankAccount.setUpdDate(WebUtils.getSystemDate());
        return  bankAccount;
    }

    @Override
    public List<ErrorInFo> checkBussinessBeforeRegistCustomer(CustomerForm customerForm) {
        List<ErrorInFo> errorInFoList = new ArrayList<>();
        if (Objects.nonNull(bankRepository.findObjectByPrimaryKey(customerForm.getBankNumber()))){
            errorInFoList.add(new ErrorInFo("bank_number", "Số tài khoản ngân hàng đã tồn tại"));
        }
        if (identificationRepository.findById(customerForm.getIdentifyNumber()).isPresent()){
            errorInFoList.add(new ErrorInFo("identity_number", "Số căn cước công dân đã tồn tại"));
        }
        return errorInFoList;
    }

    public Identification registerIdentification(CustomerForm customerForm) throws ParseException{
        Identification identification = new Identification(customerForm.getIdentifyNumber(),
                customerForm.getPlaceOfIssue(),
                customerForm.getImageBefore().getOriginalFilename(),
                customerForm.getImageAfter().getOriginalFilename(),
                DateTimeUtil.convertStringToDate(customerForm.getDateOfIssue(), AppConst.FORMAT_YYYY_MM_DD),
                DateTimeUtil.convertStringToDate(customerForm.getDateOfBirth(), AppConst.FORMAT_YYYY_MM_DD));
        identification.setEthnicGroup("");
        identification.setPermanentPlace("");
        return  identification;
    }

    private Customer convertToCustomerEntity(CustomerForm customerForm, User user) throws ParseException {
        Customer customer = new Customer();
        customer.setEmployee(new Employee(customerForm.getHdnEmployeeId()));
        customer.setCustomerCode(null);
        customer.setCustomerName(customerForm.getCustomerName());
        customer.setAddress(customerForm.getAddress());
        customer.setEmail(customerForm.getEmail());
        customer.setGender(Gender.findByOption(customerForm.getGender()));
        Optional.of(sourceRepository.findObjectByPrimaryKey(customerForm.getHdnSourceId()))
                .ifPresent(customer::setSource);
        customer.setPhoneNumber(customerForm.getPhoneNumber());
        customer.setDescription(customerForm.getDescription());
        customer.setStatus(Status.ACTIVE);
        customer.setAddUser(user.getEmployee().getId());
        customer.setUpdUser(user.getEmployee().getId());
        customer.setCreateDate(WebUtils.getSystemDate());
        customer.setUpdDate(WebUtils.getSystemDate());
        return customer;
    }

}
