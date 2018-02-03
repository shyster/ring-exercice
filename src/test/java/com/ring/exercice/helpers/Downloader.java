package com.ring.exercice.helpers;

import com.ring.exercice.core.UrlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by Vladislav Kulasov on 03.02.2018.
 */
public class Downloader implements AutoCloseable {
    private Logger logger = Logger.getLogger(this.getClass());
    private long startTimeMillis;
    private File targetFile;

    public Downloader(String url, String destinationDir) throws IOException {
        logger.info("Start download file " + url);
        String fileName = UrlUtils.getNameFileFromUrl(url);
        startTimeMillis = System.currentTimeMillis();
        InputStream inputStream = given()
                .get(url)
                .then()
                .statusCode(SC_OK)
                .extract()
                .asInputStream();
        targetFile = new File(destinationDir + "/" + fileName);
        FileUtils.copyInputStreamToFile(inputStream, targetFile);
    }

    @Override
    public void close() throws Exception {
        String log = String.format("Downloaded file '%s' for %d ms, size %d b", targetFile.getName(), System.currentTimeMillis() - startTimeMillis, targetFile.length());
        logger.info(log);
    }
}
