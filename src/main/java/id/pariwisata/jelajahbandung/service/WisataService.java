package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.dto.WisataRequest;
import id.pariwisata.jelajahbandung.dto.WisataResponse;
import id.pariwisata.jelajahbandung.model.Wisata;
import id.pariwisata.jelajahbandung.repository.WisataRepository;
import id.pariwisata.jelajahbandung.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WisataService {

    private final WisataRepository wisataRepository;

    public List<WisataResponse> getAllWisata(String nama) {
        List<Wisata> wisataList;
        if (nama != null && !nama.trim().isEmpty()) {
            wisataList = wisataRepository.findByNamaContainingIgnoreCase(nama);
        } else {
            wisataList = wisataRepository.findAll();
        }
        return wisataList.stream()
                .map(WisataResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public WisataResponse getWisataById(Long id) {
        Wisata wisata = wisataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wisata", "id", id));
        return WisataResponse.fromEntity(wisata);
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
                .build();

        Wisata savedWisata = wisataRepository.save(wisata);
        return WisataResponse.fromEntity(savedWisata);
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

        Wisata updatedWisata = wisataRepository.save(wisata);
        return WisataResponse.fromEntity(updatedWisata);
    }

    public void deleteWisata(Long id) {
        if (!wisataRepository.existsById(id)) {
            throw new ResourceNotFoundException("Wisata", "id", id);
        }
        wisataRepository.deleteById(id);
    }
}
