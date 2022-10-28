package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingInputDto> jsonIn;
    @Autowired
    private JacksonTester<BookingOutputDto> jsonOut;

    @Test
    void bookingInputDtoSerialize() throws Exception {
        BookingInputDto bookingInputDto = new BookingInputDto(LocalDateTime.now()
                .plusDays(1), LocalDateTime.now().plusDays(2), 1L);
        var result = jsonIn.write(bookingInputDto);
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.itemId");
        assertThat(result).extractingJsonPathStringValue("$.start")
                .startsWith(bookingInputDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .startsWith(bookingInputDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        assertThat(result).extractingJsonPathValue("$.itemId").isEqualTo(1);
    }

    @Test
    void bookingOutputDtoSerialize() throws Exception {
        BookingOutputDto bookingOutputDto = new BookingOutputDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), null, null, BookingStatus.WAITING);
        var result = jsonOut.write(bookingOutputDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .startsWith(bookingOutputDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .startsWith(bookingOutputDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
    }
}
