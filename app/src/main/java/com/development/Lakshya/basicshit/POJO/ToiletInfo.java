package com.development.Lakshya.basicshit.POJO;
public class ToiletInfo {

    String name, address, placeType, cost;
    float overallRatingNumStars,smellRatingNumStars,hygieneRatingNumStars;
    boolean towelSwitchSelected,soapSwitchSelected, jetSwitchSelected, mirrorSwitchSelected;
    boolean dustbinSwitchSelected, toiletPaperSwitchSelected, waterSwitchSelected, changingStationSwitchSelected;
    boolean childFriendlyCheckbox,maleCheckboxSelected, femaleCheckboxSelected, disabledCheckboxSelected;

    public ToiletInfo(String name, String address, String placeType, String cost,
                      float overallRatingNumStars, float smellRatingNumStars, float hygieneRatingNumStars,
                      boolean towelSwitchSelected, boolean soapSwitchSelected, boolean jetSwitchSelected, boolean mirrorSwitchSelected,
                      boolean dustbinSwitchSelected, boolean toiletPaperSwitchSelected, boolean waterSwitchSelected,
                      boolean changingStationSwitchSelected, boolean childFriendlyCheckbox, boolean maleCheckboxSelected,
                      boolean femaleCheckboxSelected, boolean disabledCheckboxSelected) {

        this.name = name;
        this.address = address;
        this.placeType = placeType;
        this.cost = cost;
        this.overallRatingNumStars = overallRatingNumStars;
        this.smellRatingNumStars = smellRatingNumStars;
        this.hygieneRatingNumStars = hygieneRatingNumStars;
        this.towelSwitchSelected = towelSwitchSelected;
        this.soapSwitchSelected = soapSwitchSelected;
        this.jetSwitchSelected = jetSwitchSelected;
        this.mirrorSwitchSelected = mirrorSwitchSelected;
        this.dustbinSwitchSelected = dustbinSwitchSelected;
        this.toiletPaperSwitchSelected = toiletPaperSwitchSelected;
        this.waterSwitchSelected = waterSwitchSelected;
        this.changingStationSwitchSelected = changingStationSwitchSelected;
        this.childFriendlyCheckbox = childFriendlyCheckbox;
        this.maleCheckboxSelected = maleCheckboxSelected;
        this.femaleCheckboxSelected = femaleCheckboxSelected;
        this.disabledCheckboxSelected = disabledCheckboxSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public float getOverallRatingNumStars() {
        return overallRatingNumStars;
    }

    public void setOverallRatingNumStars(float overallRatingNumStars) {
        this.overallRatingNumStars = overallRatingNumStars;
    }

    public float getSmellRatingNumStars() {
        return smellRatingNumStars;
    }

    public void setSmellRatingNumStars(float smellRatingNumStars) {
        this.smellRatingNumStars = smellRatingNumStars;
    }

    public float getHygieneRatingNumStars() {
        return hygieneRatingNumStars;
    }

    public void setHygieneRatingNumStars(float hygieneRatingNumStars) {
        this.hygieneRatingNumStars = hygieneRatingNumStars;
    }

    public boolean isTowelSwitchSelected() {
        return towelSwitchSelected;
    }

    public void setTowelSwitchSelected(boolean towelSwitchSelected) {
        this.towelSwitchSelected = towelSwitchSelected;
    }

    public boolean isSoapSwitchSelected() {
        return soapSwitchSelected;
    }

    public void setSoapSwitchSelected(boolean soapSwitchSelected) {
        this.soapSwitchSelected = soapSwitchSelected;
    }

    public boolean isJetSwitchSelected() {
        return jetSwitchSelected;
    }

    public void setJetSwitchSelected(boolean jetSwitchSelected) {
        this.jetSwitchSelected = jetSwitchSelected;
    }

    public boolean isMirrorSwitchSelected() {
        return mirrorSwitchSelected;
    }

    public void setMirrorSwitchSelected(boolean mirrorSwitchSelected) {
        this.mirrorSwitchSelected = mirrorSwitchSelected;
    }

    public boolean isDustbinSwitchSelected() {
        return dustbinSwitchSelected;
    }

    public void setDustbinSwitchSelected(boolean dustbinSwitchSelected) {
        this.dustbinSwitchSelected = dustbinSwitchSelected;
    }

    public boolean isToiletPaperSwitchSelected() {
        return toiletPaperSwitchSelected;
    }

    public void setToiletPaperSwitchSelected(boolean toiletPaperSwitchSelected) {
        this.toiletPaperSwitchSelected = toiletPaperSwitchSelected;
    }

    public boolean isWaterSwitchSelected() {
        return waterSwitchSelected;
    }

    public void setWaterSwitchSelected(boolean waterSwitchSelected) {
        this.waterSwitchSelected = waterSwitchSelected;
    }

    public boolean isChangingStationSwitchSelected() {
        return changingStationSwitchSelected;
    }

    public void setChangingStationSwitchSelected(boolean changingStationSwitchSelected) {
        this.changingStationSwitchSelected = changingStationSwitchSelected;
    }

    public boolean isChildFriendlyCheckbox() {
        return childFriendlyCheckbox;
    }

    public void setChildFriendlyCheckbox(boolean childFriendlyCheckbox) {
        this.childFriendlyCheckbox = childFriendlyCheckbox;
    }

    public boolean isMaleCheckboxSelected() {
        return maleCheckboxSelected;
    }

    public void setMaleCheckboxSelected(boolean maleCheckboxSelected) {
        this.maleCheckboxSelected = maleCheckboxSelected;
    }

    public boolean isFemaleCheckboxSelected() {
        return femaleCheckboxSelected;
    }

    public void setFemaleCheckboxSelected(boolean femaleCheckboxSelected) {
        this.femaleCheckboxSelected = femaleCheckboxSelected;
    }

    public boolean isDisabledCheckboxSelected() {
        return disabledCheckboxSelected;
    }

    public void setDisabledCheckboxSelected(boolean disabledCheckboxSelected) {
        this.disabledCheckboxSelected = disabledCheckboxSelected;
    }
}
