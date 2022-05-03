package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain

class SpinnerDomainAdapter(
    context: Context,
    resource: Int = android.R.layout.simple_spinner_dropdown_item,
    var list: List<SjDomain>
) :
    ArrayAdapter<SjDomain>(context, resource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setText(list[position].name)
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setText(list[position].name)
        return label
    }
}