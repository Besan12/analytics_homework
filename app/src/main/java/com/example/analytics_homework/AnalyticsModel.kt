package com.example.analytics_homework

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AnalyticsModel {
    private var analytics: FirebaseAnalytics = Firebase.analytics
    private var myScreenName: String? = null
    private var myScreenStartTime: Long? = null

    fun selectContent(contentId:String, contentName:String, contentType:String){
        analytics.logEvent(
            FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, contentId);
            param(FirebaseAnalytics.Param.ITEM_NAME, contentName);
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        }
    }

    fun screenView(screenClass:String, screenName:String){
        myScreenName = screenName
        myScreenStartTime = System.currentTimeMillis()
        analytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS,screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME,screenName)
        }
    }

    fun trackScreenTime() {
        if (myScreenName != null && myScreenStartTime != null) {
            val time = System.currentTimeMillis() - myScreenStartTime!!
            storeTime(myScreenName!!, time)
            myScreenName = null
            myScreenStartTime = null
        }
    }
    fun storeTime(screenName: String, time: Long){
        val timeInSeconds = time / 1000
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("time_screen").document(screenName!!)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingTime = documentSnapshot.getLong("time") ?: 0
                docRef.update("time", existingTime + timeInSeconds)
            } else {
                docRef.set(mapOf("screenName" to screenName, "time" to timeInSeconds))
            }
        }
    }

}