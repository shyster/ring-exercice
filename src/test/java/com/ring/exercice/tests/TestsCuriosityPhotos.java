package com.ring.exercice.tests;

import com.ring.exercice.helpers.CameraPhotosAmount;
import com.ring.exercice.core.CompareImages;
import com.ring.exercice.helpers.RoverCameras;
import com.ring.exercice.curiosity.photos.Photo;
import com.ring.exercice.curiosity.photos.Photos;
import com.ring.exercice.helpers.CuriosityPhotos;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static com.ring.exercice.core.Constants.*;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
public class TestsCuriosityPhotos {

    private static final long SOL = 1000;
    private static final int PHOTOS_LIMIT = 10;
    private static final String PHOTOS_SOL_DIR = DOWNLOAD_DIR + "photos/sol";
    private static final String PHOTOS_EARTH_DATE_DIR = DOWNLOAD_DIR + "photos/earthDate";
    private Logger logger = Logger.getLogger(this.getClass());
    private CuriosityPhotos curiosityPhotos = new CuriosityPhotos();
    List<Photo> limitedPhotos, filteredPhotosEarthDate;
    private Photos photosSol;

    @BeforeClass
    public void getPhotosFromNasaApi() {
        //get photos information by sol
        photosSol = curiosityPhotos.getPhotosFromNasa(curiosityPhotos.getApiUrl(SOL));
        //get photos information by earth date
        Photos photosEarthDate = curiosityPhotos.getPhotosFromNasa(curiosityPhotos.getApiUrl(curiosityPhotos.getEarthDateBySol(SOL)));
        limitedPhotos = curiosityPhotos.getLimitedPhotos(photosSol, PHOTOS_LIMIT);
        filteredPhotosEarthDate = curiosityPhotos.getFilteredPhotos(photosEarthDate, limitedPhotos);
    }

    @Test
    public void testCompareHasaPhotos() {
        curiosityPhotos.downloadPhotos(limitedPhotos, PHOTOS_SOL_DIR);
        curiosityPhotos.downloadPhotos(filteredPhotosEarthDate, PHOTOS_EARTH_DATE_DIR);
        compareFilesPhotos(limitedPhotos, PHOTOS_SOL_DIR, PHOTOS_EARTH_DATE_DIR);
    }

    @Test
    public void testMetadataPhotos() {
        Assert.assertEquals(filteredPhotosEarthDate, limitedPhotos, "Metadata is incorrect "); //validate metadata from API
    }

    @Test
    public void testAddition() {
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
                        Assert.assertEquals(CompareImages.processImage(
                                firstDir + "/" + fileName,
                                secondDir + "/" + fileName), 0D, "Files " + fileName + " are different");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
