package me.untaini.sharingmemo.service;

import me.untaini.sharingmemo.dto.MemoCreateRequestDTO;
import me.untaini.sharingmemo.dto.MemoCreateResponseDTO;
import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.entity.Memo;
import me.untaini.sharingmemo.exception.DirectoryException;
import me.untaini.sharingmemo.exception.type.DirectoryExceptionType;
import me.untaini.sharingmemo.mapper.MemoMapper;
import me.untaini.sharingmemo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoCreateResponseDTO createMemo(Directory directory, MemoCreateRequestDTO requestDTO) {
        checkChildMemosHavingSameName(directory, requestDTO.getName());

        Memo memo = MemoMapper.INSTANCE.DirectoryAndMemoCreateRequestDTOToMemo(directory, requestDTO);

        Memo savedMemo = memoRepository.save(memo);

        return MemoMapper.INSTANCE.MemoToMemoCreateResponseDTO(savedMemo);
    }

    private void checkChildMemosHavingSameName(Directory directory, String name) {
        directory.getChildMemos().stream()
                .filter(childMemo -> childMemo.getName().equals(name))
                .findAny()
                .ifPresent(childMemo -> { throw new DirectoryException(DirectoryExceptionType.EXIST_SAME_NAME); });
    }

}
