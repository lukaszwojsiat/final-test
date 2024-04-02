package pl.kurs.finaltest.factories.sqlcreators;

import org.springframework.stereotype.Service;

import java.sql.Types;

@Service
public class RetireeSqlCreator implements PersonSqlCreator {
    @Override
    public String getName() {
        return "Retiree";
    }

    @Override
    public String getSqlQueryToInsert() {
        return "insert into person (type, first_name, last_name, pesel, height, weight, email, pension, worked_years, version) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public int[] getTypesToInsert() {
        return new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.FLOAT, Types.INTEGER, Types.INTEGER};
    }
}
