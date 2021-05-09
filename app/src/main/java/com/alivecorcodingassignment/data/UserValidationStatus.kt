package com.alivecorcodingassignment.data

/**
 * Class to validate the User data
 */
class UserValidationStatus {
    //This  will be set to false if any of the validation gets failed
    var isSuccessfullyValidated = true

    var isFirstNameInvalid = false
    var firstNameInvalidMessage = "First Name can not be empty"

    var isLastNameInvalid = false
    var lastNameInvalidMessage = "Last name can not be empty"

    var isDobInvalid = false
    var dobInvalidMessage = "Please select date"
}