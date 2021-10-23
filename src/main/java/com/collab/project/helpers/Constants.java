package com.collab.project.helpers;

import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final long FALLBACK_ID = (long)0;
    public static final Integer THUMBNAIL_WIDTH = 600;
    public static final Integer THUMBNAIL_HEIGHT = 750;
    public static final Set<String> ALLOWED_IMAGE_FORMAT = new HashSet<String>() {{
        add("jpeg");
        add("jpg");
        add("gif");
        add("png");
        add("bmp");
    }};

    public static final Set<String> ALLOWED_VIDEO_FORMAT = new HashSet<String>() {{
        add("mp4");
        add("avi");
        add("webm");
        add("mpeg");
        add("mpg");
        add("mov");
        add("flv");
        add("3gp");
        add("wmv");
        add("f4v");
    }};

    public static final Set<String> ALLOWED_AUDIO_FORMAT = new HashSet<String>() {{
        add("mp4");
        add("mp3");
        add("wma");
        add("wav");
        add("acc");
        add("m4a");
        add("flac");
    }};

    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String IMAGE = "image";

    public static final String REF_ID = "refId";
    public static final String CURRENT_TIME = "timestamp";


    public static final String API_NAME ="apiName" ;
    public static final String ALLCONTROLLERS = "allControllers())";

    public static final String POINTCUT ="execution(* com.collab..controller*..*(..))";
    public static final  String EXCEPTION_POINT = "@within(com.collab.project.helpers.Monitor)";

}
