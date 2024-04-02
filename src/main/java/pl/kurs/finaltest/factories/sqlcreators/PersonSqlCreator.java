package pl.kurs.finaltest.factories.sqlcreators;

public interface PersonSqlCreator {
    public String getName();

    public String getSqlQueryToInsert();

    public int[] getTypesToInsert();
}
