package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestInputDto> jsonIn;
    @Autowired
    private JacksonTester<ItemRequestAnswerDto> jsonOut;

    @Test
    void itemRequestInputDtoSerialize() throws Exception {
        ItemRequestInputDto itemRequestInputDto = new ItemRequestInputDto("itemRequestDescription");
        var result = jsonIn.write(itemRequestInputDto);
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("itemRequestDescription");
    }

    @Test
    void itemRequestAnswerDtoSerialize() throws Exception {
        ItemRequestAnswerDto itemRequestAnswerDto = new ItemRequestAnswerDto(1L,
                "itemRequestAnswerDescription", LocalDateTime.now());
        var result = jsonOut.write(itemRequestAnswerDto);
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("itemRequestAnswerDescription");
    }
}
