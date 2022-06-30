package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LinkPasteBottomSheet(
    val saveOperation: (String) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var url: String

    companion object {
        fun newInstance(saveOperation: (String) -> Unit): LinkPasteBottomSheet {
            return LinkPasteBottomSheet(saveOperation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val saveButton = requireView().findViewById<Button>(R.id.nextButton)
        saveButton.setOnClickListener { saveOperation(url) }
        val urlTextView = requireView().findViewById<TextView>(R.id.urlTextView)
        urlTextView.setText(url)
    }

    fun show(manager: FragmentManager, tag: String?, url: String) {
        this.url = url
        show(manager, tag)
    }

}