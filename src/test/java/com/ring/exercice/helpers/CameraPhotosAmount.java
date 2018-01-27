package com.ring.exercice.helpers;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
public class CameraPhotosAmount {
    private RoverCameras cameras;
    private long photosAmount;

    public CameraPhotosAmount(RoverCameras cameras, long photosAmount) {
        this.cameras = cameras;
        this.photosAmount = photosAmount;
    }

    public RoverCameras getCameras() {
        return cameras;
    }

    public long getPhotosAmount() {
        return photosAmount;
    }
}
