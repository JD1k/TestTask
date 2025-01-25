package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Factory class to get sample list of flights.
 */
class FlightBuilder {
    static List<Flight> createFlights() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);//Базовая точка отсчета для создания перелетов.
        return Arrays.asList(// Создание списка перелетов
            //A normal flight with two hour duration
            createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),//Создается перелет, который вылетает через три дня и продолжительность полета составляет два часа.
            //A normal multi segment flight
            createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),//Создается много сегментный перелет, который также начинается через три дня и имеет два сегмента: первый длится два часа, а второй длится еще два часа.
                threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5)),
            //A flight departing in the past
            createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),//Этот перелет вылетел шесть дней назад и приземляется через три дня
            //A flight that departs before it arrives
            createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),//Перелет начинает вылетать, а приземляется раньше, чем вылетел.
            //A flight with more than two hours ground time
            createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),//Создает перелет с сегментом, где между двумя полетами интервал превышает два часа.
                threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)),
            //Another flight with more than two hours ground time
            createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),//Создается более сложный перелет с несколькими сегментами и более чем двухчасовым интервалом между некоторыми из них.
                threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(4),
                threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7)));
    }

    private static Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException(
                "you must pass an even number of dates");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }
}

/**
 * Bean that represents a flight.
 */
class Flight {
    private final List<Segment> segments;

    Flight(final List<Segment> segs) {
        segments = segs;
    }

    List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
            .collect(Collectors.joining(" "));
    }
}

/**
 * Bean that represents a flight segment.
 */
class Segment {
    private final LocalDateTime departureDate;

    private final LocalDateTime arrivalDate;

    Segment(final LocalDateTime dep, final LocalDateTime arr) {
        departureDate = Objects.requireNonNull(dep);
        arrivalDate = Objects.requireNonNull(arr);
    }

    LocalDateTime getDepartureDate() {
        return departureDate;
    }

    LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return '[' + departureDate.format(fmt) + '|' + arrivalDate.format(fmt)
            + ']';
    }
}