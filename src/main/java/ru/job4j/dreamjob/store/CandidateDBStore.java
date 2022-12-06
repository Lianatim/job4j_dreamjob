package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class CandidateDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());
    private static final String FIND_ALL = "SELECT * FROM candidate";
    private static final String ADD = "INSERT INTO candidate(name, description, photo, city_id, visible, created) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE candidate SET name = ?, description = ?, photo = ?, city_id = ?, visible = ?, created = ?";
    private static final String FIND_ID = "SELECT * FROM candidate WHERE id = ?";
    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(createCandidate(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Failed connection when find all:", e);
        }
        return candidates;
    }


    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setStatement(ps, candidate);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when add:", e);
        }
        return candidate;
    }

    public boolean update(Candidate candidate) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            setStatement(ps, candidate);
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Failed connection when update:", e);
        }
        return rsl;
    }

    public Candidate findById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = createCandidate(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when looking for id:", e);
        }
        return candidate;
    }

    private static Candidate createCandidate(ResultSet resultSet) throws SQLException {
        return new Candidate(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getBytes("photo"),
                new City(resultSet.getInt("city_id"), ""),
                resultSet.getBoolean("visible"),
                resultSet.getTimestamp("created").toLocalDateTime());
    }

    private static void setStatement(PreparedStatement ps, Candidate candidate) throws SQLException {
        ps.setString(1, candidate.getName());
        ps.setString(2, candidate.getDescription());
        ps.setBytes(3, candidate.getPhoto());
        ps.setInt(4, candidate.getCity().getId());
        ps.setBoolean(5, candidate.isVisible());
        ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
    }
}