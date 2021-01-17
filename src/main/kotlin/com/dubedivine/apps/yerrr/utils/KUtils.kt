package com.dubedivine.apps.yerrr.utils

import com.dubedivine.apps.yerrr.model.Media
import com.dubedivine.apps.yerrr.model.MediaType
import com.dubedivine.apps.yerrr.model.PhoneNumber
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.mongodb.BasicDBObject
import org.bson.types.ObjectId
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

typealias Response<T> = ResponseEntity<StatusResponseEntity<T>>

// ---------------
// GLOBAL Functions
// ---------------

fun <T> response(message: String, entity: T?, status: Boolean = true, httpStatus: HttpStatus = HttpStatus.OK): ResponseEntity<StatusResponseEntity<T>> {
    // TODO: Add some statistic here so that we can see how many og these are failing and why
    return ResponseEntity(
            StatusResponseEntity(status,
                    message, entity), httpStatus
    )
}

fun <T> createdResponse(message: String, entity: T?, status: Boolean = true): Response<T> {
    return response(
            message,
            entity,
            status,
            HttpStatus.CREATED
    )
}

// -----
// Utility
// ----

object KUtils {

    const val PAGE_SIZE = 20

    fun correctPhoneNumberLength(phoneNumber: PhoneNumber): Int =
        (phoneNumber.code?.countryCode?.length ?: 0) + phoneNumber.number.length

    fun genTypeFromContentType(contentType: String) {
        when (contentType.substringAfter(".")) {

        }
    }

    fun createMedia(files: List<MultipartFile>, gridFSOperations: GridFsOperations): ArrayList<Media> {
        return when {
            files.size == 1 && isFileAVideo(files[0].contentType) -> { // One mans there is only video
                val metaData = BasicDBObject()
                val mime = genMimeTypeForVideo(files[0].originalFilename)
                metaData[HttpHeaders.CONTENT_TYPE] = mime
                val file = files.first()
                val createdFile = storeGridFsFile(file, metaData, gridFSOperations)
                arrayListOf(Media(file.originalFilename ?: file.name, MediaType.VIDEO.ordinal, createdFile.toString()))
            }
            else -> { // pictures
                val media: ArrayList<Media> = ArrayList(files.size)
                for (file in files) {
                    val metaData = BasicDBObject()
                    metaData[HttpHeaders.CONTENT_TYPE] = file.contentType
                    val createdFile = storeGridFsFile(file, metaData, gridFSOperations)
                    media.add(Media(file.originalFilename ?: file.name, MediaType.PICTURE.ordinal, createdFile.toString()))
                }
                media
            }
        }
    }

    private fun storeGridFsFile(file: MultipartFile,
                                metaData: BasicDBObject,
                                gridFSOperations: GridFsOperations): ObjectId {
            return gridFSOperations.store(file.inputStream, file.originalFilename, file.contentType, metaData)
    }

    // MIGHT not be require my it its needed on android!!
    private fun isFileAVideo(mime: String?): Boolean {
        return when (mime?.substringBefore("/")?.toLowerCase()) {
            "video" ->
                true
            else ->
                false // TODO: Log this error
        }
    }

    private fun genMimeTypeForVideo(filename: String?): String {
        return when (filename?.substringAfter(".")) {
            "mp4" -> {
                "video/mp4"
            }
            "3gp" -> {
                "video/3gpp"
            }
            else -> "video/*"  // TODO: Log this issue
        }
    }
}