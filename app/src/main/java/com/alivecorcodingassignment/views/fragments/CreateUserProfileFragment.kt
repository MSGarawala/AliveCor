package com.alivecorcodingassignment.views.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alivecorcodingassignment.R
import com.alivecorcodingassignment.data.User
import com.alivecorcodingassignment.data.UserValidationStatus
import com.alivecorcodingassignment.databinding.FragmentCreateUserProfileBinding
import com.alivecorcodingassignment.viewmodel.UserProfileViewModel
import java.util.Calendar

/**
 * @author : Mukesh Sharma
 *
 * Fragment to create user profile
 */
class CreateUserProfileFragment : Fragment() {


    //Shared view model instance form the activity
    private val profileViewModel: UserProfileViewModel by activityViewModels()

    //Calculated age will be stored in this
    private var userDobFormatted = ""


    private var _binding: FragmentCreateUserProfileBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_next).setOnClickListener {
            observersUserValidation()
            validateUserData()

        }

        view.findViewById<EditText>(R.id.et_dob).setOnClickListener {
            hideSoftKeyboardIfOpen()
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)


        val dpd = context?.let {
            DatePickerDialog(
                it,
                { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormat = DateFormat.getDateFormat(context)
                    binding.etDob.setText(dateFormat.format(cal.time))
                    binding.etDob.error = null
                    calculateAge(cal)
                },
                year,
                month,
                day
            )
        }
        dpd?.datePicker?.maxDate = System.currentTimeMillis()
        dpd?.show()
    }

    var validationStatusObserver = Observer<UserValidationStatus> { validationStatus ->
        if (validationStatus.isSuccessfullyValidated) {
            findNavController().navigate(R.id.infoUserProfileFragment)
            removeObserver()
        } else {
            if (validationStatus.isFirstNameInvalid) {
                binding.etFirstName.error = validationStatus.firstNameInvalidMessage
            }
            if (validationStatus.isLastNameInvalid) {
                binding.etLastName.error = validationStatus.lastNameInvalidMessage
            }
            if (validationStatus.isDobInvalid) {
                binding.etDob.error = validationStatus.dobInvalidMessage
            }
        }
    }

    private fun observersUserValidation() {
        profileViewModel.observeUserValidationStatusLiveData()
            .observe(viewLifecycleOwner, validationStatusObserver)
    }

    private fun removeObserver() {
        profileViewModel.observeUserValidationStatusLiveData()
            .removeObserver(validationStatusObserver)
    }

    private fun validateUserData() {
        var user = User(
            firstName = binding.etFirstName.text.toString(),
            lastName = binding.etLastName.text.toString(),
            dob = userDobFormatted
        )
        profileViewModel.validateUserData(user)
    }

    private fun hideSoftKeyboardIfOpen() {
        val inputMethodManager: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun calculateAge(dob: Calendar) {
        val today = Calendar.getInstance()
        val years = (today.timeInMillis) - (dob.timeInMillis)

        today.timeInMillis = years

        var month = today.get(Calendar.MONTH) - 1;
        if (month <= 0) {
            month = today.get(Calendar.MONTH)
        }
        var day = today.get(Calendar.DAY_OF_MONTH)
        userDobFormatted = "${today.get(Calendar.YEAR) - 1970} years, $month months, $day days"

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
