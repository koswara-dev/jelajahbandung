package id.pariwisata.jelajahbandung.controller;

import id.pariwisata.jelajahbandung.dto.ApiResponse;
import id.pariwisata.jelajahbandung.dto.NotifikasiResponse;
import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.service.NotifikasiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifikasi")
@RequiredArgsConstructor
public class NotifikasiController {

    private final NotifikasiService notifikasiService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<NotifikasiResponse>>> getAllNotifikasi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<NotifikasiResponse> notifikasi = notifikasiService.getAllNotifikasi(page, size);
        return ResponseEntity.ok(ApiResponse.success("Notifikasi retrieved successfully", notifikasi));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@org.springframework.web.bind.annotation.PathVariable Long id) {
        notifikasiService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notifikasi marked as read"));
    }
}
