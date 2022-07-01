package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.CommitLogDTO;

public interface CommitLogService {

    void sendToCommitLog(CommitLogDTO commitLogDTO);

    void sendOrderToCommitLog(CommitLogDTO commitLogDTO);

}
