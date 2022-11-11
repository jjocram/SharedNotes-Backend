package it.marcof.sharednotes.Repository;

import it.marcof.sharednotes.model.Entity.UserNoteEntity;
import it.marcof.sharednotes.model.Entity.UserNoteKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNoteRepository extends JpaRepository<UserNoteEntity, UserNoteKey> {
}
