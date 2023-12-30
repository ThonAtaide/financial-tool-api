package br.com.financialtoolapi.application.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@Slf4j
@UtilityClass
public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public Date convertStringToDate(@NonNull final String stringDate) throws ParseException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return simpleDateFormat.parse(stringDate);
    }

    public Date getFirstDayOfMonthDate() {
        final var localDateNow = LocalDate.now();
        return Date.from(
                LocalDate
                        .of(localDateNow.getYear(), localDateNow.getMonth(), 1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
    }

}
