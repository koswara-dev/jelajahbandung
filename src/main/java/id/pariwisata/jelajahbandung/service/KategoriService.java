package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.dto.KategoriRequest;
import id.pariwisata.jelajahbandung.dto.KategoriResponse;
import id.pariwisata.jelajahbandung.model.Kategori;
import id.pariwisata.jelajahbandung.repository.KategoriRepository;
import id.pariwisata.jelajahbandung.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public id.pariwisata.jelajahbandung.dto.PagedResponse<KategoriResponse> getAllKategori(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Kategori> kategoriPage = kategoriRepository.findAll(pageable);
        org.springframework.data.domain.Page<KategoriResponse> responsePage = kategoriPage
                .map(KategoriResponse::fromEntity);
        return id.pariwisata.jelajahbandung.dto.PagedResponse.fromPage(responsePage);
    }

    public KategoriResponse getKategoriById(Long id) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori", "id", id));
        return KategoriResponse.fromEntity(kategori);
    }

    public KategoriResponse createKategori(KategoriRequest request) {
        Kategori kategori = Kategori.builder()
                .nama(request.getNama())
                .deskripsi(request.getDeskripsi())
                .urlGambar(request.getUrlGambar())
                .build();

        Kategori savedKategori = kategoriRepository.save(kategori);
        return KategoriResponse.fromEntity(savedKategori);
    }

    public KategoriResponse updateKategori(Long id, KategoriRequest request) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori", "id", id));

        kategori.setNama(request.getNama());
        kategori.setDeskripsi(request.getDeskripsi());
        kategori.setUrlGambar(request.getUrlGambar());

        Kategori updatedKategori = kategoriRepository.save(kategori);
        return KategoriResponse.fromEntity(updatedKategori);
    }

    public void deleteKategori(Long id) {
        if (!kategoriRepository.existsById(id)) {
            throw new ResourceNotFoundException("Kategori", "id", id);
        }
        kategoriRepository.deleteById(id);
    }
}
