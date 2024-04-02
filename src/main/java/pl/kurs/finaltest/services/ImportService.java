package pl.kurs.finaltest.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.exceptions.ResourceNoFoundException;
import pl.kurs.finaltest.models.ImportStatus;
import pl.kurs.finaltest.repositories.ImportStatusRepository;

@Service
public class ImportService {
    private final ImportStatusRepository importStatusRepository;

    public ImportService(ImportStatusRepository importStatusRepository) {
        this.importStatusRepository = importStatusRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportStatus save(ImportStatus importStatus) {
        return importStatusRepository.saveAndFlush(importStatus);
    }

    @Transactional(readOnly = true)
    public ImportStatus findById(Long id) {
        return importStatusRepository.findById(id).orElseThrow(() -> new ResourceNoFoundException("Nie znaleziono importu o id: " + id));
    }


}
