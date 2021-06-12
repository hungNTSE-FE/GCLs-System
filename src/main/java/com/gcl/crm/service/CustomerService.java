package com.gcl.crm.service;

import com.gcl.crm.dto.SelectItem;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.Source;
import com.gcl.crm.form.ComboboxForm;
import com.gcl.crm.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {


    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    EmployeeService employeeService;

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
}
