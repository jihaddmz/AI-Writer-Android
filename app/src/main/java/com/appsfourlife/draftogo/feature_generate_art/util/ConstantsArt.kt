package com.appsfourlife.draftogo.feature_generate_art.util

import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.feature_generate_art.models.ModelArtShowCase

object ConstantsArt {

    val LISTOF_ART_STYLES = listOf(
        App.getTextFromString(R.string.none),
        App.getTextFromString(R.string.render_3d),
        App.getTextFromString(R.string.abstract1),
        App.getTextFromString(R.string.anime),
        App.getTextFromString(R.string.cartoon),
        App.getTextFromString(R.string.digital_art),
        App.getTextFromString(R.string.illustration),
        App.getTextFromString(R.string.origami),
        App.getTextFromString(R.string.pixel_art),
        App.getTextFromString(R.string.photography),
        App.getTextFromString(R.string.pop_art),
        App.getTextFromString(R.string.retro),
        App.getTextFromString(R.string.sketch),
    )

    val LISTOF_ART_SHOWCASES = listOf(
        ModelArtShowCase(
            artID = R.drawable.fish,
            bio = App.getTextFromString(R.string.fish_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.avocado,
            bio = App.getTextFromString(R.string.avocado_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.monster,
            bio = App.getTextFromString(R.string.monster_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.astronaut,
            bio = App.getTextFromString(R.string.astronaut_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.sketch,
            bio = App.getTextFromString(R.string.sketch_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.teddy_bear,
            bio = App.getTextFromString(R.string.bear_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.orange,
            bio = App.getTextFromString(R.string.orange_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.boul,
            bio = App.getTextFromString(R.string.boul_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.computer,
            bio = App.getTextFromString(R.string.computer_art_bio)
        ),
        ModelArtShowCase(
            artID = R.drawable.dogandcat,
            bio = App.getTextFromString(R.string.dogandcat_art_bio)
        ),
    )
}