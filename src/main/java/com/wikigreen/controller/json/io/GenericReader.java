package com.wikigreen.controller.json.io;

import java.io.Reader;

public interface GenericReader<T> extends AutoCloseable {
    T read();
    void setReader(Reader reader);
}
