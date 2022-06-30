package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.CommitLogDTO;
import ru.clevertec.ecl.entty.CommitLog;

@Mapper(componentModel = "spring")
public interface CommitLogMapper {
    CommitLogDTO toCommitLogDTO(CommitLog commitLog);
}
