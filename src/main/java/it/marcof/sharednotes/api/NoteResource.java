package it.marcof.sharednotes.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import it.marcof.sharednotes.model.DTO.UserDTO;
import it.marcof.sharednotes.model.Entity.NoteEntity;
import it.marcof.sharednotes.service.NoteService;
import it.marcof.sharednotes.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController // Define a REST controller
@RequestMapping("/notes") // Map every mapping to /users urls
@RequiredArgsConstructor // Generate a constructor with all the required args
@Slf4j // Generate a logger for this class
public class NoteResource {
    private final NoteService noteService;

    private String getUsernameFromAuthorizationHeader(String authorizationHeader) {
        String username;
        try {
            username = (String) JwtUtil.parseToken(authorizationHeader.substring("Bearer ".length())).getPrincipal();
        } catch (JOSEException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (BadJOSEException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return username;
    }

    @GetMapping
    public ResponseEntity<List<NoteEntity>> findAll(@RequestHeader(name = "Authorization") String authorizationHeader) {
        return ResponseEntity.ok().body(noteService.all(getUsernameFromAuthorizationHeader(authorizationHeader)));
    }

    @PostMapping("/{id}/editors")
    public void addEditorUser(@PathVariable Long id, @RequestBody List<UserDTO> usernames) {
        for (UserDTO username : usernames) {
            noteService.addEditor(id, username.getUsername());
        }
    }

    @PostMapping
    ResponseEntity<NoteEntity> create(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody NoteEntity note) {
        return ResponseEntity.ok().body(noteService.create(note, getUsernameFromAuthorizationHeader(authorizationHeader)));
    }

    @PutMapping
    ResponseEntity<NoteEntity> save(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody NoteEntity note) {
        NoteEntity editedNote = noteService.save(note, getUsernameFromAuthorizationHeader(authorizationHeader));

        ResponseEntity.BodyBuilder responseBuilder = editedNote != null ? ResponseEntity.ok() : ResponseEntity.status(403);

        return responseBuilder.body(editedNote);
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@RequestHeader(name = "Authorization") String authorizationHeader, @PathVariable Long id) {
        String username = getUsernameFromAuthorizationHeader(authorizationHeader);

        if (noteService.delete(id, username)){
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

}
