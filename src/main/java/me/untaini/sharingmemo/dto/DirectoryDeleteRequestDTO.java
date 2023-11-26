package me.untaini.sharingmemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DirectoryDeleteRequestDTO {

    private Long directoryId;
    private Long userId;

}
