package model.parser;

import model.dto.Schedule;

import java.io.IOException;

public interface Parser {
    //todo url -> html
    Schedule[] parse(String url, String name) throws IOException;
}