package com.munawirfikri.saveoury.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.munawirfikri.saveoury.BuildConfig
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class RegisterViewModel: ViewModel() {

    private val accessToken = BuildConfig.MAPBOX_TOKEN
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            ApiConfig.provideMapboxApiService().getCity(it, accessToken).features
        }
        .asLiveData()
}