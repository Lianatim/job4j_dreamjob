package ru.job4j.dreamjob.store;


import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class CandidateStore {

    private final Map<Integer, Candidate> candidates = new HashMap<>();
    private final AtomicInteger count = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Java Junior", "Проактивное участие в рабочих встречах, предложение идей по дальнейшему развитию продукта", new byte[0], new City(1, "Москва"), LocalDateTime.now()));
        candidates.put(2, new Candidate(2, "Java Middle", "Доработка существующего кода: анализ кода, поиск и внедрение новых решений для развития продукта", new byte[0], new City(2, "СПб"), LocalDateTime.now()));
        candidates.put(3, new Candidate(3, "Python Junior", "Написание кода с использованием принципов \"clean code\"", new byte[0], new City(3, "Екб"), LocalDateTime.now()));
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        int id = count.incrementAndGet();
        candidate.setId(id);
        candidate.setCreated(LocalDateTime.now());
        candidates.putIfAbsent(id, candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public boolean update(Candidate candidate) {
        candidate.setCreated(LocalDateTime.now());
        return candidates.replace(candidate.getId(), candidates.get(candidate.getId()), candidate);
    }
}
