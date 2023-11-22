package me.untaini.sharingmemo.service;

import jakarta.transaction.Transactional;
import me.untaini.sharingmemo.dto.DirectoryCreateRequestDTO;
import me.untaini.sharingmemo.dto.DirectoryCreateResponseDTO;
import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.exception.DirectoryException;
import me.untaini.sharingmemo.exception.type.DirectoryExceptionType;
import me.untaini.sharingmemo.mapper.DirectoryMapper;
import me.untaini.sharingmemo.repository.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DirectoryService {

    private final DirectoryRepository directoryRepository;

    @Autowired
    public DirectoryService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    @Transactional
    public Long createRootDirectory(Long userId) {
        Directory directory = Directory.builder()
                .name("")
                .ownerId(userId)
                .build();

        Directory savedDirectory = directoryRepository.save(directory);

        return savedDirectory.getId();
    }

    @Transactional
    public DirectoryCreateResponseDTO createDirectory(DirectoryCreateRequestDTO requestDTO) {
        Directory parentDirectory = directoryRepository.findById(requestDTO.getParentDirId())
                .orElseThrow(() -> new DirectoryException(DirectoryExceptionType.NOT_FOUND));

        Optional.of(parentDirectory.getOwnerId() == requestDTO.getUserId())
                .orElseThrow(() -> new DirectoryException(DirectoryExceptionType.OWNER_NOT_MATCH));

        parentDirectory.getChildDirectories().stream()
                .filter(directory -> directory.getName() == requestDTO.getName())
                .findAny()
                .ifPresent(dir -> new DirectoryException(DirectoryExceptionType.EXIST_SAME_NAME));

        Directory directory = DirectoryMapper.INSTANCE.DirectoryCreateRequestDTOToDirectory(requestDTO);
        parentDirectory.addChildDirectory(directory);

        directoryRepository.save(directory);

        return DirectoryMapper.INSTANCE.DirectoryToDirectoryCreateResponseDTO(directory);
    }

}
