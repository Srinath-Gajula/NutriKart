package com.example.nutrikart.fragments

import android.content.Intent
import android.location.Address
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutrikart.R
import com.example.nutrikart.Utils
import com.example.nutrikart.activity.AuthMainActivity
import com.example.nutrikart.databinding.AddressBookLayoutBinding
import com.example.nutrikart.databinding.FragmentProfileBinding
import com.example.nutrikart.viewmodels.UserViewModel


class ProfileFragment : Fragment() {

    val viewModel : UserViewModel by viewModels()

    private lateinit var  binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)

        onBackButtonClicked()
        onLogOutClicked()
        onOrdersLayoutClicked()
        onAddressBookClicked()

        return binding.root
    }

    private fun onAddressBookClicked() {
        binding.llAddress.setOnClickListener {
            val addressBookLayoutBinding = AddressBookLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            viewModel.getUserAddress { address ->
                addressBookLayoutBinding.etAddress.setText(address.toString())
            }
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(addressBookLayoutBinding.root)
                .create()
            alertDialog.show()

            addressBookLayoutBinding.btnEdit.setOnClickListener {
                addressBookLayoutBinding.etAddress.isEnabled = true
            }
            addressBookLayoutBinding.btnSave.setOnClickListener {
                viewModel.saveAddress(addressBookLayoutBinding.etAddress.text.toString())
                alertDialog.dismiss()
                Utils.showToast(requireContext() , "Address updated")

            }

        }
    }

    private fun onOrdersLayoutClicked() {
        binding.llOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }
    }

    private fun onLogOutClicked() {
        binding.llLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val alertDialog= builder.create()
            builder.setTitle("Log out")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes") { _, _ ->  // Pass `_` for unused parameters
                    viewModel.logOutUser()

                    startActivity(Intent(requireContext() , AuthMainActivity::class.java))
                    requireActivity().finish()

                }
                .setNegativeButton("No") { _, _ ->
                    alertDialog.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }


    private fun onBackButtonClicked() {
        binding.tbProfileFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
    }


}