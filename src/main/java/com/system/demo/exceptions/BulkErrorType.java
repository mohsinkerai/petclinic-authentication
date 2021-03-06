package com.system.demo.exceptions;

public enum BulkErrorType {
    CNIC_ALREADY_RESGISTERED(1,"Volunteer with this NIC already registered"),
    VOLUNTEER_VALIDATION_ERROR(2,"INVALID TYPE, ValidationError"),
    UNABLE_TO_CONNECT(5, "Unable to create Connection to Database"),
    INVALID_CNIC(6,"CNIC is Invalid for user"),
    VALIDATION_INVALID_AGE(7,"Age type is Invalid"),
    VALIDATION_INVALID_MOBILE(8,"Mobile Phone Number is Invalid"),
    VALIADTION_INVALID_RESIDENCE(9,"Residential Address cannot be null"),
    VALIDATION_INVALID_HOMEPHONE(10,"Invalid Home Phone Number"),
    VALIDATION_INVALID_EMAIL(10,"Invalid Email Addres"),
    IMAGE_NOT_FOUND(11,"Image not found for this Volunteer"),
    INVALID_FILE_FORMAT(12,"Invalid File found,Need csv in ZIP"),
    VALIDATION_INVALID_NAME(13,"Empty Name"),
    INVALID_LOCAL_COUNCIL(14,"Local Council Cannot be null"),
    INVALID_ZONE(15,"Invalid Zone"),
    INVALID_SITE(16,"Invalid Site"),
    INVALID_COMMITTEE(17,"Invalid Committee"),
    INVALID_DAY(17,"Invalid Day"),
    INVALID_SHIFT(17,"Invalid SHIFT");

    private int code;
    private String errorMessage;

    BulkErrorType(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getAppCode() {
        return "UPLOAD_ERROR" + String.format("%04d", code);
    }

    public String getAppMessage() {
        return errorMessage;
    }

}
