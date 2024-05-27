package com.lapaix.report.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Report")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bulletin_id")
    private Long bulletinId;

    @Column(name = "code_report", unique = true, nullable = false)
    private String codeReport;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "indication")
    private String indication;

    @Column(name = "technique")
    private String technique;

    @Column(name = "titre")
    private String titre;

    @Column(name = "medecin")
    private String medecin;

    @Column(name = "conclusion")
    private String conclusion;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<ElementResultEntity> results;

}
