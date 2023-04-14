package com.appsfourlife.draftogo.util

sealed class Screens(val route: String){

    object ScreenEmail: Screens("screen_email")
    object ScreenBlog: Screens("screen_blog")
    object ScreenTwitter: Screens("screen_twitter")
    object ScreenPoem: Screens("screen_poem")
    object ScreenLetter: Screens("screen_letter")
    object ScreenArticle: Screens("screen_article")
    object ScreenEssay: Screens("screen_essay")
    object ScreenTiktok: Screens("screen_tiktok")
    object ScreenInstagram: Screens("screen_instagram")
    object ScreenCode: Screens("screen_code")
    object ScreenCV: Screens("screen_cv")
    object ScreenResume: Screens("screen_resume")
    object ScreenPersonalBio: Screens("screen_personal_bio")
    object ScreenCustom: Screens("screen_custom")
    object ScreenLaunch: Screens("screen_launch")
    object ScreenYoutube: Screens("screen_youtube")
    object ScreenFacebook: Screens("screen_facebook")
    object ScreenLinkedIn: Screens("screen_linkedin")
    object ScreenSong: Screens("screen_song")
    object ScreenPodcast: Screens("screen_podcast")
    object ScreenGame: Screens("screen_game")
    object ScreenSignIn: Screens("screen_sign_in")
    object ScreenUserAddedTemplate: Screens("screen_user_added_template")
    object ScreenSummarize: Screens("screen_summarize")
    object ScreenGrammar: Screens("screen_grammar")
    object ScreenTranslate: Screens("screen_translate")
    object ScreenHistory: Screens("screen_history")
    object ScreenFeedback: Screens("screen_feedback")

}