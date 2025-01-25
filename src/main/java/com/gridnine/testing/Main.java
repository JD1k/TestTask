package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.gridnine.testing.FlightBuilder.createFlights;

public class Main {
    public static void main(String[] args) {
    System.out.println(DepartureBeforeNow(createFlights()));
    System.out.println(ArrivalBeforeDeparture(createFlights()));

    }


    private static List<Flight> DepartureBeforeNow (List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .allMatch(segment -> segment.getDepartureDate().isAfter(LocalDateTime.now())))
                .collect(Collectors.toList());
    }


    private static List<Flight> ArrivalBeforeDeparture (List<Flight> flights){
        return flights.stream()
                .filter(flight -> flight.getSegments().stream()
                        .allMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate())))
                .collect(Collectors.toList());
    }

    private static List<Flight>  GroundTimeExceededFilter (List<Flight> flights) {
        final Duration twoHours = Duration.ofHours(2); // Две часа продолжительность

        return flights.stream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    Duration totalGroundTime = Duration.ZERO; // Инициализация общего времени на земле

                    // Перебор сегментов, чтобы подсчитать общее время на земле
                    for (int i = 1; i < segments.size(); i++) {
                        LocalDateTime arrivalTime = segments.get(i - 1).getArrivalDate();
                        LocalDateTime departureTime = segments.get(i).getDepartureDate();

                        // Вычисление времени на земле между прилетом одного сегмента и вылетом следующего
                        Duration groundTime = Duration.between(arrivalTime, departureTime);
                        totalGroundTime = totalGroundTime.plus(groundTime); // Суммируем время на земле
                    }

                    // Проверка: общее время на земле больше двух часов
                    return totalGroundTime.compareTo(twoHours) > 0;
                })
                .collect(Collectors.toList());
    }
}

