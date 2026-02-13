package id.pariwisata.jelajahbandung.controller;

import id.pariwisata.jelajahbandung.service.BackupService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/backup")
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    @GetMapping("/db")
    public void backupDatabase(HttpServletResponse response) throws IOException {
        String filename = "backup_db_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".sql";

        response.setContentType("application/sql");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            try {
                backupService.streamDatabaseBackup(outputStream);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Backup interrupted", e);
            }
        }
    }

    @GetMapping("/files")
    public void backupFiles(HttpServletResponse response) throws IOException {
        String filename = "backup_files_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".zip";

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            backupService.streamFileBackup(outputStream);
        }
    }
}
