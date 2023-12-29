package br.com.financialtoolapi.application.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
@UtilityClass
public class DateUtils {


    public Date convertStringToDate(final String stringDate) throws ParseException {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return simpleDateFormat.parse(stringDate);

    }

}
