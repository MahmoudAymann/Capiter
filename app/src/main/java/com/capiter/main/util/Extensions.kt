package com.capiter.main.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
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
import com.capiter.main.R
import com.capiter.main.app.BaseApplication
import com.capiter.main.base.view.BaseActivity
import timber.log.Timber
import java.lang.reflect.ParameterizedType


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

fun <T : Any> Any.getTClass(): Class<T> {
    return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
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

fun LifecycleOwner.navigateSafe(
    directions: NavDirections,
    navOptions: NavOptions? = null
) {
    val navController: NavController?
    val mView: View?
    if (this is Fragment) {
        navController = findNavController()
        mView = view
    } else {
        val activity = this as BaseActivity<*, *>
        navController = activity.findNavController(fragmentContainerId())
        mView = currentFocus
    }
    if (canNavigate(navController, mView)) navController.navigate(directions, navOptions)
}

fun LifecycleOwner.navigateSafe(
    @IdRes navFragmentRes: Int,
    bundle: Bundle? = null
) {
    val navController: NavController?
    val mView: View?
    if (this is Fragment) {
        navController = findNavController()
        mView = view
    } else {
        val activity = this as BaseActivity<*, *>
        navController = activity.findNavController(fragmentContainerId())
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
