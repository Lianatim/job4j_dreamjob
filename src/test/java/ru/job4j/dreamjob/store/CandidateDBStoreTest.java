package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CandidateDBStoreTest {

    private final BasicDataSource pool = new Main().loadPool();
    CandidateDBStore store = new CandidateDBStore(pool);

    @AfterEach
    public void wipeTable() throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from candidate")) {
            statement.execute();
        }
    }

    @Test
    public void whenCreateCandidate() {
        BasicDataSource pool = new Main().loadPool();
        CandidateDBStore store = new CandidateDBStore(pool);
        Candidate candidate = new Candidate(0, "Java CV", "description", new byte[0], new City(0, "Moscow"), true, LocalDateTime.now());
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName()).isEqualTo(candidate.getName());
        assertThat(candidateInDb.getDescription()).isEqualTo(candidate.getDescription());
    }

    @Test
    public void whenCreateThenUpdateCandidate() {
        Candidate candidate = new Candidate(1, "Java CV", "description1", new byte[0], new City(1, "Spb"), true, LocalDateTime.now());
        Candidate candidateUpdate = new Candidate(1, "Java newCV", "newDescription1", new byte[0], new City(1, "Spb"), true, LocalDateTime.now());
        store.add(candidate);
        store.update(candidateUpdate);
        Candidate postInDb = store.findById(candidate.getId());
        assertThat(postInDb.getName()).isEqualTo(candidateUpdate.getName());
    }

    @Test
    public void whenFindAllCandidate() {
        Candidate candidate = new Candidate(0, "Java CV", "description", new byte[0], new City(0, "Moscow"), true, LocalDateTime.now());
        Candidate candidate1 = new Candidate(1, "Java CV1", "description1", new byte[0], new City(0, "Moscow"), true, LocalDateTime.now());
        Candidate candidate2 = new Candidate(2, "Java CV2", "description2", new byte[0], new City(0, "Moscow"), true, LocalDateTime.now());
        Collection<Candidate> candidates = List.of(candidate, candidate1, candidate2);
        store.add(candidate);
        store.add(candidate1);
        store.add(candidate2);
        assertThat(candidates).isEqualTo(store.findAll());
    }

    @Test
    public void whenFindIdCandidate() {
        Candidate candidate = new Candidate(0, "Java CV", "description", new byte[0], new City(0, "Moscow"), true, LocalDateTime.now());
        Candidate candidate1 = new Candidate(1, "Java CV1", "description1", new byte[0], new City(0, "Moscow"), true, LocalDateTime.now());
        Candidate candidate2 = new Candidate(2, "Java CV2", "description2", new byte[0], new City(0, "Moscow"), true, LocalDateTime.now());
        store.add(candidate);
        store.add(candidate1);
        store.add(candidate2);
        assertThat(candidate1).isEqualTo(store.findById(candidate1.getId()));
    }
}