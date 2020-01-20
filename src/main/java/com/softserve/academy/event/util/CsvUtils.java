package com.softserve.academy.event.util;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CsvUtils {

    private static final CsvMapper mapper = new CsvMapper();

    public static <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz)
                .withHeader()
                .withColumnReordering(true);
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        return reader.<T>readValues(stream).readAll();
    }

    public static byte[] write(Object object) throws IOException {
        return mapper.writer(
                mapper.schema()
                        .withHeader()
                        .withColumnReordering(true))
                .writeValueAsBytes(object);
    }

    private CsvUtils() {
    }

}
