package com.rajankali.plasma.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rajankali.plasma.ui.PlasmaTheme
import com.rajankali.plasma.views.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseFragment : Fragment(){

    protected val uiScope = CoroutineScope(Dispatchers.Main)
    protected val ioScope = CoroutineScope(Dispatchers.IO)

    @Composable
    open fun setContent(){}

    open val layoutId: Int = -1

    protected val mainActivity by lazy { (requireActivity() as MainActivity) }
    protected val loggedUserId by lazy { mainActivity.loggedInUserId }
    protected val loginState by lazy { mainActivity.loginState }

    protected val navController by lazy {  Navigation.findNavController(requireView()) }

    protected fun fullScreen(){
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    protected fun exitFullScreen(){
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            onArgumentsReady(it)
        }
        return if(layoutId == -1) {
            ComposeView(requireContext()).apply {
                setContent {
                    PlasmaTheme {
                        Surface(color = MaterialTheme.colors.surface) {
                            Box {
                                setContent()
                            }
                        }
                    }
                }
            }
        }else{
            inflater.inflate(layoutId, container, false)
        }
    }

    protected fun updateUser(userId: Long){
        (requireActivity() as MainActivity).loggedInUserId = userId
    }

    protected fun <T: View> view(id: Int): T?{
        return view?.findViewById<T>(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    open fun initViews(){}
    open fun initObservers(){}
    open fun onArgumentsReady(bundle: Bundle){}

    override fun onStop() {
        super.onStop()
        uiScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        ioScope.cancel()
    }
}