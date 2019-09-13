package com.example.basekitimpl

import android.content.Context
import android.widget.Toast
import com.example.basekit.ToastMaker

/**
 * class to generate toast, instance will be available through appcomponent
 */
class ToastMakerImpl(private val context: Context) : ToastMaker {
    override fun showToast(res: Int) {
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
    }
}