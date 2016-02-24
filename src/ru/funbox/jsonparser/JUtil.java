package ru.funbox.jsonparser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dina Efremova
 * Created on 17.02.16.
 */
public class JUtil {

    private JUtil() {}

    public static String getAsString(Map<String, Object> map, String prefix) {
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(map.toString());
        sb.append("\n{\n");
        Set<String> keys = map.keySet();
        int k = 0;
        for (String key : keys) {
            sb.append(prefix).append("\t'").append(key).append("': ");
            Object value = map.get(key);
            if (value == null) {
                sb.append("NULL");
            } else if (value instanceof String) {
                sb.append("'").append(value).append("'");
            } else if (value instanceof Boolean) {
                sb.append(String.valueOf(value).toUpperCase());
            } else if (value instanceof Map) {
                Map<String, Object> mapValue = (Map<String, Object>) value;
                sb.append(JUtil.getAsString(mapValue, prefix + "\t"));
            } else if (value instanceof List) {
                List<Object> listValue = (List<Object>) value;
                sb.append(JUtil.getAsString(listValue, prefix + "\t"));
            } else {
                sb.append(value);
            }

            if (++k < keys.size()) {
                sb.append(", \n");
            } else {
                sb.append("\n");
            }
        }

        sb.append(prefix).append("}");
        return sb.toString();
    }

    public static String getAsString(List<Object> list, String prefix) {
        if (list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(list.toString());
        sb.append("\n[\n");
        int k = 0;
        for (Object value : list) {
            if (value == null) {
                sb.append(prefix).append("\tNULL");
            } else if (value instanceof String) {
                sb.append(prefix).append("\t'").append(value).append("'");
            } else if (value instanceof Boolean) {
                sb.append(prefix).append("\t").append(String.valueOf(value).toUpperCase());
            } else if (value instanceof Map) {
                Map<String, Object> mapValue = (Map<String, Object>) value;
                sb.append(prefix).append("\t").append(JUtil.getAsString(mapValue, prefix + "\t"));
            } else if (value instanceof List) {
                List<Object> listValue = (List<Object>) value;
                sb.append(prefix).append("\t").append(JUtil.getAsString(listValue, prefix + "\t"));
            } else {
                sb.append(prefix).append("\t").append(value);
            }

            if (++k < list.size()) {
                sb.append(", \n");
            } else {
                sb.append("\n");
            }
        }

        sb.append(prefix).append("]");
        return sb.toString();
    }
}
