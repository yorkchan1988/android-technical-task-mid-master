package com.example.minimoneybox.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)