package id.pariwisata.jelajahbandung.controller;

import id.pariwisata.jelajahbandung.dto.ApiResponse;
import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.dto.WisataRequest;
import id.pariwisata.jelajahbandung.dto.WisataResponse;
import id.pariwisata.jelajahbandung.service.WisataService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wisata")
@RequiredArgsConstructor
public class WisataController {

    private final WisataService wisataService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<WisataResponse>>> getAllWisata(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) Long kategoriId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity
                .ok(ApiResponse.success("Data wisata successfully collected",
                        wisataService.getAllWisata(nama, kategoriId, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WisataResponse>> getWisataById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Wisata found", wisataService.getWisataById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WisataResponse>> createWisata(@Valid @RequestBody WisataRequest request) {
        return ResponseEntity
                .ok(ApiResponse.success("Wisata created successfully", wisataService.createWisata(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WisataResponse>> updateWisata(@PathVariable Long id,
            @Valid @RequestBody WisataRequest request) {
        return ResponseEntity
                .ok(ApiResponse.success("Wisata updated successfully", wisataService.updateWisata(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWisata(@PathVariable Long id) {
        wisataService.deleteWisata(id);
        return ResponseEntity.ok(ApiResponse.success("Wisata deleted successfully"));
    }
}
