package me.untaini.sharingmemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DirectoryInfoResponseDTO {

    private String name;
    private List<DirectoryInfoDTO> childDirs;
    private List<MemoInfoDTO> memos;

}
