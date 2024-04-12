package com.lapaix.report.domain.dtos;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.lapaix.report.domain.entities.UniqueElementResultsEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniqueElementResultsDTO {
    private Long id;
    private boolean resultatGeneral;
    private String description;
    private String createdDate;
    private String lastModifiedDate;

    // Getters and Setters

    public static UniqueElementResultsDTO toDTO(UniqueElementResultsEntity uniqueElementResults) {
        if(uniqueElementResults == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(uniqueElementResults, UniqueElementResultsDTO.class);
    }

    public UniqueElementResultsEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, UniqueElementResultsEntity.class);
    }

    public static List<UniqueElementResultsEntity> toEntities(List<UniqueElementResultsDTO> dtos) {
        if (dtos == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return dtos.stream()
                .map(dto -> dto == null ? null : modelMapper.map(dto, UniqueElementResultsEntity.class))
                .collect(Collectors.toList());
    }

    public static List<UniqueElementResultsDTO> toDTOs(List<UniqueElementResultsEntity> uniqueElementResults) {
        if (uniqueElementResults == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return uniqueElementResults.stream()
                .map(entity -> entity == null ? null : modelMapper.map(entity, UniqueElementResultsDTO.class))
                .collect(Collectors.toList());
    }
}
