package pl.kurs.finaltest.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Entity
public class ImportStatus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate createData;
    private LocalDate startData;
    private Long processedRows;

    public ImportStatus(Status status, LocalDate createData, LocalDate startData, Long processedRows) {
        this.status = status;
        this.createData = createData;
        this.startData = startData;
        this.processedRows = processedRows;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreateData(LocalDate createData) {
        this.createData = createData;
    }

    public void setStartData(LocalDate startData) {
        this.startData = startData;
    }

    public void setProcessedRows(Long processedRows) {
        this.processedRows = processedRows;
    }
}