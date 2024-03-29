package com.lapaix.report.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.lapaix.report.domain.entities.ReportEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ReportDTO {
    private Long id;
    private Long bulletinId;
    private String codeReport;
    private LocalDateTime date;
    private String titre;
    private String medecin;
    private String conclusion;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static ReportDTO toDTO(ReportEntity report) {
        if (report == null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(report, ReportDTO.class);
    }

    public ReportEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ReportEntity.class);
    }

    public static List<ReportEntity> toEntities(List<ReportDTO> dtos) {
        if (dtos == null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        return dtos.stream()
                .map(dto -> dto == null ? null : modelMapper.map(dto, ReportEntity.class))
                .collect(Collectors.toList());
    }

    public static List<ReportDTO> toDTOs(List<ReportEntity> reports) {
        if (reports == null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        return reports.stream()
                .map(entity -> entity == null ? null : modelMapper.map(entity, ReportDTO.class))
                .collect(Collectors.toList());
    }

}
