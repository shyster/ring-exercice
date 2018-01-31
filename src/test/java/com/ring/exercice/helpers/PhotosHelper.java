package com.ring.exercice.helpers;

import com.ring.exercice.curiosity.photos.Photo;
import com.ring.exercice.curiosity.photos.Photos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
public class PhotosHelper {

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

}
