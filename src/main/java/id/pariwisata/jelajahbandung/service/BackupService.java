package id.pariwisata.jelajahbandung.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void streamDatabaseBackup(OutputStream outputStream) throws IOException, InterruptedException {
        String host = "localhost";
        String port = "5432";
        String dbName = "jelajahbandungdev";

        // Parse Jdbc Url
        // Default format: jdbc:postgresql://localhost:5432/dbName
        if (dbUrl.startsWith("jdbc:postgresql://")) {
            String cleanUrl = dbUrl.substring(18);
            int slashIndex = cleanUrl.indexOf('/');
            if (slashIndex != -1) {
                String hostPort = cleanUrl.substring(0, slashIndex);
                String database = cleanUrl.substring(slashIndex + 1);

                String[] params = database.split("\\?");
                dbName = params[0];

                String[] hp = hostPort.split(":");
                host = hp[0];
                if (hp.length > 1) {
                    port = hp[1];
                }
            }
        }

        log.info("Starting database backup for DB: {} on {}:{}", dbName, host, port);

        ProcessBuilder pb = new ProcessBuilder(
                "pg_dump",
                "-h", host,
                "-p", port,
                "-U", dbUsername,
                "-F", "p", // plain SQL format
                dbName);

        Map<String, String> env = pb.environment();
        env.put("PGPASSWORD", dbPassword);

        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = pb.start();

        try (InputStream is = process.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("pg_dump failed with exit code " + exitCode);
        }

        outputStream.flush();
    }

    public void streamFileBackup(OutputStream outputStream) throws IOException {
        Path sourcePath = Paths.get(uploadDir);
        if (!Files.exists(sourcePath)) {
            // Write empty zip or error?
            // Let's write an empty zip or a zip with a readme
            try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                ZipEntry entry = new ZipEntry("README.txt");
                zos.putNextEntry(entry);
                zos.write("Upload directory not found or empty.".getBytes());
                zos.closeEntry();
            }
            return;
        }

        try (ZipOutputStream zos = new ZipOutputStream(outputStream);
                Stream<Path> paths = Files.walk(sourcePath)) {

            paths.filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        String zipEntryName = sourcePath.relativize(path).toString().replace("\\", "/");
                        try {
                            zos.putNextEntry(new ZipEntry(zipEntryName));
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            log.error("Failed to zip file: " + path, e);
                        }
                    });

            zos.finish();
        }
    }
}
