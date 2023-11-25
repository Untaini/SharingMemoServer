package me.untaini.sharingmemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DirectoryCreateRequestDTO {

    private Long userId;
    private Long parentDirId;
    private String name;

}
