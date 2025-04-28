package com.example.nutrikart.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nutrikart.R
import com.example.nutrikart.Utils
import com.example.nutrikart.activity.UsersMainActivity
import com.example.nutrikart.databinding.FragmentOTPBinding
import com.example.nutrikart.models.Users
import com.example.nutrikart.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import kotlin.text.clear


class OTPFragment : Fragment() {

    private val viewModel : AuthViewModel by viewModels()
    private lateinit var binding : FragmentOTPBinding
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOTPBinding.inflate(layoutInflater)

        getUserNumber()
        customizingEnteringOTP()
        sendOTP()
        onLoginButtonClicked()
        onBackButtonClicked()
        setStatusBarColor()

        return binding.root
    }

    private fun onLoginButtonClicked() {
        binding.btnLogin.setOnClickListener{
            Utils.showDialog(requireContext(), message = "signing you...")
            val editTexts = arrayOf(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.etOtp5,binding.etOtp6)
            val otp = editTexts.joinToString("") { it.text.toString() }

            if (otp.length < editTexts.size){
                Utils.showToast(requireContext(), message = "Please enter right otp")
            }else{
                editTexts.forEach { it.text?.clear(); it.clearFocus() }
                verifyOtp(otp)
            }
        }
    }

//    private fun verifyOtp(otp: String) {
//        val user = Users(uid = Utils.getCurrentUserId(), userPhoneNumber = userNumber , userAddress = " ")
//        viewModel.signInWithPhoneAuthCredential(otp, userNumber , user)
//        lifecycleScope.launch {
//            viewModel.isSignedInSuccessfully.collect{
//                if(it){
//                    Utils.hideDialog()
//                    Utils.showToast(requireContext(), "Logged In...")
//                    startActivity(Intent(requireActivity() , UsersMainActivity::class.java))
//                    requireActivity().finish()
//                }
//            }
//        }
//
//    }

    private fun verifyOtp(otp: String) {

        val user = Users(uid = null, userPhoneNumber = userNumber , userAddress = " " )

        viewModel.signInWithPhoneAuthCredential(otp, userNumber, user)

        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect{
                if (it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Logged In...")
                    startActivity(Intent(requireActivity(), UsersMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP...")
        viewModel.apply{
            sendOTP(userNumber, requireActivity())
            lifecycleScope.launch {
                otpSent.collect{
                    if (it){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "OTP Sent...")
                    }
                }
            }
        }
    }

    private fun onBackButtonClicked() {
        binding.tbOtpFragment.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }

    private fun customizingEnteringOTP() {
        val editTexts = arrayOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1){
                        if (i < editTexts.size -1){
                            editTexts[i + 1].requestFocus()
                        }
                    }else if (s?.length == 0){
                        if (i>0){
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()

        binding.tvUserNumber.text = userNumber
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.green)
            this.statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}