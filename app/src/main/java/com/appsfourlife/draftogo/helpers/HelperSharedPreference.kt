package com.appsfourlife.draftogo.helpers

import android.content.Context
import androidx.core.content.edit
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.feature_generate_art.notifiers.NotifiersArt

object HelperSharedPreference {

    /**
     * shared preferences name
     **/
    const val SP_SETTINGS = "settings"
    const val SP_AUTHENTICATION = "authentication"

    /**
     * first time app launched
     **/
    const val SP_SETTINGS_IS_FIRST_TIME_LAUNCHED = "is_first_time_launched"
    const val SP_SETTINGS_IS_FIRST_TIME_V2_LAUNCHED = "is_first_time_v2_launched"
    const val SP_SETTINGS_IS_FIRST_TIME_V230_LAUNCHED = "is_first_time_v230_launched"


    /**
     * keys for settings shared preferences
     **/
    const val SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED = "nb_of_generations_consumed"
    const val SP_SETTINGS_NB_OF_WORDS_GENERATED = "nb_of_words_generated"
    const val SP_SETTINGS_NB_OF_GENERATIONS = "nb_of_generations"
    const val SP_SETTINGS_ENABLE_HEADLINES = "enable_headlines"
    const val SP_SETTINGS_LENGTH = "length"
    const val SP_SETTINGS_LETTER_TYPE = "letter_type"
    const val SP_SETTINGS_CV_TYPE = "cv_type"
    const val SP_SETTINGS_PROGRAMMING_LANGUAGE = "programming_language"
    const val SP_SETTINGS_ESSAY_TYPE = "essay_type"
    const val SP_SETTINGS_OUTPUT_LANGUAGE = "output_language"
    const val SP_SETTINGS_PODCAST_TYPE = "podcast_type"
    const val SP_SETTINGS_GAME_CONSOLE_TYPE = "console_type"
    const val SP_SETTINGS_OUTPUT_TYPEWRITER_LENGTH = "output_typewriter_length"
    const val SP_SETTINGS_SUBSCRIBE_TYPE = "subscribe_type"
    const val SP_SETTINGS_IS_SAVED_OUTPUTS_ENABLED = "is_compare_outputs_enabled"
    const val SP_SETTINGS_NB_0F_ARTS_CREDITS = "nb_0f_arts_credits"
    const val SP_SETTINGS_NB_0F_ARTS_GENERATED = "nb_0f_arts_generated"
    const val SP_SETTINGS_TYPE_OF_WRITING_MODEL = "type_of_writing_model"
    const val SP_SETTINGS_DONT_SHOW_ANYWHERE_WRITING_PERMISSION = "dont_show_anywhere_writing_permission"
    const val SP_SETTINGS_SUBSCRIBED_TO_EMAIL_SENDER = "is_subscribed_email_sender"


    /**
     * keys for authentication shared preferences
     **/
    const val SP_AUTHENTICATION_USERNAME = "username"
    const val SP_AUTHENTICATION_IS_SUBSCRIBED = "is_subscribed"
    const val SP_AUTHENTICATION_EXPIRATION_DATE = "expiration_date"
    const val SP_AUTHENTICATION_WILL_RENEW = "will_renew"

    /**
     * most common shared preferences keys
     **/

    fun getDontShowAnyWhereWritingPermission(): Boolean {
        return getBool(SP_SETTINGS, SP_SETTINGS_DONT_SHOW_ANYWHERE_WRITING_PERMISSION, false)
    }

    fun setDontShowAnyWhereWritingPermission(value: Boolean) {
        setBool(SP_SETTINGS, SP_SETTINGS_DONT_SHOW_ANYWHERE_WRITING_PERMISSION, value)
    }

    fun getTypeOfWritingModel(): String {
        return getString(SP_SETTINGS, SP_SETTINGS_TYPE_OF_WRITING_MODEL, "gpt-3.5-turbo")
    }

    fun setTypeOfWritingModel(value: String) {
        setString(SP_SETTINGS, SP_SETTINGS_TYPE_OF_WRITING_MODEL, value)
    }

    fun getNbOfArtsGenerated(): Int {
        return getInt(SP_SETTINGS, SP_SETTINGS_NB_0F_ARTS_GENERATED, 0)
    }

