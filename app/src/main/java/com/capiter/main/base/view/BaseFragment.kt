package com.capiter.main.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.capiter.main.BR
import com.capiter.main.util.bindView
import com.capiter.main.util.castToActivity

/**
 * Created by MahmoudAyman on 6/17/2020.
 **/

abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel> : Fragment() {

    abstract fun pageTitle(): String?

    protected abstract val mViewModel: VM

    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, mViewModel)
//        changeMainTitle(pageTitle())
    }


    fun closeFragment() {
        activity?.onBackPressed()
    }

    fun showProgress(show: Boolean = true) {
        castToActivity<BaseActivity<*, *>> {
            it?.showProgress?.set(show)
        }
    }

    private fun changeMainTitle(title: String?) {

    }


    override fun onPause() {
        super.onPause()
        showProgress(false)
    }

}
