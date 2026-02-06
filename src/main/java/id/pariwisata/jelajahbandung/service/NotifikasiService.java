package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.dto.NotifikasiResponse;
import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.model.Notifikasi;
import id.pariwisata.jelajahbandung.repository.NotifikasiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifikasiService {

    private final NotifikasiRepository notifikasiRepository;

    public PagedResponse<NotifikasiResponse> getAllNotifikasi(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notifikasi> notifikasiPage = notifikasiRepository.findAll(pageable);

        Page<NotifikasiResponse> notifikasiResponses = notifikasiPage.map(this::mapToResponse);

        return PagedResponse.fromPage(notifikasiResponses);
    }

    @org.springframework.transaction.annotation.Transactional
    public void markAsRead(Long id) {
        Notifikasi notifikasi = notifikasiRepository.findById(id)
                .orElseThrow(() -> new id.pariwisata.jelajahbandung.exception.ResourceNotFoundException(
                        "Notifikasi not found with id: " + id));
        notifikasi.setDibaca(true);
        notifikasiRepository.save(notifikasi);
    }

    private NotifikasiResponse mapToResponse(Notifikasi notifikasi) {
        return NotifikasiResponse.builder()
                .id(notifikasi.getId())
                .judul(notifikasi.getJudul())
                .pesan(notifikasi.getPesan())
                .tipe(notifikasi.getTipe())
                .dibaca(notifikasi.isDibaca())
                .createdAt(notifikasi.getCreatedAt())
                .build();
    }
}
