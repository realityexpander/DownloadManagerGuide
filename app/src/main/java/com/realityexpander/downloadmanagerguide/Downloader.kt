package com.realityexpander.downloadmanagerguide

interface Downloader {
    fun downloadFile(url: String): Long
}