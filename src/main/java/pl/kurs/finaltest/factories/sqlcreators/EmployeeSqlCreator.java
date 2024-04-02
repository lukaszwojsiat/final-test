package pl.kurs.finaltest.factories.sqlcreators;


import org.springframework.stereotype.Service;

import java.sql.Types;

@Service
public class EmployeeSqlCreator implements PersonSqlCreator {
    @Override
    public String getName() {
        return "Employee";
    }

    @Override
    public String getSqlQueryToInsert() {
        return "insert into person (type, first_name, last_name, pesel, height, weight, email, " +
                "employment_start_date, actual_position, actual_salary, version) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public int[] getTypesToInsert() {
        return new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT, Types.FLOAT,
                Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.FLOAT, Types.INTEGER};
    }
}
