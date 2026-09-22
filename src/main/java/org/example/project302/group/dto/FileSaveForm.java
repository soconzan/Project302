package org.example.project302.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileSaveForm {
    private List<String> fileId;

    public List<String> getFileIds(){
        return fileId;
    }
}
