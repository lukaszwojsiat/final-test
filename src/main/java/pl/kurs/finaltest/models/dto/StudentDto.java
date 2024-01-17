package pl.kurs.finaltest.models.dto;

public class StudentDto extends PersonDto {
    private String completedUniversity;
    private Integer studyYear;
    private String fieldOfStudy;
    private double scholarship;


    public void setCompletedUniversity(String completedUniversity) {
        this.completedUniversity = completedUniversity;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public void setScholarship(double scholarship) {
        this.scholarship = scholarship;
    }

    public String getCompletedUniversity() {
        return completedUniversity;
    }

    public Integer getStudyYear() {
        return studyYear;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public double getScholarship() {
        return scholarship;
    }
}
