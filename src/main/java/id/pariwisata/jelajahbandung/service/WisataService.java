package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.dto.WisataRequest;
import id.pariwisata.jelajahbandung.dto.WisataResponse;
import id.pariwisata.jelajahbandung.model.Wisata;
import id.pariwisata.jelajahbandung.repository.WisataRepository;
import id.pariwisata.jelajahbandung.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WisataService {

    private final WisataRepository wisataRepository;
    private final id.pariwisata.jelajahbandung.repository.KategoriRepository kategoriRepository;

    public id.pariwisata.jelajahbandung.dto.PagedResponse<WisataResponse> getAllWisata(String nama, Long kategoriId,
            int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Wisata> wisataPage;

        if (nama != null && !nama.trim().isEmpty() && kategoriId != null) {
            wisataPage = wisataRepository.findByNamaContainingIgnoreCaseAndKategoriId(nama, kategoriId, pageable);
        } else if (nama != null && !nama.trim().isEmpty()) {
            wisataPage = wisataRepository.findByNamaContainingIgnoreCase(nama, pageable);
        } else if (kategoriId != null) {
            wisataPage = wisataRepository.findByKategoriId(kategoriId, pageable);
        } else {
            wisataPage = wisataRepository.findAll(pageable);
        }

        org.springframework.data.domain.Page<WisataResponse> responsePage = wisataPage.map(this::mapToResponse);
        return id.pariwisata.jelajahbandung.dto.PagedResponse.fromPage(responsePage);
    }

    public WisataResponse getWisataById(Long id) {
        Wisata wisata = wisataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wisata", "id", id));
        return mapToResponse(wisata);
    }

    public WisataResponse createWisata(WisataRequest request) {
        Wisata wisata = Wisata.builder()
                .nama(request.getNama())
                .deskripsi(request.getDeskripsi())
                .alamat(request.getAlamat())
                .jamBuka(request.getJamBuka())
                .hargaTiket(request.getHargaTiket())
                .urlGambar(request.getUrlGambar())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .kategori(kategoriRepository.findById(request.getKategoriId())
                        .orElseThrow(() -> new ResourceNotFoundException("Kategori", "id", request.getKategoriId())))
                .build();

        Wisata savedWisata = wisataRepository.save(wisata);
        return mapToResponse(savedWisata);
    }

    public WisataResponse updateWisata(Long id, WisataRequest request) {
        Wisata wisata = wisataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wisata", "id", id));

        wisata.setNama(request.getNama());
        wisata.setDeskripsi(request.getDeskripsi());
        wisata.setAlamat(request.getAlamat());
        wisata.setJamBuka(request.getJamBuka());
        wisata.setHargaTiket(request.getHargaTiket());
        wisata.setUrlGambar(request.getUrlGambar());
        wisata.setLatitude(request.getLatitude());
        wisata.setLongitude(request.getLongitude());
        wisata.setKategori(kategoriRepository.findById(request.getKategoriId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori", "id", request.getKategoriId())));

        Wisata updatedWisata = wisataRepository.save(wisata);
        return mapToResponse(updatedWisata);
    }

    private WisataResponse mapToResponse(Wisata wisata) {
        return WisataResponse.builder()
                .id(wisata.getId())
                .nama(wisata.getNama())
                .deskripsi(wisata.getDeskripsi())
                .alamat(wisata.getAlamat())
                .jamBuka(wisata.getJamBuka())
                .hargaTiket(wisata.getHargaTiket())
                .urlGambar(wisata.getUrlGambar())
                .latitude(wisata.getLatitude())
                .longitude(wisata.getLongitude())
                .kategoriId(wisata.getKategori() != null ? wisata.getKategori().getId() : null)
                .kategoriNama(wisata.getKategori() != null ? wisata.getKategori().getNama() : null)
                .createdAt(wisata.getCreatedAt())
                .updatedAt(wisata.getUpdatedAt())
                .build();
    }

    public void deleteWisata(Long id) {
        if (!wisataRepository.existsById(id)) {
            throw new ResourceNotFoundException("Wisata", "id", id);
        }
        wisataRepository.deleteById(id);
    }
}
