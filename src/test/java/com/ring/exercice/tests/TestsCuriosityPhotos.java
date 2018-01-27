package com.ring.exercice.tests;

import com.ring.exercice.core.CameraPhotosAmount;
import com.ring.exercice.core.CompareImages;
import com.ring.exercice.core.RoverCameras;
import com.ring.exercice.curiosity.photos.Photo;
import com.ring.exercice.curiosity.photos.Photos;
import com.ring.exercice.helpers.CuriosityPhotos;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.ring.exercice.core.Constants.*;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
public class TestsCuriosityPhotos {

    private static final long SOL = 1000;
    private static final int PHOTOS_LIMIT = 10;
    private static final String PHOTOS_SOL_DIR = "photos/sol";
    private static final String PHOTOS_EARTH_DATE_DIR = "photos/earthDate";
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private CuriosityPhotos curiosityPhotos = new CuriosityPhotos();
    private Photos photosSol;

    @BeforeClass
    public void getPhotosFromNasaApi() {
        //get photos information by sol
        photosSol = curiosityPhotos.getPhotosFromNasa(curiosityPhotos.getApiUrl(SOL));
    }

    @Test
    public void compareHasaPhotos() {
        //get photos information by earth date
        Photos photosEarthDate = curiosityPhotos.getPhotosFromNasa(curiosityPhotos.getApiUrl(curiosityPhotos.getEarthDateBySol(SOL)));
        List<Photo> limitedPhotos = curiosityPhotos.getLimitedPhotos(photosSol, PHOTOS_LIMIT);
        List<Photo> filteredPhotosEarthDate = curiosityPhotos.getFilteredPhotos(photosEarthDate, limitedPhotos);

        curiosityPhotos.downloadPhotos(limitedPhotos, DOWNLOAD_DIR + PHOTOS_SOL_DIR);
        curiosityPhotos.downloadPhotos(filteredPhotosEarthDate, DOWNLOAD_DIR + PHOTOS_EARTH_DATE_DIR);
        compareFilesPhotos(limitedPhotos, DOWNLOAD_DIR + PHOTOS_SOL_DIR, DOWNLOAD_DIR + PHOTOS_EARTH_DATE_DIR);

        //        limitedPhotos.get(2).setSol(222);
        Assert.assertEquals(limitedPhotos, filteredPhotosEarthDate, "Metadata is incorrect "); //validate metadata from API
    }

    @Test
    public void additionTest() {
        List<CameraPhotosAmount> cameraPhotoAmounts = new ArrayList<>();
        for (RoverCameras cameras : RoverCameras.values()) {
            long amountPhotos = curiosityPhotos.getAmountPhotosByCameras(photosSol, cameras);
            logger.info(cameras.name() + "  " + amountPhotos);
            cameraPhotoAmounts.add(new CameraPhotosAmount(cameras, amountPhotos));
        }

        //validate
        for (CameraPhotosAmount cameraPhotosAmount1 : cameraPhotoAmounts) {
            for (CameraPhotosAmount cameraPhotosAmount2 : cameraPhotoAmounts) {
                if (cameraPhotosAmount1.equals(cameraPhotosAmount2)) {
                    continue;
                }

                Assert.assertFalse(cameraPhotosAmount1.getPhotosAmount() / 10 > cameraPhotosAmount2.getPhotosAmount(),
                        cameraPhotosAmount1.getCameras().name() + " took *10 more photos than " + cameraPhotosAmount2.getCameras().name());
            }
        }
    }

    private void compareFilesPhotos(List<Photo> photos, String firstDir, String secondDir) {
        photos
                .forEach(photo -> {
                    try {
                        String fileName = curiosityPhotos.getNameFileFromUrl(photo.getImgSrc());
                        logger.info("Compare " + firstDir + "/" + fileName + " & " +
                                secondDir + "/" + fileName);
                        Assert.assertEquals(0D, CompareImages.processImage(
                                firstDir + "/" + fileName,
                                secondDir + "/" + fileName), "File is different");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
