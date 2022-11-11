package it.marcof.sharednotes.model.DTO;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.marcof.sharednotes.model.Entity.NoteEntity} entity
 */
@Data
public class NoteDTO implements Serializable {
    private final Long id;
}