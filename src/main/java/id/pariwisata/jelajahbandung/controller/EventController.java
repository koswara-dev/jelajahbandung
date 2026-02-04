package id.pariwisata.jelajahbandung.controller;

import id.pariwisata.jelajahbandung.dto.ApiResponse;
import id.pariwisata.jelajahbandung.dto.EventRequest;
import id.pariwisata.jelajahbandung.dto.EventResponse;
import id.pariwisata.jelajahbandung.dto.PagedResponse;
import id.pariwisata.jelajahbandung.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<EventResponse>>> getAllEvents(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<EventResponse> events = eventService.getAllEvents(search, page, size);
        return ResponseEntity.ok(ApiResponse.success("Events retrieved successfully", events));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable Long id) {
        EventResponse event = eventService.getEventById(id);
        return ResponseEntity.ok(ApiResponse.success("Event retrieved successfully", event));
    }

    @PostMapping(consumes = { "multipart/form-data" })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@ModelAttribute EventRequest request) {
        EventResponse event = eventService.createEvent(request);
        return ResponseEntity.ok(ApiResponse.success("Event created successfully", event));
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long id,
            @ModelAttribute EventRequest request) {
        EventResponse event = eventService.updateEvent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Event updated successfully", event));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event deleted successfully"));
    }
}
