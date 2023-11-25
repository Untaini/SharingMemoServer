package me.untaini.sharingmemo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DirectoryChangeNameRequestDTO {

    private Long directoryId;
    private Long userId;
    private String name;

}
