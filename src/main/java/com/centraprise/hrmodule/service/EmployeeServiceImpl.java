package com.centraprise.hrmodule.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.centraprise.hrmodule.dao.EmployeeDAO;
import com.centraprise.hrmodule.entity.AssignmentInformation;
import com.centraprise.hrmodule.entity.BankInformation;
import com.centraprise.hrmodule.entity.EmployeeAddress;
import com.centraprise.hrmodule.entity.EmployeeDetails;
import com.centraprise.hrmodule.entity.ProvidentFundInformation;
import com.centraprise.hrmodule.exception.DatabaseException;
import com.centraprise.hrmodule.model.EmployeeCommand;
import com.centraprise.hrmodule.model.EmployeeInfoListDTO;
import com.centraprise.hrmodule.model.EmployeeListDTO;
import com.centraprise.hrmodule.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeDAO employeedao;

	@Override
	public List<EmployeeListDTO> getEmployeesList() {
		List<EmployeeDetails> employeeDetails = null;
		List<EmployeeListDTO> employeeListDTOs = new ArrayList<EmployeeListDTO>();
		try {
			employeeDetails = employeeRepository.findByEmployeeActive(true);
			if (employeeListDTOs != null) {
				int i = 1;
				for (EmployeeDetails empDetais : employeeDetails) {
					EmployeeListDTO dto = new EmployeeListDTO();
					dto.setId(i++);
					dto.setPrimaryEmployeeId(empDetais.getEmpId());
					dto.setPanNumber(empDetais.getPanNumber());
					dto.setEmployeeId(empDetais.getEmployeeNumber());
					for (AssignmentInformation info : empDetais.getAssignmentInfo()) {
						dto.setJoiningDate(info.getDateOfJoining());
					}
					dto.setName(empDetais.getEmployeeName());
					employeeListDTOs.add(dto);
				}
			}
		} catch (Exception e) {
			throw new DatabaseException("Datebase is Down");
		}
		return employeeListDTOs;
	}

	@Override

	public void saveEmployee(EmployeeCommand employeeDetails) {
		try {
			EmployeeDetails empDetails = new EmployeeDetails();
			EmployeeAddress address = new EmployeeAddress();
			AssignmentInformation assignmentInfo = new AssignmentInformation();
			BankInformation bankInfo = new BankInformation();
			ProvidentFundInformation providentInfo = new ProvidentFundInformation();
			Set<AssignmentInformation> infos = new HashSet<>();
			Set<EmployeeAddress> empaddress = new HashSet<>();
			Set<ProvidentFundInformation> fundinfos = new HashSet<ProvidentFundInformation>();
			Set<BankInformation> bankInfos = new HashSet<BankInformation>();
			try {
				System.out.println("here ok");
				empDetails.setEmployeeNumber(employeeDetails.getEmployeenumber());
				empDetails.setEmployeeName(employeeDetails.getName());
				empDetails.setSex(employeeDetails.getGender());
				empDetails.setDateOfBirth(employeeDetails.getBday());
				empDetails.setPanNumber(employeeDetails.getPan());
				empDetails.setMaritalStatus(employeeDetails.getMaritalstatus());
				empDetails.setEmailAddress(employeeDetails.getEmail());
				empDetails.setPhoneNumber("+91" + employeeDetails.getPhone());
				empDetails.setPassword(employeeDetails.getPassword());
				empDetails.setEmployeeActive(true);
				address.setFlatNumber(employeeDetails.getDoornum());
				address.setVillage(employeeDetails.getVlg());
				address.setMandal(employeeDetails.getMandal());
				address.setCountry(employeeDetails.getCountry());
				address.setState(employeeDetails.getState());
				address.setDistrict(employeeDetails.getDistrict());
				address.setPincode(Integer.parseInt(employeeDetails.getPin()));
				address.setEmployeeDetails(empDetails);
				empaddress.add(address);
				empDetails.setAddress(empaddress);

				assignmentInfo.setAssignmentEndDate(employeeDetails.getEndDate());
				assignmentInfo.setAssignmentStartDate(employeeDetails.getStartdate());
				assignmentInfo.setDateOfJoining(employeeDetails.getJoindate());
				assignmentInfo.setJob(employeeDetails.getJob());
				assignmentInfo.setManager(employeeDetails.getManager());
				assignmentInfo.setYearsOfService(Float.parseFloat(employeeDetails.getService()));
				assignmentInfo.setEmployeeDetails(empDetails);
				infos.add(assignmentInfo);
				empDetails.setAssignmentInfo(infos);

				bankInfo.setAccountNumber(employeeDetails.getAccountnumber());
				bankInfo.setBankName(employeeDetails.getBankname());
				bankInfo.setIfscCode(employeeDetails.getIfsc());
				bankInfo.setEmployeeDetails(empDetails);
				bankInfos.add(bankInfo);
				empDetails.setBankInfo(bankInfos);

				providentInfo.setUanNumber(employeeDetails.getUan());
				providentInfo.setAdharNumber(employeeDetails.getAdhar());
				providentInfo.setPrevoiusEmployee(employeeDetails.getPreviousemp());
				providentInfo.setDateOfLeaving(employeeDetails.getPreviousempleavingdate());
				providentInfo.setStartDate(employeeDetails.getPreviousempstartdate());
				providentInfo.setEndDate(employeeDetails.getPreviousempenddate());
				fundinfos.add(providentInfo);
				empDetails.setProvidentInfo(fundinfos);
				providentInfo.setEmployeeDetails(empDetails);
				employeeRepository.save(empDetails);
			} catch (Exception e) {
				throw new DatabaseException("Parsing exception miss match in data types");
			}

		} catch (DatabaseException e) {
			throw new DatabaseException("Datebase is Down");
		}
	}

	@Override
	public EmployeeInfoListDTO getEmployeeById(int employeenumber) {
		EmployeeDetails employeeDetails = null;
		EmployeeInfoListDTO dtos = new EmployeeInfoListDTO();
		try {
			employeeDetails = employeeRepository.findByEmpIdAndEmployeeActive(employeenumber, true);
			System.out.println(employeeDetails);
			dtos.setEmployeeNumber(employeeDetails.getEmployeeNumber());
			dtos.setEmployeeName(employeeDetails.getEmployeeName());
			dtos.setEmpId(employeeDetails.getEmpId());
			dtos.setSex(employeeDetails.getSex());
			dtos.setDateOfBirth(employeeDetails.getDateOfBirth());
			dtos.setPanNumber(employeeDetails.getPanNumber());
			dtos.setMaritalStatus(employeeDetails.getMaritalStatus());
			dtos.setEmailAddress(employeeDetails.getEmailAddress());
			dtos.setPhoneNumber(employeeDetails.getPhoneNumber());
			dtos.setPassword(employeeDetails.getPassword());
			if (employeeDetails.getAddress() != null) {
				for (EmployeeAddress add : employeeDetails.getAddress()) {
					dtos.setFlatNumber(add.getFlatNumber());
					dtos.setVillage(add.getVillage());
					dtos.setStreet(add.getStreet());
					dtos.setState(add.getState());
					dtos.setMandal(add.getMandal());
					dtos.setDistrict(add.getDistrict());
					dtos.setCountry(add.getCountry());
					dtos.setPincode(add.getPincode());
				}
			}
			if (employeeDetails.getAssignmentInfo() != null) {
				for (AssignmentInformation assignInfo : employeeDetails.getAssignmentInfo()) {
					dtos.setAssignmentStartDate(assignInfo.getAssignmentStartDate());
					dtos.setAssignmentEndDate(assignInfo.getAssignmentEndDate());
					dtos.setJob(assignInfo.getJob());
					dtos.setDateOfJoining(assignInfo.getDateOfJoining());
					dtos.setYearsOfService(assignInfo.getYearsOfService());
					dtos.setManager(assignInfo.getManager());
				}
			}
			if (employeeDetails.getBankInfo() != null) {
				for (BankInformation bankInfo : employeeDetails.getBankInfo()) {
					dtos.setAccountNumber(bankInfo.getAccountNumber());
					dtos.setBankName(bankInfo.getBankName());
					dtos.setIfscCode(bankInfo.getIfscCode());
				}
			}
			if (employeeDetails.getProvidentInfo() != null) {
				for (ProvidentFundInformation proviInformation : employeeDetails.getProvidentInfo()) {
					dtos.setUanNumber(proviInformation.getUanNumber());
					dtos.setAdharNumber(proviInformation.getAdharNumber());
					dtos.setPrevoiusEmployee(proviInformation.getPrevoiusEmployee());
					dtos.setDateOfLeaving(proviInformation.getDateOfLeaving());
					dtos.setStartDate(proviInformation.getStartDate());
					dtos.setEndDate(proviInformation.getEndDate());
				}
			}
		} catch (Exception e) {
			throw new DatabaseException("Datebase is Down");
		}
		return dtos;
	}

	@Override
	// @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateEmployee(EmployeeCommand employeeCommand) throws Exception {

		try {
			int count = employeedao.updateEmployee(employeeCommand);
			System.out.println("count=====>" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteEmployeeById(int employeeId) {
		int count = 0;
		try {
			// int count = employeeRepository.deleteByEmpId(employeeId);
			count = employeedao.deleteEmployee(employeeId);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}

	}
}
