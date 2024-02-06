package pl.kurs.finaltest.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
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
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
public class ImportCsvService {

    private static final String INSERT_STUDENT_SQL = "insert into person (type, first_name, last_name, pesel, height, weight, email, completed_university, study_year, field_of_study, scholarship, version) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_EMPLOYEE_SQL = "insert into person (type, first_name, last_name, pesel, height, weight, email, employment_start_date, actual_position, salary, version) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_RETIREE_SQL = "insert into person (type, first_name, last_name, pesel, height, weight, email, pension, worked_years, version) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final int[] studentTypes = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.FLOAT, Types.INTEGER};
    private final int[] employeeTypes = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.FLOAT, Types.INTEGER};
    private final int[] retireeTypes = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.FLOAT, Types.INTEGER, Types.INTEGER};

    private final JdbcTemplate jdbcTemplate;
    private AtomicLong processedRows = new AtomicLong(0L);
    private AtomicBoolean importStarted = new AtomicBoolean(false);
    private LocalDateTime importStartDate;

    public ImportCsvService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
            long s1 = System.currentTimeMillis();

            lines.map(line -> line.split(","))
                    .forEach(args -> {
                        jdbcTemplateUpdate(args); //~MySql 11s
                        processedRows.incrementAndGet();
                    });

            long s2 = System.currentTimeMillis();
            System.out.println(s2 - s1);
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

    public void jdbcTemplateUpdate(String[] args) {
        String[] arguments = Arrays.copyOf(args, args.length + 1);
        arguments[arguments.length - 1] = "0";  //Add column 'version'
        switch (args[0]) {
            case "Student" -> jdbcTemplate.update(INSERT_STUDENT_SQL, arguments, studentTypes);
            case "Employee" -> jdbcTemplate.update(INSERT_EMPLOYEE_SQL, arguments, employeeTypes);
            case "Retiree" -> jdbcTemplate.update(INSERT_RETIREE_SQL, arguments, retireeTypes);
            default -> throw new WrongPersonInformationException("Podano bledne dana osobe: " + args[0] + " /wiersz: " + (processedRows.get() + 1));
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
