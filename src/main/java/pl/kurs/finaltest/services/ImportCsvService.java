package pl.kurs.finaltest.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.exceptions.WrongPersonInformationException;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;
import pl.kurs.finaltest.models.Student;
import pl.kurs.finaltest.models.dto.StatusDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
public class ImportCsvService {

    private final PersonService personService;
    private AtomicLong processedRows = new AtomicLong(0L);
    private AtomicBoolean importStarted = new AtomicBoolean(false);
    private LocalDateTime importStartDate;

    public ImportCsvService(PersonService personService) {
        this.personService = personService;
    }

    public AtomicBoolean getImportStarted() {
        return importStarted;
    }

    @Async("threadPoolTaskExecutor")
    @Transactional
    public Future<StatusDto> addManyAsCsvFile(MultipartFile file) {
        importStartDate = LocalDateTime.now();
        importStarted.set(true);
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
                Stream<String> lines = br.lines();
        ) {
            lines.map(line -> line.split(","))
                    .forEach(args -> {
                        personService.save(convertToCorrectTypeOfPerson(args));
                        processedRows.incrementAndGet();
                    });
            return CompletableFuture.completedFuture(new StatusDto("Zaimportowano pomyslnie"));
        } catch (IOException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(new StatusDto("Napotkano blad podczaz importu"));
        } finally {
            importStarted.set(false);
            importStartDate = null;
            processedRows.set(0L);
        }
    }


    public Person convertToCorrectTypeOfPerson(String[] arg) {
        switch (arg[0]) {
            case "Student" -> {
                return new Student(arg[1], arg[2], arg[3], Double.parseDouble(arg[4]), Double.parseDouble(arg[5]),
                        arg[6], arg[7], Integer.parseInt(arg[8]), arg[9], Double.parseDouble(arg[10]));
            }
            case "Retiree" -> {
                return new Retiree(arg[1], arg[2], arg[3], Double.parseDouble(arg[4]), Double.parseDouble(arg[5]),
                        arg[6], Double.parseDouble(arg[7]), Integer.parseInt(arg[8]));
            }
            case "Employee" -> {
                return new Employee(arg[1], arg[2], arg[3], Double.parseDouble(arg[4]), Double.parseDouble(arg[5]),
                        arg[6], LocalDate.parse(arg[7]), arg[8], Double.parseDouble(arg[9]));
            }
            default -> throw new WrongPersonInformationException("Podano bledne dana osobe: " + arg[0]);
        }
    }

    public StatusDto getImportStatus() {
        if (!importStarted.get()) {
            return new StatusDto("W tym momencie nie procesuje sie zaden import");
        } else
            return new StatusDto("Status aktualnego importu " + LocalDateTime.now() + ": rozpoczeto - " + importStartDate
                    + ", przeprocesowano " + processedRows + " wierszy");
    }
}
