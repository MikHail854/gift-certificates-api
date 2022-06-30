package ru.clevertec.ecl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entty.CommitLog;

public interface CommitLogRepository extends JpaRepository<CommitLog, Integer> {

    @Query(value = "select * from commit_log order by id desc limit 1;", nativeQuery = true)
    CommitLog getLastCommit();

}
