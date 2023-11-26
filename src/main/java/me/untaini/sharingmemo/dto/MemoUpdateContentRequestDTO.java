package me.untaini.sharingmemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemoUpdateContentRequestDTO {

    private Long memoId;
    private Long ownerId;
    private String content;

}
