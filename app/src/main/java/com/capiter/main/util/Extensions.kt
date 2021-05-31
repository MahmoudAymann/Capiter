package com.capiter.main.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.capiter.main.BR
import com.capiter.main.R
import com.capiter.main.app.BaseApplication
import com.capiter.main.base.network.Resource
import com.capiter.main.base.network.response.NetworkResponse
import com.capiter.main.base.view.BaseFragment
import com.capiter.main.base.viewmodel.AndroidBaseViewModel
import com.capiter.main.data.remote.ErrorResponse
import com.capiter.main.ui.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.lang.reflect.ParameterizedType


fun Activity.showActivity(
    destActivity: Class<out AppCompatActivity>,
    intent: Intent = Intent(this, destActivity)
) {
    this.startActivity(intent)
}

fun AppCompatActivity.findFragmentById(id: Int): Fragment? {
    supportFragmentManager.let {
        return it.findFragmentById(id)
    }
}

fun <T : Any> AndroidBaseViewModel.requestNewCallRefactor(
    networkCall: suspend () -> NetworkResponse<T, ErrorResponse>,
    disableProgress: Boolean = false,
    successCallBack: (T) -> Unit
) {
    viewModelScope.launch {
        postResult(Resource.loading(null))
        isLoading.set(!disableProgress)
        val res = networkCall() // execute
        Timber.e("$res")
        when (res) {
            is NetworkResponse.Success -> {
                viewModelScope.launch(Dispatchers.Main) {
                    successCallBack(res.body)
                }
            }
            is NetworkResponse.ServerError -> postResult(Resource.error(getShownError(res.body)))
            is NetworkResponse.NetworkError -> postResult(Resource.error(app.getString(R.string.network_error)))
            is NetworkResponse.UnknownError -> postResult(Resource.error(app.getString(R.string.server_error)))
        }

    }
}

private fun getShownError(response: ErrorResponse?): String {
    return response?.validation?.let {
        "${it[0]}"
    } ?: "null"
}

fun Activity.restartApp() {
    showActivity(MainActivity::class.java)
    finishAffinity()
}

fun FragmentActivity.showLogoutDialog(onClick: () -> Unit) {
    val dialog = SweetAlertDialog(this)
        .setContentText(getString(R.string.log_out_check_message))
        .setConfirmButton(getString(R.string.yes)) {
            it.closeDialog()
            onClick()
        }
        .setCancelButton(getString(R.string.no)) { sDialog ->
            sDialog.closeDialog()
        }
        .setConfirmButtonTextColor(getColorFromRes(R.color.white))
        .setCancelButtonBackgroundColor(getColorFromRes(R.color.white))
    dialog.show()
}

fun Activity.showExitDialog() {
    val dialog = SweetAlertDialog(this)
        .setContentText(getString(R.string.exit_app))
        .setConfirmText(getString(R.string.exit))
        .setConfirmClickListener { sDialog ->
            sDialog.closeDialog()
            finishAffinity()
        }
        .setCancelText(getString(R.string.cancel))
        .setCancelClickListener { sDialog ->
            sDialog.closeDialog()
        }
        .setConfirmButtonBackgroundColor(getColorFromRes(R.color.colorPrimary))
        .setConfirmButtonTextColor(getColorFromRes(R.color.white))
        .setCancelButtonBackgroundColor(getColorFromRes(R.color.white))
        .setCancelButtonTextColor(getColorFromRes(R.color.black))
    dialog.show()
}

fun Context.showDialog(msg: String?, type: Int? = null, okClick: () -> Unit = {}) {
    SweetAlertDialog(
        this,
        type ?: SweetAlertDialog.NORMAL_TYPE
    )
        .setContentText(msg)
        .setConfirmButton(getString(R.string.yes)) { sDialog ->
            sDialog.closeDialog()
            okClick()
        }.setCancelButton(getString(R.string.no)) {
            it.closeDialog()
        }
        .setConfirmButtonBackgroundColor(getColorFromRes(R.color.black))
        .setConfirmButtonTextColor(getColorFromRes(R.color.white))
        .show()
}

fun Context.getColorFromRes(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun SweetAlertDialog.closeDialog() {
    if (!AppUtil.isOldDevice())
        dismissWithAnimation()
    else
        dismiss()
}


fun <T : AndroidViewModel> T.app() = this.getApplication<BaseApplication>()
fun String.uri(): Uri? {
    return try {
        Uri.fromFile(File(this))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Activity?.dialContactPhone(phoneNumber: String) = this?.run {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)))
}


fun validateData(size: Int?, progress: Boolean?): Boolean? {
    return if (progress == false)
        (size ?: 0 > 0)
    else false
}

fun validateHolder(size: Int?, progress: Boolean?): Boolean? {
    return if (progress == false)
        (size ?: 0 == 0)
    else false
}


fun String.isValidUrl(): Boolean {
    return try {
        URLUtil.isValidUrl(this) && Patterns.WEB_URL.matcher(this).matches()
    } catch (e: Exception) {
        Timber.e(e)
        false
    }
}


fun ImageView.loadImageFromURL(url: String, progressBar: ProgressBar? = null) {
    val requestOptions = RequestOptions()
        .centerCrop()
        .override(1600 ,1600)
    progressBar?.visible()
    Glide.with(this).asBitmap()
        .load(url)
        .apply(requestOptions)
        .error(R.drawable.ic_broken_image)
        .addListener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                Timber.e("$e url: $url")
                setImageResource(R.drawable.ic_broken_image)
                progressBar?.gone()
                return true
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                setImageBitmap(resource)
                progressBar?.gone()
                return true
            }

        })
        .into(object : BitmapImageViewTarget(this) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                super.onResourceReady(resource, transition)
                progressBar?.gone()
            }
        })
}


