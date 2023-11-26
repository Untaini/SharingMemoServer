package me.untaini.sharingmemo.service;

import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.exception.DirectoryException;
import me.untaini.sharingmemo.exception.type.DirectoryExceptionType;
import me.untaini.sharingmemo.mapper.DirectoryMapper;
import me.untaini.sharingmemo.repository.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final MemoService memoService;

    @Autowired
    public DirectoryService(DirectoryRepository directoryRepository, MemoService memoService) {
        this.directoryRepository = directoryRepository;
        this.memoService = memoService;
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
    public Long findRootDirectory(Long userId) {
        Directory rootDirectory = directoryRepository.findByOwnerIdAndParentDirIsNull(userId);

        return rootDirectory.getId();
    }

    @Transactional
    public void deleteRootDirectory(Long userId) {

        directoryRepository.deleteByOwnerIdAndParentDirIsNull(userId);

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

        if (directory.getParentDir() == null) {
            throw new DirectoryException(DirectoryExceptionType.CANNOT_CHANGE_ROOT_DIRECTORY_NAME);
        }

        checkChildDirectoriesHavingSameName(directory.getParentDir(), requestDTO.getName());

        DirectoryChangeNameResponseDTO responseDTO = DirectoryChangeNameResponseDTO.builder()
                .beforeName(directory.updateName(requestDTO.getName()))
                .build();

        directoryRepository.save(directory);

        return responseDTO;
    }

    @Transactional
    public MemoCreateResponseDTO createMemo(MemoCreateRequestDTO requestDTO) {
        Directory directory = getDirectoryById(requestDTO.getDirectoryId());

        checkOwner(directory, requestDTO.getUserId());

        return memoService.createMemo(directory, requestDTO);
    }

    private Directory getDirectoryById(Long directoryId) {
        return directoryRepository.findById(directoryId)
                .orElseThrow(() -> new DirectoryException(DirectoryExceptionType.NOT_FOUND));
    }

    private void checkOwner(Directory directory, Long userId) {
        if (!directory.getOwnerId().equals(userId)) {
            throw new DirectoryException(DirectoryExceptionType.OWNER_NOT_MATCH);
        }
    }
    private void checkChildDirectoriesHavingSameName(Directory directory, String name) {
        directory.getChildDirectories().stream()
                .filter(childDir -> childDir.getName().equals(name))
                .findAny()
                .ifPresent(childDir -> { throw new DirectoryException(DirectoryExceptionType.EXIST_SAME_NAME); });
    }

}
