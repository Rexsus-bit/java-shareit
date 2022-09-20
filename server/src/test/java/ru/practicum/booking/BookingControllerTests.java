package ru.practicum.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.common.Mapper;
import ru.practicum.item.Item;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTests {


    @MockBean
    private BookingService bookingService;

    @MockBean
    private Mapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    LocalDateTime startTime = LocalDateTime.now().plusMinutes(5).truncatedTo(ChronoUnit.SECONDS);
    LocalDateTime endTime = LocalDateTime.now().plusMinutes(10).truncatedTo(ChronoUnit.SECONDS);
    Booking booking = Booking.builder()
            .id(1L)
            .start(startTime)
            .end(endTime)
            .item(new Item())
            .status(Status.APPROVED)
            .build();
    BookingDTO bookingDTO = BookingDTO.builder()
            .id(1L)
            .start(startTime)
            .end(endTime)
            .itemId(1L)
            .bookerId(2L)
            .available(true)
            .build();

    @Test
    public void shouldCreateBookingTest() throws Exception {
        when(mapper.toBookingDto(any()))
                .thenReturn(bookingDTO);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDTO.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDTO.getStart().toString()), String.class))
                .andExpect(jsonPath("$.end", is(bookingDTO.getEnd().toString()), String.class));
    }

    @Test
    public void shouldConfirmBookingTest() throws Exception {
        when(bookingService.confirmBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().toString()), String.class))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString()), String.class));
    }

    @Test
    public void shouldGetBookingByIdTest() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(booking);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().toString()), String.class))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString()), String.class));
    }

    @Test
    public void shouldGetAllBookingsTest() throws Exception {

        when(bookingService.getAllBookings(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(List.of(booking));
        mvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "5")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(booking.getStart().toString()), String.class))
                .andExpect(jsonPath("$.[0].end", is(booking.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.[0].status", is(booking.getStatus().toString()), String.class));
    }

    @Test
    public void shouldGetAllByOwnerId() throws Exception {
        when(bookingService.getAllOwnerBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "5")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(booking.getStart().toString()), String.class))
                .andExpect(jsonPath("$.[0].end", is(booking.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.[0].status", is(booking.getStatus().toString()), String.class));
    }

}
