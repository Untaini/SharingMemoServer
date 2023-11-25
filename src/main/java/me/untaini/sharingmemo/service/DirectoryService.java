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
        Directory parentDirectory = getDirectoryById(requestDTO.getParentDirId());

        checkOwner(parentDirectory, requestDTO.getUserId());
        checkChildDirectoriesHavingSameName(parentDirectory, requestDTO.getName());

        Directory directory = DirectoryMapper.INSTANCE.DirectoryCreateRequestDTOToDirectory(requestDTO);
        parentDirectory.addChildDirectory(directory);

        directoryRepository.save(parentDirectory);

        return DirectoryMapper.INSTANCE.DirectoryToDirectoryCreateResponseDTO(directory);
    }

    @Transactional
    public DirectoryChangeNameResponseDTO changeDirectoryName(DirectoryChangeNameRequestDTO requestDTO) {
        Directory directory = getDirectoryById(requestDTO.getDirectoryId());

        checkOwner(directory, requestDTO.getUserId());

        if (directory.getName().equals(requestDTO.getName())) {
            throw new DirectoryException(DirectoryExceptionType.ALREADY_HAS_SAME_NAME);
        }

        checkChildDirectoriesHavingSameName(directory.getParentDir(), requestDTO.getName());

        DirectoryChangeNameResponseDTO responseDTO = DirectoryChangeNameResponseDTO.builder()
                .beforeName(directory.updateName(requestDTO.getName()))
                .build();

        directoryRepository.save(directory);

        return responseDTO;
    }

    public Directory getDirectoryById(Long directoryId) {
        return directoryRepository.findById(directoryId)
                .orElseThrow(() -> new DirectoryException(DirectoryExceptionType.NOT_FOUND));
    }

    public void checkOwner(Directory directory, Long userId) {
        if (!directory.getOwnerId().equals(userId)) {
            throw new DirectoryException(DirectoryExceptionType.OWNER_NOT_MATCH);
        }
    }
    public void checkChildDirectoriesHavingSameName(Directory directory, String name) {
        directory.getChildDirectories().stream()
                .filter(childDir -> childDir.getName().equals(name))
                .findAny()
                .ifPresent(childDir -> { throw new DirectoryException(DirectoryExceptionType.EXIST_SAME_NAME); });
    }

}
