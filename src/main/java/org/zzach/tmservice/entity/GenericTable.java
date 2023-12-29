package org.zzach.tmservice.entity;

import org.zzach.tmservice.enums.TypeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name ="metadatatables")
public class GenericTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datatable")
    private long id;
    @Column(name = "nom_table", nullable = false)
    private String tableName;

    @OneToMany
    private List<MetaColumn> metaColumns;

    private String entityColumnId;
    private TypeEntity typeEntity;

    private TypeEntity visibleBy;
    private TypeEntity modifiableBy;

    private String description;
    private boolean published;
    @OneToMany
    private List<User> assignedUsers;
    @ManyToOne
    @JoinColumn(name = "fk_database_id")
    private MetaDatabase database;


}
