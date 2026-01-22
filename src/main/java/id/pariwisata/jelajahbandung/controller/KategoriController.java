package id.pariwisata.jelajahbandung.controller;

import id.pariwisata.jelajahbandung.dto.ApiResponse;
import id.pariwisata.jelajahbandung.dto.KategoriRequest;
import id.pariwisata.jelajahbandung.dto.KategoriResponse;
import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.service.KategoriService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kategori")
@RequiredArgsConstructor
public class KategoriController {

    private final KategoriService kategoriService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<KategoriResponse>>> getAllKategori(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .ok(ApiResponse.success("Data kategori successfully collected",
                        kategoriService.getAllKategori(search, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KategoriResponse>> getKategoriById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Kategori found", kategoriService.getKategoriById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KategoriResponse>> createKategori(@Valid @RequestBody KategoriRequest request) {
        return ResponseEntity
                .ok(ApiResponse.success("Kategori created successfully", kategoriService.createKategori(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KategoriResponse>> updateKategori(@PathVariable Long id,
            @Valid @RequestBody KategoriRequest request) {
        return ResponseEntity
                .ok(ApiResponse.success("Kategori updated successfully", kategoriService.updateKategori(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteKategori(@PathVariable Long id) {
        kategoriService.deleteKategori(id);
        return ResponseEntity.ok(ApiResponse.success("Kategori deleted successfully"));
    }
}
