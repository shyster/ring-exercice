package com.ring.exercice.core;

import org.apache.commons.io.FilenameUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Vladislav Kulasov on 03.02.2018.
 */
public class UrlUtils {

    public static String getNameFileFromUrl(String url) throws MalformedURLException {
        return FilenameUtils.getName(new URL(url).getPath());
    }
}
