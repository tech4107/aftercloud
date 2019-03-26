package com.centraprise.hrmodule.dao;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.centraprise.hrmodule.model.EmployeeCommand;

@Repository
public class EmployeeDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// @Autowired
	// private PlatformTransactionManager transactionManager;

	private static final String delete_by_id = "update employee e set e.active=? where e.emp_id=?";
	private static final String update_by__employee_id = "update employee e set e.emp_name=?,e.gender=?,e.date_of_birth=?"
			+ ",e.marital_status=?,e.password=? where e.emp_id=?";

	private static final String update_employee_address_by_id = "update employee_address a set  a.flat_number=?,a.village=?,"
			+ "a.mandal=?,a.district=?,a.state=?,a.pincode=?,a.country=? where a.emp_id=?";

	private static final String update_assignment_by_id = "update assignment_info info set info.assignment_start_date=?,"
			+ "info.assignment_end_date=?,info.job=?,info.date_of_joining=?,info.years_of_service=?,info.manager=? where info.emp_id=?";

	private static final String update_employee_bank_by_id = "update Bank_details bankInfo set bankInfo.bank_name=?,"
			+ "bankInfo.account_number=?,bankInfo.ifsc_code=? where bankInfo.emp_id=?";

	private static final String update_provident_info_by_id = "update Provident_fund_details  provident set provident.uan_number=?,"
			+ "provident.adhar_number=?,provident.prevoius_employee=?,provident.date_of_leaving=?,provident.start_date=?,"
			+ "provident.end_date=? where provident.emp_id=?";

	public int deleteEmployee(int empId) {
		int count = 0;
		int[] types = new int[2];
		types[0] = Types.BOOLEAN;
		types[1] = Types.INTEGER;
		count = jdbcTemplate.update(delete_by_id, new Object[] { false, empId }, types);
		System.out.println(count);
		return count;
	}

	public int updateEmployee(EmployeeCommand employeeCommand) throws Exception {
		// TransactionDefinition def = new DefaultTransactionDefinition();
		// TransactionStatus status = transactionManager.getTransaction(def);
		int count = 0;
		int[] types = new int[6];
		types[0] = Types.VARCHAR;
		types[1] = Types.VARCHAR;
		types[2] = Types.VARCHAR;
		types[3] = Types.VARCHAR;
		types[4] = Types.VARCHAR;
		types[5] = Types.INTEGER;
		try {
			count = jdbcTemplate.update(update_by__employee_id,
					new Object[] { employeeCommand.getName(), employeeCommand.getGender(), employeeCommand.getBday(),
							employeeCommand.getMaritalstatus(), employeeCommand.getPassword(),
							employeeCommand.getEmpId() },
					types);

			int[] types1 = new int[8];
			types[0] = Types.VARCHAR;
			types[1] = Types.VARCHAR;
			types[2] = Types.VARCHAR;
			types[3] = Types.VARCHAR;
			types[4] = Types.VARCHAR;
			types[5] = Types.INTEGER;
			types[6] = Types.VARCHAR;
			types[7] = Types.INTEGER;
			jdbcTemplate.update(update_employee_address_by_id,
					new Object[] { employeeCommand.getDoornum(), employeeCommand.getVlg(), employeeCommand.getMandal(),
							employeeCommand.getDistrict(), employeeCommand.getState(), employeeCommand.getPin(),
							employeeCommand.getCountry(), employeeCommand.getEmpId() },
					types1);

			int[] types2 = new int[7];
			types[0] = Types.VARCHAR;
			types[1] = Types.VARCHAR;
			types[2] = Types.VARCHAR;
			types[3] = Types.VARCHAR;
			types[4] = Types.FLOAT;
			types[5] = Types.VARCHAR;
			types[6] = Types.INTEGER;
			jdbcTemplate.update(update_assignment_by_id,
					new Object[] { employeeCommand.getStartdate(), employeeCommand.getEndDate(),
							employeeCommand.getJob(), employeeCommand.getJoindate(), employeeCommand.getService(),
							employeeCommand.getManager(), employeeCommand.getEmpId() },
					types2);

			int[] types3 = new int[4];
			types[0] = Types.VARCHAR;
			types[1] = Types.VARCHAR;
			types[2] = Types.VARCHAR;
			types[3] = Types.INTEGER;
			jdbcTemplate.update(
					update_employee_bank_by_id, new Object[] { employeeCommand.getBankname(),
							employeeCommand.getAccountnumber(), employeeCommand.getIfsc(), employeeCommand.getEmpId() },
					types3);

			int[] types4 = new int[7];
			types[0] = Types.VARCHAR;
			types[1] = Types.VARCHAR;
			types[2] = Types.VARCHAR;
			types[3] = Types.VARCHAR;
			types[4] = Types.VARCHAR;
			types[5] = Types.VARCHAR;
			types[6] = Types.INTEGER;
			jdbcTemplate.update(update_provident_info_by_id,
					new Object[] { employeeCommand.getUan(), employeeCommand.getAdhar(),
							employeeCommand.getPreviousemp(), employeeCommand.getPreviousempleavingdate(),
							employeeCommand.getPreviousempstartdate(), employeeCommand.getPreviousempenddate(),
							employeeCommand.getEmpId() },
					types4);
			// transactionManager.commit(status);
			return count;
		} catch (Exception e) {
			// transactionManager.rollback(status);
			throw new Exception("Database is down ");
		}
	}
}
