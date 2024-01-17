package pl.kurs.finaltest.models.dto;

public class RetireeDto extends PersonDto {
    private double pension;
    private Integer workedYears;

    public void setPension(double pension) {
        this.pension = pension;
    }

    public void setWorkedYears(Integer workedYears) {
        this.workedYears = workedYears;
    }

    public double getPension() {
        return pension;
    }

    public Integer getWorkedYears() {
        return workedYears;
    }
}
