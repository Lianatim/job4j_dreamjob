package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateDBStore;

import java.util.Collection;

@Service
@ThreadSafe
public class CandidateService {

    private final CandidateDBStore candidateDBStore;
    private final CityService cityService;

    public CandidateService(CandidateDBStore candidateDBStore, CityService cityService) {
        this.candidateDBStore = candidateDBStore;
        this.cityService = cityService;
    }

    public Collection<Candidate> findAll() {
        Collection<Candidate> candidates = candidateDBStore.findAll();
        candidates.forEach(
                candidate -> candidate.setCity(
                        cityService.findById(candidate.getCity().getId())
                )
        );
        return candidates;
    }

    public void add(Candidate candidate) {
        candidateDBStore.add(candidate);
    }
    public Candidate findById(int id) {
        return candidateDBStore.findById(id);
    }

    public boolean update(Candidate candidate) {
        return candidateDBStore.update(candidate);
    }
}
