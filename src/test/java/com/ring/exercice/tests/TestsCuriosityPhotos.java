package com.ring.exercice.tests;

import com.ring.exercice.core.UrlUtils;
import com.ring.exercice.helpers.*;
import com.ring.exercice.core.CompareImages;
import com.ring.exercice.curiosity.photos.Photo;
import com.ring.exercice.curiosity.photos.Photos;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static com.ring.exercice.core.Constants.*;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
public class TestsCuriosityPhotos {
    private static final ReferenceRover rover = ReferenceRover.CURIOSITY;
    private static final String PHOTOS_SOL_DIR = DOWNLOAD_DIR + "photos/sol";
    private static final String PHOTOS_EARTH_DATE_DIR = DOWNLOAD_DIR + "photos/earthDate";
    private Logger logger = Logger.getLogger(this.getClass());
    private PhotosHelper photosHelper = new PhotosHelper();
    private RestClient photoDowloader = new RestClient();
    List<Photo> limitedPhotos, filteredPhotosEarthDate;
    private Photos photosSol;

    @Parameters({"1000", "10"})
    @BeforeClass
    public void getPhotosFromNasaApi(@Optional("1000") long sol, @Optional("10") long photosLimit) {
        //get photos information by sol
        photosSol = photoDowloader.getMetadataPhotosFromNasa(photoDowloader.getApiUrl(sol, rover));
        //get photos information by earth date
        Photos photosEarthDate = photoDowloader.getMetadataPhotosFromNasa(photoDowloader.getApiUrl(DatePlanetCalculator.getCuriosityEarthDateBySol(sol), rover));
        limitedPhotos = photosHelper.getLimitedPhotos(photosSol, photosLimit);
        filteredPhotosEarthDate = photosHelper.getFilteredPhotos(photosEarthDate, limitedPhotos);
    }

    @Test
    public void testCompareHasaPhotos() {
        photoDowloader.downloadPhotos(limitedPhotos, PHOTOS_SOL_DIR);
        photoDowloader.downloadPhotos(filteredPhotosEarthDate, PHOTOS_EARTH_DATE_DIR);
        compareFilesPhotos(limitedPhotos, PHOTOS_SOL_DIR, PHOTOS_EARTH_DATE_DIR);
        Assert.assertEquals(filteredPhotosEarthDate, limitedPhotos, "Metadata is incorrect "); //validate metadata from API
    }

    @Test
    public void testCompareHasaPhotosByMd5() {
        compareFilesPhotosByMd5(limitedPhotos, filteredPhotosEarthDate);
    }

    @Test
    public void testAddition() {
        List<CameraPhotosAmount> cameraPhotoAmounts = new ArrayList<>();
        for (RoverCameras cameras : RoverCameras.values()) {
            long amountPhotos = photosHelper.getAmountPhotosByCameras(photosSol, cameras);
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

    private void compareFilesPhotosByMd5(List<Photo> photos1, List<Photo> photos2){
        photos1
                .forEach(photo -> {
                    try {
                        String md5HexPhoto1 = photoDowloader.getMd5HexPhoto(photo.getImgSrc());
                        String md5HexPhoto2;
                        try {
                            md5HexPhoto2 = photoDowloader.getMd5HexPhoto(photos2.get(photos2.indexOf(photo)).getImgSrc());
                        } catch (ArrayIndexOutOfBoundsException e){
                            logger.info(UrlUtils.getNameFileFromUrl(photo.getImgSrc()) +  " not found in " + photos2);
                            throw new FileNotFoundException(photo.getImgSrc());
                        }

                        String log = String.format("compare: %s photo1 md5 = %s & photo2 md5 = %s",
                                UrlUtils.getNameFileFromUrl(photo.getImgSrc()), md5HexPhoto1, md5HexPhoto2);

                        logger.info(log);

                        Assert.assertEquals(md5HexPhoto1, md5HexPhoto2, "Files are not equal " + UrlUtils.getNameFileFromUrl(photo.getImgSrc()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
    }

    private void compareFilesPhotos(List<Photo> photos, String firstDir, String secondDir) {
        photos
                .forEach(photo -> {
                    try {
                        String fileName = UrlUtils.getNameFileFromUrl(photo.getImgSrc());
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
