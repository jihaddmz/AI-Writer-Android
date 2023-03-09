package com.appsfourlife.draftogo.feature_generate_text.util

sealed class Screens(val route: String){

    object ScreenHome: Screens("screen_home")
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
    object ScreenSong: Screens("screen_song")
    object ScreenSettings: Screens("screen_settings")
    object ScreenPodcast: Screens("screen_podcast")
    object ScreenGame: Screens("screen_game")
}