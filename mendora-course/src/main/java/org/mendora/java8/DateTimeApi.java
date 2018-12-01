package org.mendora.java8;

import java.time.*;

public class DateTimeApi {
    public static void main(String[] args) {
        // 凌晨
        final LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        System.out.println(startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());



    }
}
