package com.tratsiak.telegram.bot.mvc.lib.core.path.impl;

import com.tratsiak.telegram.bot.mvc.lib.core.path.PathParser;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultPathParser implements PathParser {
    @Override
    public String getPath(String path) {
        return path.split("\\?", 2)[0];
    }

    @Override
    public Map<String, String> getParam(String path) {
        String[] splitPart = path.split("\\?", 2);
        Map<String, String> parametersMap = null;

        if (splitPart.length > 1) {
            String parameters = splitPart[1];

            String[] paramsArray = parameters.split("&");
            parametersMap = Arrays.stream(paramsArray).map(p -> p.split("="))
                    .collect(Collectors.toMap(p -> p[0], v -> v[1]));
        }
        return parametersMap;
    }
}
