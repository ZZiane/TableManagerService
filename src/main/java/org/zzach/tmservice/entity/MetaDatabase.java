package org.zzach.tmservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metadatabases")
public class MetaDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "fk_datasource_id")
    private DataSource dataSource;
    private String host;
    private String source;
    private String username;
    private String password;
    @JoinColumn(name ="fk_user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private User owner;


}
