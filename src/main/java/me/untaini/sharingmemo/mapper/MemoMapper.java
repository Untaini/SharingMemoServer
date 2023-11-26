package me.untaini.sharingmemo.mapper;

import me.untaini.sharingmemo.dto.MemoCreateRequestDTO;
import me.untaini.sharingmemo.dto.MemoCreateResponseDTO;
import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.entity.Memo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemoMapper {

    MemoMapper INSTANCE = Mappers.getMapper(MemoMapper.class);

    @Mapping(source = "directory", target = "directory")
    @Mapping(source = "requestDTO.userId", target = "ownerId")
    @Mapping(source = "requestDTO.name", target = "name")
    Memo DirectoryAndMemoCreateRequestDTOToMemo(Directory directory, MemoCreateRequestDTO requestDTO);


    MemoCreateResponseDTO MemoToMemoCreateResponseDTO(Memo memo);
}
