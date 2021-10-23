package com.collab.project.util;

import flexjson.JSONSerializer;

public class JsonUtils {

    public static String deepSerialize(Object o) {
        JSONSerializer ser = (new JSONSerializer()).transform(new ExcludeNullTransformer(), new Class[]{Void.TYPE});
        ser.exclude(new String[]{"*.class"});
        ser.prettyPrint(false);
        return ser.deepSerialize(o);
    }

    public static String toJson(Object object) {
        JSONSerializer ser = (new JSONSerializer()).transform(new ExcludeNullTransformer(), new Class[]{Void.TYPE});
        ser.exclude(new String[]{"*.class"});
        return ser.serialize(object);
    }
}
