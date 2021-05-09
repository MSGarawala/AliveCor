package com.alivecorcodingassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alivecorcodingassignment.data.User
import com.alivecorcodingassignment.data.UserValidationStatus

/**
 * @author Mukesh Sharma
 * Shared view model for UserProfileActivity
 */
class UserProfileViewModel : ViewModel() {

    //This will hold the created user profile data
    var userLiveData: MutableLiveData<User> = MutableLiveData()

    //This is used to validate the user
    private var userValidationStatusLiveData: MutableLiveData<UserValidationStatus> =
        MutableLiveData()

    fun observeUserData(): LiveData<User> {
        return userLiveData
    }

    fun observeUserValidationStatusLiveData(): LiveData<UserValidationStatus> {
        return userValidationStatusLiveData
    }

    private fun setUserData(user: User) {
        userLiveData.value = user

    }

    fun validateUserData(user: User) {
        var userValidationStatus = UserValidationStatus()

        if (user.firstName.isNullOrEmpty()) {
            userValidationStatus.isFirstNameInvalid = true
            userValidationStatus.isSuccessfullyValidated = false
        }
        if (user.lastName.isNullOrEmpty()) {
            userValidationStatus.isLastNameInvalid = true
            userValidationStatus.isSuccessfullyValidated = false
        }

        if (user.dob.isNullOrEmpty()) {
            userValidationStatus.isDobInvalid = true
            userValidationStatus.isSuccessfullyValidated = false
        }

        //if all validation are success, then set the user in shared ViewModel so that it can be used in other places
        if (userValidationStatus.isSuccessfullyValidated) {
            setUserData(user)
        }
        userValidationStatusLiveData.value = userValidationStatus
    }

}