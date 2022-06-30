package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.entty.CommitLog;

public interface CommitLogService {
    void sendToCommitLog(CommitLogDTO commitLogDTO);
//    void save(CommitLog commitLog);

//    CommitLogDTO getLastCommit();
}
