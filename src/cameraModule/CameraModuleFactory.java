package cameraModule;

import org.openbw.bwapi4j.BW;
import org.openbw.bwapi4j.TilePosition;

public class CameraModuleFactory {
    public static CameraModule createCameraModule(TilePosition startPos, BW game){
        return new CameraModuleImpl(startPos, game);
    }
}
