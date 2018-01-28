package com.ring.exercice.helpers;

import com.ring.exercice.curiosity.photos.Photo;
import com.ring.exercice.curiosity.photos.Photos;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
import static com.ring.exercice.core.Constants.*;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
public class PhotosHelper {
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * return filtered photos
     *
     * @param photos       Photos
     * @param photosFilter List<Photo>
     * @return List<Photo> return filtered photos
     */
    public List<Photo> getFilteredPhotos(Photos photos, List<Photo> photosFilter) {
        List<Photo> filtredPhotos = new ArrayList<>();
        for (Photo photo : photosFilter) { //get only the same photo
            for (Photo photoEarchDate : photos.getPhotos()) {
                if (photoEarchDate.getId().equals(photo.getId())) {
                    filtredPhotos.add(photoEarchDate);
                    break;
                }
            }
        }

        return filtredPhotos;
    }

    public List<Photo> getLimitedPhotos(Photos photos, long limit) {
        return photos
                .getPhotos()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public long getAmountPhotosByCameras(Photos photos, RoverCameras cameras) {
        List<Photo> filteredList = photos.getPhotos().stream()
                .filter(s -> s.getCamera().getName().equals(cameras.name()))
                .collect(Collectors.toList());
        return filteredList.stream().count();
    }

    public Photos getMetadataPhotosFromNasa(String url) {
        logger.info(url);
        return given().get(url)
                .then()
                .statusCode(SC_OK)
                .extract()
                .jsonPath()
                .getObject(".", Photos.class);
    }

    public void downloadPhotos(List<Photo> photos, String destinationDir) {
        photos
                .forEach(photo -> {
                    downloadPhoto(photo, destinationDir);
                });
    }

    private void downloadPhoto(Photo photo, String destinationDir) {
        try {
            downloadPhoto(photo.getImgSrc(), destinationDir);
        } catch (IOException e) {
            logger.error("Can't download file " + photo.getImgSrc());
            throw new RuntimeException(e);
        }
    }

    private void downloadPhoto(String url, String destinationDir) throws IOException {
        logger.info("Download file " + url);
        String fileName = getNameFileFromUrl(url);
        InputStream inputStream = given()
                .get(url)
                .then()
                .statusCode(SC_OK)
                .extract()
                .asInputStream();
        File targetFile = new File(destinationDir + "/" + fileName);
        FileUtils.copyInputStreamToFile(inputStream, targetFile);
    }

    public String getApiUrl(long solAmount, ReferenceRover rover) {
        String url = String.format(API_URL_PHOTOS_TEMPLATE, rover.getName());
        return String.format("%s?%s=%d&%s=%s", url, API_URL_SOL, solAmount, API_URL_KEY, API_USER_KEY);
    }

    public String getApiUrl(Date date, ReferenceRover rover) {
        String url = String.format(API_URL_PHOTOS_TEMPLATE, rover.getName());
        return String.format("%s?%s=%TF&%s=%s", url, API_EARTH_DATE, date, API_URL_KEY, API_USER_KEY);
    }

    public String getNameFileFromUrl(String url) throws MalformedURLException {
        return FilenameUtils.getName(new URL(url).getPath());
    }
}
