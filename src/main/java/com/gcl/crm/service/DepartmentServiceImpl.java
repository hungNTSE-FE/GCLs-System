package com.gcl.crm.service;

import com.gcl.crm.entity.Department;
import com.gcl.crm.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAllDepartments(Pageable pageable) {
        return departmentRepository.findAllBy(pageable);
    }

    @Override
    public Department findDepartmentById(Long id) {
        return departmentRepository.findDepartmentById(id);
    }

    @Override
    public List<Department> findDepartmentsByName(String keyword, Pageable pageable) {
        return departmentRepository.findAllByNameContaining(keyword, pageable);
    }

    @Override
    public List<String> getPageList(int pageSize, String keyword) {
        List<String> urlList = new ArrayList<>();
        int page = 0;
        int record = departmentRepository.countAllByNameContaining(keyword);
        if (record > 0){
            page = record / pageSize;
            if (record % pageSize != 0){
                page++;
            }
            if (page > 0){
                String url = "/department/";
                String param = "";
                if (!keyword.isEmpty()){
                    url += "search/";
                    param += "?k=" + keyword;
                }
                for (int i = 1; i <= page; i++){
                    urlList.add(url + i + param);
                }
            }
        }
        return urlList;
    }

    @Override
    public void createDepartment(Department department) {
        departmentRepository.save(department);
    }
}
