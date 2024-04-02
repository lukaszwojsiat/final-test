package pl.kurs.finaltest.factories.sqlcreators;

import org.springframework.stereotype.Service;

import java.sql.Types;

@Service
public class StudentSqlCreator implements PersonSqlCreator {
    @Override
    public String getName() {
        return "Student";
    }

    @Override
    public String getSqlQueryToInsert() {
        return "insert into person (type, first_name, last_name, pesel, height, weight, email, completed_university, " +
                "study_year, field_of_study, scholarship, version) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public int[] getTypesToInsert() {
        return new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT,
                Types.FLOAT, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.FLOAT, Types.INTEGER};
    }
}
