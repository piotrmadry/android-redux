package com.zumba.redux

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> Fragment.viewModel(crossinline provider: () -> T): Lazy<T> =
    viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
        }
    }

inline fun <reified T : ViewModel> ComponentActivity.viewModel(crossinline provider: () -> T): Lazy<T> =
    viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
        }
    }