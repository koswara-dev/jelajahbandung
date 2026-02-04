package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.dto.EventRequest;
import id.pariwisata.jelajahbandung.dto.EventResponse;
import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.exception.ResourceNotFoundException;
import id.pariwisata.jelajahbandung.model.Event;
import id.pariwisata.jelajahbandung.model.KategoriEvent;
import id.pariwisata.jelajahbandung.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final FileStorageService fileStorageService;
    private final id.pariwisata.jelajahbandung.repository.NotifikasiRepository notifikasiRepository;
    private final org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    public PagedResponse<EventResponse> getAllEvents(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Event> events;

        if (search != null && !search.isEmpty()) {
            events = eventRepository.findByNamaContainingIgnoreCase(search, pageable);
        } else {
            events = eventRepository.findAll(pageable);
        }

        Page<EventResponse> eventResponses = events.map(this::mapToResponse);

        return PagedResponse.fromPage(eventResponses);
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return mapToResponse(event);
    }

    @Transactional
    public EventResponse createEvent(EventRequest request) {
        String urlGambar = null;
        if (request.getGambar() != null && !request.getGambar().isEmpty()) {
            urlGambar = fileStorageService.storeFile(request.getGambar());
        }

        id.pariwisata.jelajahbandung.model.KategoriEvent kategori = null;
        if (request.getKategori() != null && !request.getKategori().isEmpty()) {
            try {
                kategori = id.pariwisata.jelajahbandung.model.KategoriEvent.valueOf(request.getKategori());
            } catch (IllegalArgumentException e) {
                // Handle invalid enum value if necessary, or let it be null
            }
        }

        Event event = Event.builder()
                .nama(request.getNama())
                .lokasi(request.getLokasi())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .urlGambar(urlGambar)
                .kategori(kategori)
                .deskripsi(request.getDeskripsi())
                .tanggalMulai(request.getTanggalMulai())
                .tanggalSelesai(request.getTanggalSelesai())
                .waktuMulai(request.getWaktuMulai())
                .waktuSelesai(request.getWaktuSelesai())
                .htmRegular(request.getHtmRegular())
                .htmVip(request.getHtmVip())
                .statusHtm(request.getStatusHtm())
                .build();

        Event savedEvent = eventRepository.save(event);

        // Create Notification
        id.pariwisata.jelajahbandung.model.Notifikasi notifikasi = id.pariwisata.jelajahbandung.model.Notifikasi
                .builder()
                .judul("Event Baru: " + savedEvent.getNama())
                .pesan("Event baru telah ditambahkan di " + savedEvent.getLokasi())
                .tipe("EVENT")
                .dibaca(false)
                .build();

        id.pariwisata.jelajahbandung.model.Notifikasi savedNotifikasi = notifikasiRepository.save(notifikasi);

        // Send via WebSocket
        messagingTemplate.convertAndSend("/topic/notifikasi", savedNotifikasi);

        return mapToResponse(savedEvent);
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        event.setNama(request.getNama());
        event.setLokasi(request.getLokasi());
        event.setLatitude(request.getLatitude());
        event.setLongitude(request.getLongitude());
        event.setDeskripsi(request.getDeskripsi());
        event.setTanggalMulai(request.getTanggalMulai());
        event.setTanggalSelesai(request.getTanggalSelesai());
        event.setWaktuMulai(request.getWaktuMulai());
        event.setWaktuSelesai(request.getWaktuSelesai());
        event.setHtmRegular(request.getHtmRegular());
        event.setHtmVip(request.getHtmVip());
        event.setStatusHtm(request.getStatusHtm());

        if (request.getKategori() != null && !request.getKategori().isEmpty()) {
            try {
                event.setKategori(KategoriEvent.valueOf(request.getKategori()));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum value
            }
        }

        if (request.getGambar() != null && !request.getGambar().isEmpty()) {
            String urlGambar = fileStorageService.storeFile(request.getGambar());
            event.setUrlGambar(urlGambar);
        }

        Event updatedEvent = eventRepository.save(event);
        return mapToResponse(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        eventRepository.delete(event);
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .nama(event.getNama())
                .lokasi(event.getLokasi())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .urlGambar(event.getUrlGambar())
                .kategori(event.getKategori() != null ? event.getKategori().name() : null)
                .deskripsi(event.getDeskripsi())
                .tanggalMulai(event.getTanggalMulai())
                .tanggalSelesai(event.getTanggalSelesai())
                .waktuMulai(event.getWaktuMulai())
                .waktuSelesai(event.getWaktuSelesai())
                .htmRegular(event.getHtmRegular())
                .htmVip(event.getHtmVip())
                .statusHtm(event.getStatusHtm())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
