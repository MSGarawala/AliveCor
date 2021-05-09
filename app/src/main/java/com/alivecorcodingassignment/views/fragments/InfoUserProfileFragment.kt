package com.alivecorcodingassignment.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alivecorcodingassignment.databinding.FragmentInfoUserProfileBinding
import com.alivecorcodingassignment.viewmodel.UserProfileViewModel

/**
 * @author Mukesh Sharma
 * Fragment class to display the user profile
 */
class InfoUserProfileFragment : Fragment() {

    private var _binding: FragmentInfoUserProfileBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    //Shared view model instance form the activity
    private val profileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoUserProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MUkesh","onViewCreated called")
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        observersUserInfo()
    }

    private fun observersUserInfo() {
        profileViewModel.observeUserData().observe(viewLifecycleOwner, Observer { userInfo ->
            binding.tvFirstName.text = userInfo.firstName
            binding.tvLastName.text = userInfo.lastName
            binding.tvDob.text = userInfo.dob
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}