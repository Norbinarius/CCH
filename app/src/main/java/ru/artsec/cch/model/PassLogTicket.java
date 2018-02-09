package ru.artsec.cch.model;

/**
 * Created by Norbinarius on 31.01.2018.
 */

public class PassLogTicket {

    private String doorID;
    private String doorName;
    private String placeID;
    private String placeName;
    private String passTime;
    private String isEnter;

    public String getDoorID() {
        return doorID;
    }

    public void setDoorID(String doorID) {
        this.doorID = doorID;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getIsEnter() {
        return isEnter;
    }

    public void setIsEnter(String isEnter) {
        this.isEnter = isEnter;
    }
}