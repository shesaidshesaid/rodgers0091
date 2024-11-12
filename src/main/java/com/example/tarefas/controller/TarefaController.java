package com.example.tarefas.controller;

import com.example.tarefas.model.Tarefa;
import com.example.tarefas.repository.TarefaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private static final Logger logger = LoggerFactory.getLogger(TarefaController.class);

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Método para obter todas as tarefas
    @GetMapping
    public ResponseEntity<Iterable<Tarefa>> getAllTarefas() {
        logger.info("Fetching all tasks");
        Iterable<Tarefa> tarefas = tarefaRepository.findAll();
        return ResponseEntity.ok(tarefas);
    }

    // Método para criar uma nova tarefa
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Tarefa> criarTarefa(
            @RequestParam("titulo") String titulo,
            @RequestParam("descricao") String descricao,
            @RequestParam(value = "concluida", required = false) Boolean concluida,
            @RequestPart(name = "foto", required = false) MultipartFile foto,
            @RequestParam(value = "fotoSenha", required = false) String fotoSenha) {
        logger.info("Creating new task with title: {}", titulo);
        try {
            Tarefa tarefa = new Tarefa();
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            tarefa.setConcluida(concluida != null ? concluida : false);
            if (foto != null && !foto.isEmpty()) {
                String originalFilename = foto.getOriginalFilename();
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    String filename = StringUtils.cleanPath(originalFilename);
                    Path uploadPath = Paths.get("uploads/");
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                        logger.info("Created upload directory: {}", uploadPath.toString());
                    }
                    Path filePath = uploadPath.resolve(filename);
                    Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    tarefa.setFotoUrl("/uploads/" + filename);
                    logger.info("Uploaded file: {}", filename);
                }
            }
            if (fotoSenha != null && !fotoSenha.isEmpty()) {
                String hashedSenha = passwordEncoder.encode(fotoSenha);
                tarefa.setFotoSenha(hashedSenha);
                logger.info("Password for photo set");
            }
            Tarefa tarefaSalva = tarefaRepository.save(tarefa);
            logger.info("Task created with ID: {}", tarefaSalva.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(tarefaSalva);
        } catch (IOException e) {
            logger.error("Error creating task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método para atualizar uma tarefa existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTarefa(
            @PathVariable Long id,
            @RequestParam("titulo") String titulo,
            @RequestParam("descricao") String descricao,
            @RequestParam(value = "concluida", required = false) Boolean concluida,
            @RequestPart(name = "foto", required = false) MultipartFile foto,
            @RequestParam(value = "fotoSenha", required = false) String fotoSenha) {
        logger.info("Updating task with ID: {}", id);
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefa.setTitulo(titulo);
                    tarefa.setDescricao(descricao);
                    if (concluida != null) {
                        tarefa.setConcluida(concluida);
                    }
                    if (foto != null && !foto.isEmpty()) {
                        String originalFilename = foto.getOriginalFilename();
                        if (originalFilename != null && !originalFilename.isEmpty()) {
                            String filename = StringUtils.cleanPath(originalFilename);
                            Path uploadPath = Paths.get("uploads/");
                            try {
                                if (!Files.exists(uploadPath)) {
                                    Files.createDirectories(uploadPath);
                                    logger.info("Created upload directory: {}", uploadPath.toString());
                                }
                                Path filePath = uploadPath.resolve(filename);
                                Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                                tarefa.setFotoUrl("/uploads/" + filename);
                                logger.info("Uploaded file: {}", filename);
                            } catch (IOException e) {
                                logger.error("Error uploading file", e);
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a foto");
                            }
                        }
                    }
                    // Atualizar fotoSenha apenas se fornecida
                    if (fotoSenha != null && !fotoSenha.isEmpty()) {
                        String hashedSenha = passwordEncoder.encode(fotoSenha);
                        tarefa.setFotoSenha(hashedSenha);
                        logger.info("Password for photo updated");
                    } else {
                        logger.info("Password for photo not changed");
                    }
                    Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
                    logger.info("Task updated with ID: {}", tarefaAtualizada.getId());
                    return ResponseEntity.ok(tarefaAtualizada);
                })
                .orElseGet(() -> {
                    logger.warn("Task with ID: {} not found", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
                });
    }

    // Método para deletar uma tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        logger.info("Deleting task with ID: {}", id);
        if (tarefaRepository.existsById(id)) {
            tarefaRepository.deleteById(id);
            logger.info("Task with ID: {} deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Task with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para servir fotos protegidas
    @PostMapping("/fotos/{filename}")
    public ResponseEntity<Resource> getFoto(
            @PathVariable String filename,
            @RequestParam("fotoSenha") String fotoSenha) {
        logger.info("Fetching photo with filename: {}", filename);
        try {
            Path filePath = Paths.get("uploads/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            Optional<Tarefa> optionalTarefa = tarefaRepository.findByFotoUrl("/uploads/" + filename);
            if (optionalTarefa.isPresent()) {
                Tarefa tarefa = optionalTarefa.get();
                if (tarefa.getFotoSenha() != null) {
                    logger.info("Comparing entered password with stored hash");
                    if (!passwordEncoder.matches(fotoSenha, tarefa.getFotoSenha())) {
                        logger.warn("Unauthorized access attempt to photo: {}", filename);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                }
            }
            logger.info("Photo fetched successfully: {}", filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .body(resource);
        } catch (IOException e) {
            logger.error("Error fetching photo", e);
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para validar a senha da foto
    @PostMapping("/validar-senha")
    public ResponseEntity<?> validarSenha(@RequestBody Map<String, String> payload) {
        Long fotoId = Long.parseLong(payload.get("fotoId"));
        String fotoSenha = payload.get("fotoSenha");

        Optional<Tarefa> optionalTarefa = tarefaRepository.findById(fotoId);
        if (optionalTarefa.isPresent()) {
            Tarefa tarefa = optionalTarefa.get();
            if (passwordEncoder.matches(fotoSenha, tarefa.getFotoSenha())) {
                return ResponseEntity.ok(Collections.singletonMap("senhaCorreta", true));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("senhaCorreta", false));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }
    }
}
