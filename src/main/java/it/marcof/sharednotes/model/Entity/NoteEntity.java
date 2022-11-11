package it.marcof.sharednotes.model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity // This class maps a table to DB
@Table(name = "NOTES") // The table name
@Getter // This generates getter methods for all the attributes
@Setter // This generates setter methods for all the attributes
@ToString // This generates the toString method
@NoArgsConstructor // This generates the constructor with no args
@AllArgsConstructor // This generates the constructor with all the needed args
public class NoteEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String content;

    @OneToMany(mappedBy = "note")
    @JsonIgnore
    @ToString.Exclude
    Collection<UserNoteEntity> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NoteEntity that = (NoteEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