    fun incrementNbOfArtsGenerated() {
        setInt(SP_SETTINGS, SP_SETTINGS_NB_0F_ARTS_GENERATED, getNbOfArtsGenerated() + 1)
    }

    fun setNbOfArtsGenerated(value: Int) {
        setInt(SP_SETTINGS, SP_SETTINGS_NB_0F_ARTS_GENERATED, value)
    }

    fun getNbOfArtsCredits(): Int {
        return getInt(SP_SETTINGS, SP_SETTINGS_NB_0F_ARTS_CREDITS, 0)
    }

    fun setNbOfArtsCredits() {
        setInt(SP_SETTINGS, SP_SETTINGS_NB_0F_ARTS_CREDITS, NotifiersArt.credits.value)
    }

    fun getIsSavedOutputsEnabled(): Boolean{
        return getBool(SP_SETTINGS, SP_SETTINGS_IS_SAVED_OUTPUTS_ENABLED, true)
    }

    fun getSubscriptionType(): String {
        return getString(SP_SETTINGS, SP_SETTINGS_SUBSCRIBE_TYPE, "base")
    }

    fun setSubscriptionType(type: String) {
        setString(SP_SETTINGS, SP_SETTINGS_SUBSCRIBE_TYPE, type)
    }
    fun getOutputLanguage(): String{
        val sp = App.context.getSharedPreferences(SP_SETTINGS, Context.MODE_PRIVATE)
        return sp.getString(SP_SETTINGS_OUTPUT_LANGUAGE, App.getTextFromString(R.string.english))!!
    }

    fun getUsername(): String {
        val sp = App.context.getSharedPreferences(SP_AUTHENTICATION, Context.MODE_PRIVATE)
        return sp.getString(SP_AUTHENTICATION_USERNAME, "")!!
    }

    fun setUsername(value: String) {
        setString(SP_AUTHENTICATION, SP_AUTHENTICATION_USERNAME, value)
    }

    fun getNbOfGenerationsConsumed(): Int {
        return getInt(SP_SETTINGS, SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED, 0)
    }

    fun getNbOfWordsGenerated(): Int {
        return getInt(SP_SETTINGS, SP_SETTINGS_NB_OF_WORDS_GENERATED, 0)
    }

    fun addToNbOfWordsGenerated(value: Int) {
        val nbOfWordsGenerated = getNbOfWordsGenerated()
        setInt(SP_SETTINGS, SP_SETTINGS_NB_OF_WORDS_GENERATED, nbOfWordsGenerated + value)
    }

    fun incrementNbOfGenerationsConsumed() {
        val nbOfGenerationsConsumed = getInt(SP_SETTINGS, SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED, 0)
        val sp = App.context.getSharedPreferences(SP_SETTINGS, Context.MODE_PRIVATE)
        sp.edit {
            putInt(SP_SETTINGS_NB_OF_GENERATIONS_CONSUMED, nbOfGenerationsConsumed + 1)
            apply()
        }
    }

    /**
     * getters and setters for shared preferences keys as per type
     **/
    fun setString(name: String, key: String, value: String, context: Context = App.context) {
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        sp.edit {
            putString(key, value)
            apply()
        }
    }

    fun setInt(
        name: String,
        key: String,
        value: Int,
        context: Context = App.context
    ) {
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        sp.edit {
            putInt(key, value)
            apply()
        }
    }

    fun setFloat(
        name: String,
        key: String,
        value: Float,
        context: Context = App.context
    ) {
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        sp.edit {
            putFloat(key, value)
            apply()
        }
    }

    fun setBool(
        name: String,
        key: String,
        value: Boolean,
        context: Context = App.context
    ) {
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        sp.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getString(name: String, key: String, value: String, context: Context = App.context): String {
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sp.getString(key, value)!!
    }

    fun getInt(name: String, key: String, value: Int, context: Context = App.context): Int {
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sp.getInt(key, value)
    }

    fun getFloat(name: String, key: String, value: Float, context: Context = App.context): Float {
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sp.getFloat(key, value)
    }

    fun getBool(name: String, key: String, value: Boolean, context: Context = App.context): Boolean{
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sp.getBoolean(key, value)
    }
}