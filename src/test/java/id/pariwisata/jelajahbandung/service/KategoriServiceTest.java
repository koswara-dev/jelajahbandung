package id.pariwisata.jelajahbandung.service;

import id.pariwisata.jelajahbandung.dto.KategoriRequest;
import id.pariwisata.jelajahbandung.dto.KategoriResponse;
import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.exception.ResourceNotFoundException;
import id.pariwisata.jelajahbandung.model.Kategori;
import id.pariwisata.jelajahbandung.repository.KategoriRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KategoriServiceTest {

    @Mock
    private KategoriRepository kategoriRepository;

    @InjectMocks
    private KategoriService kategoriService;

    @Test
    @DisplayName("Should return all kategori when search is null or empty")
    void getAllKategori_NoSearch() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Kategori kategori = Kategori.builder()
                .id(1L)
                .nama("Test Kategori")
                .deskripsi("Deskripsi")
                .urlGambar("http://image.url")
                .build();
        Page<Kategori> kategoriPage = new PageImpl<>(Collections.singletonList(kategori));

        when(kategoriRepository.findAll(pageable)).thenReturn(kategoriPage);

        // Act
        PagedResponse<KategoriResponse> result = kategoriService.getAllKategori(null, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Kategori", result.getContent().get(0).getNama());
        verify(kategoriRepository, times(1)).findAll(pageable);
        verify(kategoriRepository, never()).findByNamaContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should return filtered kategori when search is provided")
    void getAllKategori_WithSearch() {
        // Arrange
        String search = "Test";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Kategori kategori = Kategori.builder()
                .id(1L)
                .nama("Test Kategori")
                .build();
        Page<Kategori> kategoriPage = new PageImpl<>(Collections.singletonList(kategori));

        when(kategoriRepository.findByNamaContainingIgnoreCase(search, pageable)).thenReturn(kategoriPage);

        // Act
        PagedResponse<KategoriResponse> result = kategoriService.getAllKategori(search, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(kategoriRepository, times(1)).findByNamaContainingIgnoreCase(search, pageable);
        verify(kategoriRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return kategori by id when found")
    void getKategoriById_Success() {
        // Arrange
        Long id = 1L;
        Kategori kategori = Kategori.builder()
                .id(id)
                .nama("Test Kategori")
                .build();
        when(kategoriRepository.findById(id)).thenReturn(Optional.of(kategori));

        // Act
        KategoriResponse result = kategoriService.getKategoriById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(kategoriRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when kategori not found by id")
    void getKategoriById_NotFound() {
        // Arrange
        Long id = 1L;
        when(kategoriRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> kategoriService.getKategoriById(id));
        verify(kategoriRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should create new kategori")
    void createKategori_Success() {
        // Arrange
        KategoriRequest request = new KategoriRequest();
        request.setNama("New Kategori");
        request.setDeskripsi("New Deskripsi");
        request.setUrlGambar("http://new.image");

        Kategori savedKategori = Kategori.builder()
                .id(1L)
                .nama(request.getNama())
                .deskripsi(request.getDeskripsi())
                .urlGambar(request.getUrlGambar())
                .build();

        when(kategoriRepository.save(any(Kategori.class))).thenReturn(savedKategori);

        // Act
        KategoriResponse result = kategoriService.createKategori(request);

        // Assert
        assertNotNull(result);
        assertEquals(savedKategori.getId(), result.getId());
        assertEquals(request.getNama(), result.getNama());
        verify(kategoriRepository, times(1)).save(any(Kategori.class));
    }

    @Test
    @DisplayName("Should update existing kategori")
    void updateKategori_Success() {
        // Arrange
        Long id = 1L;
        KategoriRequest request = new KategoriRequest();
        request.setNama("Updated Kategori");
        request.setDeskripsi("Updated Deskripsi");
        request.setUrlGambar("http://updated.image");

        Kategori existingKategori = Kategori.builder()
                .id(id)
                .nama("Old Kategori")
                .build();

        Kategori updatedKategori = Kategori.builder()
                .id(id)
                .nama(request.getNama())
                .deskripsi(request.getDeskripsi())
                .urlGambar(request.getUrlGambar())
                .build();

        when(kategoriRepository.findById(id)).thenReturn(Optional.of(existingKategori));
        when(kategoriRepository.save(any(Kategori.class))).thenReturn(updatedKategori);

        // Act
        KategoriResponse result = kategoriService.updateKategori(id, request);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Kategori", result.getNama());
        verify(kategoriRepository, times(1)).findById(id);
        verify(kategoriRepository, times(1)).save(existingKategori);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent kategori")
    void updateKategori_NotFound() {
        // Arrange
        Long id = 1L;
        KategoriRequest request = new KategoriRequest();
        when(kategoriRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> kategoriService.updateKategori(id, request));
        verify(kategoriRepository, times(1)).findById(id);
        verify(kategoriRepository, never()).save(any(Kategori.class));
    }

    @Test
    @DisplayName("Should delete kategori when found")
    void deleteKategori_Success() {
        // Arrange
        Long id = 1L;
        when(kategoriRepository.existsById(id)).thenReturn(true);

        // Act
        kategoriService.deleteKategori(id);

        // Assert
        verify(kategoriRepository, times(1)).existsById(id);
        verify(kategoriRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent kategori")
    void deleteKategori_NotFound() {
        // Arrange
        Long id = 1L;
        when(kategoriRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> kategoriService.deleteKategori(id));
        verify(kategoriRepository, times(1)).existsById(id);
        verify(kategoriRepository, never()).deleteById(anyLong());
    }
}
