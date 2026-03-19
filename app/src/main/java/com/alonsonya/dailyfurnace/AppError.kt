package com.alonsonya.dailyfurnace

sealed class AppError {
    object NoInternet : AppError()
    object NotFound : AppError()
    object Server : AppError()
    object Empty : AppError()
    object Unknown : AppError()
}