package it.marcof.sharednotes.model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USERS_NOTES") // The table name
@Getter // This generates getter methods for all the attributes
@Setter // This generates setter methods for all the attributes
@ToString // This generates the toString method
@NoArgsConstructor // This generates the constructor with no args
@AllArgsConstructor // This generates the constructor with all the needed args
public class UserNoteEntity {
    @EmbeddedId
    UserNoteKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    UserEntity user;

    @ManyToOne
    @MapsId("noteId")
    @JoinColumn(name = "note_id")
    NoteEntity note;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserNoteEntity that = (UserNoteEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
