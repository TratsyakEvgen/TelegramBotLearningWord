package com.tratsiak.telegram.bot.mvc.lib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class BotPath {

    private static int MAX_LENGTH = 64;

    private String path;
    private Map<String, String> parameters;

    public static BotPath parse(String path) throws NotValidPathException {
        validatePathWithParameters(path);

        String[] splitPart = path.split("\\?", 2);
        String command = splitPart[0];

        Map<String, String> parametersMap = null;
        if (splitPart.length > 1) {
            String parameters = splitPart[1];

            String[] paramsArray = parameters.split("&");
            parametersMap = Arrays.stream(paramsArray).map(p -> p.split("="))
                    .collect(Collectors.toMap(p -> p[0], v -> v[1]));
        }


        return new BotPath(command, parametersMap);


    }

    public static void validatePathWithParameters(String path) throws NotValidPathException {
        validate(path, "(/[\\p{IsAlphabetic}\\d]+)+(\\?([\\p{IsAlphabetic}\\d]+=[\\p{IsAlphabetic}\\d]+)" +
                "(&[\\p{IsAlphabetic}\\d]+=[\\p{IsAlphabetic}\\d]+)*)?");
    }

    public static void validatePath(String path) throws NotValidPathException {
        validate(path, "(/[\\p{IsAlphabetic}\\d]+)+");
    }

    private static void validate(String path, String regex) throws NotValidPathException {
        if (path == null){
            throw new NotValidPathException("Path is null");
        }
        int length = path.length();
        if (length > MAX_LENGTH) {
            throw new NotValidPathException("Path " + path +
                    " is not valid, max length should be " + MAX_LENGTH + ", current length =" + length);
        }
        boolean isValid = path.matches(regex);
        if (!isValid) {
            throw new NotValidPathException("Path " + path + " is not valid");
        }
    }


}
