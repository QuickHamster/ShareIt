package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    private BookingInputDto bookingInputDto;
    private BookingOutputDto bookingOutputDto;
    private List<Booking> bookingList;

    private static final String X_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void BeforeEach() {
        bookingInputDto = new BookingInputDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 1L);
        bookingOutputDto = new BookingOutputDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), null, null, BookingStatus.WAITING);
        bookingList = new ArrayList<>();
    }

    @Test
    void addBooking() throws Exception {
        doReturn(bookingOutputDto).when(bookingService).add(anyLong(), any());

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", startsWith(bookingOutputDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))), String.class))
                .andExpect(jsonPath("$.end", startsWith(bookingOutputDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))), String.class));
    }

    @Test
    void approvedBooking() throws Exception {
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingOutputDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(X_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserBookings() throws Exception {
        when(bookingService.getUserBookings(anyInt(),any(BookingState.class),anyInt(),anyInt()))
                .thenReturn(bookingList);

        mockMvc.perform(get("/bookings?state=ALL")
                        .header(X_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsForAllItemsUser() throws Exception {
        when(bookingService.getBookingsForAllItemsUser(anyInt(),any(BookingState.class),anyInt(),anyInt()))
                .thenReturn(bookingList);

        mockMvc.perform(get("/bookings/owner")
                        .header(X_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
