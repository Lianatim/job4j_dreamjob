package ru.job4j.dreamjob.store;


import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CandidateStore {

    private static final CandidateStore CANDIDATE_STORE = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Sergey", "Java Junior", LocalDateTime.now()));
        candidates.put(2, new Candidate(2, "Sveta", "Java Middle", LocalDateTime.now()));
        candidates.put(3, new Candidate(3, "Maksim", "Python Junior", LocalDateTime.now()));
    }

    public static CandidateStore instOf() {
        return CANDIDATE_STORE;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
