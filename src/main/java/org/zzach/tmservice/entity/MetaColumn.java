package org.zzach.tmservice.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "metacolumns")
public class MetaColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "column_name")
    private String columnName;
    @Column(name = "constraint_column")
    private String constraint;
    private boolean editable;
    private boolean visible;
    private String description;

}
