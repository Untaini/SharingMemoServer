package me.untaini.sharingmemo.mapper;

import me.untaini.sharingmemo.dto.MemoOpeningRequestDTO;
import me.untaini.sharingmemo.dto.MemoSessionDTO;
import me.untaini.sharingmemo.entity.MemoSession;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemoSessionMapper {
    MemoSessionMapper INSTANCE = Mappers.getMapper(MemoSessionMapper.class);

    MemoSession MemoOpeningRequestDTOToMemoSession(MemoOpeningRequestDTO requestDTO);

    MemoSessionDTO MemoSessionToMemoSessionDTO(MemoSession memo);

}
