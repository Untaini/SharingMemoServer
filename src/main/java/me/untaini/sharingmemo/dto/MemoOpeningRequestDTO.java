package me.untaini.sharingmemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemoOpeningRequestDTO {

    private String sessionId;
    private Long memoId;
    private Long userId;

}
