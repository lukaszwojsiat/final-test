package pl.kurs.finaltest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class FinalTestApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(FinalTestApplication.class, args);

//        BufferedWriter bw = new BufferedWriter(new FileWriter("persons.csv"));
//        bw.write("dupa");
//        for (long i = 0; i < 40; i++) {
//            String pesel1 = String.valueOf(i + 11111111111L);
//            bw.write("Student,Imietest" + i + ",Nazwiskotest" + i + "," + pesel1 + "," + i + "," + i + ",test" + i + "@test.pl" + ",test" + i + "," + i + ",test" + i + "," + i);
//            bw.newLine();
//            String pesel2 = String.valueOf(i + 50 + 11111111111L);
//            bw.write("Retiree,Imietest" + i + ",Nazwiskotest" + i + "," + pesel2 + "," + i + "," + i + ",test" + i + "@test.pl" + "," + i + "," + i);
//            bw.newLine();
//            String pesel3 = String.valueOf(i + 100 + 11111111111L);
//            bw.write("Employee,Imietest" + i + ",Nazwiskotest" + i + "," + pesel3 + "," + i + "," + i + ",test" + i + "@test.pl" + ",2000-01-01" + ",test" + i + "," + i);
//            bw.newLine();
//        }
//        bw.close();
    }

}
