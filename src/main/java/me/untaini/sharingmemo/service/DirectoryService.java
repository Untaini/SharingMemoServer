package me.untaini.sharingmemo.service;

import jakarta.transaction.Transactional;
import me.untaini.sharingmemo.dto.DirectoryChangeNameRequestDTO;
import me.untaini.sharingmemo.dto.DirectoryChangeNameResponseDTO;
import me.untaini.sharingmemo.dto.DirectoryCreateRequestDTO;
import me.untaini.sharingmemo.dto.DirectoryCreateResponseDTO;
import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.exception.DirectoryException;
import me.untaini.sharingmemo.exception.type.DirectoryExceptionType;
import me.untaini.sharingmemo.mapper.DirectoryMapper;
import me.untaini.sharingmemo.repository.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public Long findRootDirectory(Long userId) {
        Directory rootDirectory = directoryRepository.findByOwnerIdAndParentDirIsNull(userId);

        return rootDirectory.getId();
    }

    @Transactional
    public DirectoryCreateResponseDTO createDirectory(DirectoryCreateRequestDTO requestDTO) {
        Directory parentDirectory = directoryRepository.findById(requestDTO.getParentDirId())
                .orElseThrow(() -> new DirectoryException(DirectoryExceptionType.NOT_FOUND));

        if (!parentDirectory.getOwnerId().equals(requestDTO.getUserId())) {
            throw new DirectoryException(DirectoryExceptionType.OWNER_NOT_MATCH);
        }

        parentDirectory.getChildDirectories().stream()
                .filter(directory -> directory.getName().equals(requestDTO.getName()))
                .findAny()
                .ifPresent(dir -> { throw new DirectoryException(DirectoryExceptionType.EXIST_SAME_NAME); });

        Directory directory = DirectoryMapper.INSTANCE.DirectoryCreateRequestDTOToDirectory(requestDTO);
        parentDirectory.addChildDirectory(directory);

        directoryRepository.save(directory);

        return DirectoryMapper.INSTANCE.DirectoryToDirectoryCreateResponseDTO(directory);
    }

    @Transactional
    public DirectoryChangeNameResponseDTO changeDirectoryName(DirectoryChangeNameRequestDTO requestDTO) {
        Directory directory = directoryRepository.findById(requestDTO.getDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryExceptionType.NOT_FOUND));

        if (!directory.getOwnerId().equals(requestDTO.getUserId())) {
            throw new DirectoryException(DirectoryExceptionType.OWNER_NOT_MATCH);
        }

        if (directory.getName().equals(requestDTO.getName())) {
            throw new DirectoryException(DirectoryExceptionType.ALREADY_HAS_SAME_NAME);
        }

        directory.getParentDir().getChildDirectories().stream()
                .filter(childDir -> childDir.getName().equals(requestDTO.getName()))
                .findAny()
                .ifPresent(childDir -> { throw new DirectoryException(DirectoryExceptionType.EXIST_SAME_NAME); });

        DirectoryChangeNameResponseDTO responseDTO = DirectoryChangeNameResponseDTO.builder()
                .beforeName(directory.updateName(requestDTO.getName()))
                .build();

        directoryRepository.save(directory);

        return responseDTO;
    }

}
