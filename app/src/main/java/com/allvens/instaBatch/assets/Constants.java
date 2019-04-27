package com.allvens.instaBatch.assets;

public enum Constants {
    // KEYS
    BOOLEAN_GET_CROP_PARAMETERS_FOR_IMAGES("_get_crop_parameters_for_images"),
    LIST_IMAGES_AND_PARAMETERS("list_images_and_parameters"),
    LIST_IMAGE_URI_PATHS("list_image_uri_paths"),
    Boolean_CROPPED_IMAGES("have_images_been_cropped"),

    // PREFS
    PREFS_NAMES("com.allvens.InstaBatch"),
    ADD_PADDING_TO_TOP_BOTTOM("add_padding_to_image"),
    DELETE_IMAGES_AFTER_CROPPING("delete_image_after_cropping"),
    PERMISSIONS_ACCEPTED("permissions_accepted"),

    // OTHER
    DEBUG_KEY("debug_key");

    String value;

    Constants(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
