package com.example.bolmal.member.mock;

import com.example.bolmal.member.service.port.LocalDateTimeHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class FakeLocalDateTimeHolder implements LocalDateTimeHolder {

    LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
    LocalDateTime cutOffDays = LocalDateTime.of(2024, 12, 2, 1, 1, 1);


    @Override
    public LocalDateTime now() {
        return localDateTime;
    }

    @Override
    public LocalDateTime minusDays(long days) {
        return localDateTime.minusDays(days);
    }

    @Test
    @DisplayName("now()를 통해서 정해진 날짜를 반환 할 수 있다")
    public void fakeLocalDateTime_now_test(){
        //given
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder();

        //when
        LocalDateTime now = fakeLocalDateTimeHolder.now();

        //then
        assertThat(now).isEqualTo(localDateTime);
    }

    @Test
    @DisplayName("minusDays()를 통해서 파라미터의 날짜만큼 이전의 날짜를 반환 할 수 있다")
    public void fakeLocalDateTime_minus_test(){
        //given
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder();

        //when
        LocalDateTime cutoffdays = fakeLocalDateTimeHolder.minusDays(30);

        //then
        assertThat(cutoffdays).isEqualTo(cutOffDays);
    }

    @Test
    @DisplayName("minusDays()를 통해서 파라미터의 정해진 날짜가 아닌 날짜를 반환하면 null이 아닌 오류를 반환한다")
    public void fakeLocalDateTime_minus_test_valid(){
        //given
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder();

        //when
        LocalDateTime cutoffdays = fakeLocalDateTimeHolder.minusDays(31);

        //then
        assertThatThrownBy(()->assertThat(cutoffdays).isEqualTo(cutOffDays))
                .isInstanceOf(AssertionFailedError.class);
    }
}
