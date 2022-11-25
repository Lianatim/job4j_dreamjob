package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateStore;

import java.util.Collection;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateStore candidateStore;

    public CandidateService(CandidateStore candidateStore) {
        this.candidateStore = candidateStore;
    }

    public Collection<Candidate> findAll() {
        return candidateStore.findAll();
    }

    public void add(Candidate candidate) {
        candidateStore.add(candidate);
    }
    public Optional<Candidate> findById(int id) {
        return candidateStore.findById(id);
    }

    public boolean update(Candidate candidate) {
        return candidateStore.update(candidate);
    }
}
