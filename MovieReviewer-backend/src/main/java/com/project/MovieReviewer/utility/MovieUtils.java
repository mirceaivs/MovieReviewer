package com.project.MovieReviewer.utility;

public class MovieUtils {

    public static Integer parseIntOrNull(String value) {
        try { return value == null ? null : Integer.parseInt(value.replaceAll("[^\\d]", "")); }
        catch (Exception e) { return null; }
    }
    public static Double parseDoubleOrNull(String value) {
        try { return value == null ? null : Double.parseDouble(value.replaceAll(",", ".")); }
        catch (Exception e) { return null; }
    }
    public static Long parseLongOrNull(String value) {
        try { return value == null ? null : Long.parseLong(value.replaceAll("[^\\d]", "")); }
        catch (Exception e) { return null; }
    }


}