inline fun <reified T : BaseFragment<*, *>> FragmentActivity.replaceFragment(
    bundle: Bundle? = null
) {
    val fragment = T::class.java.newInstance()
    fragment.let { myFrag ->
        supportFragmentManager.commit {
            if (!AppUtil.isOldDevice())
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            myFrag.arguments = bundle
            replace(R.id.fragment_container_view, myFrag, myFrag.tag)
            addToBackStack(myFrag::class.java.name)
        }
    }
}


fun String.removeSpaces(): String = this.replace(" ", "").trim()

fun CharSequence.removeSpaces(): CharSequence = this.toString().replace(" ", "").trim()

fun Fragment.showKeyboard(show: Boolean) {
    view?.let { activity?.showKeyboard(it, show) }
}

fun Activity.showKeyboard(show: Boolean) {
    showKeyboard(currentFocus ?: View(this), show)
}

fun Context.showKeyboard(view: View, show: Boolean) {
    with(view) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show)
            inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        else
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}


fun View.initAnimation(@AnimRes animRes: Int, seconds: Int, onEndCallBack: () -> Unit = {}) {
    val animation = AnimationUtils.loadAnimation(context, animRes)
    animation.duration = seconds.toLong() * 1000
    startAnimation(animation)
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            onEndCallBack()
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }
    })
}

fun <T : Any> Any.getTClass(): Class<T> {
    return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
}

inline fun <reified VM : ViewModel> ViewModelStoreOwner.bindViewModel(
    binding: ViewDataBinding,
    factory: ViewModelProvider.Factory? = null, body: VM.() -> Unit
): VM {
    return if (this is Fragment) {
        val vm by lazy {
            ViewModelProvider(
                this,
                factory ?: defaultViewModelProviderFactory
            )[VM::class.java]
        }
        binding.setVariable(BR.viewModel, vm)
        vm.apply(body)
    } else {
        val act = this as AppCompatActivity
        val vm by lazy {
            ViewModelProvider(
                act,
                factory ?: defaultViewModelProviderFactory
            )[VM::class.java]
        }
        binding.setVariable(BR.viewModel, vm)
        vm.apply(body)
    }
}


@Suppress("UNCHECKED_CAST")
fun <B : ViewDataBinding> LifecycleOwner.bindView(container: ViewGroup? = null): B {
    return if (this is Activity) {
        val inflateMethod = getTClass<B>().getMethod("inflate", LayoutInflater::class.java)
        val invokeLayout = inflateMethod.invoke(null, this.layoutInflater) as B
        this.setContentView(invokeLayout.root)
        invokeLayout
    } else {
        val fragment = this as Fragment
        val inflateMethod = getTClass<B>().getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Cannot access view bindings. View lifecycle is ${lifecycle.currentState}!")
        }
        val invoke: B = inflateMethod.invoke(null, layoutInflater, container, false) as B
        invoke
    }
}

fun <T : Any?, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(if (this is Fragment) viewLifecycleOwner else this, Observer {
        if (lifecycle.currentState == Lifecycle.State.RESUMED) {
            body(it)
        }
    })
}

inline fun <reified T : AppCompatActivity> Fragment.castToActivity(
    callback: (T?) -> Unit
): T {
    return if (requireActivity() is T) {
        callback(requireActivity() as T)
        requireActivity() as T
    } else {
        Timber.e("class cast exception, check your fragment is in the correct activity")
        callback(null)
        throw ClassCastException()
    }

}


fun View.visible(){
    visibility = View.VISIBLE
}
fun View.gone(){
    visibility = View.GONE
}
fun View.invisible(){
    visibility = View.INVISIBLE
}


fun LifecycleOwner.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
    val navController: NavController?
    val mView: View?
    if (this is Fragment) {
        navController = findNavController()
        mView = view
    } else {
        val activity = this as Activity
        navController = activity.findNavController(R.id.fragment_container_view)
        mView = currentFocus
    }
    if (canNavigate(navController, mView)) navController.navigate(directions, navOptions)
}

fun LifecycleOwner.navigateSafe(@IdRes navFragmentRes: Int, bundle: Bundle? = null) {
    val navController: NavController?
    val mView: View?
    if (this is Fragment) {
        navController = findNavController()
        mView = view
    } else {
        val activity = this as Activity
        navController = activity.findNavController(R.id.fragment_container_view)
        mView = currentFocus
    }
    if (canNavigate(navController, mView)) navController.navigate(navFragmentRes, bundle)
}

fun canNavigate(controller: NavController, view: View?): Boolean {
    val destinationIdInNavController = controller.currentDestination?.id
    // add tag_navigation_destination_id to your res\values\ids.xml so that it's unique:
    val destinationIdOfThisFragment =
        view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController

    // check that the navigation graph is still in 'this' fragment, if not then the app already navigated:
    return if (destinationIdInNavController == destinationIdOfThisFragment) {
        view?.setTag(R.id.tag_navigation_destination_id, destinationIdOfThisFragment)
        true
    } else {
        Timber.e("May not navigate: current destination is not the current fragment.")
        false
    }
}
