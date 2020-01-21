package com.softserve.academy.event.util;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CsvUtils {

    private static final CsvMapper mapper = new CsvMapper();
    public static final CsvSchema CONTACT_WITH_HEADER_SCHEMA = CsvSchema.builder()
            .setUseHeader(true)
            .addColumn("email")
            .addColumn("name")
            .build();

    public static <T> List<T> read(Class<T> clazz, CsvSchema schema, InputStream stream) throws IOException {
        return mapper.reader(schema).forType(clazz).<T>readValues(stream).readAll();
    }

    private CsvUtils() {
    }

}
