package com.wikigreen.controller.json.io;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface GenericWriter<T> extends AutoCloseable{
    void write(T t) throws IOException;
    void writeList(List<T> list) throws IOException;
    void writeEmpty() throws IOException;
    void setWriter(Writer writer);
}
