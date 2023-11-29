package me.untaini.sharingmemo.service;

import me.untaini.sharingmemo.dto.*;
import me.untaini.sharingmemo.entity.Directory;
import me.untaini.sharingmemo.entity.Memo;
import me.untaini.sharingmemo.entity.MemoSession;
import me.untaini.sharingmemo.exception.DirectoryException;
import me.untaini.sharingmemo.exception.MemoException;
import me.untaini.sharingmemo.exception.type.DirectoryExceptionType;
import me.untaini.sharingmemo.exception.type.MemoExceptionType;
import me.untaini.sharingmemo.mapper.MemoMapper;
import me.untaini.sharingmemo.mapper.MemoSessionMapper;
import me.untaini.sharingmemo.repository.MemoRepository;
import me.untaini.sharingmemo.repository.MemoSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemoSessionRepository memoSessionRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository, MemoSessionRepository memoSessionRepository) {
        this.memoRepository = memoRepository;
        this.memoSessionRepository = memoSessionRepository;
    }

    @Transactional
    public Pair<Timestamp, MemoInfoResponseDTO> getMemoInfo(MemoInfoRequestDTO requestDTO) {
        Memo memo = getMemoById(requestDTO.getMemoId());

        checkOwner(memo, requestDTO.getUserId());

        MemoInfoResponseDTO responseDTO = MemoMapper.INSTANCE.MemoToMemoInfoResponseDTO(memo);

        return Pair.of(memo.getLastModifiedTimestamp(), responseDTO);
    }

    @Transactional
    public MemoCreateResponseDTO createMemo(Directory directory, MemoCreateRequestDTO requestDTO) {
        checkChildMemosHavingSameName(directory, requestDTO.getName());

        Memo memo = MemoMapper.INSTANCE.DirectoryAndMemoCreateRequestDTOToMemo(directory, requestDTO);

        Memo savedMemo = memoRepository.save(memo);

        return MemoMapper.INSTANCE.MemoToMemoCreateResponseDTO(savedMemo);
    }

    @Transactional
    public MemoChangeNameResponseDTO changeMemoName(MemoChangeNameRequestDTO requestDTO) {
        Memo memo = getMemoById(requestDTO.getMemoId());

        checkOwner(memo, requestDTO.getOwnerId());

        if (memo.getName().equals(requestDTO.getName())) {
            throw new MemoException(MemoExceptionType.ALREADY_HAS_SAME_NAME);
        }

        checkChildMemosHavingSameName(memo.getDirectory(), requestDTO.getName());

        MemoChangeNameResponseDTO responseDTO = MemoChangeNameResponseDTO.builder()
                .beforeName(memo.updateName(requestDTO.getName()))
                .build();

        memoRepository.save(memo);

        return responseDTO;
    }

    @Transactional
    public void updateContent(MemoUpdateContentRequestDTO requestDTO) {
        Memo memo = getMemoById(requestDTO.getMemoId());

        memo.updateContent(requestDTO.getContent());

        memoRepository.save(memo);
    }

    @Transactional
    public void deleteMemo(MemoDeleteRequestDTO requestDTO) {
        Memo memo = getMemoById(requestDTO.getMemoId());

        checkOwner(memo, requestDTO.getUserId());

        if (memoSessionRepository.existsByMemoId(memo.getId())) {
            throw new MemoException(MemoExceptionType.CANNOT_DELETE_BECAUSE_OF_OPENING);
        }

        memoRepository.delete(memo);
    }

    @Transactional
    public MemoSessionDTO openMemo(MemoOpeningRequestDTO requestDTO) {
        Memo memo = getMemoById(requestDTO.getMemoId());

        checkOwner(memo, requestDTO.getUserId());

        if (memoSessionRepository.existsByMemoId(requestDTO.getMemoId())) {
            throw new MemoException(MemoExceptionType.ALREADY_OPEN);
        }

        if (memoSessionRepository.existsById(requestDTO.getSessionId())) {
            throw new MemoException(MemoExceptionType.CANNOT_OPEN_MORE);
        }

        MemoSession memoSession = MemoSessionMapper.INSTANCE.MemoOpeningRequestDTOToMemoSession(requestDTO);

        memoSessionRepository.save(memoSession);

        return MemoSessionMapper.INSTANCE.MemoSessionToMemoSessionDTO(memoSession);
    }

    @Transactional
    public void closeMemo(MemoClosingRequestDTO requestDTO) {
        if (!memoSessionRepository.existsById(requestDTO.getSessionId())) {
            throw new MemoException(MemoExceptionType.MEMO_NOT_OPEN);
        }

        MemoSession session = memoSessionRepository.getReferenceById(requestDTO.getSessionId());

        memoSessionRepository.delete(session);
    }

    public void deleteMemosInSelectedDirectories(List<Long> directoryIdList) {
        List<Long> memoIdList = memoRepository.findAllByDirectory_IdIn(directoryIdList).stream()
                .map(Memo::getId)
                .toList();

        if (memoSessionRepository.existsMemoSessionsByMemoIdIn(memoIdList)) {
            throw new MemoException(MemoExceptionType.EXIST_OPENED_DESCENDENT_MEMO);
        }

        memoRepository.deleteAllByIdInBatch(memoIdList);
    }

    private Memo getMemoById(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new MemoException(MemoExceptionType.MEMO_NOT_FOUND));
    }

    private void checkOwner(Memo memo, Long userId) {
        if (!memo.getOwnerId().equals(userId)) {
            throw new MemoException(MemoExceptionType.OWNER_NOT_MATCH);
        }
    }

    private void checkChildMemosHavingSameName(Directory directory, String name) {
        directory.getChildMemos().stream()
                .filter(childMemo -> childMemo.getName().equals(name))
                .findAny()
                .ifPresent(childMemo -> { throw new DirectoryException(DirectoryExceptionType.EXIST_SAME_NAME); });
    }

}
