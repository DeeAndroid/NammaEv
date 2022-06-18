package com.nammaev.ui.view.parts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nammaev.databinding.FragmentAddressDialogBinding

typealias onSelection = (address: String) -> Unit

class AddressBottomSheetDialog(private val onSelection: onSelection) : BottomSheetDialogFragment() {

    var binding: FragmentAddressDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddressDialogBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            btnProceed.setOnClickListener {
                if (etAddress.text.toString().isEmpty()) {
                    etAddress.error = "Address needed"
                    return@setOnClickListener
                }
                onSelection.invoke(etAddress.text.toString())
                dismiss()
            }
            btnCancel.setOnClickListener { dismiss() }
        }
    }

    companion object {
        private val TAG = AddressBottomSheetDialog.javaClass.simpleName
        fun showAddressBottomSheet(
            fragmentManager: FragmentManager,
            onSelection: onSelection
        ) {
            AddressBottomSheetDialog(onSelection).show(fragmentManager, TAG)
        }
    }
}