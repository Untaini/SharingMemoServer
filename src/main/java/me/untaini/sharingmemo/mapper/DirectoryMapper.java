package me.untaini.sharingmemo.mapper;

import me.untaini.sharingmemo.dto.DirectoryCreateRequestDTO;
import me.untaini.sharingmemo.dto.DirectoryCreateResponseDTO;
import me.untaini.sharingmemo.dto.DirectoryInfoDTO;
import me.untaini.sharingmemo.entity.Directory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DirectoryMapper {
    DirectoryMapper INSTANCE = Mappers.getMapper(DirectoryMapper.class);

    @Mapping(source = "userId", target = "ownerId")
    Directory DirectoryCreateRequestDTOToDirectory(DirectoryCreateRequestDTO directoryCreateRequestDTO);

    DirectoryCreateResponseDTO DirectoryToDirectoryCreateResponseDTO(Directory directory);


    DirectoryInfoDTO DirectoryToDirectoryInfoDTO(Directory directory);
}
